package com.hotsix.www.hotsixpaymentmodule;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dusdj.termp.R;

/**
 * Created by KSH's-PC on 2017-11-22.
 */

public class PaymentSelectActivity extends AppCompatActivity{

    protected String randomOrderNumber;
    protected String paymentValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpayment);

        paymentValue = getIntent().getStringExtra("POINTVALUE");
        randomOrderNumber = getIntent().getStringExtra("ORDERNUMBER");

    }

    protected void cardClick(View v){
        Intent intent = new Intent(this, PaymentCardActivity.class);
        intent.putExtra("ORDERNUMBER",randomOrderNumber);
        intent.putExtra("POINTVALUE",paymentValue);
        startActivity(intent);
        this.finish();
    }

    protected void accountClick(View v){
        Intent intent = new Intent(this, PaymentAccountActivity.class);
        intent.putExtra("ORDERNUMBER",randomOrderNumber);
        intent.putExtra("POINTVALUE",paymentValue);
        startActivity(intent);
        this.finish();
    }

}
