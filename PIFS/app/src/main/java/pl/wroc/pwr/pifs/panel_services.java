package pl.wroc.pwr.pifs;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class panel_services extends ListActivity {
    // Progress Dialog
    Button btnAll;

    HashMap<String,List<String>> state_category;
    List<String> state_list;
    ExpandableListView choiceList;
    state_adapter adapter;


    private ProgressDialog pDialog;
    String tokenID;
    String varState;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> ordersList;

    // url to get all products list
    private static String url_orders = "http://serviceofelectronicdevices.azurewebsites.net/Api/Orders";
    // JSON Node names
    private static final String TAG_ORDERS = "Orders";
    private static final String TAG_ID_ORDER = "Id";
    private static final String TAG_DEVICE_BRAND = "DeviceBrand";
    private static final String TAG_DEVICE_MODEL = "DeviceModel";
    private static final String TAG_CURRENTSTATE = "CurrentState";

    // products JSONArray
    JSONArray orders = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_services);
        choiceList = (ExpandableListView) findViewById(R.id.expListView);
        state_category = dataProvider.getInfo(getBaseContext());
        state_list = new ArrayList<String>(state_category.keySet());
        adapter = new state_adapter(this, state_category, state_list);
        choiceList.setAdapter(adapter);

        choiceList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                choiceList.collapseGroup(groupPosition);
                varState = state_category.get(state_list.get(groupPosition)).get(childPosition).split("\\.")[0];
                ordersList = new ArrayList<HashMap<String, String>>();
                new LoadAllProducts().execute();
                return false;
            }
        });

        varState="All";
        btnAll = (Button) findViewById(R.id.btn_All);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varState="All";
                ordersList = new ArrayList<HashMap<String, String>>();
                new LoadAllProducts().execute();
            }
        });

        GlobalCatcher mApp = ((GlobalCatcher)getApplicationContext());
        tokenID = mApp.getSomeVariable2();

        // Hashmap for ListView
        ordersList = new ArrayList<HashMap<String, String>>();

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
                String id_order = ((TextView) view.findViewById(R.id.id_order)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        services_details.class);
                // sending pid to next activity
                in.putExtra(TAG_ID_ORDER, id_order);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
    }
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
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
            pDialog = new ProgressDialog(panel_services.this);
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
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_orders, "GET", params4);

            // Check your log cat for JSON reponse
            try {
                Log.d("AllServices", json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("AllServices", "Catched error1");
            }
            try {
                // Getting Array of Products
                orders = json.getJSONArray(TAG_ORDERS);
                // looping through All Products
                for (int i = 0; i < orders.length(); i++) {
                    JSONObject c = orders.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_ID_ORDER);
                    String brand = c.getString(TAG_DEVICE_BRAND);
                    String name = c.getString(TAG_DEVICE_MODEL);
                    String rec1_curStatus="";
                    JSONObject d = c.getJSONObject(TAG_CURRENTSTATE);
                    try{
                        rec1_curStatus = new String(d.getString("State").getBytes("ISO-8859-1"), "UTF-8");
                    } catch (Exception e){
                        Log.e("AllServices", e.getMessage());
                    }
                    String rec2_curStatus="";
                    // rec1_curStatus - this string is for number of state
                    if (rec1_curStatus.equals("0"))
                    {
                        //rec2_curStatus = getString(R.string.state_accepted).split("\\.")[1];
                        rec2_curStatus = getString(R.string.state_accepted);
                    }
                    if (rec1_curStatus.equals("1"))
                    {
                        rec2_curStatus = getString(R.string.state_update);
                    }
                    if (rec1_curStatus.equals("2"))
                    {
                        rec2_curStatus = getString(R.string.state_waitingComp);
                    }
                    if (rec1_curStatus.equals("3"))
                    {
                        rec2_curStatus = getString(R.string.state_inProgres);
                    }
                    if (rec1_curStatus.equals("4"))
                    {
                        rec2_curStatus = getString(R.string.state_contactClient);
                    }
                    if (rec1_curStatus.equals("5"))
                    {
                        rec2_curStatus = getString(R.string.state_ready);
                    }
                    if (rec1_curStatus.equals("6"))
                    {
                        rec2_curStatus = getString(R.string.state_done);
                    }
                    if (rec1_curStatus.equals("7"))
                    {
                        rec2_curStatus = getString(R.string.state_rejected);
                    }

                    String finalName = id+". "+brand+" "+name+" - "+rec2_curStatus;

                    if (rec1_curStatus.equals(varState)){
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID_ORDER, id);
                        map.put(TAG_DEVICE_MODEL, finalName);
                        // adding HashList to ArrayList
                        ordersList.add(map);
                    }
                    if (varState.equals("All")){
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID_ORDER, id);
                        map.put(TAG_DEVICE_MODEL, finalName);
                        // adding HashList to ArrayList
                        ordersList.add(map);
                    }
                }
            } catch (Exception e){
                Log.d("AllServices", "Catched error2");
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
                    SpecialAdapter adapter = new SpecialAdapter(
                            panel_services.this, ordersList,
                            R.layout.list_item, new String[] {TAG_ID_ORDER,
                            TAG_DEVICE_MODEL},
                            new int[] { R.id.id_order, R.id.device_name});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}
