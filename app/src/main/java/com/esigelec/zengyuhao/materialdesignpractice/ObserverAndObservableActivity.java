package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class ObserverAndObservableActivity extends Activity {
    private TextView txt_log;
    private Button btn_obs, btn_obs_arg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer_and_observable);

        txt_log = (TextView) findViewById(R.id.txt_log);
        txt_log.setMovementMethod(new ScrollingMovementMethod());

        MyObserver mObserver = new MyObserver();
        MyObserverWithArg mObserverArg = new MyObserverWithArg();

        final MyObservable observable = new MyObservable();
        final MyObservableWithArg observableWithArg = new MyObservableWithArg();

        observable.addObserver(mObserver);
        observable.addObserver(mObserverArg);
        observableWithArg.addObserver(mObserver);
        observableWithArg.addObserver(mObserverArg);

        btn_obs = (Button) findViewById(R.id.btn_obs);
        btn_obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observable.setData(new Random().nextInt());
            }
        });

        btn_obs_arg = (Button) findViewById(R.id.btn_obs_arg);
        btn_obs_arg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observableWithArg.setData(new Random().nextInt());
            }
        });
    }

    public class MyObservable extends Observable {
        protected int data = 0;

        public void setData(int data) {
            this.data = data;
            setChanged();
            notifyObservers();
        }

        private int getData() {
            return data;
        }
    }

    public class MyObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            appendText(txt_log, "MyObserver ----> " + ((MyObservable) observable).getData() + "\n");
        }
    }

    public class MyObservableWithArg extends MyObservable {
        private final static String flag = "secret";

        public void setData(int data) {
            this.data = data;
            setChanged();
            notifyObservers(flag);
        }
    }


    public class MyObserverWithArg implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (data instanceof String) {
                String flag = (String) data;
                appendText(txt_log, "MyObserverWithArg ----> WITH FLAG :" + ((MyObservable) observable).getData
                        () + "\n");
            }
        }
    }

    private void appendText(TextView target, String content) {
        target.append(content);
        scrollToBottom(target);
    }

    private void scrollToBottom(TextView target) {
        if (target.getLayout() != null) {
            final int scrollAmount = target.getLayout().getLineTop(target.getLineCount()) - target.getHeight();
            //获取当前字体的高、宽
            Paint paint = new Paint();
            paint.setTextSize(target.getTextSize());
            Paint.FontMetrics fm = paint.getFontMetrics();
            int fontHeight = (int) Math.ceil(fm.descent - fm.top);
            //满足滚动条件，则滚动文字到底部
            if (scrollAmount > fontHeight)
                target.scrollTo(0, scrollAmount);
        }
    }
}
