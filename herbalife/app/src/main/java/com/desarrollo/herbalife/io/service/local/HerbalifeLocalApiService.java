package com.desarrollo.herbalife.io.service.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Desarrollo on 23/01/16.
 */
public class HerbalifeLocalApiService {
    private static final String PREFERENCES = "Preferences";
    private static final String TOKEN = "token";
    private static final String ID = "id-publicacion";

    Context context;

    public HerbalifeLocalApiService(Context context) {
        this.context = context;
    }

    public String getImei(){
        String imei;
        try {
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            imei = mngr.getDeviceId();
            if(imei == null){
                imei = "";
            }
            return imei;
        }catch (Exception e){
        }
        imei = getUniquePsuedoID();
        return imei;
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            Log.i("uniquePsuedoID", "serial = " + serial);
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    public String getImeiType(){
        return "1";
    }

    public String getPlatform(){
        return "1";
    }

    public String getSO(){
        return "Android";
    }

    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getOperator() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String cadena = telephonyManager.getSimOperatorName();
        if (cadena == null) {
            return "";
        } else {
            return cadena;
        }
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public void saveId(String token) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(ID, token);
        editor.commit();
    }

    public String getToken() {
        return getPreferences().getString(TOKEN, "");
    }

    public String getID() {
        return getPreferences().getString(ID, "");
    }


    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getPreferences();
        return preferences.edit();
    }
    public void clear() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }
}
