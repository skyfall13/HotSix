package com.hotsix.www.hotsixshoppingcart_module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dusdj.termp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by KSH's-PC on 2017-11-29.
 */

public class MealTicketViewAvtivity extends AppCompatActivity {

    //TextView 변수 정의
    TextView foodNameTextView;
    TextView storeNameTextView;
    TextView mealTicketNumberTextView;
    ImageView QRCodeImageView;


    //String[] rank;
    String[] mealTicketOrderNumber;   // 식권주문번호
    String[] foodName;     // 음식명
    String[] storeName;    // 상점명
    String inputQRText;    // QR코드로 만들어줄 Text값
    String inputText;
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealticket);


        //메인Activity에서 listView item 클릭시
        Intent intent = getIntent();

        //아이템뷰 리스트 클릭시 포지션 가져오기
        position = intent.getExtras().getInt("position");
        foodName = intent.getStringArrayExtra("foodName");
        storeName = intent.getStringArrayExtra("storeName");
        mealTicketOrderNumber = intent.getStringArrayExtra("mealTicketOrderNumber");

        //activity_mealticket.xml 안의 TextView 위치
        mealTicketNumberTextView=(TextView)findViewById(R.id.mealtiTicketLayout_mealTicketNumberLabel);
        foodNameTextView =(TextView)findViewById(R.id.mealtiTicketLayout_foodNameLabel);
        storeNameTextView = (TextView)findViewById(R.id.mealtiTicketLayout_storeNameLabel);
        QRCodeImageView = (ImageView)findViewById(R.id.mealTicketBarcodeImageView);

        //position 순대로 TextView에 해당 리스트 뷰의 값들을 넣음. / 식권번호, 음식명, 식당명, 추후 바코드까지.
        foodNameTextView.setText(foodName[position]);
        mealTicketNumberTextView.setText(mealTicketOrderNumber[position]);
        storeNameTextView.setText(storeName[position]);

        inputQRText = "{" +"foodName:"+ foodName[position] +","+"storeName:"+storeName[position]+","+"mealTicketOrderNumber:"+mealTicketOrderNumber[position]+"}";

        makeQRcode();
    }

    public void mealTicketBackButton_onClick(View v){
        this.finish();
    }

    public void makeQRcode()
    {

        // 한글깨짐 수정 부분 ISO-8859-1이 기본 인코딩 폼이라 이를 UTF-8로 하여서 수정함.
        try{
            inputText = new String(inputQRText.getBytes("UTF-8"),"ISO-8859-1");
        }catch(Exception e){
            e.printStackTrace();
        }

        //오픈소스 사용
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{

            BitMatrix bitMatrix = multiFormatWriter.encode(inputText, BarcodeFormat.QR_CODE,200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            // 식권 QR코드 이미지 뷰에 QR코드 이미지 출력
            QRCodeImageView.setImageBitmap(bitmap);

        }catch(WriterException e){
            e.printStackTrace();
        }
    }
}
