package com.pomelo.pixibo.Main;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pomelo.pixibo.Model.ResultModel;
import com.pomelo.pixibo.R;
import com.pomelo.pixibo.Session;
import com.pomelo.pixibo.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CalculatedSizeActivity extends AppCompatActivity {


    private List<ResultModel> resultModelList = new ArrayList<>();
    private TextView tv_item_fit,tv_best_size,tv_pref_fit_value;
    private TextView tv_bust_fit,tv_waist_fit,tv_hips_fit;
    private TextView tv_fit_toggle,tv_back,tv_bra_size,tv_closest_fit;
    private ImageView iv_terrible,iv_very_bad,iv_okay,iv_love_it,iv_amazing;
    private Button bt_start_over,bt_add_bag;
    private LinearLayout layout_fit;
    private PopupWindow popup_fit;
    private RadioGroup rg_fit;
    private RadioButton rb;
    private ViewGroup root;
    private boolean isRecommended;
    private Session session;
    private int size_count;
    private String size_small = "";
    private String size_recommended = "";
    private String size_large = "";
    private String fit_checkedId = "";
    private Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculated_size);

        session = new Session(this);
        root = (ViewGroup) getWindow().getDecorView().getRootView();
        tv_item_fit = findViewById(R.id.tv_item_fit);
        tv_best_size = findViewById(R.id.tv_best_size);
        tv_pref_fit_value = findViewById(R.id.tv_pref_fit_value);

        tv_fit_toggle = findViewById(R.id.tv_fit_toggle);
        tv_back = findViewById(R.id.tv_back);
        tv_closest_fit = findViewById(R.id.tv_closest_fit);

        tv_bust_fit = findViewById(R.id.tv_bust_fit);
        tv_waist_fit = findViewById(R.id.tv_waist_fit);
        tv_hips_fit = findViewById(R.id.tv_hips_fit);

        iv_terrible = findViewById(R.id.iv_terrible);
        iv_very_bad = findViewById(R.id.iv_very_bad);
        iv_okay = findViewById(R.id.iv_okay);
        iv_love_it = findViewById(R.id.iv_love_it);
        iv_amazing = findViewById(R.id.iv_amazing);

        layout_fit = findViewById(R.id.layout_fit);

        bt_start_over = findViewById(R.id.bt_start_over);
        bt_add_bag = findViewById(R.id.bt_add_bag);


        tv_pref_fit_value.setText(session.get_fit());

        tv_fit_toggle.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_expand_down),null);

        intent = getIntent();

        if (intent.hasExtra("LIST"))
        {
            resultModelList = intent.getParcelableArrayListExtra("LIST");
            set_data();
        }
        else
        {
            if (session.getIsSize())
            {
                get_old_data(session.get_JSON());

                tv_best_size.setText("Size "+session.get_size());
                //tv_best_size.setCompoundDrawables(null,null,null,null);

                setFitImage(session.get_confidence());
                setFit(tv_bust_fit,session.get_bust());
                setFit(tv_waist_fit,session.get_waist());
                setFit(tv_hips_fit,session.get_hip());

                if (!session.getIsRecommended())
                {
                    tv_closest_fit.setVisibility(View.VISIBLE);
                    tv_best_size.setTextColor(getColor(R.color.grey_dark));
                    tv_item_fit.setText("OUR SIZES MAY NOT BE A PERFECT FIT");
                    isRecommended = false;


                }
            }

        }


        tv_best_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecommended)
                {
                    fit_popup();
                }

            }
        });

        tv_pref_fit_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(CalculatedSizeActivity.this,MeasurementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("data","pref_fit");
                intent.putParcelableArrayListExtra("LIST", (ArrayList<? extends Parcelable>) resultModelList);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(CalculatedSizeActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


        bt_start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CalculatedSizeActivity.this,MeasurementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        tv_fit_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layout_fit.getVisibility() == View.VISIBLE)
                {
                    tv_fit_toggle.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_expand_down),null);
                    layout_fit.setVisibility(View.GONE);
                }
                else
                {
                    tv_fit_toggle.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_expand_up),null);
                    layout_fit.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public void setFitImage(String confidence)
    {
        Log.e("confidence",confidence);
        switch (confidence)
        {
            case "1":
                iv_terrible.setImageDrawable(getDrawable(R.drawable.ic_terrible_inactive));
                iv_very_bad.setImageDrawable(getDrawable(R.drawable.ic_very_bad_disable));
                iv_okay.setImageDrawable(getDrawable(R.drawable.ic_okay_disable));
                iv_love_it.setImageDrawable(getDrawable(R.drawable.ic_love_it_disable));
                iv_amazing.setImageDrawable(getDrawable(R.drawable.ic_amazing_disable));
                break;
            case "2":
                iv_terrible.setImageDrawable(getDrawable(R.drawable.ic_terrible_inactive));
                iv_very_bad.setImageDrawable(getDrawable(R.drawable.ic_very_bad_inactive));
                iv_okay.setImageDrawable(getDrawable(R.drawable.ic_okay_disable));
                iv_love_it.setImageDrawable(getDrawable(R.drawable.ic_love_it_disable));
                iv_amazing.setImageDrawable(getDrawable(R.drawable.ic_amazing_disable));
                break;
            case "3":
                iv_terrible.setImageDrawable(getDrawable(R.drawable.ic_terrible_active));
                iv_very_bad.setImageDrawable(getDrawable(R.drawable.ic_very_bad_active));
                iv_okay.setImageDrawable(getDrawable(R.drawable.ic_okay_active));
                iv_love_it.setImageDrawable(getDrawable(R.drawable.ic_love_it_disable));
                iv_amazing.setImageDrawable(getDrawable(R.drawable.ic_amazing_disable));
                break;
            case "4":
                iv_terrible.setImageDrawable(getDrawable(R.drawable.ic_terrible_active));
                iv_very_bad.setImageDrawable(getDrawable(R.drawable.ic_very_bad_active));
                iv_okay.setImageDrawable(getDrawable(R.drawable.ic_okay_active));
                iv_love_it.setImageDrawable(getDrawable(R.drawable.ic_love_it_active));
                iv_amazing.setImageDrawable(getDrawable(R.drawable.ic_amazing_disable));
                break;
            case "5":
                iv_terrible.setImageDrawable(getDrawable(R.drawable.ic_terrible_active));
                iv_very_bad.setImageDrawable(getDrawable(R.drawable.ic_very_bad_active));
                iv_okay.setImageDrawable(getDrawable(R.drawable.ic_okay_active));
                iv_love_it.setImageDrawable(getDrawable(R.drawable.ic_love_it_active));
                iv_amazing.setImageDrawable(getDrawable(R.drawable.ic_amazing_active));
                break;
        }
    }

    public void setFit(TextView textView ,String value)
    {
        switch (value)
        {
            case "0":
                textView.setText("-");
                textView.setTextColor(getColor(R.color.grey_dark));
                break;
            case "1":
                textView.setText("Too Tight");
                textView.setTextColor(getColor(R.color.grey_dark));
                break;
            case "2":
                textView.setText("Snug Fit");
                textView.setTextColor(getColor(R.color.grey_dark));
                break;
            case "3":
                textView.setText("Ideal Fit");
                textView.setTextColor(getColor(R.color.green_fit));
                break;
            case "4":
                textView.setText("Loose Fit");
                textView.setTextColor(getColor(R.color.green_fit));
                break;
            case "5":
                textView.setText("Too Loose");
                textView.setTextColor(getColor(R.color.green_fit));
                break;
        }
    }

    public void fit_popup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popupView = layoutInflater.inflate(R.layout.popup_best_size, root,false);
        popup_fit = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        tv_bra_size = popupView.findViewById(R.id.tv_bra_size);
        tv_bra_size.setText("For this item, you’ll look best in "+session.get_size());
        rg_fit = popupView.findViewById(R.id.rg_fit);




        switch (size_count)
        {
            case 2:
                if (size_small.equals(""))
                {
                    rb = popupView.findViewById(R.id.radio_a);
                    rb.setVisibility(View.GONE);

                    rg_fit.check(R.id.radio_b);
                    change_radio_text((RadioButton) popupView.findViewById(R.id.radio_b),size_recommended);
                    change_radio_text((RadioButton) popupView.findViewById(R.id.radio_c),size_large);
                }
                else if(size_large.equals(""))
                {
                    rb = popupView.findViewById(R.id.radio_c);
                    rb.setVisibility(View.GONE);

                    rg_fit.check(R.id.radio_b);
                    change_radio_text((RadioButton) popupView.findViewById(R.id.radio_a),size_small);
                    change_radio_text((RadioButton) popupView.findViewById(R.id.radio_b),size_recommended);
                }



                break;

            case 3:
                rg_fit.check(R.id.radio_b);
                change_radio_text((RadioButton) popupView.findViewById(R.id.radio_a),size_small);
                change_radio_text((RadioButton) popupView.findViewById(R.id.radio_b),size_recommended);
                change_radio_text((RadioButton) popupView.findViewById(R.id.radio_c),size_large);
                break;
        }

        if (!fit_checkedId.equals(""))
        {
            rg_fit.check(Integer.parseInt(fit_checkedId));
        }

        rg_fit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                fit_checkedId = String.valueOf(checkedId);

                switch (checkedId)
                {
                    case R.id.radio_a:

                        Log.e("Clicked","A");
                        if (size_count == 2)
                        {
                            setFitImage(resultModelList.get(0).getConfidence());
                            setFit(tv_bust_fit,resultModelList.get(0).getBust());
                            setFit(tv_waist_fit,resultModelList.get(0).getWaist());
                            setFit(tv_hips_fit,resultModelList.get(0).getHip());
                            tv_best_size.setText("Size "+size_small);
                            tv_best_size.setTextColor(getColor(R.color.grey_dark));
                            tv_item_fit.setText("For this item, we would recommend Size "+size_recommended);
                        }
                        else
                        {
                            setFitImage(resultModelList.get(0).getConfidence());
                            setFit(tv_bust_fit,resultModelList.get(0).getBust());
                            setFit(tv_waist_fit,resultModelList.get(0).getWaist());
                            setFit(tv_hips_fit,resultModelList.get(0).getHip());
                            tv_best_size.setText("Size "+size_small);
                            tv_best_size.setTextColor(getColor(R.color.grey_dark));
                            tv_item_fit.setText("For this item, we would recommend Size "+size_recommended);
                        }

                        break;

                    case R.id.radio_b:
                        Log.e("Clicked","B");
                        if (size_count == 2)
                        {
                            setFitImage(resultModelList.get(0).getConfidence());
                            setFit(tv_bust_fit,resultModelList.get(0).getBust());
                            setFit(tv_waist_fit,resultModelList.get(0).getWaist());
                            setFit(tv_hips_fit,resultModelList.get(0).getHip());
                            tv_best_size.setText("Size "+size_recommended);
                            tv_best_size.setTextColor(getColor(R.color.black));
                            tv_item_fit.setText("For this item, you’ll look best in");
                        }
                        else
                        {
                            setFitImage(resultModelList.get(1).getConfidence());
                            setFit(tv_bust_fit,resultModelList.get(1).getBust());
                            setFit(tv_waist_fit,resultModelList.get(1).getWaist());
                            setFit(tv_hips_fit,resultModelList.get(1).getHip());
                            tv_best_size.setText("Size "+size_recommended);
                            tv_best_size.setTextColor(getColor(R.color.black));
                            tv_item_fit.setText("For this item, you’ll look best in");
                        }

                        break;
                    case R.id.radio_c:
                        Log.e("Clicked","C");
                        if (size_count == 2)
                        {
                            setFitImage(resultModelList.get(1).getConfidence());
                            setFit(tv_bust_fit,resultModelList.get(1).getBust());
                            setFit(tv_waist_fit,resultModelList.get(1).getWaist());
                            setFit(tv_hips_fit,resultModelList.get(1).getHip());
                            tv_best_size.setText("Size "+size_large);
                            tv_best_size.setTextColor(getColor(R.color.grey_dark));
                            tv_item_fit.setText("For this item, we would recommend Size "+size_recommended);
                        }
                        else
                        {
                            setFitImage(resultModelList.get(2).getConfidence());
                            setFit(tv_bust_fit,resultModelList.get(2).getBust());
                            setFit(tv_waist_fit,resultModelList.get(2).getWaist());
                            setFit(tv_hips_fit,resultModelList.get(2).getHip());
                            tv_best_size.setText("Size "+size_large);
                            tv_best_size.setTextColor(getColor(R.color.grey_dark));
                            tv_item_fit.setText("For this item, we would recommend Size "+size_recommended);
                        }

                }
                //Log.e("fit_checkedId",fit_checkedId);

                popup_fit.dismiss();
            }
        });

        popup_fit.setTouchable(true);
        popup_fit.setFocusable(true);

        Utils.applyDim(root, 0.7f);


        popup_fit.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
            }
        });

        popup_fit.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }

    public void change_radio_text(RadioButton radioButton , String text)
    {
        radioButton.setText(text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(CalculatedSizeActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
        finish();
    }


    public void get_old_data(String result)
    {
        try
        {
            resultModelList.clear();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("fys");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                ResultModel model = new ResultModel();

                model.setRecommended(obj.getBoolean("recommended"));
                model.setSize(obj.getString("size"));
                model.setBust(String.valueOf(obj.getInt("bust")));
                model.setWaist(String.valueOf(obj.getInt("waist")));
                model.setHip(String.valueOf(obj.getInt("hip")));
                model.setBust8Bit(String.valueOf(obj.getInt("bust8Bit")));
                model.setWaist8Bit(String.valueOf(obj.getInt("waist8Bit")));
                model.setWaist8Bit(String.valueOf(obj.getInt("hip8Bit")));
                model.setConfidence(String.valueOf(obj.getInt("confidence")));

                resultModelList.add(model);

            }
            set_data();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void set_data()
    {
        size_count = resultModelList.size();


        switch (resultModelList.size())
        {
            case 1:

                tv_best_size.setText("Size "+resultModelList.get(0).getSize());
                tv_best_size.setCompoundDrawables(null,null,null,null);

                setFitImage(resultModelList.get(0).getConfidence());
                setFit(tv_bust_fit,resultModelList.get(0).getBust());
                setFit(tv_waist_fit,resultModelList.get(0).getWaist());
                setFit(tv_hips_fit,resultModelList.get(0).getHip());

                if (resultModelList.get(0).isRecommended())
                {
                    tv_best_size.setTextColor(getColor(R.color.black));
                }
                else
                {

                    session.setIsRecommended(false);
                    tv_closest_fit.setVisibility(View.VISIBLE);
                    tv_best_size.setTextColor(getColor(R.color.grey_dark));
                    tv_item_fit.setText("OUR SIZES MAY NOT BE A PERFECT FIT");
                    isRecommended = false;
                }
                break;

            case 2:

                if (resultModelList.get(0).isRecommended()||resultModelList.get(1).isRecommended() )
                {
                    isRecommended = true;

                    if (resultModelList.get(0).isRecommended())
                    {
                        tv_best_size.setText("Size "+resultModelList.get(0).getSize());

                        size_recommended = resultModelList.get(0).getSize();
                        size_large = resultModelList.get(1).getSize();

                        setFitImage(resultModelList.get(0).getConfidence());
                        setFit(tv_bust_fit,resultModelList.get(0).getBust());
                        setFit(tv_waist_fit,resultModelList.get(0).getWaist());
                        setFit(tv_hips_fit,resultModelList.get(0).getHip());

                    }
                    else if (resultModelList.get(1).isRecommended())
                    {
                        tv_best_size.setText("Size "+resultModelList.get(1).getSize());
                        size_small = resultModelList.get(0).getSize();
                        size_recommended = resultModelList.get(1).getSize();

                        setFitImage(resultModelList.get(1).getConfidence());
                        setFit(tv_bust_fit,resultModelList.get(1).getBust());
                        setFit(tv_waist_fit,resultModelList.get(1).getWaist());
                        setFit(tv_hips_fit,resultModelList.get(1).getHip());
                    }
                }


                break;

            case 3:

                if (resultModelList.get(0).isRecommended()||resultModelList.get(1).isRecommended() || resultModelList.get(2).isRecommended() )
                {
                    isRecommended = true;

                    size_small = resultModelList.get(0).getSize();
                    size_recommended = resultModelList.get(1).getSize();
                    size_large = resultModelList.get(2).getSize();


                    if (resultModelList.get(0).isRecommended())
                    {
                        tv_best_size.setText("Size "+resultModelList.get(0).getSize());

                        setFitImage(resultModelList.get(0).getConfidence());
                        setFit(tv_bust_fit,resultModelList.get(0).getBust());
                        setFit(tv_waist_fit,resultModelList.get(0).getWaist());
                        setFit(tv_hips_fit,resultModelList.get(0).getHip());
                    }
                    else if (resultModelList.get(1).isRecommended())
                    {
                        tv_best_size.setText("Size "+resultModelList.get(1).getSize());

                        setFitImage(resultModelList.get(1).getConfidence());
                        setFit(tv_bust_fit,resultModelList.get(1).getBust());
                        setFit(tv_waist_fit,resultModelList.get(1).getWaist());
                        setFit(tv_hips_fit,resultModelList.get(1).getHip());
                    }
                    else if (resultModelList.get(2).isRecommended())
                    {
                        tv_best_size.setText("Size "+resultModelList.get(2).getSize());

                        setFitImage(resultModelList.get(2).getConfidence());
                        setFit(tv_bust_fit,resultModelList.get(2).getBust());
                        setFit(tv_waist_fit,resultModelList.get(2).getWaist());
                        setFit(tv_hips_fit,resultModelList.get(2).getHip());
                    }
                }
                break;
        }
    }
}
