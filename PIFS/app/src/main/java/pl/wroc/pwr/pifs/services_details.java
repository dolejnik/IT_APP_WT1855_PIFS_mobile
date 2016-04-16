package pl.wroc.pwr.pifs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class services_details extends Activity {
    TextView txtOrderID;
    TextView txtBrand;
    TextView txtModel;
    TextView txtStatus;
    TextView txtMore;

    String id_order;
    String tokenID;
    // Progress Dialog
    private ProgressDialog pDialog;
    // products JSONArray
    JSONArray orders = null;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_orders = "http://serviceofelectronicdevices.azurewebsites.net/Api/Orders";

    // JSON Node names
    private static final String TAG_ORDERS = "Orders";
    private static final String TAG_ID_ORDER = "Id";
    private static final String TAG_DEVICEMODEL = "DeviceModel";
    private static final String TAG_DEVICEBRAND = "DeviceBrand";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_services_details);

        GlobalCatcher mApp = ((GlobalCatcher)getApplicationContext());
        tokenID = mApp.getSomeVariable2();

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (id_order) from intent
        id_order = i.getStringExtra(TAG_ID_ORDER);

        // Getting complete product details in background thread
        new GetProductDetails().execute();
    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(services_details.this);
            pDialog.setMessage("Loading order details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Building Parameters
                    List<NameValuePair> params4 = new ArrayList<NameValuePair>();
                    params4.add(new BasicNameValuePair("Token", tokenID));
                    // getting JSON string from URL
                    JSONObject json = jsonParser.makeHttpRequest(url_orders, "GET", params4);

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
                            String id = c.getString(TAG_ID_ORDER);
                            if (id.equals(id_order)) {
                                Log.d("AllServices", "We've got this rec");
                                // Storing each json item in variable
                                String rec_id = c.getString(TAG_ID_ORDER);
                                String rec_brand = c.getString(TAG_DEVICEBRAND);
                                String rec_model = c.getString(TAG_DEVICEMODEL);
                                txtOrderID = (TextView) findViewById(R.id.text_orderid);
                                txtOrderID.setText(rec_id);
                                txtBrand = (TextView) findViewById(R.id.text_brand);
                                txtBrand.setText(rec_brand);
                                txtModel = (TextView) findViewById(R.id.text_model);
                                txtModel.setText(rec_model);

                            }



                        }
                    } catch (Exception e){
                        Log.d("AllServices", "Catched error2");
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

}
