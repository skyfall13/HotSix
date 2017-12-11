package com.hotsix.www.hotsixshoppingcart_module;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dusdj.termp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {

    // ShoppingCart Variables
    ListView list;
    ShoppingCartListViewAdapter shoppingCartListViewAdapter;
    String[] rank;
    String[] mealTicketOrderNumber;   // 식권주문번호
    String[] foodName;     // 음식명
    String[] storeName;    // 상점명
    int userIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        //For db URL
        String url = "http://zxc293.cafe24.com/hotsix/cart.jsp";
        CustomTask task = new CustomTask(url);
        task.execute();

    }

    @Override
    protected void onRestart(){
        super.onRestart();

        //For db URL
        String url = "http://zxc293.cafe24.com/hotsix/cart.jsp";
        CustomTask task = new CustomTask(url);
        task.execute();
    }

    public void BackButton_onClick(View v){
        this.finish();
    }


    public void makeCartList(){
         /* Create cart list */
        // activity_shopping_cart 에 위치한 listView
        list = (ListView)findViewById(R.id.shoppingCartListView);

        //pass results to ListViewAdapter class
        shoppingCartListViewAdapter = new ShoppingCartListViewAdapter(ShoppingCartActivity.this, rank ,foodName, storeName, mealTicketOrderNumber);

        // Adapter와 ListView를 결합(Bind)함
        list.setAdapter(shoppingCartListViewAdapter);

        //capture ListView item Click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShoppingCartActivity.this, MealTicketViewAvtivity.class);

                //Pass all data rank
                intent.putExtra("position",position);
                intent.putExtra("rank",rank);
                intent.putExtra("foodName",foodName);
                intent.putExtra("storeName",storeName);
                intent.putExtra("mealTicketOrderNumber",mealTicketOrderNumber);
                startActivity(intent);
            }
        });
    }

    /* Inner Class */
    public class CustomTask extends AsyncTask<Void, Void, String> {

        private String url;

        public CustomTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {

            //값 전달용
            StringBuffer sbParams = new StringBuffer();
            // HttpURLConnection 참조 변수.
            HttpURLConnection urlConn = null;

            /**
             * 1. StringBuffer에 파라미터 연결
             * */
            // 보낼 데이터가 없으면 파라미터를 비운다.
            if (userIndex == -1)
                sbParams.append("");
                // 보낼 데이터가 있으면 파라미터를 채운다.
            else {
                // 파라미터 키와 값.
                String key = "userIndex";
                String value = Integer.toString(userIndex);
                sbParams.append(key).append("=").append(value);
                Log.i("parameter : ", "\n\n" + sbParams.toString() + "\n\n");
            }
        /*
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
            try{
                URL url = new URL(this.url);
                urlConn = (HttpURLConnection) url.openConnection();

                // [2-1]. urlConn 설정.
                urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                // [2-2]. parameter 전달 및 데이터 읽어오기.
                String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
                OutputStream os = urlConn.getOutputStream();
                os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.


                // [2-3]. 연결 요청 확인.
                // 실패 시 null을 리턴하고 메서드를 종료.
                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return null;

                // [2-4]. 읽어온 결과물 리턴.
                // 요청한 URL의 출력물을 BufferedReader로 받는다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                // 출력물의 라인과 그 합에 대한 변수.
                String line;
                String page = "";

                // 라인을 받아와 합친다.
                while ((line = reader.readLine()) != null){
                    page += line;
                }

                return page;

            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            doJSONParser(s);
        }
    }

    void doJSONParser(String result){
        StringBuffer sb = new StringBuffer();
        String str = result;

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성

            //dummy arrayList
            ArrayList<String> st1 = new ArrayList<String>();
            ArrayList<String> st2 = new ArrayList<String>();
            ArrayList<String> st3 = new ArrayList<String>();
            ArrayList<String> st4 = new ArrayList<String>();

            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String cartindex = jObject.getString("cartindex");
                String menuname = jObject.getString("menuname");
                String ticketordernumber = jObject.getString("ticketordernumber");
                String strname = jObject.getString("storename");

                sb.append(
                        "cartindex:" + cartindex +
                                "tiketordernumber:" + ticketordernumber +
                                "menuname:" + menuname+
                                "storename:" + strname + "\n"
                );

                st1.add(cartindex);
                st2.add(ticketordernumber);
                st3.add(menuname);
                st4.add(strname);

                rank = st1.toArray(new String[st1.size()]);
                mealTicketOrderNumber = st2.toArray(new String[st2.size()]);
                foodName = st3.toArray(new String[st3.size()]);
                storeName = st4.toArray(new String[st4.size()]);
            }
            Log.i("가져온 정보 : ", sb.toString());
            if(rank != null)
                makeCartList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()
}
