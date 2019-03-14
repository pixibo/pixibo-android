package com.pomelo.pixibo.Main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.pomelo.pixibo.Model.ResultModel;
import com.pomelo.pixibo.R;
import com.pomelo.pixibo.Session;
import com.pomelo.pixibo.Utils.Utils;
import com.pomelo.pixibo.WebResources.GET;
import com.pomelo.pixibo.WebResources.NetworkUtils;
import com.pomelo.pixibo.WebResources.Result;
import com.pomelo.pixibo.WebResources.URI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeasurementActivity extends AppCompatActivity implements Result {

    private SeekBar seek_height,seek_weight;
    private TextView tv_info_popup,tv_height_value,tv_height_minus,tv_height_plus,tv_cm,tv_ft;
    private TextView tv_back,tv_kg,tv_lb,tv_weight_minus,tv_weight_value,tv_weight_plus;
    private TextView tv_bra_country,tv_bra_band,tv_bra_cup;
    private TextView tv_privacy_policy,tv_error,tv_tight,tv_regular,tv_loose,tv_recommended_size,tv_fit_size,tv_ok;
    private PopupWindow popup_bra_size,popup_band,popup_cup,popup_error;
    private ViewGroup root;
    private RelativeLayout layout_popup,layout_country,layout_band,layout_cup,layout_data_entry,layout_fit,layout_size;
    private RelativeLayout layout_animation,layout_main;
    private RelativeLayout layout_fit_tight,layout_fit_regular,layout_fit_loose;
    private int height_progress = 160;
    private int weight_progress = 50;
    private boolean isCM = true;
    private boolean isKG = true;
    private String bra_size = "UK";
    private String band_size = "";
    private String cup_size = "";
    private String ftp_size = "2";
    private String recommended_size = "";
    private String band_checkedId = "" ;
    private String bra_checkedId = "" ;
    private String cup_checkedId = "" ;
    private Button bt_next,bt_submit;
    private RadioGroup radio_gp_bra,radio_gp_band,radio_gp_cup;
    private RadioButton rb;
    private View view_band,view_cup;
    private List<ResultModel> resultModelList = new ArrayList<>();
    private Session session;
    private Intent intent;
    private boolean isChangeFit ;
    private LottieAnimationView loading_animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        session = new Session(this);
        root = (ViewGroup) getWindow().getDecorView().getRootView();
        seek_height = findViewById(R.id.seek_height);
        seek_weight = findViewById(R.id.seek_weight);

        layout_fit_tight = findViewById(R.id.layout_fit_tight);
        layout_fit_regular = findViewById(R.id.layout_fit_regular);
        layout_fit_loose = findViewById(R.id.layout_fit_loose);

        layout_popup = findViewById(R.id.layout_popup);
        layout_country = findViewById(R.id.layout_country);
        layout_band = findViewById(R.id.layout_band);
        layout_cup = findViewById(R.id.layout_cup);

        layout_data_entry = findViewById(R.id.layout_data_entry);
        layout_fit = findViewById(R.id.layout_fit);
        layout_size = findViewById(R.id.layout_size);

        layout_animation = findViewById(R.id.layout_animation);
        layout_main = findViewById(R.id.layout_main);

        view_band = findViewById(R.id.view_band);
        view_cup = findViewById(R.id.view_cup);

        tv_tight = findViewById(R.id.tv_tight);
        tv_regular = findViewById(R.id.tv_regular);
        tv_loose = findViewById(R.id.tv_loose);

        loading_animation = findViewById(R.id.loading_animation);
        loading_animation.setAnimation(R.raw.ic_loading_json);

        tv_info_popup = findViewById(R.id.tv_info_popup);
        tv_height_value = findViewById(R.id.tv_height_value);
        tv_height_minus = findViewById(R.id.tv_height_minus);
        tv_height_plus = findViewById(R.id.tv_height_plus);
        tv_cm = findViewById(R.id.tv_cm);
        tv_ft = findViewById(R.id.tv_ft);

        tv_kg = findViewById(R.id.tv_kg);
        tv_lb = findViewById(R.id.tv_lb);
        tv_weight_minus = findViewById(R.id.tv_weight_minus);
        tv_weight_value = findViewById(R.id.tv_weight_value);
        tv_weight_plus = findViewById(R.id.tv_weight_plus);

        tv_bra_country = findViewById(R.id.tv_bra_country);
        tv_bra_band = findViewById(R.id.tv_bra_band);
        tv_bra_cup = findViewById(R.id.tv_bra_cup);
        tv_back = findViewById(R.id.tv_back);

        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
        tv_error = findViewById(R.id.tv_error);
        tv_recommended_size = findViewById(R.id.tv_recommended_size);
        tv_fit_size = findViewById(R.id.tv_fit_size);

        bt_submit = findViewById(R.id.bt_submit);
        bt_next = findViewById(R.id.bt_next);

        seek_height.setMax(69);
        seek_weight.setMax(69);

        seek_height.setProgress(30);
        seek_weight.setProgress(20);



        intent = getIntent();

        if (intent.hasExtra("data"))
        {

            isChangeFit = true;
            layout_data_entry.setVisibility(View.GONE);
            layout_fit.setVisibility(View.VISIBLE);


            if (session.getIsRecommended())
            {
                Log.e("data","Recommended");
                layout_size.setVisibility(View.VISIBLE);
                tv_recommended_size.setText("We recommend this in Size "+ session.get_size() +". For a better fit, tell us your personal fit preference.");
            }


            if (intent.hasExtra("LIST"))
            {
                Log.e("data","Has LIST");
                resultModelList = intent.getParcelableArrayListExtra("LIST");
            }
        }
        else
        {
            session.set_fit("REGULAR");
        }







        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeasurementActivity.this,MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }
        });

        tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wearepixibo.com/privacypolicy"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cup_size.equals(""))
                {
                    tv_error.setVisibility(View.VISIBLE);

                    view_cup.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red_error));

                    if (band_size.equals(""))
                    {
                        view_band.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red_error));
                    }

                }
                else
                {
                    tv_error.setVisibility(View.GONE);

                    session.set_bra_size_temp(bra_size.toLowerCase());
                    session.set_band_size_temp(band_size);
                    session.set_cup_size_temp(cup_size);
                    session.set_height_progress_temp(String.valueOf(height_progress));
                    session.set_weight_progress_temp(String.valueOf(weight_progress));

                    //                layout_data_entry.setVisibility(View.GONE);
                    //                layout_fit.setVisibility(View.GONE);
                    getProfile(String.valueOf(height_progress),String.valueOf(weight_progress),bra_size.toLowerCase(),band_size,cup_size,ftp_size,session.get_ALTID());

                }
            }
        });


        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isChangeFit && ftp_size.equalsIgnoreCase("2"))
                {

                    layout_main.setVisibility(View.GONE);
                    layout_animation.setVisibility(View.VISIBLE);
                    loading_animation.playAnimation();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent1 = new Intent(MeasurementActivity.this,CalculatedSizeActivity.class);
                            intent1.putParcelableArrayListExtra("LIST", (ArrayList<? extends Parcelable>) resultModelList);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                            loading_animation.cancelAnimation();
                        }
                    }, 1000);


                }
                else
                {
                    // show final json loading screen.
                    //                layout_data_entry.setVisibility(View.GONE);
                    //                layout_fit.setVisibility(View.GONE);
                    getProfile(session.get_height_progress_temp(),session.get_weight_progress_temp(),session.get_bra_size_temp(),session.get_band_size_temp(),session.get_cup_size_temp(),ftp_size,session.get_ALTID());
                }




            }
        });

        switch (session.get_fit())
        {
            case "TIGHT":
                ftp_size = "1";
                layout_fit_tight.setBackground(getDrawable(R.drawable.bg_button_fit_fill));
                tv_tight.setTextColor(getColor(R.color.white));
                layout_fit_regular.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_regular.setTextColor(getColor(R.color.black));
                layout_fit_loose.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_loose.setTextColor(getColor(R.color.black));
                tv_fit_size.setText("Fits tighter than intended.");
                break;

            case "REGULAR":
                ftp_size = "2";
                layout_fit_regular.setBackground(getDrawable(R.drawable.bg_button_fit_fill));
                tv_regular.setTextColor(getColor(R.color.white));
                layout_fit_tight.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_tight.setTextColor(getColor(R.color.black));
                layout_fit_loose.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_loose.setTextColor(getColor(R.color.black));
                tv_fit_size.setText("Fits exactly as intended.");
                break;
            case "LOOSE":
                ftp_size = "3";
                layout_fit_loose.setBackground(getDrawable(R.drawable.bg_button_fit_fill));
                tv_loose.setTextColor(getColor(R.color.white));
                layout_fit_regular.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_regular.setTextColor(getColor(R.color.black));
                layout_fit_tight.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_tight.setTextColor(getColor(R.color.black));
                tv_fit_size.setText("Fits looser than intended.");
                break;
        }


        layout_fit_tight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.set_fit("TIGHT");
                ftp_size = "1";
                layout_fit_tight.setBackground(getDrawable(R.drawable.bg_button_fit_fill));
                tv_tight.setTextColor(getColor(R.color.white));

                layout_fit_regular.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_regular.setTextColor(getColor(R.color.black));

                layout_fit_loose.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_loose.setTextColor(getColor(R.color.black));

                tv_fit_size.setText("Fits tighter than intended.");
            }
        });

        layout_fit_regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.set_fit("REGULAR");
                ftp_size = "2";
                layout_fit_regular.setBackground(getDrawable(R.drawable.bg_button_fit_fill));
                tv_regular.setTextColor(getColor(R.color.white));

                layout_fit_tight.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_tight.setTextColor(getColor(R.color.black));

                layout_fit_loose.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_loose.setTextColor(getColor(R.color.black));
                tv_fit_size.setText("Fits exactly as intended.");
            }
        });

        layout_fit_loose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.set_fit("LOOSE");
                ftp_size = "3";
                layout_fit_loose.setBackground(getDrawable(R.drawable.bg_button_fit_fill));
                tv_loose.setTextColor(getColor(R.color.white));

                layout_fit_regular.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_regular.setTextColor(getColor(R.color.black));

                layout_fit_tight.setBackground(getDrawable(R.drawable.bg_button_fit));
                tv_tight.setTextColor(getColor(R.color.black));
                tv_fit_size.setText("Fits looser than intended.");
            }
        });

        Log.e("Seek Progress",String.valueOf(seek_height.getProgress()));

        seek_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                height_progress = progress + 130;

                //Log.e("Height Progress",String.valueOf(height_progress));

                if (isCM)
                {
                    tv_height_value.setText(String.valueOf(height_progress)+ " CM");
                }
                else
                {
                    tv_height_value.setText(Utils.centimeterToFeet(String.valueOf(height_progress))+ " FT");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        tv_info_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_popup.setVisibility(View.VISIBLE);
            }
        });

        tv_height_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (height_progress > 130 && height_progress <= 199)
                {
                    height_progress = height_progress - 1;
                    seek_height.setProgress(height_progress - 130);
                }

                if (isCM)
                {
                    tv_height_value.setText(String.valueOf(height_progress)+ " CM");
                }
                else
                {
                    tv_height_value.setText(Utils.centimeterToFeet(String.valueOf(height_progress))+ " FT");
                }
            }
        });

        tv_height_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (height_progress >= 130 && height_progress < 199)
                {
                    height_progress = height_progress + 1;
                    seek_height.setProgress(height_progress - 130);
                }

                if (isCM)
                {
                    tv_height_value.setText(String.valueOf(height_progress)+ " CM");
                }
                else
                {
                    tv_height_value.setText(Utils.centimeterToFeet(String.valueOf(height_progress))+ " FT");
                }
            }
        });

        tv_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_cm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_ft.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tv_cm.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                tv_ft.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_ft.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_measurements));
                isCM = true;
                seek_height.setMax(70);
                tv_height_value.setText(String.valueOf(height_progress)+ " CM");

            }
        });

        tv_ft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ft.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_cm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tv_ft.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                tv_cm.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_cm.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_measurements));
                isCM = false;
                seek_height.setMax(70);
                tv_height_value.setText(Utils.centimeterToFeet(String.valueOf(height_progress))+ " FT");

            }
        });


        seek_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //Log.e("Weight Progress",String.valueOf(weight_progress));
                weight_progress = progress + 30;
                if (isKG)
                {
                    tv_weight_value.setText(String.valueOf(weight_progress)+ " KG");
                }
                else
                {
                    tv_weight_value.setText(Utils.kgTolb(String.valueOf(weight_progress))+ " LB");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        tv_weight_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weight_progress > 30 && weight_progress <= 99)
                {
                    weight_progress = weight_progress - 1;
                    seek_weight.setProgress(weight_progress - 30);
                }

                if (isKG)
                {
                    tv_weight_value.setText(String.valueOf(weight_progress)+ " KG");
                }
                else
                {
                    tv_weight_value.setText(Utils.kgTolb(String.valueOf(weight_progress))+ " LB");
                }
            }
        });

        tv_weight_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weight_progress >= 30 && weight_progress < 99)
                {
                    weight_progress = weight_progress + 1;
                    seek_weight.setProgress(weight_progress - 30);
                }

                if (isKG)
                {
                    tv_weight_value.setText(String.valueOf(weight_progress)+ " KG");
                }
                else
                {
                    tv_weight_value.setText(Utils.kgTolb(String.valueOf(weight_progress))+ " LB");
                }
            }
        });

        tv_kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_kg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_lb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tv_kg.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                tv_lb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_lb.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_measurements));
                isKG = true;
                seek_weight.setMax(70);
                tv_weight_value.setText(String.valueOf(weight_progress)+ " KG");

            }
        });

        tv_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_lb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_kg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tv_lb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                tv_kg.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tv_kg.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_measurements));
                isKG = false;
                seek_weight.setMax(70);
                tv_weight_value.setText(Utils.kgTolb(String.valueOf(weight_progress))+ " LB");

            }
        });



        layout_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_popup.setVisibility(View.GONE);
            }
        });

        layout_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bra_size_popup();
            }
        });

        layout_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                band_popup();
            }
        });

        layout_cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cup_popup();
            }
        });

    }



    public void bra_size_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popupView = layoutInflater.inflate(R.layout.popup_bra_size, root,false);
        popup_bra_size = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        radio_gp_bra = popupView.findViewById(R.id.radio_gp_bra);

        switch (bra_size)
        {
            case "UK":
                radio_gp_bra.check(R.id.radio_uk);
                break;
            case "EU":
                radio_gp_bra.check(R.id.radio_eu);
                break;
            case "AU":
                radio_gp_bra.check(R.id.radio_au);
                break;
            case "FR":
                radio_gp_bra.check(R.id.radio_fr);
                break;
        }

        radio_gp_bra.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) popupView.findViewById(checkedId);
                bra_size = radioButton.getText().toString();
                tv_bra_country.setText(bra_size);
                tv_bra_country.setTextColor(getColor(R.color.black));

                tv_bra_band.setTextColor(getColor(R.color.grey_measurement));
                tv_bra_band.setText("Band");

                tv_bra_cup.setTextColor(getColor(R.color.grey_measurement));
                tv_bra_cup.setText("Cup");

                band_checkedId = "";
                cup_checkedId = "";

                band_size = "";
                cup_size = "";

                tv_error.setVisibility(View.GONE);

                view_cup.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                view_band.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                popup_bra_size.dismiss();
               // Log.e("Bra_size",bra_size);
            }
        });



        popup_bra_size.setTouchable(true);
        popup_bra_size.setFocusable(true);

        Utils.applyDim(root, 0.7f);


        popup_bra_size.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
            }
        });

        popup_bra_size.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }
    public void band_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_band_size, root,false);
        popup_band = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popup_band.setTouchable(true);
        popup_band.setFocusable(true);


        view_band.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        radio_gp_band = popupView.findViewById(R.id.radio_gp_band);

        if (!band_checkedId.equals(""))
        {

            radio_gp_band.check(Integer.parseInt(band_checkedId));
        }

        if(bra_size.equalsIgnoreCase("UK"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_a),"30");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_b),"32");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_c),"34");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_d),"36");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_e),"38");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_f),"40");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_g),"42");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_h),"44");
        }
        else if (bra_size.equalsIgnoreCase("EU"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_a),"65");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_b),"70");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_c),"75");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_d),"80");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_e),"85");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_f),"90");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_g),"95");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_h),"100");
        }
        else if (bra_size.equalsIgnoreCase("AU"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_a),"8");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_b),"10");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_c),"12");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_d),"14");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_e),"16");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_f),"18");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_g),"20");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_h),"22");
        }
        else if (bra_size.equalsIgnoreCase("FR"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_a),"80");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_b),"85");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_c),"90");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_d),"95");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_e),"100");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_f),"105");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_g),"110");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_band_h),"115");
        }

        radio_gp_band.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) popupView.findViewById(checkedId);
                band_size = radioButton.getText().toString();
                band_checkedId = String.valueOf(checkedId);
                tv_bra_band.setText(radioButton.getText().toString());
                tv_bra_band.setTextColor(getColor(R.color.black));
                popup_band.dismiss();

               // Log.e("Band_size",band_size);
            }
        });



        Utils.applyDim(root, 0.7f);


        popup_band.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
            }
        });

        popup_band.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }
    public void cup_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popupView = layoutInflater.inflate(R.layout.popup_cup_size, root,false);
        popup_cup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popup_cup.setTouchable(true);
        popup_cup.setFocusable(true);

        view_cup.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        tv_error.setVisibility(View.GONE);

        radio_gp_cup = popupView.findViewById(R.id.radio_gp_cup);

        if (!cup_checkedId.equals(""))
        {

            radio_gp_cup.check(Integer.parseInt(cup_checkedId));
        }

        if(bra_size.equalsIgnoreCase("UK"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_A),"A");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_B),"B");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_C),"C");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_D),"D");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_E),"DD");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_F),"E");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_G),"F");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_H),"G");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_I),"H");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_J),"I");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_K),"J");
        }
        else if (bra_size.equalsIgnoreCase("EU"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_A),"A");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_B),"B");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_C),"C");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_D),"D");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_E),"E");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_F),"F");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_G),"G");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_H),"H");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_I),"I");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_J),"J");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_K),"K");
        }
        else if (bra_size.equalsIgnoreCase("AU"))
        {
            rb = popupView.findViewById(R.id.radio_K);
            rb.setVisibility(View.GONE);
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_A),"AA");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_B),"A");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_C),"B");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_D),"C");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_E),"D");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_F),"DD");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_G),"E");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_H),"F");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_I),"G");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_J),"H");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_K),"I");

        }
        else if (bra_size.equalsIgnoreCase("FR"))
        {
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_A),"A");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_B),"B");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_C),"C");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_D),"D");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_E),"E");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_F),"F");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_G),"G");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_H),"H");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_I),"I");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_J),"J");
            change_radio_text((RadioButton) popupView.findViewById(R.id.radio_K),"K");
        }

        radio_gp_cup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) popupView.findViewById(checkedId);
                cup_size = radioButton.getText().toString();
                cup_checkedId = String.valueOf(checkedId);
                tv_bra_cup.setText(radioButton.getText().toString());
                tv_bra_cup.setTextColor(getColor(R.color.black));
                popup_cup.dismiss();

               // Log.e("Band_size",band_size);
            }
        });

        Utils.applyDim(root, 0.7f);

        popup_cup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
            }
        });

        popup_cup.showAtLocation(popupView, Gravity.CENTER, 0, 0);

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


    public void change_radio_text(RadioButton radioButton , String text)
    {
        radioButton.setText(text);
    }


    private void getProfile(String ht, String wt, String rg, String bs, String cu, String ftp, String altId) {

        try {


            if (NetworkUtils.getInstance(this).isConnectedToInternet()) {

                GET post = new GET(this, URI.www +session.get_CLIENTID()+"/"+session.get_SKUID()+ "?ht="+ht+"&wt="+wt+"&rg="+rg+"&bs="+bs+"&cu="+cu+"&bu=0&ws=0&hp=0&age=0&ftp="+ftp+"&uid="+Utils.deviceID(this)+"&altId="+altId, Utils.TYPE.GetFit, this);
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
            case GetFit:
                try {

                   // Utils.hideLoading();

                    if (statusCode == 200)
                    {
                        resultModelList.clear();

                        session.set_JSON(result);
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("fys");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ResultModel model = new ResultModel();

                            if (obj.getBoolean("recommended"))
                            {
                                layout_size.setVisibility(View.VISIBLE);

                                recommended_size = obj.getString("size");
                                session.set_size(recommended_size);
                                session.set_confidence(String.valueOf(obj.getInt("confidence")));
                                session.set_bust(String.valueOf(obj.getInt("bust")));
                                session.set_waist(String.valueOf(obj.getInt("waist")));
                                session.set_hip(String.valueOf(obj.getInt("hip")));
                                session.setIsRecommended(true);

                            }
                            session.setIsSize(true);
                            model.setRecommended(obj.getBoolean("recommended"));
                            model.setSize(obj.getString("size"));
                            model.setBust(String.valueOf(obj.getInt("bust")));
                            model.setWaist(String.valueOf(obj.getInt("waist")));
                            model.setHip(String.valueOf(obj.getInt("hip")));
                            model.setBust8Bit(String.valueOf(obj.getInt("bust8Bit")));
                            model.setWaist8Bit(String.valueOf(obj.getInt("waist8Bit")));
                            model.setWaist8Bit(String.valueOf(obj.getInt("hip8Bit")));
                            model.setConfidence(String.valueOf(obj.getInt("confidence")));


                            if (jsonArray.length() == 1)
                            {
                                recommended_size = obj.getString("size");
                                session.set_size(recommended_size);
                                session.set_confidence(String.valueOf(obj.getInt("confidence")));
                                session.set_bust(String.valueOf(obj.getInt("bust")));
                                session.set_waist(String.valueOf(obj.getInt("waist")));
                                session.set_hip(String.valueOf(obj.getInt("hip")));
                                session.setIsRecommended(false);
                            }

                            resultModelList.add(model);

                        }





                        tv_recommended_size.setText("We recommend this in Size "+ recommended_size +".\nFor a better fit, tell us your personal fit preference.");

                        layout_data_entry.setVisibility(View.GONE);
                        layout_fit.setVisibility(View.VISIBLE);

                        if (isChangeFit || !ftp_size.equalsIgnoreCase("2"))
                        {
                            layout_main.setVisibility(View.GONE);
                            layout_animation.setVisibility(View.VISIBLE);
                            loading_animation.playAnimation();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent1 = new Intent(MeasurementActivity.this,CalculatedSizeActivity.class);
                                    intent1.putParcelableArrayListExtra("LIST", (ArrayList<? extends Parcelable>) resultModelList);
                                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent1);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                    loading_animation.cancelAnimation();
                                }
                            }, 1000);


                        }



                        //gotoresult();
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
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(MeasurementActivity.this,MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
        finish();
    }


}
