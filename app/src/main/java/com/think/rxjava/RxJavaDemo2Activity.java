package com.think.rxjava;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.demonstrate.DemonstrateUtil;
import com.example.demonstrate.DialogUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJavaDemo2Activity extends AppCompatActivity implements View.OnClickListener {

    protected Button btn;
    protected ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_rx_java_demo2);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            DialogUtil.showListDialog(this, "rxJava操作！", new String[]{
                    "0发送事件io线程并变换主线程接收",
                    "1子线程发送事件主线程接收",
                    "2默认线程发送事件默认线程接收",
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            show0();
                            break;
                        case 1:
                            show1();
                            break;
                        case 2:
                            show2();
                            break;
                    }
                }
            });
        }
    }

    private void show2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                DemonstrateUtil.showLogResult("发送的线程名称：" + Thread.currentThread().getName());
                DemonstrateUtil.showLogResult("发送的线程id：" + Thread.currentThread().getId());

                DemonstrateUtil.showLogResult("发送的数据:" + 1);
                e.onNext(1);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                DemonstrateUtil.showLogResult("接收的线程：" + Thread.currentThread().getName());
                DemonstrateUtil.showLogResult("接收的线程id：" + Thread.currentThread().getId());
                DemonstrateUtil.showLogResult("接收到的数据:-integer:" + integer);
            }
        });
    }

    private void show1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                DemonstrateUtil.showLogResult("发送的线程名称：" + Thread.currentThread().getName());
                DemonstrateUtil.showLogResult("发送的线程id：" + Thread.currentThread().getId());

                DemonstrateUtil.showLogResult("发送的数据:" + 1);
                e.onNext(1);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        DemonstrateUtil.showLogResult("接收的线程：" + Thread.currentThread().getName());
                        DemonstrateUtil.showLogResult("接收的线程id：" + Thread.currentThread().getId());
                        DemonstrateUtil.showLogResult("接收到的数据:-integer:" + integer);
                    }
                });
    }

    private void show0() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                DemonstrateUtil.showLogResult("所在的线程：", Thread.currentThread().getName());
                DemonstrateUtil.showLogResult("发送的数据:", 1 + "");
                e.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        DemonstrateUtil.showLogResult("所在的线程：", Thread.currentThread().getName());
                        DemonstrateUtil.showLogResult("接收到的数据:", "integer:" + integer);
                    }
                });
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(RxJavaDemo2Activity.this);
        iv = (ImageView) findViewById(R.id.iv);
    }
}
