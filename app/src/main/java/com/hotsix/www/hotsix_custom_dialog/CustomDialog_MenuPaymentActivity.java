package com.hotsix.www.hotsix_custom_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dusdj.termp.MainActivity;
import com.example.dusdj.termp.MenuActivity;
import com.example.dusdj.termp.R;


/**
 * Created by KSH's-PC on 2017-12-06.
 */

public class CustomDialog_MenuPaymentActivity extends Dialog implements View.OnClickListener{

    private static final int LAYOUT = R.layout.customdialog_menupayment;

    //View 정의
    private TextView foodNameTV;
    private TextView pointViewTV;
    private TextView paymentCostTV;
    private Button paymentAcceptButton;
    private Button paymentCancelButton;

    // 생성자 호출시 필요한 변수 정의
    private Context context;
    private String foodName;
    private int position; //2차원 배열 두번째 index
    private int point;
    private int costPoint;
    private String[] stores;
    private String[] itemMenu;
    private String[] menuCost;

    //for menuBuy
    private int userIndex = -1;
    private int storeIndex = -1;
    private String[] menuIndex;

    private menuBuyTask task;



    public CustomDialog_MenuPaymentActivity(Context context){
        super(context,R.style.MyCustomDialogStyle);
    }
    //생성자
    public  CustomDialog_MenuPaymentActivity(Context context, int index, int position, String[] storesdata, String[] itemMenudata, String[] menuCostData, int point, int userIndex, String[] menuIndex, int storeIndex){
        super(context, R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        this.context = context;
        this.position = position;
        this.stores = storesdata;
        this.itemMenu = itemMenudata;
        this.menuCost = menuCostData;
        this.point = point; // 추후 변경 소지 있음.

        //for menuBuy
        this.userIndex = userIndex;
        this.storeIndex = storeIndex;
        this.menuIndex = menuIndex;

        //requestWindowFeature(Window.FEATURE_NO_TITLE); // 제목 없애는 부분
        setTitle("결제창");
        setContentView(R.layout.customdialog_menupayment);
        this.setCanceledOnTouchOutside(false); //외부 클릭시 사라지지 않음
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        foodNameTV = (TextView)findViewById(R.id.CustomDialog_FoodNameView);
        pointViewTV = (TextView)findViewById(R.id.CustomDialog_PointView);
        paymentCostTV = (TextView)findViewById(R.id.CustomDialog_PaymentCost);
        paymentAcceptButton = (Button)findViewById(R.id.CustomDialog_PaymentAccept);
        paymentCancelButton = (Button)findViewById(R.id.CustomDialog_PaymentCancel);

        foodNameTV.setText(itemMenu[position]);
        pointViewTV.setText(Integer.toString(point));
        paymentCostTV.setText(menuCost[position]);

        costPoint = Integer.parseInt(menuCost[position]);

        paymentAcceptButton.setOnClickListener(this);
        paymentCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.CustomDialog_PaymentAccept:
                int resultPoint;

                if(((point - costPoint) < 0 )  || point <= 0){
                    Toast.makeText(getContext(),"포인트가 부족합니다",Toast.LENGTH_SHORT).show();
                }else {
                    resultPoint = point - costPoint;
                    System.out.println(resultPoint);

                    //최종결제
                    String url = "http://zxc293.cafe24.com/hotsix/menuBuy.jsp";
                    task = new menuBuyTask(url, userIndex, Integer.parseInt(menuIndex[position]), storeIndex);
                    // 1. 매장 대기인원++, nowTicket++
                    // 2. 장바구니에 userindex, menuIndex, storeIndex 를 넣는다. cartIndex는 ++, 식권번호는 1에서 가져옴.
                    // 3. 유저의 index로 검색해서 검색한 menu의 price만큼 포인트 차감.
                    task.execute();

                    Toast.makeText(getContext(),"메뉴 결제 완료",Toast.LENGTH_SHORT).show();
                    ((MenuActivity) MenuActivity.menuContext).finish();
                }

                this.dismiss();
                break;

            case R.id.CustomDialog_PaymentCancel:
                //((MenuActivity) MenuActivity.menuContext).finish();
                this.dismiss();
                break;

        }
    }
}