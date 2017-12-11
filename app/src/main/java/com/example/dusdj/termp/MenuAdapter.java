package com.example.dusdj.termp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by DusDj on 2017-11-28.
 * 메뉴 어댑터
 */

public class MenuAdapter extends ArrayAdapter<String>
{
    Context context;
    String[] strings;
    String[][] totals;
    String[][] tickets;
    int index;

    public MenuAdapter(Context context, String[] strings, String[][] totals, String[][] tickets, int index)
    {
        super(context, R.layout.content_menulist, strings);
        this.context = context;

        this.totals = totals;
        this.tickets = tickets;
        this.strings = strings;
        this.index = index;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.content_menulist, parent, false);

            TextView t1 = (TextView) rowView.findViewById(R.id.label);
            TextView t2 = (TextView) rowView.findViewById(R.id.label2);
            TextView t3 = (TextView) rowView.findViewById(R.id.label3);

            t1.setText(strings[index]);
            t2.setText(totals[index][position]);
            t3.setText(tickets[index][position]);
        return rowView;
    }
}