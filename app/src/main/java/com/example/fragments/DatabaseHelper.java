package com.example.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class DatabaseHelper extends SQLiteOpenHelper {


    // Table names
    public static String TABLE_FAVORITES = "my_favorite";
    public static String TABLE_ALL = "all_courses";

    // DB name
    private static String DB_NAME = "test3";


    // column names for All courses table
    public static final String col1 = "name";
    public static final String col2 = "cost";
    public static final String col3 = "hardship";
    public static final String col4 = "duration";
    public static final String col5 = "desc";
    public static final String col6 = "format";
    public static final  String col7 = "url";
    public static final String col8 = "language";
    public  static final String col9 = "content";
    public static final String col10 = "features";
    public static final String col11 = "ratio";
    public static final String col12 = "results";
    public static final String col13 = "url_site";
    public static final String col14 = "practice";

    // column names for Favorite courses table
    public static final String COURSE_ID = "course_id";

    private Context context;
    private  static final String URL = "https://github.com/nwb2025/something_great/blob/master/final.xls?raw=true";
    private AsyncHttpClient client;
    private static final String TAG = "DatabaseHelper";


    public  DatabaseHelper(Context context)
    {
        super(context,DB_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query_create_table = "Create table " + TABLE_ALL + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + col1 + " TEXT, " + col2 + " INTEGER, " + col3 + " TEXT, " + col4 + " TEXT, " + col5 + " TEXT, " + col6 + " TEXT, " + col7 + " TEXT, " + col8 + " TEXT, " + col9 + " TEXT, " + col10 + " TEXT, " + col11 + " TEXT, " + col12 + " TEXT, " + col13 + " TEXT," + col14 + " TEXT)";
        db.execSQL(query_create_table);
        String query_create_table2 = "Create table " + TABLE_FAVORITES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_ID + " INTEGER)";
        db.execSQL(query_create_table2);
        fillDBIn(db);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_ALL);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }


    public boolean addQuery(String name,int cost,String hardship,String duration,String desc, String format,String url,String language,String content,String features,String ratio,String results,String url_site,String practice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col1,name);
        values.put(col2,cost);
        values.put(col3,hardship);
        values.put(col4,duration);
        values.put(col5,desc);
        values.put(col6,format);
        values.put(col7,url);
        values.put(col8,language);
        values.put(col9,content);
        values.put(col10,features);
        values.put(col11,ratio);
        values.put(col12,results);
        values.put(col13,url_site);
        values.put(col14,practice);

        long result = db.insert(TABLE_ALL,null,values);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    // it's okay
    public boolean addToFavorite(int course_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COURSE_ID,course_id);

        long result = db.insert(TABLE_FAVORITES,null,values);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<Course> getAllCourses(){
        ArrayList<Course> courses = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ALL;
        Cursor data = db.rawQuery(query,null);
        if(data.moveToFirst())
        {
            do{
                courses.add(new Course(Integer.parseInt(
                        data.getString(0)),
                        data.getString(1),
                        Integer.parseInt(data.getString(2)),
                        data.getString(3),
                        data.getString(4),
                        data.getString(5),
                        data.getString(6),
                        data.getString(7),
                        data.getString(8),
                        data.getString(9),
                        data.getString(10),
                        data.getString(11),
                        data.getString(12),
                        data.getString(13),
                        data.getString(14)));
            }while(data.moveToNext());
        }
        data.close();
        return courses;
    }

    public ArrayList<Course> getFavCourses(){
        ArrayList<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();



        String query = "SELECT * FROM " + TABLE_ALL + " WHERE "
                + " ID IN " + "( SELECT " + COURSE_ID + " FROM " + TABLE_FAVORITES + " )";

        Cursor data = db.rawQuery(query,null);
        if(data.moveToFirst())
        {
            do{
                courses.add(new Course(Integer.parseInt(
                        data.getString(0)),
                        data.getString(1),
                        Integer.parseInt(data.getString(2)),
                        data.getString(3),
                        data.getString(4),
                        data.getString(5),
                        data.getString(6),
                        data.getString(7),
                        data.getString(8),
                        data.getString(9),
                        data.getString(10),
                        data.getString(11),
                        data.getString(12),
                        data.getString(13),
                        data.getString(14)));

            }while(data.moveToNext());

        }
        data.close();

        return courses;
    }
    public boolean deleteData(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_FAVORITES + " WHERE id=" + id;
        db.execSQL(query); //this is another way how we can execute an sql query to our db

        return true;

    }

    public void deleteCourse(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int r = db.delete(TABLE_FAVORITES,COURSE_ID + " = ?",new String[]{String.valueOf(id)});

    }


    public Cursor searchFor(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_ALL + " WHERE  cost>0";
        return db.rawQuery(query,null);
    }



    public Course getAllCourse(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ALL + " WHERE id=" + id;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Course course = new Course(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14));

        return course;

    }
    public boolean checkIfExists(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COURSE_ID + " = " + id ;


        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()!=0) {
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;}

    }

    public Course getFavCourse(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Course course = null;

        String query = "SELECT * FROM " + TABLE_ALL + " WHERE id=" + id ;

        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            course = new Course(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),cursor.getString(14));
        }
        cursor.close();

        // TODO we have to check the result
        return course;

    }
    public Cursor exQuery2(LinkedHashMap<String,String> params){
        String query = "SELECT * FROM " + TABLE_ALL + " WHERE ";
        String gap =" ";

        SQLiteDatabase db = this.getReadableDatabase();
        int i=0;
        int size = params.size();
        for(Map.Entry<String,String> entry:params.entrySet()){
            i+=1;
            String key = entry.getKey();
            String value = entry.getValue();
            if(i==size){
                gap=" ";
            }
            else{
                gap = " AND ";
            }
            if(key.equals(DatabaseHelper.col1))
                query+= key + " LIKE \"%" + value + "%\"" + gap;
            else if(key.equals(DatabaseHelper.col2) && value.equals("1"))
                query+= key + ">0 "  + gap;
            else
                query+= key + "=\"" + value + "\"" + gap;



        }
        return db.rawQuery(query,null);
    }

    public void fillDBIn(SQLiteDatabase db)
    {


        ContentValues values = new ContentValues();

        client = new AsyncHttpClient(); // to donwload a file from web
        client.get(URL, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.d(TAG, "onFailure: Unable to donwload the file  " + throwable.getStackTrace());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                // first - set some settings to our workbook
                Workbook workbook = null;
                WorkbookSettings settings = new WorkbookSettings();
                settings.setGCDisabled(true);
                if(file !=null){
                    // if something goes wrong like we couldnt read the file
                    try{

                        workbook = workbook.getWorkbook(file);
                        // create a new sheet
                        Sheet sheet = workbook.getSheet(0);
                        for(int i=1;i<sheet.getRows();i++)
                        {
                            Cell[] row = sheet.getRow(i);
                            values.put(col1, row[0].getContents());
                            values.put(col2, Integer.parseInt(row[1].getContents()));
                            values.put(col3, row[2].getContents());
                            values.put(col4, row[3].getContents());
                            values.put(col5, row[4].getContents());
                            values.put(col6, row[5].getContents());
                            values.put(col7, row[6].getContents());
                            values.put(col8, row[7].getContents());
                            values.put(col9, row[8].getContents());
                            values.put(col10,row[9].getContents());
                            values.put(col11, row[10].getContents());
                            values.put(col12,row[11].getContents());
                            values.put(col13, row[12].getContents());
                            values.put(col14,row[13].getContents());

                            db.insert(TABLE_ALL,null,values);

                        }


                    }catch (IOException | BiffException e){

                    }

                }

            }
        });

    }





}
