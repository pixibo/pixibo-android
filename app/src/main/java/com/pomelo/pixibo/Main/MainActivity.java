package com.pomelo.pixibo.Main;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pomelo.pixibo.R;
import com.pomelo.pixibo.Session;
import com.pomelo.pixibo.Utils.CustomTypefaceSpan;
import com.pomelo.pixibo.Utils.Utils;
import com.pomelo.pixibo.WebResources.GET;
import com.pomelo.pixibo.WebResources.NetworkUtils;
import com.pomelo.pixibo.WebResources.Result;
import com.pomelo.pixibo.WebResources.URI;

public class MainActivity extends AppCompatActivity implements Result {

    private Button bt_check_size;
    private Intent intent;
    private String SKUID = "22593";
    private String CLIENTID = "qxfgc8rhrozb";
    private String ALTID = "user_id";
    private Session session;
    private PopupWindow popup_error;
    private ViewGroup root;
    private TextView tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);

        root = (ViewGroup) getWindow().getDecorView().getRootView();

        bt_check_size = findViewById(R.id.bt_check_size);




        if (session.getIsRecommended())
        {
            Typeface regular_extd = null;
            Typeface medium_extd = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                regular_extd = getResources().getFont(R.font.akzidenz_grotesk_next_regular_extd);
                medium_extd = getResources().getFont(R.font.akzidenz_grotesk_next_medium_extd);
            }
            else
            {
                regular_extd =  ResourcesCompat.getFont(this, R.font.akzidenz_grotesk_next_regular_extd);
                medium_extd =  ResourcesCompat.getFont(this, R.font.akzidenz_grotesk_next_medium_extd);
            }

            TypefaceSpan akzidenz_grotesk_regular_extd = new CustomTypefaceSpan("", regular_extd);
            TypefaceSpan akzidenz_grotesk_medium_extd = new CustomTypefaceSpan("", medium_extd);


            String text = "You’ll Look Best in SIZE "+ session.get_size();

            final SpannableStringBuilder sb = new SpannableStringBuilder(text);
            sb.setSpan(akzidenz_grotesk_regular_extd, 0, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(akzidenz_grotesk_medium_extd, 20, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            bt_check_size.setText(sb);
        }
        else if (!session.getIsRecommended() && session.getIsSize())
        {
            bt_check_size.setText("CHECK YOUR FIT");
        }

        // starting measurement activity to get exact size.
        bt_check_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.getIsSize())
                {
                    intent = new Intent(MainActivity.this, CalculatedSizeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    initialize(CLIENTID,ALTID);
                }

            }
        });

    }

    public void error_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_error, root,false);
        popup_error = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popup_error.setTouchable(true);
        popup_error.setFocusable(true);
        popup_error.setOutsideTouchable(false);


        tv_ok = popupView.findViewById(R.id.tv_ok);

        Utils.applyDim(root, 0.7f);

        popup_error.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
            }
        });


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_error.dismiss();
            }
        });


        popup_error.showAtLocation(popupView, Gravity.CENTER, 0, 0);


    }

    private void initialize(String ClientId , String AltId) {

        session.set_CLIENTID(ClientId);
        session.set_ALTID(AltId);

        presentFyf(SKUID,AltId);
    }

    private void presentFyf(String SKUId,String AltId) {

        session.set_SKUID(SKUId);
        try {
            if (NetworkUtils.getInstance(this).isConnectedToInternet()) {

                GET post = new GET(this, URI.www_validate +"qxfgc8rhrozb"+"/"+session.get_CLIENTID()+"/"+SKUId+"?uid="+Utils.deviceID(this)+"&altId="+AltId, Utils.TYPE.Initialize, this);
                post.execute();
            } else {
                error_popup();
            }
        } catch (Exception e) {
            error_popup();
            e.printStackTrace();
        }
    }

    private void set_event() {

        try {
            if (NetworkUtils.getInstance(this).isConnectedToInternet()) {

                GET post = new GET(this, URI.www_event +"qxfgc8rhrozb"+"/"+session.get_SKUID()+"?eventType=click&page=pdpapp&event=sizeSelection&uid="+ Utils.deviceID(this), Utils.TYPE.Event, this);
                post.execute();
            } else {
                error_popup();
            }
        } catch (Exception e) {
            error_popup();
            e.printStackTrace();
        }
    }



    @Override
    public void getWebResponse(String result, Utils.TYPE type, int statusCode) {
        switch (type)
        {
            case Event:
                try
                {
                    if (statusCode != 200)
                    {
                        error_popup();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case Initialize:
                try
                {
                 if (statusCode == 200)
                 {
                     set_event();
                     intent = new Intent(MainActivity.this, MeasurementActivity.class);
                     startActivity(intent);
                     finish();
                 }
                 else
                 {
                     error_popup();
                 }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }
}
