package com.example.mataturoutesystem;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.mataturoutesystem.PaypalConfig.PaypalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;

public class FragPayment extends Fragment {

    Button paypalButton, mpesaButton;

    ProgressDialog paypalbtn;

    public static final int PAYMENT_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PaypalConfig.PAYPAL_CLIENT_ID);



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.frag_payment, container, false);
        paypalButton = (Button) rootView.findViewById(R.id.PayPalButton);
        mpesaButton = (Button) rootView.findViewById(R.id.MPESAButton);
        paypalbtn = new ProgressDialog(getActivity());

        paypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paypalbtn.setIndeterminate(true);
                paypalbtn.setTitle("Proceeding to payment...");
                paypalbtn.show();
                startActivity(new Intent(getContext(), CheckOutPayment.class));
                paypalbtn.dismiss();
            }
        });

        mpesaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MpesaPaymentCheckout.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}

