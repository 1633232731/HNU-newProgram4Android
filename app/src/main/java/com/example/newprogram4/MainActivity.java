package com.example.newprogram4;

import android.content.Intent;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    private final OkHttpClient client = new OkHttpClient();

//    static class Person{
//        String username;
//        String password;
//        String age;
//        String gender;
//        String teleno;
//        String email;
//        Date time;
//        public Person() {
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }



    private String requestURL(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
//            String res = Objects.requireNonNull(response.body()).string();
//            JSONObject jsonObject = JSON.parseObject(res);
            return response.body().string();
            //JSONObject jsonObject = response.body().string();
            //System.out.println(jsonObject);
        } else {
            Looper.prepare();
            Toast toast=Toast.makeText(getApplicationContext(), "Network Connection Failed", Toast.LENGTH_SHORT);
            toast.show();
            Looper.loop();// 进入loop中的循环，查看消息队列
            throw new IOException("Unexpected code " + response);
        }
    }






    public void onClickLogin(View view){
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        if (username.equals("")){
            // 没有输入用户名
            Toast toast=Toast.makeText(getApplicationContext(), "Username Cannot be Empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (password.equals("")){
            // 没有输入密码
            Toast toast=Toast.makeText(getApplicationContext(), "Password Cannot be Empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
                String url = String.format("https://www.csgoha.xyz:8888/login?username=%s&password=%s",username,password);
                System.out.println("url : " + url);
                System.out.println("连接获取数据...");
                String res = null;
                try {
                    res = MainActivity.this.requestURL(url);
                } catch (IOException e) {
                    Looper.prepare();
                    Toast toast=Toast.makeText(getApplicationContext(), "Network Connection Failed", Toast.LENGTH_SHORT);
                    toast.show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                    e.printStackTrace();
                }

                if (res != null){
                    // 将json字符串转为json对象
                    JSONObject object = JSON.parseObject(res);
                    System.out.println(object.toJSONString());
                    // 获取状态码
                    String statusCode = (String) object.get("statusCode");
//                    JSONObject person = object.getJSONObject("userInfo");
//                    String username = person.getString("username");
//                    System.out.println(username);
//                    System.out.println(statusCode);

                    //assert statusCode != null;

                    if (statusCode.equals("200")){
                        // 登录成功
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Access Successful", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(MainActivity.this, ShowData.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }else if(statusCode.equals("404")){
                        // 查无此人
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Invalid Password or Username", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }else{
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Invalid Password or Username", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }

                }
            }
        }).start();

    }

    public void onClickRegister(View view) {
        Intent intent=new Intent(MainActivity.this, Register.class);
        //启动
        startActivity(intent);
    }
}