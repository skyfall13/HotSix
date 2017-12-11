package com.hotsix.www.hotsixpaymentmodule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dusdj.termp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by KSH's-PC on 2017-11-24.
 */

public class PaymentCardActivity extends AppCompatActivity {

    protected String randomOrderNumber;
    protected String paymentValue;
    protected String totalCardNumber;
    pointBuyTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardpayment);

        paymentValue = getIntent().getStringExtra("POINTVALUE");
        randomOrderNumber = getIntent().getStringExtra("ORDERNUMBER");

        //주문번호 TextView
        TextView cardOrderNumberView = (TextView)findViewById(R.id.CardOrderNumber);
        cardOrderNumberView.setText(randomOrderNumber);

        // 가격 TextView
        TextView cardPriceView = (TextView)findViewById(R.id.CardPayPriceView);
        cardPriceView.setText(paymentValue);

        // price DB로 넘겨주는 부분
        int price = Integer.parseInt(tokenizePrice(paymentValue));
        //Toast.makeText(this,"보내줄 가격: " + price,Toast.LENGTH_SHORT).show(); // price값 테스트 Toast

        //Login 됐다고 침.
        int userIndex = 1;
        String url = "http://zxc293.cafe24.com/hotsix/pointBuy.jsp";
        task = new pointBuyTask(url, userIndex, price);

    }

    protected void onCardAcceptClick(View v){

        mergeCardNumber();

        /* DB for Point Plus */
        task.execute();
        Toast.makeText(this,"결제 완료",Toast.LENGTH_SHORT).show();
        this.finish();
    }

    protected void onCardCencelClick(View v){
        this.finish();
    }

    protected String tokenizePrice(String str){
        String[] result = str.split(" ");
        return result[0];
    }

    protected void mergeCardNumber()
    {
        // 카드번호 저장부
        String cardNumber1;
        String cardNumber2;
        String cardNumber3;
        String cardNumber4;

        EditText cardNumberEditText1 = (EditText)findViewById(R.id.CardNumber1);
        cardNumber1 = cardNumberEditText1.getText().toString();

        EditText cardNumberEditText2 = (EditText)findViewById(R.id.CardNumber2);
        cardNumber2 = cardNumberEditText2.getText().toString();

        EditText cardNumberEditText3 = (EditText)findViewById(R.id.CardNumber3);
        cardNumber3 = cardNumberEditText3.getText().toString();

        EditText cardNumberEditText4 = (EditText)findViewById(R.id.CardNumber4);
        cardNumber4 = cardNumberEditText4.getText().toString();

        totalCardNumber = cardNumber1+cardNumber2+cardNumber3+cardNumber4;
    }

}
