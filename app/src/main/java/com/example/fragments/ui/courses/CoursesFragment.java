package com.example.fragments.ui.courses;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragments.Course;
import com.example.fragments.DatabaseHelper;
import com.example.fragments.FavCourseRecViewAdapter;
import com.example.fragments.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CoursesFragment extends Fragment {

    private TextView textView;
    private Button btn_add;
    private ArrayList<Course> courses=null;
    private RecyclerView recyclerView;
    private ArrayList<Course> fav_courses = new ArrayList<>();
    private Course deleted_course = null;
    private static final String TAG = "CoursesFragment";
    private DatabaseHelper db;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = null;
        db  = new DatabaseHelper(getContext());
        fav_courses = db.getFavCourses();
        db.close();

        if (fav_courses.size() == 0){
            root = inflater.inflate(R.layout.fragment_courses, container, false);
            textView = root.findViewById(R.id.text_courses);
            btn_add = root.findViewById(R.id.button_add);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*ExploreFragment fragment = new ExploreFragment(); // to where we're going
                    FragmentManager fragmentManager = getFragmentManager();
                    /// first arg - is our container where we will locate our new fragment;
                    fragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment,fragment,fragment.getTag())
                            .commit();*/
                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_explore);
                }
            });
        }
        else
        {
            root = inflater.inflate(R.layout.fragment_course_2,container,false);
            recyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
            FavCourseRecViewAdapter adapter = new FavCourseRecViewAdapter(root.getContext(),this);
            adapter.setCourses(fav_courses);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            // simple callback
            // itemtouchelper.callback is the class that allows us to drag & drop and move our rec view
            ItemTouchHelper.SimpleCallback touchhelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                private static final String TAG = "CoursesFragment";

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
                {
                    DatabaseHelper d = new DatabaseHelper(getContext());

                    int position = viewHolder.getAdapterPosition();
                    Course deleted_course = fav_courses.get(position);
                    switch (direction)
                    {
                        case ItemTouchHelper.LEFT:

                            d.deleteCourse(fav_courses.get(position).getId());
                            fav_courses.remove(position);
                            adapter.setCourses(fav_courses);
                            adapter.notifyDataSetChanged();
                            d.close();

                            Snackbar.make(recyclerView,"Курс был успешно удален",Snackbar.LENGTH_LONG)
                                    .setAction("Отменить", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DatabaseHelper d = new DatabaseHelper(getContext());
                                            d.addToFavorite(deleted_course.getId());
                                            fav_courses.clear();
                                            fav_courses = new ArrayList<>(d.getFavCourses());
                                            adapter.setCourses(fav_courses);
                                            adapter.notifyDataSetChanged();
                                            d.close();

                                        }
                                    })
                                    .setBackgroundTintList(getContext().getColorStateList(R.color.purple_500)).show();
                            break;


                    }


                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                        float dY, int actionState, boolean isCurrentlyActive)
                {
                    // set a new background and icon for swiapeable rec view
                    new RecyclerViewSwipeDecorator.Builder(getContext(),c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                            .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.red))
                            .addSwipeLeftActionIcon(R.drawable.delete)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

            //create  newly created callback and bound it to our recview
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchhelper);
            itemTouchHelper.attachToRecyclerView(recyclerView);


        }


        return root;
    }

}