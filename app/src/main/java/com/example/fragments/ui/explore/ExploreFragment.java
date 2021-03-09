package com.example.fragments.ui.explore;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fragments.Course;
import com.example.fragments.CourseRecyclerView_Adapter;
import com.example.fragments.DatabaseHelper;
import com.example.fragments.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;



public class ExploreFragment extends Fragment {
    private static final String TAG = "ExploreFragment";
    private  DatabaseHelper db;
    private RecyclerView recyclerView;
    private Button checkBox_en,checkBox_ru, checkBox_paid , checkBox_free,checkBox_course,checkBox_lec,checkBox_begginer;
    private SearchView sv;
    private Context context;
    private Handler handler;

    private LinkedHashMap<String,String> course = new LinkedHashMap<>();
    private  static  ArrayList<Button> buttons=null;
    private static ArrayList<Course> courses = new ArrayList<>();
    private static CourseRecyclerView_Adapter adapter;
    private Button button;


   

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);



        context = root.getContext();
        recyclerView = root.findViewById(R.id.recylerview_courses);
        checkBox_en = root.findViewById(R.id.checkbox_en);
        checkBox_course = root.findViewById(R.id.checkbox_course);
        checkBox_free = root.findViewById(R.id.checkbox_free);
        checkBox_lec = root.findViewById(R.id.button_lec);
        checkBox_ru = root.findViewById(R.id.checkbox_ru);
        checkBox_paid = root.findViewById(R.id.checkbox_paid);
        checkBox_begginer = (Button) root.findViewById(R.id.checkbox_bg);
        sv = (SearchView) root.findViewById(R.id.sv);
        buttons = new ArrayList<Button>(){{
            add(checkBox_en);
            add(checkBox_course);
            add(checkBox_free);
            add(checkBox_lec);
            add(checkBox_ru);
            add(checkBox_paid);
            add(checkBox_begginer);
        }};


        adapter = new CourseRecyclerView_Adapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        /*handler = new Handler(){
            public void handleMessage(android.os.Message message){
                if(message.what == 1){
                    adapter.setCourses(courses);
                }
            }
        };*/
        //loadData();
        db = new DatabaseHelper(getContext());
        courses = db.getAllCourses();
        adapter.setCourses(courses);

        /* Here're the button listeners */
        checkBox_free.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<Course> filtered_c = new ArrayList<>();
                for(Course c:courses){
                    if(c.getCost()==0)
                        filtered_c.add(c);
                }

                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                setOffButton(checkBox_free);

                return false;
            }
        });
        checkBox_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_free.isSelected()) {
                    checkBox_free.setSelected(false);
                    setOffFilters();
                }
                else
                    checkBox_free.setSelected(true);
                course.put(DatabaseHelper.col2,"0");
            }
        });
        checkBox_paid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ArrayList<Course> filtered_c = new ArrayList<>();
                for(Course c:courses){
                    if(c.getCost()>0)
                        filtered_c.add(c);
                }
                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                setOffButton(checkBox_paid);
                return false;
            }
        });
        checkBox_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_paid.isSelected()){
                    checkBox_paid.setSelected(false);
                    setOffFilters();}
                else
                    checkBox_paid.setSelected(true);
                /// another query !
                course.put(DatabaseHelper.col2,"1");

            }
        });
        checkBox_en.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<Course> filtered_c = new ArrayList<>();
                for(Course c:courses){
                    if(c.getLanguage().equals("English"))
                        filtered_c.add(c);
                }

                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                setOffButton(checkBox_en);

                return false;
            }
        });
        checkBox_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_en.isSelected()){
                    checkBox_en.setSelected(false);
                    setOffFilters();}
                else
                    checkBox_en.setSelected(true);
                course.put(DatabaseHelper.col8,"English");
            }
        });

        checkBox_ru.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<Course> filtered_c = new ArrayList<>();
                for(Course c:courses){
                    if(c.getLanguage().equals("Русский"))
                        filtered_c.add(c);
                }
                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                setOffButton(checkBox_ru);

                return false;
            }
        });
        checkBox_ru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_ru.isSelected()){
                    checkBox_ru.setSelected(false);
                    setOffFilters();}
                else checkBox_ru.setSelected(true);
                course.put(DatabaseHelper.col8,"Русский");
            }
        });

        checkBox_lec.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ArrayList<Course> filtered_c= new ArrayList<>();
                for(Course c:courses){
                    if(c.getFormat().contains("лекции"))
                        filtered_c.add(c);
                }
                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                setOffButton(checkBox_lec);
                return false;
            }
        });
        checkBox_lec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_lec.isSelected()){
                    checkBox_lec.setSelected(false);
                    setOffFilters();}
                else checkBox_lec.setSelected(true);
                course.put(DatabaseHelper.col6,"лекции");
            }
        });

        checkBox_course.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<Course> filtered_c = new ArrayList<>();
                for(Course c:courses){
                    if(c.getFormat().contains("Курс"))
                        filtered_c.add(c);
                }
                setOffButton(checkBox_course);
                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        checkBox_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_course.isSelected()){
                    checkBox_course.setSelected(false);
                    setOffFilters();}
                else checkBox_course.setSelected(true);
                course.put(DatabaseHelper.col6,"Курс");
            }
        });


        checkBox_begginer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ArrayList<Course> filtered_c = new ArrayList<>();
                for(Course c:courses){
                    if(c.getHardship().equals("начальный"))
                        filtered_c.add(c);
                }
                setOffButton(checkBox_begginer);
                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        checkBox_begginer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_begginer.isSelected()){
                    checkBox_begginer.setSelected(false);
                    setOffFilters();}
                else checkBox_begginer.setSelected(true);
                course.put(DatabaseHelper.col3,"начальный");
            }

        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                course.put(DatabaseHelper.col1,query);
                Cursor cursor1 =  db.exQuery2(course);

                ArrayList<Course> filtered_c = new ArrayList<>();
                while(cursor1.moveToNext())
                {
                    filtered_c.add(new Course(Integer.parseInt(cursor1.getString(0)), cursor1.getString(1),Integer.parseInt(cursor1.getString(2)), cursor1.getString(3), cursor1.getString(4), cursor1.getString(5), cursor1.getString(6), cursor1.getString(7), cursor1.getString(8), cursor1.getString(9), cursor1.getString(10), cursor1.getString(11), cursor1.getString(12), cursor1.getString(13),cursor1.getString(14)));
                }
                adapter.setCourses(filtered_c);
                adapter.notifyDataSetChanged();
                course.clear();
                setOffButton(null);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                button.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // here we put another button into searchview dynamically
        int sub_area = sv.getContext().getResources().getIdentifier("android:id/submit_area",null,null);
        LinearLayout linearLayoutSearchView = (LinearLayout) sv.getChildAt(0);

        button = new Button(context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOffButton(null);
                setOffFilters();
            }
        });
        button.setText("сбросить");
        button.setVisibility(View.INVISIBLE);
        button.setBackground(context.getResources().getDrawable(R.drawable.rounded_corners_edittext));
        linearLayoutSearchView.addView(button);




        return root;
    }
    private void setOffButton(Button current_button){
        for(Button b:buttons){
            if(b!= current_button){
                if (b.isSelected())
                    b.setSelected(false);
            }
        }
    }
    public void setOffFilters(){
        course.clear();
        adapter.setCourses(courses);
        adapter.notifyDataSetChanged();
        button.setVisibility(View.INVISIBLE);
    }


    
    private void loadData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db = new DatabaseHelper(getContext());
                courses = db.getAllCourses();
                handler.sendEmptyMessage(1);
            }
        });
        thread.start();
    }







}