package com.hotsix.www.hotsixshoppingcart_module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dusdj.termp.R;

import org.w3c.dom.Text;

/**
 * Created by KSH's-PC on 2017-11-28.
 */

public class ShoppingCartListViewAdapter extends BaseAdapter {

    //ShoppingCart Variables
    Context context;
    String[] rank;
    String[] mealTicketOrderNumber;   // 식권주문번호
    String[] foodName;     // 음식명
    String[] storeName;    // 상점명
    LayoutInflater inflater;  // 뭔지 모르겠다.

    public ShoppingCartListViewAdapter(Context context, String[] rank, String[] foodName, String[] storeName, String[] mealTicketOrderNumber){
        this.context = context;
        this.rank = rank;
        this.foodName = foodName;
        this.storeName = storeName;
        this.mealTicketOrderNumber = mealTicketOrderNumber;
    }

    @Override
    public int getCount(){
        return rank.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //TextView Variables
        TextView rankTextView;
        TextView foodNameTextView;
        TextView storeNameTextView;
        TextView mealTicketNumberTextView;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.activity_shopping_cart_item,parent,false);

        //Local the TextViews in activity_shopping_cart_item.xml
        rankTextView = (TextView)itemView.findViewById(R.id.rankTextView);
        foodNameTextView = (TextView)itemView.findViewById(R.id.foodNameLabel);
        storeNameTextView = (TextView)itemView.findViewById(R.id.storeNameLabel);
        mealTicketNumberTextView = (TextView)itemView.findViewById(R.id.mealTicketNumberLabel);

        //Capture position and set to the TextViews
        rankTextView.setText(rank[position]);
        mealTicketNumberTextView.setText(mealTicketOrderNumber[position]);
        foodNameTextView.setText(foodName[position]);
        if(foodNameTextView.getText().length() > 6)
            foodNameTextView.setTextSize(10);
        storeNameTextView.setText(storeName[position]);

        return itemView;
    }
}
