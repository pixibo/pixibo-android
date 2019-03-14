package com.pomelo.pixibo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void settoken(String token) {
        prefs.edit().putString("token", token).commit();
    }


    public void set_bra_size(String bra_size) {
        prefs.edit().putString("bra_size", bra_size).commit();
    }

    public void set_size(String size) {
        prefs.edit().putString("size", size).commit();
    }

    public void set_fit(String fit) {
        prefs.edit().putString("fit", fit).commit();
    }

    public void set_confidence(String confidence) {
        prefs.edit().putString("confidence", confidence).commit();
    }

    public void set_bust(String bust) {
        prefs.edit().putString("bust", bust).commit();
    }

    public void set_waist(String waist) {
        prefs.edit().putString("waist", waist).commit();
    }

    public void set_hip(String hip) {
        prefs.edit().putString("hip", hip).commit();
    }


    public void set_bra_size_temp(String bra_size_temp) {
        prefs.edit().putString("bra_size_temp", bra_size_temp).commit();
    }

    public void set_band_size_temp(String band_size_temp) {
        prefs.edit().putString("band_size_temp", band_size_temp).commit();
    }

    public void set_cup_size_temp(String cup_size_temp) {
        prefs.edit().putString("cup_size_temp", cup_size_temp).commit();
    }

    public void set_height_progress_temp(String height_progress_temp) {
        prefs.edit().putString("height_progress_temp", height_progress_temp).commit();
    }

    public void set_weight_progress_temp(String weight_progress_temp) {
        prefs.edit().putString("weight_progress_temp", weight_progress_temp).commit();
    }

    public void set_SKUID(String SKUID) {
        prefs.edit().putString("SKUID", SKUID).commit();
    }

    public void set_CLIENTID(String CLIENTID) {
        prefs.edit().putString("CLIENTID", CLIENTID).commit();
    }

    public void set_ALTID(String ALTID) {
        prefs.edit().putString("ALTID", ALTID).commit();
    }

    public void set_JSON(String JSON) {
        prefs.edit().putString("JSON", JSON).commit();
    }

    public void setIsRecommended(boolean IsRecommended){
        prefs.edit().putBoolean("IsRecommended", IsRecommended).commit();
    }

    public void setIsSize(boolean IsSize){
        prefs.edit().putBoolean("IsSize", IsSize).commit();
    }


    public String gettoken() {
        String token = prefs.getString("token","");
        return token;
    }

    public String get_bra_size() {
        String bra_size = prefs.getString("bra_size","UK");
        return bra_size;
    }

    public String get_size() {
        String size = prefs.getString("size","");
        return size;
    }

    public String get_fit() {
        String fit = prefs.getString("fit","");
        return fit;
    }

    public String get_confidence() {
        String confidence = prefs.getString("confidence","");
        return confidence;
    }

    public String get_bust() {
        String bust = prefs.getString("bust","");
        return bust;
    }

    public String get_waist() {
        String waist = prefs.getString("waist","");
        return waist;
    }

    public String get_hip() {
        String hip = prefs.getString("hip","");
        return hip;
    }

    public String get_bra_size_temp() {
        String bra_size_temp = prefs.getString("bra_size_temp","");
        return bra_size_temp;
    }

    public String get_band_size_temp() {
        String band_size_temp = prefs.getString("band_size_temp","");
        return band_size_temp;
    }

    public String get_cup_size_temp() {
        String cup_size_temp = prefs.getString("cup_size_temp","");
        return cup_size_temp;
    }

    public String get_height_progress_temp() {
        String height_progress_temp = prefs.getString("height_progress_temp","");
        return height_progress_temp;
    }

    public String get_weight_progress_temp() {
        String weight_progress_temp = prefs.getString("weight_progress_temp","");
        return weight_progress_temp;
    }

    public String get_SKUID() {
        String SKUID = prefs.getString("SKUID","");
        return SKUID;
    }

    public String get_CLIENTID() {
        String CLIENTID = prefs.getString("CLIENTID","");
        return CLIENTID;
    }

    public String get_ALTID() {
        String ALTID = prefs.getString("ALTID","");
        return ALTID;
    }

    public String get_JSON() {
        String JSON = prefs.getString("JSON","");
        return JSON;
    }

    public boolean getIsRecommended() {
        boolean IsRecommended = prefs.getBoolean("IsRecommended",false);
        return IsRecommended;
    }

    public boolean getIsSize() {
        boolean IsSize = prefs.getBoolean("IsSize",false);
        return IsSize;
    }
}
