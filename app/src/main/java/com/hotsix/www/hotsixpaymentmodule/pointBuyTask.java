package com.hotsix.www.hotsixpaymentmodule;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DusDj on 2017-12-08.
 */

/* Inner Class */
public class pointBuyTask extends AsyncTask<Void, Void, String> {

    private String url;
    private int userIndex = -1;
    private int price = -1;

    public pointBuyTask(String url, int userIndex, int price) {
        this.url = url;
        this.userIndex = userIndex;
        this.price = price;
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
        if (userIndex == -1 || price == -1)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터 키와 값.
            String key = "userIndex";
            String value = Integer.toString(userIndex);
            sbParams.append(key).append("=").append(value);

            sbParams.append("&");

            key = "plusPoint";
            value = Integer.toString(price);
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
        Log.i("결제완료 : ", "---------------" + price);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
}}