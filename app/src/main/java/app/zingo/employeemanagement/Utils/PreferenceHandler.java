package app.zingo.employeemanagement.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */

public class PreferenceHandler {


    private SharedPreferences sh;

    private PreferenceHandler() {

    }

    private PreferenceHandler(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private static PreferenceHandler instance = null;

    /**
     *
     * @param mContext
     * @return {@link PreferenceHandler}
     */
    public synchronized static PreferenceHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new PreferenceHandler(mContext);
        }
        return instance;
    }

    public void setUserId(int id)
    {
        sh.edit().putInt(Constants.USER_ID,id).apply();
    }

    public int getUserId()
    {
        return sh.getInt(Constants.USER_ID,0);
    }

    public void setListSize(int id)
    {
        sh.edit().putInt("List",id).apply();
    }



    public void setUserName(String username)
    {
        sh.edit().putString(Constants.USER_NAME,username).apply();
    }

    public String getUserName()
    {
        return sh.getString(Constants.USER_NAME,"");
    }

    public void setPhoneNumber(String phonenumber)
    {
        sh.edit().putString(Constants.USER_PHONENUMER,phonenumber).apply();
    }

    public String getPhoneNumber()
    {
        return sh.getString(Constants.USER_PHONENUMER,"");
    }

    public void clear(){
        sh.edit().clear().apply();

    }

    public void setUserFullName(String approved)
    {
        sh.edit().putString(Constants.USER_FULL_NAME,approved).apply();
    }

    public String getUserFullName()
    {
        return sh.getString(Constants.USER_FULL_NAME,"");
    }


    public void setMappingId(int mapid)
    {
        sh.edit().putInt(Constants.MAPPING_ID,mapid).apply();
    }

    public int getMappingId()
    {
        return sh.getInt(Constants.MAPPING_ID,0);
    }

    public void setLoginTime(String loginTime)
    {
        sh.edit().putString(Constants.LOGIN_TIME,loginTime).apply();
    }

    public String getLoginTime()
    {
        return sh.getString(Constants.LOGIN_TIME,"");
    }

    public void setLogOutTime(String logOutTime)
    {
        sh.edit().putString(Constants.LOGOUT_TIME,logOutTime).apply();
    }

    public String getLogOutTime()
    {
        return sh.getString(Constants.LOGOUT_TIME,"");
    }

    public void setLoginStatus(String loginStatus)
    {
        sh.edit().putString(Constants.LOGIN_STATUS,loginStatus).apply();
    }

    public String getLoginStatus()
    {
        return sh.getString(Constants.LOGIN_STATUS,"Logout");
    }

    public void setMeetingLoginStatus(String loginStatus)
    {
        sh.edit().putString(Constants.MEET_LOGIN_STATUS,loginStatus).apply();
    }

    public String getMeetingLoginStatus()
    {
        return sh.getString(Constants.MEET_LOGIN_STATUS,"Logout");
    }

    public void setMeetingLoginTime(String loginTime)
    {
        sh.edit().putString(Constants.MEET_LOGIN_TIME,loginTime).apply();
    }

    public String getMeetingLoginTime()
    {
        return sh.getString(Constants.MEET_LOGIN_TIME,"");
    }

    public void setMeetingLogOutTime(String logOutTime)
    {
        sh.edit().putString(Constants.MEET_LOGOUT_TIME,logOutTime).apply();
    }

    public String getMeetingLogOutTime()
    {
        return sh.getString(Constants.MEET_LOGOUT_TIME,"");
    }

}
