package com.mojo.japanslots;

import android.webkit.WebView;

public interface DesignInterface {
    void setViewSettings();
    void setChromeClientSettings();
    void setViewClientSettings();
    void setProgress(WebView view, int newProgress);
}
