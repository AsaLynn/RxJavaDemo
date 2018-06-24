package com.think.rxjava;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.demonstrate.DemonstrateUtil;
import com.example.demonstrate.DialogUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJavaDemo3Activity extends AppCompatActivity implements View.OnClickListener {

    protected Button btnBackpressure;
    private Flowable mFlowable;
    private Subscriber mSubscriber;
    private Subscription mSubscription;
    private Flowable flowableLATEST;
    private Subscriber subscriberLatest;
    private Subscription subscriptionLatest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_rx_java_demo3);
        initView();
        init4();
        init6();
    }

    private void init6() {
        flowableLATEST = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                for (int i = 1; i<=200; i++) {
                    emitter.onNext(i);
                    DemonstrateUtil.showLogResult("LATEST生产onNext:"+i);
                }
            }
        }, BackpressureStrategy.LATEST);

        //mSubscription = s;
        subscriberLatest = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscriptionLatest = s;
                s.request(100);
            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("Latest消费onNext:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                DemonstrateUtil.showLogResult("onError");
                DemonstrateUtil.showLogResult(t.getMessage());
                DemonstrateUtil.showLogResult(t.toString());
            }

            @Override
            public void onComplete() {
                DemonstrateUtil.showLogResult("onComplete");
            }
        };
    }

    private void init4() {
        mFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                for (int i = 1; i <= 200; i++) {
                    emitter.onNext(i);
                    DemonstrateUtil.showLogResult("生产onNext:"+i);
                }
            }
        }, BackpressureStrategy.DROP);

        //mSubscription = s;
        mSubscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
                s.request(100);
            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("消费onNext:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                DemonstrateUtil.showLogResult("onError");
                DemonstrateUtil.showLogResult(t.getMessage());
                DemonstrateUtil.showLogResult(t.toString());
            }

            @Override
            public void onComplete() {
                DemonstrateUtil.showLogResult("onComplete");
            }
        };

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_backpressure) {
            DialogUtil.showListDialog(this, "Flowable的理解使用", new String[]{
                    "0事件堆积现象",
                    "1正常使用策略ERROR!",
                    "2使用策略ERROR出现的异常!",
                    "3使用策略BUFFER,更大的缓存池",
                    "4使用策略DROP,事件关联100",
                    "5使用策略DROP,再申请100",
                    "6使用策略LATEST,事件关联100",
                    "7使用策略LATEST,再申请100",
                    "8使用策略MISSING",
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
                        case 3:
                            show3();
                            break;
                        case 4:
                            show4();
                            break;
                        case 5:
                            show5();
                            break;
                        case 6:
                            show6();
                            break;
                        case 7:
                            show7();
                            break;
                        case 8:
                            show8();
                            break;
                    }
                }
            });
        }
    }

    private void show8() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 200; i++) {
                    DemonstrateUtil.showLogResult("MISSING-生成emitter" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.MISSING).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //mSubscription = s;
                        //s.request(0);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        DemonstrateUtil.showLogResult("MISSING-消费onNext" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DemonstrateUtil.showLogResult("onError" + t.getMessage());
                        DemonstrateUtil.showLogResult("onError" + t.toString());
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        DemonstrateUtil.showLogResult("onComplete");
                    }
                });
    }

    private void show7() {
        subscriptionLatest.request(100);
    }

    private void show6() {
        flowableLATEST.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberLatest);
    }

    private void show5() {
        //128-100-100=  -72.
        mSubscription.request(100);
    }

    private void show4() {
        mFlowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }


    private void show3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 200; i++) {
                    DemonstrateUtil.showLogResult("emitter" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //mSubscription = s;
                        //s.request(0);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        DemonstrateUtil.showLogResult("onNext" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DemonstrateUtil.showLogResult("onError" + t.getMessage());
                        DemonstrateUtil.showLogResult("onError" + t.toString());
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        DemonstrateUtil.showLogResult("onComplete");
                    }
                });
    }

    private void show2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 200; i++) {
                    DemonstrateUtil.showLogResult("emitter" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //mSubscription = s;
                        //s.request(0);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        DemonstrateUtil.showLogResult("onNext" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DemonstrateUtil.showLogResult("onError" + t.getMessage());
                        DemonstrateUtil.showLogResult("onError" + t.toString());
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        DemonstrateUtil.showLogResult("onComplete");
                    }
                });
    }

    private void show1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 127; i++) {//128---   0--->126
                    DemonstrateUtil.showLogResult("emitter " + i);
                    emitter.onNext(i);
                }
                DemonstrateUtil.showLogResult("emitter complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR) //增加了一个参数,设置处理策略.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        DemonstrateUtil.showLogResult("onSubscribe");
                        //用来向生产者申请可以消费的事件数量,这样我们便可以根据本身的消费能力进行消费事件.
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        DemonstrateUtil.showLogResult("onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DemonstrateUtil.showLogResult("onError: " + t.getMessage());
                        DemonstrateUtil.showLogResult("onError: " + t.toString());
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        DemonstrateUtil.showLogResult("onComplete: ");
                    }
                });
    }

    private void show0() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                while (true) {
                    for (int i = 0; i < 129; i++) {
                        e.onNext(1);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(5000);
                        DemonstrateUtil.showLogResult("接受到" + integer);
                    }
                });
    }

    private void initView() {
        btnBackpressure = (Button) findViewById(R.id.btn_backpressure);
        btnBackpressure.setOnClickListener(RxJavaDemo3Activity.this);
    }
}
