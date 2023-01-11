package com.example.terid_off;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageButton mypage_btn, engnote_page, dictionary_page, example_page, ybm_link;
    private TextView nickname;
    private Button logout_btn;
    private Toast toast;
    private long backKeyPressedTime = 0;
    long now = System.currentTimeMillis();

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if(System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this, "이용해 주셔서 감사합니다.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");

        nickname = findViewById(R.id.nickname);
        nickname.setText(userName);

        mypage_btn = findViewById(R.id.mypage_btn);
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
        });

        logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        engnote_page = findViewById(R.id.engnote_page);
        engnote_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EngnoteActivity.class);
                startActivity(intent);
                intent.putExtra("userName", userName);
            }
        });

        example_page = findViewById(R.id.example_page);
        example_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExampleActivity.class);
                startActivity(intent);
                intent.putExtra("userName", userName);
            }
        });

        dictionary_page = findViewById(R.id.dictionary_page);
        dictionary_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DictionaryActivity.class);
                startActivity(intent);
                intent.putExtra("userName", userName);
            }
        });

        ybm_link = findViewById(R.id.ybm_link);
        ybm_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://m.exam.toeic.co.kr/");
                Intent intent  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
}