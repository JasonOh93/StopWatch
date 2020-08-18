package com.jasonoh.stopwatchex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btn_start, btn_pause, btn_stop;
    TextView tv_time;

    boolean isRun = true; //시작, 종료용
    boolean isPause = false; //일시정지용

    MyThread t; //내가 만든 Thread 참조변수

    int pauseCnt = 0; //일시정지 버튼 클릭용 카운트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle( R.string.app_title );

        btn_start = findViewById(R.id.btn01);
        btn_pause = findViewById(R.id.btn02);
        btn_stop = findViewById(R.id.btn03);
        tv_time = findViewById(R.id.tv01);

    }//onCreate method

    class MyThread extends Thread {
        @Override
        public synchronized void run() {

            final long startTime = System.currentTimeMillis();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "mm : ss : SSS" );

            while (isRun) {

                if(!isPause) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long endTime = System.currentTimeMillis();
                            tv_time.setText( "Time\n" + simpleDateFormat.format( endTime - startTime ).substring(0, 12) );
                        }
                    });//runOnUiThread

                    SystemClock.sleep(10);
                }//if

            }//while

        }//run method
    }//MyThread class

    public synchronized void clickBtn(View view) {

        switch ( view.getId() ) {
            case R.id.btn01 : //start
                //Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

                isRun = true;

                t = new MyThread();
                t.start();

                break;

            case R.id.btn02 : //pause
                //Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show();
                try {

                    if(isRun) {

                        pauseCnt++;

                        if( (pauseCnt % 2) == 1 ) {
                            btn_pause.setText( "Re Start" );
                            isPause = true;
                        } else if( (pauseCnt % 2) == 0 ) {
                            btn_pause.setText( "Pause" );
                            isPause = false;
                            pauseCnt = 0;
                        }//if ~ else if문
                    }


                } catch (Exception e) { }
                break;

            case R.id.btn03 : //stop
                //Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();

                isRun = false; //while 종료시켜 Thread 실행 종료
                isPause = false; //일시정지
                btn_pause.setText( "Pause" ); //일시정지중 종료시 다시 일시정지 버튼에 이름 셋팅
                pauseCnt = 0; //일시정지 누를때마다 카운트 재설정


                break;
            default :
                break;
        }//switch

    }//clickBtn method
}//MainActivity class
