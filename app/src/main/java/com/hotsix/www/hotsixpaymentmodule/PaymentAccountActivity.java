package com.hotsix.www.hotsixpaymentmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dusdj.termp.R;

import java.util.Random;

/**
 * Created by KSH's-PC on 2017-11-24.
 */

public class PaymentAccountActivity extends AppCompatActivity{

    protected String randomOrderNumber;
    protected String paymentValue;
    pointBuyTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountpayment);

        //이전 Activity에서 주문 번호 ,충전할 포인트 값을 가져옴.
        paymentValue = getIntent().getStringExtra("POINTVALUE"); // PaymentPointSelectActivity에서 생성
        randomOrderNumber = getIntent().getStringExtra("ORDERNUMBER"); // 주문번호 PaymentActivity에서 생성

        //주문번호 TextView
        TextView accountorderNumberView = (TextView)findViewById(R.id.AccountOrderNumber);
        accountorderNumberView.setText(randomOrderNumber);

        // 가격 TextView
        TextView accountPriceView = (TextView)findViewById(R.id.AccountPayPriceView);
        accountPriceView.setText(paymentValue);

        // price DB로 넘겨주는 부분
        int price = Integer.parseInt(tokenizePrice(paymentValue));
//        Toast.makeText(this,"보내줄 가격: " + price,Toast.LENGTH_SHORT).show(); // price값 테스트 Toast

        //Login 됐다고 침.
        int userIndex = 1;
        String url = "http://zxc293.cafe24.com/hotsix/pointBuy.jsp";
        task = new pointBuyTask(url, userIndex, price);
    }

    protected void onAccountAcceptClick(View v){

        /* DB for Point Plus */
        task.execute();

        Toast.makeText(this,"계좌이체 완료",Toast.LENGTH_SHORT).show();
        this.finish();
    }

    protected void onAccountCencelClick(View v){
        this.finish();
    }

    protected String tokenizePrice(String str){
        String[] result = str.split(" ");
        return result[0];
    }

}
