package com.think.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zxn on 2019-4-15 12:57:47.
 */
public class RxJavaDemo4Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    protected Button btn1;
    protected Button btn2;
    private String mParam1;
    private String tag = "RxJavaDemo4Activity";

    public static void jumpTo(Context context, String param1) {
        Intent intent = new Intent(context, RxJavaDemo4Activity.class);
        intent.putExtra(ARG_PARAM1, param1);
        context.startActivity(intent);
    }

    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, RxJavaDemo4Activity.class);
        context.startActivity(intent);
    }

//    protected int getLayoutResId() {
//        return R.layout.activity_rx_java_demo4;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParam1 = getIntent().getStringExtra(ARG_PARAM1);
        super.setContentView(R.layout.activity_rx_java_demo4);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            testCount();
        } else if (view.getId() == R.id.btn2) {
            stop();
        }
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(RxJavaDemo4Activity.this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(RxJavaDemo4Activity.this);
    }

    Disposable disposable;  //   全局变量

    void testCount() {
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return aLong + 1;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {
                        Log.d(tag, "count: " + count);
                        if (count == 10) {
                            if (disposable != null) {
                                disposable.dispose();
                            }
                        }
                    }
                });
    }

    void stop() {
        if (disposable != null) {
            disposable.dispose();
        }
    }


}
