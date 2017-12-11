package com.example.dusdj.termp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hotsix.www.hotsixpaymentmodule.PaymentPointSelectActivity;
import com.hotsix.www.hotsixshoppingcart_module.ShoppingCartActivity;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] stores;
    String[] itemTotals;
    String[] itemTickets;
    int userIndex = -1;

    String userId = "000000000";
    int userPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Login 됐다고 침.
        userIndex = 1;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //For user db
        String url2 = "http://zxc293.cafe24.com/hotsix/user.jsp";
        UserTask task2 = new UserTask(url2);
        task2.execute();

        //For db Store
        String url = "http://zxc293.cafe24.com/hotsix/store.jsp";
        CustomTask task = new CustomTask(url);
        task.execute();
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        //For user db
        String url2 = "http://zxc293.cafe24.com/hotsix/user.jsp";
        UserTask task2 = new UserTask(url2);
        task2.execute();

        //For db Store
        String url = "http://zxc293.cafe24.com/hotsix/store.jsp";
        CustomTask task = new CustomTask(url);
        task.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
                    .setTitle("종료창")
                    .setMessage("정말 앱을 종료하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.finish();
                        }
                    })
                    .setNegativeButton("아니요",null)
                    .show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {


        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(MainActivity.this,  ShoppingCartActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_point) {
            Intent i = new Intent(MainActivity.this,  PaymentPointSelectActivity.class);
            startActivityForResult(i,1);

        } else if (id == R.id.nav_logOut) {
            //For logOut (point 초기화)
            String url = "http://zxc293.cafe24.com/hotsix/pointReset.jsp";
            pointResetTask task = new pointResetTask(url, userIndex);
            task.execute();
            this.onRestart();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void makeStoreList(){
        ListView mainListView = (ListView)findViewById(R.id.mainListView);

        // Create Store List
        CustomAdapter adpt = new CustomAdapter(this, stores, itemTotals, itemTickets);
        mainListView.setAdapter(adpt);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,  MenuActivity.class);
                i.putExtra("storeIndex", position);
                startActivity(i);
            }
        });
    }

    public void makeUserNav(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvStuNumber = (TextView) navigationView.findViewById(R.id.stuNumber);
        tvStuNumber.setText(userId);
        TextView tvStuPoint = (TextView) navigationView.findViewById(R.id.stuPoint);
        tvStuPoint.setText(Integer.toString(userPoint));
    }


    /* Inner Class */
    public class CustomTask extends AsyncTask<Void, Void, String> {

        private String url;

        public CustomTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {

            // HttpURLConnection 참조 변수.
            HttpURLConnection urlConn = null;

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

            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String storename = jObject.getString("storename");
                String standtotal = jObject.getString("standtotal");
                String nowticket = jObject.getString("nowticket");

                sb.append(
                        "storename:" + storename +
                                "standtotal:" + standtotal +
                                "nowticket:" + nowticket + "\n"
                );

                st1.add(storename);
                st2.add(standtotal);
                st3.add(nowticket);

                stores = st1.toArray(new String[st1.size()]);
                itemTotals = st2.toArray(new String[st2.size()]);
                itemTickets = st3.toArray(new String[st3.size()]);

            }
            Log.i("가져온 정보 : ", sb.toString());
            makeStoreList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()



    /* Inner Class */
    public class UserTask extends AsyncTask<Void, Void, String> {

        private String url;

        public UserTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {

            // HttpURLConnection 참조 변수.
            HttpURLConnection urlConn = null;

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

            doJSONParserUser(s);
        }
    }

    void doJSONParserUser(String result){
        StringBuffer sb = new StringBuffer();
        String str = result;

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성

            //dummy arrayList
            String st1;
            int st2;

            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String id = jObject.getString("userid");
                String point = jObject.getString("userpoint");

                sb.append(
                        "id:" + id +
                                "point:" + point + "\n"
                );

                st1 = id;
                st2 = Integer.parseInt(point);

                userId = st1;
                userPoint = st2;

            }
            Log.i("가져온 정보 : ", sb.toString());
            makeUserNav();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()

    /* Inner Class */
public class pointResetTask extends AsyncTask<Void, Void, String> {

    private String url;
    private int userIndex = -1;

    public pointResetTask(String url, int userIndex) {
        this.url = url;
        this.userIndex = userIndex;
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
        Log.i("리셋완료 : ", "---------------");
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
}}
}




