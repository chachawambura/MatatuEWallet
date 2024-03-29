package com.example.mataturoutesystem;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mataturoutesystem.PaypalConfig.PaypalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import static com.example.mataturoutesystem.FragPayment.PAYMENT_REQUEST_CODE;

public class CheckOutPayment extends AppCompatActivity {
    EditText edtamount;
    String amount = "";
    Button btnPay;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PaypalConfig.PAYPAL_CLIENT_ID);
    ProgressDialog pressedPayment;

    @Override
    public void onDestroy() {
        this.stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutpayment);

        btnPay = findViewById(R.id.payNow);
        edtamount = findViewById(R.id.editAmount);
        pressedPayment = new ProgressDialog(this);


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressedPayment.setIndeterminate(true);
                pressedPayment.setTitle("Redirecting you to PayPal Login...");
                pressedPayment.show();
              processPayment();
            }
        });

    }
    private void processPayment() {
        amount = edtamount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Pay for Matatu Trip", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYMENT_REQUEST_CODE );



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PAYMENT_REQUEST_CODE){
            if (resultCode == RESULT_OK);{
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                pressedPayment.dismiss();
                if(confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("Payment Details", paymentDetails)
                                .putExtra("Payment Amount", amount)


                        );
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else if(resultCode == Activity.RESULT_CANCELED)
                    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();

            }


        }
        else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}

