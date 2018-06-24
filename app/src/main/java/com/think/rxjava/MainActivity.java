package com.think.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button btn1;
    protected Button btn2;
    protected Button btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            startActivity(new Intent(this, RxJavaDemo1Activity.class));
        } else if (view.getId() == R.id.btn2) {
            startActivity(new Intent(this, RxJavaDemo2Activity.class));
        } else if (view.getId() == R.id.btn3) {
            startActivity(new Intent(this, RxJavaDemo3Activity.class));
        }
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(MainActivity.this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(MainActivity.this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(MainActivity.this);
    }
}
