package pl.wroc.pwr.pifs;

/**
 * Created by Vreon-PC on 16.05.2016.
 */
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class panel_components extends ListActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    String tokenID;
    String orderID;
    String taskId;
    String orderId;
    String compId;

    String cId;
    String cBrand;
    String cModel;
    String cPrice;


    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> componentList;

    // url to get all products list
    private static String url_orders = "http://serviceofelectronicdevices.azurewebsites.net/Api/Orders";
    // JSON Node names
    private static final String TAG_TASKS = "Tasks";
    private static final String TAG_TASK = "Task";
    private static final String TAG_ID_TASK = "Id";
    private static final String TAG_STATE_TASK = "State";
    private static final String TAG_DATE_TO = "DateTo";
    private static final String TAG_COMPONENT_MODEL = "Model";
    private static final String TAG_COMPONENTSLIST = "ComponentsList";

    // products JSONArray
    JSONArray orders = null;
    JSONArray components = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_components);

        GlobalCatcher mApp = ((GlobalCatcher)getApplicationContext());
        tokenID = mApp.getSomeVariable2();
        orderID = mApp.getSomeVariable4();
        Log.d("GCatcher", orderID);

        // Hashmap for ListView
        componentList = new ArrayList<HashMap<String, String>>();

        // Loading orders in Background Thread
        new LoadAllProducts().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                compId = ((TextView) view.findViewById(R.id.id_task)).getText()
                        .toString();

                Log.d("AllComponents", orderId);
                Log.d("AllComponents", taskId);
                Log.d("AllComponents", compId);
                Log.d("AllComponents", tokenID);
                //Here prepare Post messagge consist of:
                //OrderId - task
                //TaskId - task
                //ComponentId - componentlist
                //Token -knows

                String userPrompt = getString(R.string.userPrompt);
                String userPromptYes = getString(R.string.userPromptYes);
                String userPromptNo = getString(R.string.userPromptNo);

                String oneRec="";
                try{
                    int foo = Integer.parseInt(compId);
                    String rec1_Model = componentList.get(foo-1).get("Model");
                    oneRec = rec1_Model.split("\\.")[1];
                    //Log.d("AllComponents", oneRec);
                } catch (Exception e){
                    Log.d("AllComponents", "Catched error5");
                }
                String userPromptFinal = userPrompt+" "+oneRec;


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                //Send POST Message

                                try {
                                    // Building Parameters
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("OrderId", orderId));
                                    params.add(new BasicNameValuePair("TaskId", taskId));
                                    params.add(new BasicNameValuePair("ComponentId", compId));
                                    params.add(new BasicNameValuePair("Token", tokenID));

                                    // getting product details by making HTTP request
                                    JSONObject json = jParser.makeHttpRequest(
                                            url_orders, "POST", params);

                                    // check your log for json response
                                    Log.d("AllComponents", json.toString());
                                    Toast.makeText(panel_components.this, getString(R.string.chooseCompToast),
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), panel_services.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(panel_components.this, getString(R.string.jsonExToast),
                                            Toast.LENGTH_LONG).show();
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(panel_components.this);
                builder.setMessage(userPromptFinal).setPositiveButton(userPromptYes, dialogClickListener)
                        .setNegativeButton(userPromptNo, dialogClickListener).show();

            }
        });
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(panel_components.this);
            pDialog.setMessage(getString(R.string.loading_services));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params4 = new ArrayList<NameValuePair>();
            params4.add(new BasicNameValuePair("Token", tokenID));
            params4.add(new BasicNameValuePair("orderId",orderID));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_orders, "GET", params4);

            // Check your log cat for JSON reponse
            try {
                Log.d("AllComponents0", json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("AllComponents", "Catched error1");
            }
            try {
                // Getting Array of Products
                orders = json.getJSONArray(TAG_TASKS);
                // looping through All Products
                for (int i = 0; i < orders.length(); i++) {
                    JSONObject c = orders.getJSONObject(i);
                    JSONObject d = c.getJSONObject(TAG_TASK);
                    String state = d.getString(TAG_STATE_TASK);
                    String rec1_dateTo = d.getString(TAG_DATE_TO);

                    String rec1_dateTo2 = rec1_dateTo.replaceAll("T", " ");
                    String rec1_dateTo3 = rec1_dateTo2.split("\\.", 2)[0];
                    if (rec1_dateTo3.equals("null") && state.equals("4")){
                        Log.d("AllComponents", "We've got this record with state 4");
                        //Log.d("AllComponents", c.toString());
                        JSONObject task = c.getJSONObject("Task");
                        taskId = task.getString("Id");
                        orderId = task.getString("OrderId");
                        try {
                            JSONArray e = c.getJSONArray("ComponentsList");
                            for (int j = 0; j < e.length(); j++) {
                                JSONObject f = e.getJSONObject(j);
                                cId = f.getString("Id");
                                cBrand = f.getString("Brand");
                                cModel = f.getString("Model");
                                cPrice = f.getString("Price");

                                String finalName = cId+". "+cBrand+" - "+cModel+", Cena(PLN): "+cPrice;
                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();
                                // adding each child node to HashMap key => value
                                map.put(TAG_ID_TASK, cId);
                                map.put(TAG_COMPONENT_MODEL, finalName);
                                // adding HashList to ArrayList
                                componentList.add(map);
                            }
                            /*
                            "Brand": "Seagate",
                            "Model": "Laptop Thin HDD 500GB",
                            "Price": 200
                            */
                        } catch (JSONException g){
                            Log.d("AllComponents", g.toString());
                        }
                    }
                }
            } catch (Exception e){
                Log.d("AllComponents", "Catched error3");
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            panel_components.this, componentList,
                            R.layout.list_item2, new String[] {TAG_ID_TASK,
                            TAG_COMPONENT_MODEL},
                            new int[] { R.id.id_task, R.id.component_name});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}
