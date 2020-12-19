package com.example.newprogram4;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Register extends AppCompatActivity {



    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private EditText age;
    private EditText teleno;
    private EditText email;
    private Switch switchGender;


    private String gender = "0";    // 0 是男，1 是女

    public void setGender(String gender) {
        this.gender = gender;
    }

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        age = findViewById(R.id.age);
        switchGender = findViewById(R.id.gender);
        //gender = findViewById(R.id.gender);
        teleno = findViewById(R.id.teleno);
        email = findViewById(R.id.email);

        // 监听switch事件
        switchGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    // 开启switch,女
                    setGender("1");
                } else {
                    // 关闭swtich，男
                    setGender("0");
                }
            }
        });

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


    public void onClickRegisterUser(View view){
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        final String confirmPassword = this.confirmPassword.getText().toString();
        final String age = this.age.getText().toString();
        final String teleno = this.teleno.getText().toString();
        final String email = this.email.getText().toString();
        final String gender = this.gender;

        // 正则匹配用户名，字母开头，字母数字下划线构成，5-10位
        final String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{4,9}$";

        // 正则匹配密码，必须为字母+数字，长度6——12   不能全是数字或者全是字母
        final String passwordPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,12}$";

        // 匹配年龄
        final String agePattern = "^((1[0-5])|[1-9])?\\d$";

        // 手机号
        final String telenoPattern = "^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";

        //email
        final String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";




        if (username.equals("")){
            // 用户名不能为空
            Toast toast=Toast.makeText(getApplicationContext(), "Username Cannot be Empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (username.length() < 5){
            // 用户名长度不小于5
            Toast toast=Toast.makeText(getApplicationContext(), "Username Length should Over 5", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (username.length() > 10){
            // 用户名长度不大于10
            Toast toast=Toast.makeText(getApplicationContext(), "Username Length Limited 10", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!username.matches(usernamePattern)){
            Toast toast=Toast.makeText(getApplicationContext(), "Username Inappropriate", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (password.equals("")){
            // 密码不能为空
            Toast toast=Toast.makeText(getApplicationContext(), "Password Cannot be Empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (password.length() > 12){
            // 密码长度不大于12
            Toast toast=Toast.makeText(getApplicationContext(), "Password Length Limited 12", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (password.length() < 6){
            // 密码长度不小于6
            Toast toast=Toast.makeText(getApplicationContext(), "Password Length Should be Over 6", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (!password.matches(passwordPattern)){
            Toast toast=Toast.makeText(getApplicationContext(), "Password Should Mix Letters ,Numbers and Underlines", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (!password.equals(confirmPassword)){
            Toast toast=Toast.makeText(getApplicationContext(), "Password Confirmed Incorrect", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (!age.matches(agePattern) && !age.equals("")){
            Toast toast=Toast.makeText(getApplicationContext(), "Age Inappropriate", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // 填了电话再匹配
        if (!teleno.matches(telenoPattern) && !teleno.equals("")){
            Toast toast=Toast.makeText(getApplicationContext(), "Invalid Teleno", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (!email.matches(emailPattern) && !email.equals("")){
            Toast toast=Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                String url = String.format("https://www.csgoha.xyz:8888/addPerson?username=%s&password=%s&age=%s&teleno=%s&email=%s&gender=%s",username,password,age,teleno,email,gender);
                System.out.println("url : " + url);
                System.out.println("连接获取数据...");
                String res = null;
                try {
                    res = Register.this.requestURL(url);
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
                    if (statusCode.equals("200")){
                        // 注册成功
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent=new Intent(Register.this,MainActivity.class);
                        //启动
                        startActivity(intent);
                        Looper.loop();// 进入loop中的循环，查看消息队列
                        // 跳转到登录界面
                    }else{
                        // 用户名已存在
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Username already Existed", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }
                }
            }
        }).start();
    }
}