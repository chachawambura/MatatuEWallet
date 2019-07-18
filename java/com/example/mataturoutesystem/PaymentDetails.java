package com.example.mataturoutesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {
    TextView txtId, txtAmount, txtStatus;
    Button saveInfo;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtAmount = findViewById(R.id.txtAmount);
        txtId = findViewById(R.id.txtId);
        txtStatus = findViewById(R.id.txtStatus);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        saveInfo = findViewById(R.id.buttonSave);
        // Getting the intent
        Intent intent = getIntent();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePaymentDetails();

            }
        });


        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Payment Details"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("Payment Amount"));

        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response, String paymentAmount){
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("$"+ paymentAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void savePaymentDetails(){
        String amount = txtAmount.getText().toString().trim();
        String pay_id = txtId.getText().toString().trim();
        String status = txtStatus.getText().toString().trim();

        PaymentInformation paymentInformation = new PaymentInformation(amount,pay_id,status);

        databaseReference.child("Payment").setValue(paymentInformation);

    }


}
