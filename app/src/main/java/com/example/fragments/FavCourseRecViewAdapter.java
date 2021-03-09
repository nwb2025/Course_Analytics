package com.example.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

// creating the custom rec adapter it takes the custom view holder
// recyclerview requires to implement a viewholder
// RecyclerView.Adapter<FavCourseRecViewAdapter.ViewHolder> - it means what kind of conten (what type of object this adapter contains)
public class FavCourseRecViewAdapter extends
        RecyclerView.Adapter<FavCourseRecViewAdapter.ViewHolder>
{
    private ArrayList<Course> fav_courses = new ArrayList<>();
    private Context context;
    private static final String TAG = "FavCourseRecViewAdapter";
    private Fragment fragment;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mycourses,parent,false);
        ViewHolder viewHolder = new ViewHolder(view); //set the created view to our viewholder
        return  viewHolder;
    }

    //here we set the content for our viewHolder , fill it in
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.name.setText(fav_courses.get(pos).getName());
        holder.desc.setText(fav_courses.get(pos).getDesc());
        if(!fav_courses.get(pos).getUrl().equals("")){
            Glide.with(context)
                    .asBitmap()
                    .load(fav_courses.get(pos).getUrl())
                    .into(holder.iv);
        }else{
            holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.default_img2));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Course_information_activity.class);
                intent.putExtra("position_fv",fav_courses.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return fav_courses.size();
    }

    //provides a direct refence to each view within a data item
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name,desc;
        public ImageView iv;
        public CardView cardView;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_course_name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            cardView = (CardView) itemView.findViewById(R.id.fav_card);
        }

    }

    public FavCourseRecViewAdapter(Context context,Fragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    public void setCourses(ArrayList<Course> courses) { this.fav_courses = courses;}
}
