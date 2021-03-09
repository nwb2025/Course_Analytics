package com.example.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.ScrollView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.r0adkll.slidr.Slidr;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class Course_information_activity extends AppCompatActivity {
    private static final String TAG = "Course_information_acti";
    private ImageView level_iv,dur_iv,lan_iv,format_iv,image_dot,results_iv,price_iv;
    private TextView name_tv,about_tv,level_tv1,level_tv2,dur_tv1,dur_tv2,lan_tv1,lan_tv2,format_tv1,format_tv2,textview_item,features_tv,results_tv1,results_tv2,link_tv,price_tv,price_tv2;
    private ScrollView scrollView;
    private ExpandableListView ex_lv;
    private ExpandableTextView desc;
    private LinearLayout relLayout;
    private PieChart pieChart_ratio;
    private RelativeLayout relativeLayout;
    private Button btn_add;
    private DatabaseHelper db;
    private static final ArrayList<Integer> colors = new ArrayList<Integer>(){{
        add(Color.parseColor("#FF001A55"));
        add(Color.parseColor("#FF3949AB"));
        add(Color.parseColor("#FF000000"));
        add(Color.parseColor("#7E001A55"));
        add(Color.parseColor("#FF281253"));
    }};



    @Override
    protected  void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.course_information);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Slidr.attach(this);

        scrollView = (ScrollView) findViewById(R.id.scrollview_course);


        results_iv = (ImageView) findViewById(R.id.results_iv);
        results_tv1 = (TextView) findViewById(R.id.results_tv1);
        results_tv2 = (TextView) findViewById(R.id.resylts_tv2);
        link_tv = (TextView) findViewById(R.id.link_tv);


        level_iv = findViewById(R.id.level_iv);
        dur_iv = findViewById(R.id.dur_iv);
        lan_iv = findViewById(R.id.lan_iv);
        format_iv = findViewById(R.id.format_iv);

        name_tv = findViewById(R.id.name_tv);

        about_tv = findViewById(R.id.about_tv);
        desc = (ExpandableTextView) findViewById(R.id.desc);
        level_tv1 = findViewById(R.id.level_tv1);
        level_tv2 = findViewById(R.id.level_tv2);
        dur_tv1 = findViewById(R.id.dur_tv1);
        dur_tv2 = findViewById(R.id.dur_tv2);
        lan_tv1 = findViewById(R.id.lan_tv1);
        lan_tv2 = findViewById(R.id.lan_tv2);
        format_tv1 = findViewById(R.id.format_tv1);
        format_tv2 = findViewById(R.id.format_tv2);
        image_dot = (ImageView) findViewById(R.id.image_dot);
        textview_item = (TextView) findViewById(R.id.textview_item);
        price_iv = (ImageView) findViewById(R.id.price_iv);
        price_tv = (TextView) findViewById(R.id.price_tv1);
        price_tv2 = (TextView) findViewById(R.id.price_tv2);

        features_tv = (TextView) findViewById(R.id.features_tv);
        relLayout = (LinearLayout) findViewById(R.id.layout_features);

        ex_lv = (ExpandableListView) findViewById(R.id.ex_lv);
        pieChart_ratio = (PieChart) findViewById(R.id.pie_chart_ratio);

        relativeLayout = (RelativeLayout) findViewById(R.id.chapter);
        btn_add = (Button) findViewById(R.id.btn_add);


        Course course;
        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        int index = intent.getIntExtra("position", -1);
        if (index == -1) {
            index = intent.getIntExtra("position_fv", -1);
            course = db.getFavCourse(index);
        } else {
            course = db.getAllCourse(index);
        }


        relativeLayout.setBackgroundColor(colors.get((int)(Math.random() * 5)));

        if(db.checkIfExists(course.getId())){
            btn_add.setClickable(false);
            btn_add.setBackgroundTintList(getResources().getColorStateList(R.color.button_add));
        }
        else {


            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.addToFavorite(course.getId())) {// it's ok
                        Toast.makeText(Course_information_activity.this, "Курс успешно добавлен в Избранное", Toast.LENGTH_SHORT).show();

                    }
                    btn_add.setClickable(false);
                    btn_add.setBackgroundTintList(getResources().getColorStateList(R.color.button_add));



                }
            });
        }

        name_tv.setText(course.getName());
        desc.setExpandLines(5); // show the first 5 lines
        desc.setText(course.getDesc());

        // setting the listener for shrinking/expanding a text view
        desc.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                course.setShrink(isShrink);


            }
        });
        desc.setText(course.getDesc());
        desc.resetState(course.isShrink());
        price_tv.setText("Стоимость");
        if(course.getCost() == 0)
            price_tv2.setText("бесплатно");
        else{
            price_tv2.setText(String.valueOf(course.getCost()) + " ₽");
        }


        List<String> feat = Arrays.asList(course.getFeatures().trim().split("\\r?\\n"));


        for(int i=0;i<feat.size();i++)
        {

            RelativeLayout relativeLayout = new RelativeLayout(Course_information_activity.this);


            LinearLayout.LayoutParams layoutParams_layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            RelativeLayout.LayoutParams layoutParams_textview = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(26,26);


            ImageView imageView = new ImageView(Course_information_activity.this);
            imageView.generateViewId();

            TextView textView = new TextView(Course_information_activity.this);

            layoutParams_textview.addRule(RelativeLayout.RIGHT_OF,imageView.getId());
            //layoutParams_textview.leftMargin = 50; another way to set margins
            layoutParams_textview.setMargins(50,10,0,0);

            textView.setText(feat.get(i));
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textView.setLayoutParams(layoutParams_textview);

            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            imageView.setImageResource(R.drawable.tick);
            imageView.setColorFilter(getResources().getColor(R.color.purple_500));
            imageView.setLayoutParams(layoutParams);

            relativeLayout.setLayoutParams(layoutParams_layout);
            relativeLayout.addView(imageView);
            relativeLayout.addView(textView);

            relLayout.addView(relativeLayout);
        }



        if(course.getHardship().equals("начальный")){
            level_tv1.setText("Начальный уровень");
            level_tv2.setText("низкий порог вхождения");
        }
        else if(course.getHardship().equals("продвинутый")){
            level_tv1.setText("Продвинутый уровень");
            level_tv2.setText("даст  более глубокое понимание предмета");
        }
        else if(course.getHardship().equals("средний")){
            level_tv1.setText("Средний уровень");
            level_tv2.setText("позволит улучшить уже имеющиеся знания");
        }
        if(course.getDuration().equals("Не ограничена")){
            dur_tv1.setText(course.getDuration());
            dur_tv2.setText("можно проходить в любое время в свободном темпе");
        }else{
            dur_tv1.setText(course.getDuration());
            dur_tv2.setText("ориентировочно курс может быть пройден за 2 месяца");

        }

        if(course.getLanguage().length()>15){
            lan_tv1.setText("Преподается на языках: " + course.getLanguage());
            lan_tv2.setText("несколько вариантов языков");
        }else{
            lan_tv1.setText("Преподается на языках: " + course.getLanguage());
            lan_tv2.setText("");
        }

        if(course.getFormat().equals("Курс")){
            format_tv1.setText("Формат подачи материала:  Курс ");
            format_tv2.setText("вся информация собрана в одном месте");
        }else if(course.getFormat().equals("лекции") || course.getFormat().equals("Видео-лекции")){
            format_tv1.setText("Формат подачи материала:  Лекции");
            format_tv2.setText("научный подход");
        }

        else{
            format_tv1.setText("Формат подачи материала: cмешанный");
            format_tv2.setText("");
        }
        List<String> headers =  new ArrayList<>();
        headers.add("Содержание");

        List<String> content  = Arrays.asList(course.getContent().split("\\r?\\n"));

        HashMap<String, List<String>> childs_content = new HashMap<String,List<String>>();
        childs_content.put(headers.get(0),content);

        ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(),headers,childs_content);
        ex_lv.setAdapter(adapter);

        // setting the arrow to the end of a group item
        DisplayMetrics displayMetrics = new DisplayMetrics(); // get the infromation about our display
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int width = displayMetrics.widthPixels; // get the width of the display
        ex_lv.setIndicatorBounds(width-GetDipsFromPixel(35),width-GetDipsFromPixel(25));


        ex_lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setExpandableListViewHeight(ex_lv,groupPosition);
                return false;
            }
        });

        pieChart_ratio.setUsePercentValues(true);
        ArrayList<PieEntry> ratios = new ArrayList<>(); // create a list for our entries
        List<String> r_data = Arrays.asList(course.getRatio().split(" "));
        ratios.add(new PieEntry(Integer.parseInt(r_data.get(0)),"изображение"));
        ratios.add(new PieEntry(Integer.parseInt(r_data.get(1)),"текст"));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#D4BBF6"));
        colors.add(Color.parseColor("#B38FE6"));


        PieDataSet pieDataSet = new PieDataSet(ratios,"");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData();
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setDataSet(pieDataSet);

        pieChart_ratio.setData(pieData);
        pieChart_ratio.getDescription().setEnabled(false);
        pieChart_ratio.setCenterText("Доля Визуализации");

        pieChart_ratio.animate();

        String text = new String("Перейти к материалам курса");
        link_tv.setText("visit site");
        link_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT,course.getUrl());
                Intent chooser = Intent.createChooser(intent,"Choose an app");
                startActivity(chooser);
                //WebView webView = new WebView(Course_information_activity.this);
                //webView.loadUrl(course.getUrl());
            }
        });
        SpannableString spannable_str = new SpannableString(text);
        ClickableSpan c = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW); // start intent to load a url
                intent.setData(Uri.parse(course.getUrl_site()));
                startActivity(intent);
            }
        };
        spannable_str.setSpan(c,0,26, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        link_tv.setText(spannable_str);
        link_tv.setMovementMethod(LinkMovementMethod.getInstance()); // so the clicks will work


        results_tv2.setText(course.getResults().toLowerCase());






    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setExpandableListViewHeight(ExpandableListView listView,
                                             int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    // a function that transfers pixels to dp
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 250.5f);
    }
}
