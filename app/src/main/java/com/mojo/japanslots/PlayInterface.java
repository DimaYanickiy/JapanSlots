package com.mojo.japanslots;

import android.widget.ImageButton;

public interface PlayInterface {
    void secondsTimer();
    void checkLines();
    void setPoints(ImageButton imageButton, int randNum);
    void gameStops();
    void gameSpin();
    int genRandomNum();
    void loadProperties();
    void setProperties();
}
