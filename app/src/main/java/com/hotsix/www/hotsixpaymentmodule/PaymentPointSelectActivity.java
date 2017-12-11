package com.hotsix.www.hotsixpaymentmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dusdj.termp.R;

/**
 * Created by KSH's-PC on 2017-11-28.
 */

public class PaymentPointSelectActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_pointselect);

    }

    protected void pointPaymentAcceptClick(View v){ // 포인트 결제 부분
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroupPointSelectView);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                Toast.makeText(PaymentPointSelectActivity.this, radioButton.getText().toString() + "체크", Toast.LENGTH_SHORT).show();

                switch (checkedId){
                    case R.id.point1000:
                        break;
                    case R.id.point3000:
                        break;
                    case R.id.point5000:
                        break;
                    case R.id.point10000:
                        break;
                    case R.id.point20000:
                        break;
                }
            }
        });

        RadioButton selectedRadioButton = (RadioButton)findViewById(radioGroup1.getCheckedRadioButtonId());

        if(selectedRadioButton == null){
            Toast.makeText(this,"충전할 포인트를 선택하세요",Toast.LENGTH_SHORT).show();
        }else {
            String selectedValue = selectedRadioButton.getText().toString();
            selectedValue = selectedValue.equals("무한") ? "00" : selectedValue;
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("POINTVALUE", selectedValue);
            startActivity(intent);
            this.finish();
        }
    }

    protected void pointPaymentCancelClick(View v){
        this.finish();
    }
}
