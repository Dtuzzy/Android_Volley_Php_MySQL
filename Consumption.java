package com.business.e_gas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Consumption extends AppCompatActivity {

    Spinner station;
    Spinner product;
    Spinner group;
    EditText consumption;
    DatePicker dates;
    Button send;
    String dateFormat;
    ProgressDialog progressDialog;

    private String url = "https://wuxiancorp.com/gas_station/consumption.php";
    private String TAG = MainActivity.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        station =  findViewById(R.id.consumption_spinner);
        product =  findViewById(R.id.consumption_spinner2);
        group  =  findViewById(R.id.consumption_spinner3);
        consumption = findViewById(R.id.consumption_qty);
        dates = findViewById(R.id.consumption_datePicker);
        send = findViewById(R.id.consumption_send);
        SimpleDateFormat date_format = new SimpleDateFormat("yy-MM-dd");
        dateFormat = date_format.format(new Date(dates.getYear(), dates.getMonth(),dates.getDayOfMonth()));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    final String st =  getStationCode(station.getSelectedItemPosition());
                    final String st_grp = getGroupCode(group.getSelectedItemPosition());
                    final String pro = getProductValue(product.getSelectedItemPosition());
                    pushRecords(st, pro, st_grp, consumption.getText().toString(), dateFormat);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public String getGroupCode(int position) {
        String value = "";

        if (position == 0) {
            value = "SIV-GEN";
        } else if (position == 1) {
            value = "SIV-PRO";
        } else if (position == 2) {
            value = "SIV-STAFF";
        }

        return value;

    }


    public String getStationCode(int position) {
        String value = "";

        if (position == 0) {
            value = "LA01";
        } else if (position == 1) {
            value = "AB02";
        } else if (position == 2) {
            value = "BE03";
        }

        return value;

    }

    public String getProductValue(int position) {
        String value = "";

        if (position == 0) {
            value = "PMS";
        } else if (position == 1) {
            value = "DPK";
        } else if (position == 2) {
            value = "AGO";
        }

        return value;

    }

    public void pushRecords(String code, String pro, String grp, String quantity, String date){
            System.out.println(grp);
        progressDialog = new ProgressDialog(Consumption.this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Sending Report"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();

        Map<String, String> params = new HashMap<>();
        params.put("station_code", code);
        params.put("product_group", grp);
        params.put("product_type", pro);
        params.put("sales_quantity", quantity);
        params.put("post_date", date);

        JsonObjectRequest loginForm = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());
                        if(response.toString().contains("success")){
                            startActivity(new Intent(Consumption.this, Success_Page.class));
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"Sending Failed", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e(TAG, error.getMessage());
                    Toast.makeText(getApplicationContext(),"Sending Failed", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Log.e(TAG, error.getMessage());
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),"Server Failed", Toast.LENGTH_LONG).show();
                    Log.e(TAG, error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.e(TAG, error.getMessage());
                    Toast.makeText(getApplicationContext(),"Network Failed", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Log.e(TAG, error.getMessage());
                }
            }
        });
        loginForm.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(loginForm,TAG);
        //  AppController.getInstance().addToRequestQueue(loginForm);
        // Toast.makeText(getApplicationContext(), "Record Sent ", Toast.LENGTH_LONG).show();
    }

}
