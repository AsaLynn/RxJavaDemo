package com.think.rxjava;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.demonstrate.DemonstrateUtil;
import com.example.demonstrate.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static io.reactivex.Observable.fromIterable;

public class RxJavaDemo1Activity extends AppCompatActivity implements View.OnClickListener {

    protected Button btnSend1;
    protected Button btnSend2;
    protected Button btnSend3;
    protected Button btnSend4;
    protected Button btnSend5;
    protected Button btnSend6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_rx_java_demo1);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send1) {
            test1();//普通使用
        } else if (view.getId() == R.id.btn_send2) {
            test2();//链式调用
        } else if (view.getId() == R.id.btn_send3) {
            test3();//发送中,中断.
        } else if (view.getId() == R.id.btn_send4) {
            test4();//只关心onnext事件的操作
        } else if (view.getId() == R.id.btn_send5) {
            test5();//几种被观察者的创建方式
        } else if (view.getId() == R.id.btn_send6) {
            test6();//常用的操作符
        } else if (view.getId() == R.id.btn_send_test) {
            //发送练习1
            RxJavaDemo4Activity.jumpTo(this);
        }

    }



    private void test6() {
        DialogUtil.showListDialog(this, "rxjava的操作符号使用", new String[]{
                "0map()操作符",
                "1flatMap()操作符",
                "2filter()操作符",
                "3take()操作符",
                "4doOnNext()操作符",
                "5flatMap()操作符转换集合",
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        map0();
                        break;
                    case 1:
                        map1();
                        break;
                    case 2:
                        map2();
                        break;
                    case 3:
                        map3();
                        break;
                    case 4:
                        map4();
                        break;
                    case 5:
                        map5();
                        break;
                }
            }
        });
    }

    private void map5() {
        ArrayList<Integer> item = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            item.add(0);
        }

        Observable
                .just(item)
                .flatMap(new Function<List<Integer>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource apply(List<Integer> integers) throws Exception {
                        Observable<Integer> iterable = Observable.fromIterable(integers);
                        return iterable;
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                DemonstrateUtil.showLogResult(s.toString());
            }
        });


    }

    private void map4() {
        Observable.just(new ArrayList<String>() {
            {
                for (int i = 0; i < 6; i++) {
                    add("data" + i);
                }
            }
        }).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return fromIterable(strings);
            }
        }).take(5).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                DemonstrateUtil.showLogResult("额外的准备工作!");
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                DemonstrateUtil.showLogResult(s.toString());
            }
        });
    }

    private void map3() {
        Observable.just(new ArrayList<String>() {
            {
                for (int i = 0; i < 8; i++) {
                    add("data" + i);
                }
            }
        }).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return fromIterable(strings);
            }
        }).take(10).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                DemonstrateUtil.showLogResult(s.toString());
            }
        });
    }

    private void map2() {
        Observable
                .just(new ArrayList<String>() {
                    {
                        for (int i = 0; i < 5; i++) {
                            add("data" + i);
                        }
                    }
                })
                .flatMap(new Function<List<String>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(List<String> strings) throws Exception {
                        return fromIterable(strings);
                    }
                }).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object s) throws Exception {
                String newStr = (String) s;
                if (newStr.contains("3")) {
                    return true;
                }
                return false;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                DemonstrateUtil.showLogResult((String) o);
            }
        });
    }

    private void map1() {
        Observable.just(new ArrayList<String>() {
            {
                for (int i = 0; i < 3; i++) {
                    add("data" + i);
                }
            }
        }).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return fromIterable(strings);
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                DemonstrateUtil.showLogResult("flatMap转换后,接收到的" + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void map0() {
        Observable.just("hellorxjava")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return s.length();
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("接收到被转换的数据结果:" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void test5() {
        DialogUtil.showListDialog(this, "rxjava的其他操作", new String[]{
                "0just()方式创建Observable",
                "1fromIterable()方式创建Observable",
                "2defer()方式创建Observable",
                "3interval( )方式创建Observable",
                "4timer( )方式创建Observable",
                "5range( )方式创建Observable",
                "6repeat( )方式创建Observable",
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        other0();
                        break;
                    case 1:
                        other1();
                        break;
                    case 2:
                        other2();
                        break;
                    case 3:
                        other3();
                        break;
                    case 4:
                        other4();
                        break;
                    case 5:
                        other5();
                        break;
                    case 6:
                        other6();
                        break;
                }
            }
        });
    }

    private void other6() {
        Observable.just(123).repeat().subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("重复integer" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void other5() {
        Observable.range(1, 5).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("连续收到:" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void other4() {
        Observable.timer(5, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                DemonstrateUtil.showLogResult("延迟5s后调用了:onNext");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void other3() {
        Observable.interval(3, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                DemonstrateUtil.showLogResult("数字是:" + aLong);
                //DemonstrateUtil.showToastResult(RxJavaDemo1Activity.this,"数字是:"+aLong);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void other2() {
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("hello,defer");
            }
        });

        //上游衔接下游!
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                DemonstrateUtil.showLogResult(s);
                DemonstrateUtil.showToastResult(RxJavaDemo1Activity.this, s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void other1() {
        fromIterable(new ArrayList<String>() {
            {
                for (int i = 0; i < 5; i++) {
                    add("Hello," + i);
                }
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                DemonstrateUtil.showToastResult(RxJavaDemo1Activity.this, s);
                DemonstrateUtil.showLogResult(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void other0() {
        Observable.just("hello,you hao!").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                DemonstrateUtil.showLogResult(s);
                DemonstrateUtil.showToastResult(RxJavaDemo1Activity.this, s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void test4() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                DemonstrateUtil.showLogResult("emitter 1");
                emitter.onNext(1);

                DemonstrateUtil.showLogResult("emitter 2");
                emitter.onNext(2);

                DemonstrateUtil.showLogResult("emitter 3");
                emitter.onNext(3);

                DemonstrateUtil.showLogResult("complete");
                emitter.onComplete();

                DemonstrateUtil.showLogResult("emitter 4");
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                DemonstrateUtil.showLogResult("accept:" + integer);
            }
        });
    }

    private void test3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                DemonstrateUtil.showLogResult("emitter 1");
                emitter.onNext(1);

                DemonstrateUtil.showLogResult("emitter 2");
                emitter.onNext(2);

                DemonstrateUtil.showLogResult("emitter 3");
                emitter.onNext(3);

                DemonstrateUtil.showLogResult("complete");
                emitter.onComplete();

                DemonstrateUtil.showLogResult("emitter 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                DemonstrateUtil.showLogResult("subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                DemonstrateUtil.showLogResult("onNext:" + value);
                i++;
                if (i == 2) {
                    DemonstrateUtil.showLogResult("dispose:" + value);
                    mDisposable.dispose();
                    DemonstrateUtil.showLogResult("isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                DemonstrateUtil.showLogResult("error:");
            }

            @Override
            public void onComplete() {
                DemonstrateUtil.showLogResult("complete");
            }
        });

    }

    private void test2() {
        //链式调用
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                DemonstrateUtil.showLogResult("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("onNext-->integer" + integer);
            }

            @Override
            public void onError(Throwable e) {
                DemonstrateUtil.showLogResult("onError");
            }

            @Override
            public void onComplete() {
                DemonstrateUtil.showLogResult("onComplete");
            }
        });
    }

    private void test1() {

        //创建上游,数据发射源!
        //ObservableOnSubscribe对象作为参数，它的作用相当于一个计划表，当 Observable被订阅的时候，
        // ObservableOnSubscribe的subscribe()方法会自动被调用，事件序列就会依照设定依次触发
        //ObservableEmitter,发射器,触发事件.
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        });

        //创建下游,数据接收处!
        Observer<Integer> observer = new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                DemonstrateUtil.showLogResult("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                DemonstrateUtil.showLogResult("onNext--integer" + integer);
            }

            @Override
            public void onError(Throwable e) {
                DemonstrateUtil.showLogResult("onError");
            }

            @Override
            public void onComplete() {
                DemonstrateUtil.showLogResult("onComplete");
            }
        };

        //数据源连接接收处,上游衔接下游!
        //只有当上游和下游建立连接之后, 上游才会开始发送事件
        observable.subscribe(observer);
    }

    private void initView() {
        btnSend1 = (Button) findViewById(R.id.btn_send1);
        btnSend1.setOnClickListener(RxJavaDemo1Activity.this);
        btnSend2 = (Button) findViewById(R.id.btn_send2);
        btnSend2.setOnClickListener(RxJavaDemo1Activity.this);
        btnSend3 = (Button) findViewById(R.id.btn_send3);
        btnSend3.setOnClickListener(RxJavaDemo1Activity.this);
        btnSend4 = (Button) findViewById(R.id.btn_send4);
        btnSend4.setOnClickListener(RxJavaDemo1Activity.this);
        btnSend5 = (Button) findViewById(R.id.btn_send5);
        btnSend5.setOnClickListener(RxJavaDemo1Activity.this);
        btnSend6 = (Button) findViewById(R.id.btn_send6);
        btnSend6.setOnClickListener(RxJavaDemo1Activity.this);
        findViewById(R.id.btn_send_test).setOnClickListener(RxJavaDemo1Activity.this);
    }
}
