package com.example.newprogram4;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ChangeUserInfo extends AppCompatActivity {


    // 除了username，所有先前的数据都从数据库来，username从上个页面传值来
    private String previousUsername;
    private String previousPassword;
    private String previousAge;
    private String previousGender;
    private String previousTeleno;
    private String previousEmail;
    private String previousRegisterTime;

    public String getPreviousRegisterTime() {
        return previousRegisterTime;
    }

    public void setPreviousRegisterTime(String previousRegisterTime) {
        this.previousRegisterTime = previousRegisterTime;
    }

    public String getPreviousUsername() {
        return previousUsername;
    }

    public void setPreviousUsername(String previousUsername) {
        this.previousUsername = previousUsername;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    public String getPreviousAge() {
        return previousAge;
    }

    public void setPreviousAge(String previousAge) {
        this.previousAge = previousAge;
    }

    public String getPreviousGender() {
        return previousGender;
    }

    public void setPreviousGender(String previousGender) {
        this.previousGender = previousGender;
    }

    public String getPreviousTeleno() {
        return previousTeleno;
    }

    public void setPreviousTeleno(String previousTeleno) {
        this.previousTeleno = previousTeleno;
    }

    public String getPreviousEmail() {
        return previousEmail;
    }

    public void setPreviousEmail(String previousEmail) {
        this.previousEmail = previousEmail;
    }

    private EditText username;
    private EditText password;
    private EditText age;
    private EditText teleno;
    private EditText email;
    private Switch switchGender;
    private String gender = "0";    // 0 是男，1 是女
    private EditText confirmPassword;

    private TextView registerTime;

    public void setGender(String gender) {
        this.gender = gender;
    }

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        age = findViewById(R.id.age);
        switchGender = findViewById(R.id.gender);
        //gender = findViewById(R.id.gender);
        teleno = findViewById(R.id.teleno);
        email = findViewById(R.id.email);
        registerTime = findViewById(R.id.registerTime);
        confirmPassword = findViewById(R.id.confirmPassword);

        // 获取上一页的username
        this.getLastPageValue();
        // 从服务器获取信息
        this.getUserInfo();

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
    /**
     * 访问函数
     */
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


    private Handler handler = new Handler();

    /**
     * 从上一页面获取username
     */
    private void getLastPageValue(){
        Intent intent = getIntent();
        this.setPreviousUsername(intent.getStringExtra("username"));
    }

    /**
     * 从服务器获取完数据后将数据渲染到页面上
     */
    private void renderData(){
        Looper.prepare();
        username.setText(getPreviousUsername());
//        password.setText(getPreviousPassword());
//        confirmPassword.setText(getPreviousPassword());
        age.setText(getPreviousAge());
        teleno.setText(getPreviousTeleno());
        email.setText(getPreviousEmail());


        handler.post(new Runnable() {
            @Override
            public void run() {
                registerTime.setText(String.format("注册时间 %s", getPreviousRegisterTime()));
            }
        });
        //registerTime.setText(String.format("注册时间 %s",getPreviousRegisterTime()));
//        // 渲染注册时间
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                registerTime.setText(String.format("注册时间 %s",getPreviousRegisterTime()));
//            }
//        }).start();


        // 性别和用户名不能变
        switchGender.setClickable(false);
        username.setFocusableInTouchMode(false);

        if (getPreviousGender().equals("0")){
            // 男
            switchGender.setChecked(false);
        } else {
            switchGender.setChecked(true);
        }
        Looper.loop();// 进入loop中的循环，查看消息队列
    }



    /**
     * 从服务器获取用户数据作为默认值
     */
    private void getUserInfo(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String url = String.format("https://www.csgoha.xyz:8888/queryPerson?username=%s",getPreviousUsername());
                System.out.println("url : " + url);
                System.out.println("连接获取数据...");
                String res = null;
                try {
                    res = ChangeUserInfo.this.requestURL(url);
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
                        // 获取旧信息成功
                        // 解析获取到的JSON并填充到previous数据中
                        JSONObject person = object.getJSONObject("userInfo");
                        setPreviousUsername(person.getString("username"));
                        setPreviousPassword(person.getString("password"));
                        setPreviousAge(person.getString("age"));
                        setPreviousGender(person.getString("gender"));
                        setPreviousTeleno(person.getString("teleno"));
                        setPreviousEmail(person.getString("email"));
                        setPreviousRegisterTime(person.getString("time").toString());
                        // 渲染信息
                        renderData();
                    }else{
                        // 获取旧信息失败
                        // 该人不存在，不该出现这种情况
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Fatal Error! User doesn't Exist", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }
                }
            }
        }).start();
    }


    public void onClickChangeUserInfo(View view){
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        final String age = this.age.getText().toString();
        final String teleno = this.teleno.getText().toString();
        final String email = this.email.getText().toString();
        final String confirmPassword = this.confirmPassword.getText().toString();
        final String gender = this.gender;


        // 正则匹配密码，必须为字母+数字，长度6——12
        final String passwordPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";

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

        if (username.length() > 10){
            // 用户名长度不大于10
            Toast toast=Toast.makeText(getApplicationContext(), "Username Length Limited 10", Toast.LENGTH_SHORT);
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
            Toast toast=Toast.makeText(getApplicationContext(), "Password Should Mix Letters and Numbers", Toast.LENGTH_SHORT);
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
                String url = String.format("https://www.csgoha.xyz:8888/updatePerson?username=%s&password=%s&age=%s&teleno=%s&email=%s",username,password,age,teleno,email);
                System.out.println("url : " + url);
                System.out.println("连接获取数据...");
                String res = null;
                try {
                    res = ChangeUserInfo.this.requestURL(url);
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
                        Toast toast=Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent=new Intent(ChangeUserInfo.this,MainActivity.class);
                        //启动
                        startActivity(intent);
                        Looper.loop();// 进入loop中的循环，查看消息队列
                        // 跳转到登录界面
                    }else{
                        // 用户名已存在
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "Fatal Error! User doesn't Exist", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();// 进入loop中的循环，查看消息队列
                    }
                }
            }
        }).start();

    }
}