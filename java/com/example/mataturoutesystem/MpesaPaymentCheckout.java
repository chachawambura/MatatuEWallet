package com.example.mataturoutesystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MpesaPaymentCheckout extends AppCompatActivity {
    SharedPreference sharedPreference;
    EditText phone, amount;
    Button mpesapay;
    String phonenumber, mpesaamount;
    String userID;

    String initialCheckoutRequestID;
    String finalResponseCode;
    String retrievecheckoutidURL = "https://www.fuelpap.co.ke/mataturoutesystem/mataturetrievecheckoutrequestid.php";
    String retrieveResponseCodeURL = "https://www.fuelpap.co.ke/mataturoutesystem/mataturetrieveresponsecode.php";
    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer timer = new Timer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpesacheckout);
        phone = findViewById(R.id.mpesaeditPhone);
        amount = findViewById(R.id.mpesaeditAmount);
        mpesapay = findViewById(R.id.mpesapayNow);

        sharedPreference = new SharedPreference(this);
        userID = sharedPreference.getPhoneNumber();

        mpesapay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = phone.getText().toString();
                mpesaamount = amount.getText().toString();
                String sanitizedPhoneNumber = phonenumber.replaceFirst("^0","254");
                String url = "https://www.fuelpap.co.ke/apis/matatumpesa.php";
                new PostMpesaDetails(sanitizedPhoneNumber, mpesaamount, url).execute();
                Log.d("MPESAPay","Error");

            }
        });
    }

    public void endMPesaProcess(){
        Log.i("Matatu App", "Process has ended");
    }


    public void doTimerTask(){
//        secondPD = new ProgressDialog(this);
//        secondPD.setIndeterminate(true);
//        secondPD.setMessage("Checking if your payment is successful...");
//        secondPD.show();

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        postandGetResponseCode();
                        // update TextView
                        //hTextView.setText("Timer: " + nCounter);

                        Log.d("TIMER", "TimerTask run");
                        if (finalResponseCode.equals("0")){
                            stopTask();
                        }
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        timer.schedule(mTimerTask, 13000, 3000);  //

    }

    public void stopTask(){

        if(mTimerTask!=null){

            Log.d("TIMER", "timer canceled");
            Toast.makeText(this, "posted successfully", Toast.LENGTH_SHORT).show();
            mTimerTask.cancel();
//            secondPD.dismiss();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

    }

    public void getCheckoutRequestID (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, retrievecheckoutidURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        initialCheckoutRequestID = response ;
                        //Toast.makeText(SearchActivity.this,FinalJSonObject,Toast.LENGTH_LONG).show();


                        doTimerTask();
                        // Calling method to parse JSON object.
//                        new ParseJSonDataClass(SearchActivity.this).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(MpesaPaymentCheckout.this,error.getMessage(), Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userID);

                return params;
            }
        };

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(MpesaPaymentCheckout.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    public  void postandGetResponseCode(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, retrieveResponseCodeURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        finalResponseCode = response ;



                        // Calling method to parse JSON object.
                       endMPesaProcess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
//                        progressDialog.dismiss();
                        Log.e("error", "error:"+error.getMessage() +"...code..." + finalResponseCode + ".... req id..." +initialCheckoutRequestID);
                        Toast.makeText(MpesaPaymentCheckout.this,error.getMessage(),Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("checkoutrequest_id", initialCheckoutRequestID);

                return params;
            }
        };

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(MpesaPaymentCheckout.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    public class PostMpesaDetails extends AsyncTask<Void, Void, String> {
        private String phonenumber, amount, url, response;


        public PostMpesaDetails(String phonenumber, String amount, String url){
            this.phonenumber = phonenumber;
            this.amount = amount;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<String, String>();
            data.put("phone", phonenumber);
            data.put("amount", amount);
            Log.d("doInBack","Final3");


            URL mainUrl;


            try{
                mainUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)mainUrl.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(data));
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    response = reader.readLine();

                }else {
                    response = "Error Posting";
                }
                Log.d("responns",response);

            }catch (Exception e){e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("PreExecuted","Final");
        }

        protected void onPostExecute (String s){
            super.onPostExecute(s);
            getCheckoutRequestID();
            Log.d("PostExecuted","Final2");
        }

        private String getPostDataString(HashMap<String, String> params){
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String>entry: params.entrySet()){
                if (first) {
                    first = false;
                }else
                    result.append("&");

                try {
                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return result.toString();
        }

    }

}
