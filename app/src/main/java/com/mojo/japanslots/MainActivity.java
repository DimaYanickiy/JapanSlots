package com.mojo.japanslots;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout back;
    ImageButton btn_change_theme, btn_play, btn_share, btn_exit;
    TextView app_name;

    private String theme = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back = (LinearLayout)findViewById(R.id.layaut_theme);
        app_name = (TextView)findViewById(R.id.app_name);
        btn_change_theme = (ImageButton) findViewById(R.id.btn_change_theme);
        btn_play = (ImageButton) findViewById(R.id.btn_play);
        btn_share = (ImageButton) findViewById(R.id.btn_share);
        btn_exit = (ImageButton) findViewById(R.id.btn_exit);
        btn_change_theme.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_exit.setOnClickListener(this);

        loadTheme();

        if(theme == "day"){
            app_name.setTextColor(Color.BLACK);
            back.setBackground(getResources().getDrawable(R.drawable.daybackground));
        } else{
            app_name.setTextColor(Color.WHITE);
            back.setBackground(getResources().getDrawable(R.drawable.nightbackground));
        }
        saveTheme(theme);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change_theme:
                if(theme == "day"){
                    theme = "night";
                    app_name.setTextColor(Color.WHITE);
                    back.setBackground(getResources().getDrawable(R.drawable.nightbackground));
                } else{
                    theme = "day";
                    app_name.setTextColor(Color.BLACK);
                    back.setBackground(getResources().getDrawable(R.drawable.daybackground));
                }
                saveTheme(theme);
                break;
            case R.id.btn_play:
                startActivity(new Intent(MainActivity.this, PlayActivity.class));
                break;
            case R.id.btn_share:
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("plain/text");
                shareIntent.putExtra("android.intent.extra.TEXT", "Скачайте приложение ссылка и получите бонус!!!");
                startActivity(Intent.createChooser(shareIntent, "Поделится"));
                break;
            case R.id.btn_exit:
                createAlertDialog();
                break;
        }
    }
    public void createAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Do you realy want exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> MainActivity.super.onBackPressed()).create().show();
    }
    public void saveTheme(String th){
        SharedPreferences sp = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        sp.edit().putString("theme", th).apply();
    }
    public void loadTheme(){
        SharedPreferences sp = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        sp.getString("theme", "day");
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}