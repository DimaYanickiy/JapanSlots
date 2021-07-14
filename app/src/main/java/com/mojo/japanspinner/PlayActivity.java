package com.mojo.japanspinner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity implements PlayInterface, View.OnClickListener {

    private boolean wasRun;
    private int seconds;
    private String time;
    SharedPreferences sp;

    private ImageButton circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15;
    private int index1, index2, index3, index4, index5, index6, index7, index8, index9, index10, index11, index12, index13, index14, index15;

    private ImageButton btn_back, play_10_tokens, play_20_tokens;
    private TextView spins;
    private int spinsBalance;
    private int tik;
    private String theme = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        sp = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        circle1 = (ImageButton)findViewById(R.id.circle1);
        circle2 = (ImageButton)findViewById(R.id.circle2);
        circle3 = (ImageButton)findViewById(R.id.circle3);
        circle4 = (ImageButton)findViewById(R.id.circle4);
        circle5 = (ImageButton)findViewById(R.id.circle5);
        circle6 = (ImageButton)findViewById(R.id.circle6);
        circle7 = (ImageButton)findViewById(R.id.circle7);
        circle8 = (ImageButton)findViewById(R.id.circle8);
        circle9 = (ImageButton)findViewById(R.id.circle9);
        circle10 = (ImageButton)findViewById(R.id.circle10);
        circle11 = (ImageButton)findViewById(R.id.circle11);
        circle12 = (ImageButton)findViewById(R.id.circle12);
        circle13 = (ImageButton)findViewById(R.id.circle13);
        circle14 = (ImageButton)findViewById(R.id.circle14);
        circle15 = (ImageButton)findViewById(R.id.circle15);

        spins = (TextView)findViewById(R.id.spins);

        play_10_tokens = (ImageButton)findViewById(R.id.play_10_tokens);
        play_20_tokens = (ImageButton)findViewById(R.id.play_20_tokens);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        play_10_tokens.setOnClickListener(this);
        play_20_tokens.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        loadProperties();

        LinearLayout lr = (LinearLayout)findViewById(R.id.lr);

        if(theme == "day"){
            spins.setTextColor(Color.BLACK);
            lr.setBackground(getResources().getDrawable(R.drawable.daybackground));
        } else{
            spins.setTextColor(Color.WHITE);
            lr.setBackground(getResources().getDrawable(R.drawable.nightbackground));
        }

        secondsTimer();
    }

    @Override
    public void secondsTimer() {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if(wasRun){
                    seconds++;
                    gameSpin();
                }
                h.postDelayed(this, 100);
            }
        });
    }

    @Override
    public void checkLines() {
        if((index2 == index5 && index5 == index8 && index8 == index11 && index11 == index14) || (index2 == index5 && index5 == index8 && index8 == index11 && index11 == index13) || (index2 == index5 && index5 == index8 && index8 == index11 && index11 == index15) || (index1 == index5 && index5 == index9 && index9 == index11 && index11 == index13) || (index3 == index5 && index5 == index7 && index7 == index11 && index11 == index15) || (index1 == index5 && index5 == index8 && index8 == index11 && index11 == index14) || (index3 == index5 && index5 == index8 && index8 == index11 && index11 == index14)){
            spinsBalance+=tik*10;
        }
        else if((index2 == index5 && index5 == index8 && index8 == index11) || (index2 == index5 && index5 == index8 && index8 == index12) || (index2 == index5 && index5 == index8 && index8 == index10) || (index1 == index5 && index5 == index8 && index8 == index11) || (index3 == index5 && index5 == index8 && index8 == index11)){
            spinsBalance+=tik*5;
        }
        else if((index2 == index5 && index5 == index8) || (index1 == index5 && index5 == index7) || (index3 == index5 && index5 == index9)){
            spinsBalance+=tik*3;
        }
        else if(index2 == index5 || index1 == index5 || index3 == index5){
            spinsBalance+=tik*2;
        }
        spins.setText("Spins: " + spinsBalance);
    }

    @Override
    public void setPoints(ImageButton imageButton, int randNum) {
        switch (randNum){
            case 1:
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.point1));
                break;
            case 2:
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.point2));
                break;
            case 3:
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.point3));
                break;
            case 4:
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.point4));
                break;
            case 5:
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.point5));
                break;
            case 6:
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.pointbonus));
                break;
        }
    }

    @Override
    public void gameStops() {
        checkLines();
        seconds = 0;
    }

    @Override
    public void gameSpin() {
        if(seconds<=15){
            index1 = genRandomNum();
            index2 = genRandomNum();
            index3 = genRandomNum();
            setPoints(circle1, index1);
            setPoints(circle2, index2);
            setPoints(circle3, index3);
        }
        if(seconds<=25){
            index4 = genRandomNum();
            index5 = genRandomNum();
            index6 = genRandomNum();
            setPoints(circle4, index4);
            setPoints(circle5, index5);
            setPoints(circle6, index6);
        }
        if(seconds<=30){
            index7 = genRandomNum();
            index8 = genRandomNum();
            index9 = genRandomNum();
            setPoints(circle7, index7);
            setPoints(circle8, index8);
            setPoints(circle9, index9);
        }
        if(seconds<=35){
            index10 = genRandomNum();
            index11 = genRandomNum();
            index12 = genRandomNum();
            setPoints(circle10, index10);
            setPoints(circle11, index11);
            setPoints(circle12, index12);
        }
        if(seconds<=40){
            index13 = genRandomNum();
            index14 = genRandomNum();
            index15 = genRandomNum();
            setPoints(circle13, index13);
            setPoints(circle14, index14);
            setPoints(circle15, index15);
        }
        if(seconds>40){
            wasRun = false;
            gameStops();
        }
    }

    @Override
    public int genRandomNum() {
        Random randomNum = new Random();
        return Math.abs(randomNum.nextInt()%6)+1;
    }

    @Override
    public void loadProperties() {
        spinsBalance = sp.getInt("balance", 1000);
        theme = sp.getString("theme", "day");
    }

    @Override
    public void setProperties() {
        sp.edit().putInt("balance", spinsBalance).apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_10_tokens:
                if(!wasRun){
                    tik = 10;
                    spinsBalance -= tik;
                    spins.setText("Spins: " + spinsBalance);
                    wasRun = true;
                }
                break;
            case R.id.play_20_tokens:
                if(!wasRun) {
                    tik = 20;
                    spinsBalance -= tik;
                    spins.setText("Spins: " + spinsBalance);
                    wasRun = true;
                }
                break;
            case R.id.btn_back:
                if(!wasRun){
                    setProperties();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setProperties();
        finish();
        super.onBackPressed();
    }
}