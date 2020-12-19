package com.example.newprogram4;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ShowData extends AppCompatActivity {

    private String username;
    private TextView welcome;
    private ImageView image;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        this.getLastPageValue();

        welcome = findViewById(R.id.welcome);
        image = findViewById(R.id.image);
    }

    // 获取上一个页面传来的username
    private void getLastPageValue(){
        Intent intent = getIntent();
        this.setUsername(intent.getStringExtra("username"));

        // 渲染用户名
        new Thread(new Runnable() {
            @Override
            public void run() {
                welcome.setText(String.format("Welcome %s",getUsername()));
            }
        }).start();
    }

    public void navigateToChangeUserInfo(View view){
        Intent intent = new Intent(ShowData.this, ChangeUserInfo.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }


    public void getData(View view){
        Toast toast=Toast.makeText(getApplicationContext(), "GO GO GO", Toast.LENGTH_SHORT);
        toast.show();

        image.setVisibility(0);
    }



}