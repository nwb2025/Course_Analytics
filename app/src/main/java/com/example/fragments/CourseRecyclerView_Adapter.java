package com.example.fragments;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;



public class CourseRecyclerView_Adapter  extends  RecyclerView.Adapter<CourseRecyclerView_Adapter.ViewHolder> implements Filterable {
    private static final String TAG = "CourseRecyclerView_Adap";
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Course> filtered_courses = new ArrayList<>();
    private Context context;


    public  CourseRecyclerView_Adapter(Context context){
        this.context = context;
    }

    @NonNull
    // inflate the item layout and create a  viewholder(a single element of recyclerview)
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_course,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    // to set viewholder attributes based on the data

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.courseName.setText(filtered_courses.get(pos).getName().replaceAll("\\s+"," "));

        if (filtered_courses.get(pos).getDuration().equals("не ограничена"))
            holder.dur.setText("∞");
        else
            holder.dur.setText(filtered_courses.get(pos).getDuration());
        holder.level.setText(filtered_courses.get(pos).getHardship());
        holder.lang.setText(filtered_courses.get(pos).getLanguage());

        if(filtered_courses.get(pos).getCost()==0)
            holder.cost.setText("бесплатно");
        else
            holder.cost.setText(String.valueOf(filtered_courses.get(pos).getCost()) + " ₽" );
        holder.desc.setText(filtered_courses.get(pos).getDesc());
        holder.practice_tv2.setText(filtered_courses.get(pos).getPractice());
        holder.format_tv2.setText(filtered_courses.get(pos).getFormat());

        if(!filtered_courses.get(pos).getUrl().equals("")){
            Glide.with(context)
                    .asBitmap()
                    .load(filtered_courses.get(pos).getUrl())
                    .into(holder.image_course);
        }else{
            holder.image_course.setImageDrawable(context.getResources().getDrawable(R.drawable.default_img));
        }

        holder.see_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Course_information_activity.class);
                intent.putExtra("position",filtered_courses.get(pos).getId());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return filtered_courses.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image_course,cost_iv,level_iv,lang_iv,duration_iv,format_iv,practice_iv;
        private TextView courseName,dur,cost,lang,level,desc,format_tv1,format_tv2,practice_tv1,practice_tv2;
        private CardView cardview_course;
        private Button see_course;

        public ViewHolder(View itemview)
        {
            super(itemview);
            image_course = itemview.findViewById(R.id.image_course);
            cost_iv = itemview.findViewById(R.id.cost_iv);
            level_iv = itemview.findViewById(R.id.level_iv);
            lang_iv = itemview.findViewById(R.id.lang_iv);
            duration_iv = itemview.findViewById(R.id.duration_iv);
            courseName = itemview.findViewById(R.id.course_name);
            dur= itemview.findViewById(R.id.dur);
            level = itemview.findViewById(R.id.level);
            lang = itemview.findViewById(R.id.lang);
            cost = itemview.findViewById(R.id.cost);
            desc = itemview.findViewById(R.id.desc);
            practice_iv = (ImageView) itemview.findViewById(R.id.practice_iv);
            format_iv = (ImageView) itemview.findViewById(R.id.format_iv);
            practice_tv1 = (TextView) itemview.findViewById(R.id.practice_tv1);
            practice_tv2 = (TextView) itemview.findViewById(R.id.practice_tv2);
            format_tv1 = (TextView) itemview.findViewById(R.id.format_tv1);
            format_tv2 = (TextView) itemview.findViewById(R.id.format_tv2);
            see_course = itemview.findViewById(R.id.see_course);
            cardview_course = itemview.findViewById(R.id.cardview_course);
        }
    }
    public void setCourses(ArrayList<Course> courses){
        this.courses =new ArrayList<>(courses);
        this.filtered_courses = new ArrayList<>(courses);
        notifyDataSetChanged();
    }



    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0 || constraint.length()<3) {
                    results.values = courses;
                    results.count = courses.size();
                } else {
                    ArrayList<Course> filtered_res = new ArrayList<>();
                    String query = constraint.toString().toLowerCase().trim(); // trim removes empty spaces

                    for (Course c : courses) {
                        if (c.getName().toLowerCase().contains(query)) {
                            filtered_res.add(c);
                        }
                    }
                    results.values = filtered_res;
                    results.count = filtered_res.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered_courses = (ArrayList) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
