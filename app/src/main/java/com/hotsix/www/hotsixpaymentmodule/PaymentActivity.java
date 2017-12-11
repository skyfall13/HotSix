package com.hotsix.www.hotsixpaymentmodule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dusdj.termp.R;

import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    protected String randomOrderNumber;
    protected String paymentValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // POINTVALUE값 가져오기
        paymentValue = getIntent().getStringExtra("POINTVALUE");

        //주문번호 TextView
        TextView orderNumberView = (TextView)findViewById(R.id.PayOrderNumberView);
        long orderNumber = generateNumber(9);
        randomOrderNumber = Long.toString(orderNumber);
        orderNumberView.setText(randomOrderNumber);

        // 가격 TextView
        TextView priceView = (TextView)findViewById(R.id.PriceView);
        priceView.setText(paymentValue);

    }

    public long generateNumber (int length){ // 랜덤 넘버 생성 함수
        String numStr = "1";
        String plusNumStr = "1";

        for (int i = 0; i < length; i++) {
            numStr += "0";

            if (i != length - 1) {
                plusNumStr += "0";
            }
        }

        Random random = new Random();
        long result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);

        if (result > Integer.parseInt(numStr)) {
            result = result - Integer.parseInt(plusNumStr);
        }

        return result;
    }

    protected void payAcceptClick(View v){
        Intent intent = new Intent(this, PaymentSelectActivity.class);
        intent.putExtra("ORDERNUMBER",randomOrderNumber);
        intent.putExtra("POINTVALUE",paymentValue);
        startActivity(intent);

        this.finish();
    }

    protected void payCancelClick(View v){
        this.finish();
    }
}
