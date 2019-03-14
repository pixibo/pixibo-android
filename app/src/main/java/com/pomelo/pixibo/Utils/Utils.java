package com.pomelo.pixibo.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Toast;

import com.pomelo.pixibo.R;


public class Utils {

    public static ProgressDialog progress;
    public static AsyncTask<String, Void, String> asnservice;

    public static enum TYPE {
        GetFit,Event,Initialize

    }

    public static void showToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public static void showLoading(Context mContext, boolean cancelable) {
        showLoading(mContext, null, cancelable);
    }

    public static void showLoading(Context mContext, final AsyncTask<String, Void, String> service, boolean cancelable) {
        progress = new ProgressDialog(mContext, R.style.PopupDialog);
        progress.setCancelable(cancelable);
        progress.setCanceledOnTouchOutside(false);
        asnservice = service;
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (service != null)
                    service.cancel(true);
            }
        });

        progress.setCanceledOnTouchOutside(cancelable);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.show();
    }


    public static void hideLoading() {
        if (progress != null)
            progress.dismiss();
    }

    @SuppressLint("HardwareIds")
    public static String deviceID(Context context) {
        String id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }


    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public static String centimeterToFeet(String centemeter) {
        int feetPart = 0;
        int inchesPart = 0;
        if(!TextUtils.isEmpty(centemeter)) {
            double dCentimeter = Double.valueOf(centemeter);
            feetPart = (int) Math.floor((dCentimeter / 2.54) / 12);
            inchesPart = (int) Math.ceil((dCentimeter / 2.54) - (feetPart * 12));
        }
        return String.format("%d' %d''", feetPart, inchesPart);
    }

    public static String kgTolb(String kg) {
        int lbPart = 0;
        if(!TextUtils.isEmpty(kg)) {
            double dKg = Double.valueOf(kg);
            lbPart = (int) Math.floor(dKg * 2.205);

        }
        return String.format("%d", lbPart);
    }


}
