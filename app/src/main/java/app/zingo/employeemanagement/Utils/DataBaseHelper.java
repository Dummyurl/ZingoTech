package app.zingo.employeemanagement.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import app.zingo.employeemanagement.Model.LoginDetails;
import app.zingo.employeemanagement.Model.MeetingDetails;

/**
 * Created by ZingoHotels Tech on 13-08-2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Employee.db";
    private static final int DATABASE_VERSION = 2;


    public static final String LOG_TABLE_NAME = "login";
    public static final String LOG_COLUMN_ID = "_id";
    public static final String LOG_USER_ID = "userid";
    public static final String LOG_DATE = "date";
    public static final String LOG_DURATION = "duration";
    public static final String LOG_IN_TIME = "login";
    public static final String LOG_LATLNG = "logltng";
    public static final String LOG_OUT_LATLNG  = "logoutltng";
    public static final String LOG_OUT_TIME = "logout";

    public static final String MEET_TABLE_NAME = "meetlogin";
    public static final String MEET_COLUMN_ID = "_id";
    public static final String MEET_USER_ID = "userid";
    public static final String MEET_LOG_DATE = "date";
    public static final String MEET_LOG_DURATION = "duration";
    public static final String MEET_LOG_IN_TIME = "login";
    public static final String MEET_LOG_LATLNG = "logltng";
    public static final String MEET_LOG_OUT_LATLNG  = "logoutltng";
    public static final String MEET_LOG_OUT_TIME = "logout";

    public Context context;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "CREATE TABLE " + LOG_TABLE_NAME +
                        "(" + LOG_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        LOG_USER_ID + " INTEGER, " +
                        LOG_DATE + " TEXT, " +
                        LOG_IN_TIME + " TEXT, " +
                        LOG_DURATION + " TEXT, " +
                        LOG_LATLNG + " TEXT, " +
                        LOG_OUT_LATLNG + " TEXT, " +
                        LOG_OUT_TIME + " TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + MEET_TABLE_NAME +
                        "(" + MEET_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        MEET_USER_ID + " INTEGER, " +
                        MEET_LOG_DATE + " TEXT, " +
                        MEET_LOG_IN_TIME + " TEXT, " +
                        MEET_LOG_DURATION + " TEXT, " +
                        MEET_LOG_LATLNG + " TEXT, " +
                        MEET_LOG_OUT_LATLNG + " TEXT, " +
                        MEET_LOG_OUT_TIME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOG_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertLog(int userId,String date,String login,String logout,String duration,String inlatlng,String outlatlng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LOG_USER_ID, userId);
        contentValues.put(LOG_DATE, date);
        contentValues.put(LOG_IN_TIME, login);
        contentValues.put(LOG_OUT_TIME, logout);
        contentValues.put(LOG_DURATION, duration);
        contentValues.put(LOG_LATLNG, inlatlng);
        contentValues.put(LOG_OUT_LATLNG, outlatlng);


        db.insert(LOG_TABLE_NAME, null, contentValues);
        return true;
    }

    public void updateLog(int userId,String date,String login,String logout,String duration,int id,String inlatlng,String outlatlng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LOG_USER_ID, userId);
        contentValues.put(LOG_DATE, date);
        contentValues.put(LOG_IN_TIME, login);
        contentValues.put(LOG_OUT_TIME, logout);
        contentValues.put(LOG_DURATION, duration);
        contentValues.put(LOG_LATLNG, inlatlng);
        contentValues.put(LOG_OUT_LATLNG, outlatlng);

        db.update(LOG_TABLE_NAME,contentValues,LOG_COLUMN_ID+"="+id,null );

        /*db.update(LOG_TABLE_NAME,
                contentValues,
                LOG_USER_ID+ " = ? AND " + LOG_DATE + " = ?"+ " = ? AND " + LOG_COLUMN_ID + " = ?",
                new String[]{""+userId, date,""+id});*/

        //db.update(LOG_TABLE_NAME,contentValues,LOG_USER_ID+"="+userId AND ,null );

    }

    public ArrayList<LoginDetails> getLoginDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + LOG_TABLE_NAME, null );
        ArrayList<LoginDetails> array = new ArrayList<>();

        while(res.moveToNext()){
            String date = res.getString(res.getColumnIndex("date"));
            String login = res.getString(res.getColumnIndex("login"));
            String logout = res.getString(res.getColumnIndex("logout"));
            String duration = res.getString(res.getColumnIndex("duration"));
            String inlnglat = res.getString(res.getColumnIndex("logltng"));
            String outlnglat = res.getString(res.getColumnIndex("logoutltng"));
            int  id = res.getInt(res.getColumnIndex("_id"));
            int  userId = res.getInt(res.getColumnIndex("userid"));


           LoginDetails loginDetails = new LoginDetails();
           loginDetails.setId(id);
           loginDetails.setDate(date);
           loginDetails.setLogin(login);
           loginDetails.setLogout(logout);
           loginDetails.setUserId(userId);
           loginDetails.setDuration(duration);
           loginDetails.setLoglng(inlnglat);
           loginDetails.setOutlnglt(outlnglat);


           array.add(loginDetails);



        }
        return array;
    }

    public boolean insertLogMeet(int userId,String date,String login,String logout,String duration,String inlatlng,String outlatlng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MEET_USER_ID, userId);
        contentValues.put(MEET_LOG_DATE, date);
        contentValues.put(MEET_LOG_IN_TIME, login);
        contentValues.put(MEET_LOG_OUT_TIME, logout);
        contentValues.put(MEET_LOG_DURATION, duration);
        contentValues.put(MEET_LOG_LATLNG, inlatlng);
        contentValues.put(MEET_LOG_OUT_LATLNG, outlatlng);


        db.insert(MEET_TABLE_NAME, null, contentValues);
        return true;
    }

    public void updateLogMeet(int userId,String date,String login,String logout,String duration,int id,String inlatlng,String outlatlng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MEET_USER_ID, userId);
        contentValues.put(MEET_LOG_DATE, date);
        contentValues.put(MEET_LOG_IN_TIME, login);
        contentValues.put(MEET_LOG_OUT_TIME, logout);
        contentValues.put(MEET_LOG_DURATION, duration);
        contentValues.put(MEET_LOG_LATLNG, inlatlng);
        contentValues.put(MEET_LOG_OUT_LATLNG, outlatlng);


        db.update(MEET_TABLE_NAME,contentValues,MEET_COLUMN_ID+"="+id,null );


    }

    public ArrayList<MeetingDetails> getLoginDetailsMeet() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + MEET_TABLE_NAME, null );
        ArrayList<MeetingDetails> array = new ArrayList<>();

        while(res.moveToNext()){
            String date = res.getString(res.getColumnIndex("date"));
            String login = res.getString(res.getColumnIndex("login"));
            String logout = res.getString(res.getColumnIndex("logout"));
            String duration = res.getString(res.getColumnIndex("duration"));
            String inlnglat = res.getString(res.getColumnIndex("logltng"));
            String outlnglat = res.getString(res.getColumnIndex("logoutltng"));

            int  id = res.getInt(res.getColumnIndex("_id"));
            int  userId = res.getInt(res.getColumnIndex("userid"));


            MeetingDetails loginDetails = new MeetingDetails();
            loginDetails.setId(id);
            loginDetails.setDate(date);
            loginDetails.setLogin(login);
            loginDetails.setLogout(logout);
            loginDetails.setUserId(userId);
            loginDetails.setDuration(duration);
            loginDetails.setLoglng(inlnglat);
            loginDetails.setOutlnglt(outlnglat);


            array.add(loginDetails);



        }
        return array;
    }

    public LoginDetails getLoginDetail(int userId,String date) {
        SQLiteDatabase db = this.getReadableDatabase();

       // Cursor res = db.rawQuery("select 1 from " + LOG_TABLE_NAME+ " where " + LOG_USER_ID + " = ? " + " AND " + LOG_DATE + "= ? ", new String[] {userId+"", date});
        Cursor res =  db.rawQuery( "SELECT 1 FROM " + LOG_TABLE_NAME+" WHERE "+LOG_USER_ID+ " = ?"+userId+" AND " + LOG_DATE + " = "+date, null );
        ArrayList<LoginDetails> array = new ArrayList<>();


            String dates = res.getString(res.getColumnIndex("date"));
            String login = res.getString(res.getColumnIndex("login"));
            String logout = res.getString(res.getColumnIndex("logout"));
            String duration = res.getString(res.getColumnIndex("duration"));
            int  id = res.getInt(res.getColumnIndex("_id"));
            int  userIds = res.getInt(res.getColumnIndex("userid"));


            LoginDetails loginDetails = new LoginDetails();
            loginDetails.setId(id);
            loginDetails.setDate(dates);
            loginDetails.setLogin(login);
            loginDetails.setLogout(logout);
            loginDetails.setUserId(userIds);
            loginDetails.setDuration(duration);


        return loginDetails;
    }



}
