package app.zingo.employeemanagement.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.zingo.employeemanagement.Adapter.NavigationListAdapter;
import app.zingo.employeemanagement.Google.TrackGPS;
import app.zingo.employeemanagement.Model.LoginDetails;
import app.zingo.employeemanagement.Model.MeetingDetails;
import app.zingo.employeemanagement.Model.NotificationManager;
import app.zingo.employeemanagement.Model.Options;
import app.zingo.employeemanagement.Model.ProfileNotification;
import app.zingo.employeemanagement.R;
import app.zingo.employeemanagement.Utils.Constants;
import app.zingo.employeemanagement.Utils.DataBaseHelper;
import app.zingo.employeemanagement.Utils.PreferenceHandler;
import app.zingo.employeemanagement.Utils.ThreadExecuter;
import app.zingo.employeemanagement.Utils.Util;
import app.zingo.employeemanagement.WepAPI.NotificationAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LandingScreenActivity extends AppCompatActivity {

    //Ui declaration
    private static ImageView mDrawerIcon;
    private static DrawerLayout drawerLayout;
    private static TextView mProfileName,mGreeting,mLoggedTime,mMasterText,mMeetingText;
    private static CardView mMasterLogin,mMeetingLogin;
    ListView mNavBarList;
    LinearLayout mNavBarLayout;

    ArrayList<Options> optionsList;
    String option;

    //Location
    TrackGPS gps;
    double latitude,longitude;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1,MY_PERMISSIONS_REQUEST_RESULT = 1;


    //Database
    DataBaseHelper db = new DataBaseHelper(LandingScreenActivity.this);
    ArrayList<LoginDetails> loginDetails;
    ArrayList<MeetingDetails> meetingDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_landing_screen);

            checkPermissions();
            gps = new TrackGPS(LandingScreenActivity.this);

            //Ui initialize
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_menu_option);
            mDrawerIcon = (ImageView)findViewById(R.id.drawer_icon_menu_option);
            mProfileName = (TextView) findViewById(R.id.employee_name);
            mLoggedTime = (TextView) findViewById(R.id.log_out_time);
            mGreeting = (TextView) findViewById(R.id.employee_greeting);
            mMeetingText = (TextView) findViewById(R.id.meeting_login_text);
            mMasterText = (TextView) findViewById(R.id.master_login_text);
            mMasterLogin = (CardView) findViewById(R.id.master_login);
            mMeetingLogin = (CardView) findViewById(R.id.meeting_login);
            mNavBarLayout = (LinearLayout) findViewById(R.id.navbar_layout);
            mNavBarList = (ListView) findViewById(R.id.navbar_list);

            //Navigation menu item
            optionsList = new ArrayList<>();
            TypedArray optionsIcon = LandingScreenActivity.this.getResources().obtainTypedArray(R.array.options_icon);
            String[] optionsName = getResources().getStringArray(R.array.options_name);

            if(optionsIcon.length()==optionsName.length){

                for (int i=0;i<optionsIcon.length();i++)
                {
                    Options options = new Options();
                    options.setOptionIcon(optionsIcon.getResourceId(i,-1));
                    options.setOptionName(optionsName[i]);
                    optionsList.add(options);
                }


            }

            String masterloginStatus = PreferenceHandler.getInstance(LandingScreenActivity.this).getLoginStatus();
            String loginStatus = PreferenceHandler.getInstance(LandingScreenActivity.this).getMeetingLoginStatus();

            if(masterloginStatus!=null&&!masterloginStatus.isEmpty()){

                if(masterloginStatus.equalsIgnoreCase("Login")){

                    mMasterText.setText("Logout");
                }else if(masterloginStatus.equalsIgnoreCase("Logout")){

                    mMasterText.setText("Login");
                }

            }else{
                mMasterText.setText("Login");
            }

            if(loginStatus!=null&&!loginStatus.isEmpty()){

                if(loginStatus.equalsIgnoreCase("Login")){

                    mMeetingText.setText("Logout");
                }else if(loginStatus.equalsIgnoreCase("Logout")){

                    mMeetingText.setText("Login");
                }

            }else{
                mMeetingText.setText("Login");
            }

            if(db.getLoginDetails()!=null&&db.getLoginDetails().size()!=0){

                loginDetails = db.getLoginDetails();

                for(int i=0;i<loginDetails.size();i++){

                    System.out.println("Database "+loginDetails.get(i).getId());
                    System.out.println("Login time = "+loginDetails.get(i).getLogin());
                    System.out.println("Logout time = "+loginDetails.get(i).getLogout());
                    System.out.println("User id = "+loginDetails.get(i).getUserId());
                    System.out.println("Date = "+loginDetails.get(i).getDate());
                    System.out.println("Duration = "+loginDetails.get(i).getDuration());
                    System.out.println("Duration = "+loginDetails.get(i).getLoglng());
                    System.out.println("Duration = "+loginDetails.get(i).getOutlnglt());
                }

            }

            NavigationListAdapter adapter = new NavigationListAdapter(getApplicationContext(),optionsList);
            mNavBarList.setAdapter(adapter);

            mNavBarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    displayMenu(optionsList.get(i));
                }
            });

            mDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    drawerLayout.openDrawer(Gravity.LEFT);

                }
            });

            setupFields();

            mMasterLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String loginStatus = PreferenceHandler.getInstance(LandingScreenActivity.this).getLoginStatus();

                    if(loginStatus!=null&&!loginStatus.isEmpty()){

                        if(loginStatus.equalsIgnoreCase("Login")){
                            masterloginalert("Login");

                        }else if(loginStatus.equalsIgnoreCase("Logout")){

                            masterloginalert("Logout");
                        }

                    }else{
                        masterloginalert("Logout");
                    }




                }
            });

            mMeetingLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String loginStatus = PreferenceHandler.getInstance(LandingScreenActivity.this).getMeetingLoginStatus();
                    String masterloginStatus = PreferenceHandler.getInstance(LandingScreenActivity.this).getLoginStatus();


                    if(masterloginStatus.equals("Login")){
                        if(loginStatus!=null&&!loginStatus.isEmpty()){

                            if(loginStatus.equalsIgnoreCase("Login")){
                                meetingloginalert("Login");

                            }else if(loginStatus.equalsIgnoreCase("Logout")){

                                meetingloginalert("Logout");
                            }

                        }else{
                            meetingloginalert("Logout");
                        }
                    }else{
                        Toast.makeText(LandingScreenActivity.this, "First Check-in Master", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setupFields(){

        String name = PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName();

        if(name!=null&&!name.isEmpty()){

            mProfileName.setText("Hello "+name+",");

        }else{
            mProfileName.setText("Hi User,");
        }

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if(hours>=1 && hours<=12){
            mGreeting.setText("Good Morning,");
        }else if(hours>=12 && hours<=16){
            mGreeting.setText("Good Afternoon,");
        }else if(hours>=16 && hours<=20){
            mGreeting.setText("Good Evening,");
        }else if(hours>=20 && hours<=24){
            mGreeting.setText("Good Night,");

        }

        String loginStatus = PreferenceHandler.getInstance(LandingScreenActivity.this).getLoginStatus();
        String logoutTime = PreferenceHandler.getInstance(LandingScreenActivity.this).getLogOutTime();
        String loginTime = PreferenceHandler.getInstance(LandingScreenActivity.this).getLoginTime();

        if(loginStatus.equalsIgnoreCase("Login")){

            if(loginTime!=null&&!loginTime.isEmpty()&&!loginTime.equals("")){
                mLoggedTime.setText("Last Check-in Time :"+loginTime);
            }


        }else{

            if(logoutTime!=null&&!logoutTime.isEmpty()&&!logoutTime.equals("")){
                mLoggedTime.setText("Last Check-out Time :"+logoutTime);
            }

        }
    }

    public void displayMenu(Options options){

        String name = options.getOptionName();

        switch (name){

            case "Profile":
                Intent profile = new Intent(LandingScreenActivity.this,ProfileActivity.class);
                startActivity(profile);
                break;

            case "Logout":
                if(PreferenceHandler.getInstance(LandingScreenActivity.this).getLoginStatus().equalsIgnoreCase("Login")){

                    Toast.makeText(LandingScreenActivity.this, "Logout from Master", Toast.LENGTH_SHORT).show();

                }else{
                    Intent logout = new Intent(LandingScreenActivity.this,LoginScreenActivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PreferenceHandler.getInstance(LandingScreenActivity.this).clear();
                    startActivity(logout);
                }

                break;


        }

    }

    public void masterloginalert(final String status){

        try {

            String message = "Login";
            option = "Check-In";

            if(status.equalsIgnoreCase("Login")){

                message = "Do you want to Log-Out?";
                option = "Log-Out";

            }else if(status.equalsIgnoreCase("Logout")){

                message = "Do you want to Log-In?";
                option = "Log-In";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(LandingScreenActivity.this);
            builder.setTitle(message);

            builder.setPositiveButton(option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {



                    if(locationCheck()){
                        if(gps.canGetLocation())
                        {
                            System.out.println("Long and lat Rev"+gps.getLatitude()+" = "+gps.getLongitude());
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                            String date = sdf.format(new Date());

                            ProfileNotification notifyprofile = new ProfileNotification();
                            notifyprofile.setProfileId(153);
                            notifyprofile.setMessage(option+"="+date+"="+gps.getLatitude()+"-"+gps.getLongitude()+"="+PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName());
                            notifyprofile.setTitle("Master Login From "+PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName());
                            notifyprofile.setSenderId(Constants.SENDERID);
                            notifyprofile.setServerId(Constants.SERVERID);
                            sendNotificationByProfileId(notifyprofile,status,"Master");

                        }
                        else
                        {

                        }
                    }





                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void meetingloginalert(final String status){

        try{

            if(locationCheck()){
                String message = "Login";
                option = "Check-In";

                if(status.equalsIgnoreCase("Login")){

                    message = "Do you want to Check-Out?";
                    option = "Check-Out";

                }else if(status.equalsIgnoreCase("Logout")){

                    message = "Do you want to Check-In?";
                    option = "Check-In";
                }

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LandingScreenActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View views = inflater.inflate(R.layout.custom_alert_box_meeting, null);

                builder.setView(views);
                final Button mSave = (Button) views.findViewById(R.id.save);
                mSave.setText(option);
                final EditText mRemarks = (EditText) views.findViewById(R.id.meeting_remarks);
                final TextInputEditText mClient = (TextInputEditText) views.findViewById(R.id.client_name);
                final TextInputEditText mPurpose = (TextInputEditText) views.findViewById(R.id.purpose_meeting);


                final android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                mSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String client = mClient.getText().toString();
                        String purpose = mPurpose.getText().toString();
                        String remark = mRemarks.getText().toString();

                        if(client==null||client.isEmpty()){

                            Toast.makeText(LandingScreenActivity.this, "Please mention client/hotel name", Toast.LENGTH_SHORT).show();

                        }else if(purpose==null||purpose.isEmpty()){

                            Toast.makeText(LandingScreenActivity.this, "Please mention purpose of meeting", Toast.LENGTH_SHORT).show();

                        }else if(remark==null||remark.isEmpty()){

                            Toast.makeText(LandingScreenActivity.this, "Please mention remarks about meeting", Toast.LENGTH_SHORT).show();

                        }else{

                            if(gps.canGetLocation())
                            {
                                System.out.println("Long and lat Rev"+gps.getLatitude()+" = "+gps.getLongitude());
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();

                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                                String date = sdf.format(new Date());

                                String meeting = client+"="+purpose+"="+remark;

                                ProfileNotification notifyprofile = new ProfileNotification();
                                notifyprofile.setProfileId(153);
                                notifyprofile.setMessage(option+"="+date+"="+gps.getLatitude()+"-"+gps.getLongitude()+"="+PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName()+"="+meeting);
                                notifyprofile.setTitle("Meeting Login From "+PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName());
                                notifyprofile.setSenderId(Constants.SENDERID);
                                notifyprofile.setServerId(Constants.SERVERID);
                                sendNotificationByProfileId(notifyprofile,status,"Meeting");
                                dialog.dismiss();


                            }
                            else
                            {

                            }

                        }
                    }
                });

            }else{

            }




        }catch (Exception e){
            e.printStackTrace();
        }

        /*try {

            String message = "Login";
            option = "Check-In";

            if(status.equalsIgnoreCase("Login")){

                message = "Do you want to Check-Out?";
                option = "Check-Out";

            }else if(status.equalsIgnoreCase("Logout")){

                message = "Do you want to Check-In?";
                option = "Check-In";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(LandingScreenActivity.this);
            builder.setTitle(message);

            builder.setPositiveButton(option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {



                    if(locationCheck()){
                        if(gps.canGetLocation())
                        {
                            System.out.println("Long and lat Rev"+gps.getLatitude()+" = "+gps.getLongitude());
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                            String date = sdf.format(new Date());

                            ProfileNotification notifyprofile = new ProfileNotification();
                            notifyprofile.setProfileId(153);
                            notifyprofile.setMessage(option+"="+date+"="+gps.getLatitude()+"-"+gps.getLongitude()+"="+PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName());
                            notifyprofile.setTitle("Meeting Login From "+PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName());
                            notifyprofile.setSenderId(Constants.SENDERID);
                            notifyprofile.setServerId(Constants.SERVERID);
                            sendNotificationByProfileId(notifyprofile,status,"Meeting");

                        }
                        else
                        {

                        }
                    }





                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }*/
    }

    public boolean locationCheck(){

        final boolean status = false;
        LocationManager lm = (LocationManager)LandingScreenActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(LandingScreenActivity.this);
            dialog.setMessage("Gps Not enable");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    LandingScreenActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub


                }
            });
            dialog.show();
            return false;
        }else{
            return true;
        }
    }


    public boolean checkPermissions() {
        if ((ContextCompat.checkSelfPermission(LandingScreenActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(LandingScreenActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(LandingScreenActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(LandingScreenActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(LandingScreenActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(LandingScreenActivity.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(LandingScreenActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(LandingScreenActivity.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(LandingScreenActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(LandingScreenActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length > 0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                gps = new TrackGPS(LandingScreenActivity.this);
            }
        }
    }

    private void sendNotificationByProfileId(final ProfileNotification notification,final String status,final String type) {

        /*final ProgressDialog dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(LandingScreenActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                NotificationAPI travellerApi = Util.getClient().create(NotificationAPI.class);
                Call<ArrayList<String>> response = travellerApi.sendnotificationToProfile(auth_string,notification);

                response.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {


                        System.out.println(response.code());
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {


                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(notification.getTitle());
                                nf.setNotificationFor(notification.getMessage());
                                nf.setHotelId(notification.getHotelId());
                                savenotification(nf,status,type);
                                if(status.equalsIgnoreCase("Login")&&type.equalsIgnoreCase("Master")){
                                    mMasterText.setText("Log-in");
                                }else if(status.equalsIgnoreCase("Logout")&&type.equalsIgnoreCase("Master")){
                                    mMasterText.setText("Log-out");
                                }else if(status.equalsIgnoreCase("Login")&&type.equalsIgnoreCase("Meeting")){
                                    mMeetingText.setText("Meeting Check-in");
                                }else if(status.equalsIgnoreCase("Logout")&&type.equalsIgnoreCase("Meeting")){
                                    mMeetingText.setText("Meeting Check-out");
                                }


                                Log.d("Notification","Sent to bill");


                            }
                        }
                        else
                        {
                            Log.d("Notification","not Sent to bill");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(LandingScreenActivity.this, "Notification sent failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void savenotification(final NotificationManager notification,final String status,final String type) {

        final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(LandingScreenActivity.this);
                NotificationAPI saveNotify = Util.getClient().create(NotificationAPI.class);
                Call<NotificationManager> response = saveNotify.saveNotification(auth_string,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        if(response.code() == 200||response.code() == 201)
                        {
                            if(response.body() != null)
                            {
                                SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd,yyyy");
                                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
                                int userId = PreferenceHandler.getInstance(LandingScreenActivity.this).getUserId();

                                if(type.equals("Master")){
                                    if(status.equalsIgnoreCase("Login")){
                                        if(db.getLoginDetails()!=null&&db.getLoginDetails().size()!=0) {

                                            loginDetails = db.getLoginDetails();
                                        }

                                        if(loginDetails!=null&&loginDetails.size()!=0){
                                           // LoginDetails logs  = db.getLoginDetail(userId,sdfDate.format(new Date()));
                                            LoginDetails logs  = new LoginDetails();
                                            for(int i=0;i<loginDetails.size();i++){

                                                if(loginDetails.get(i).getUserId() == userId && loginDetails.get(i).getDate().equalsIgnoreCase(""+sdfDate.format(new Date()))&&loginDetails.get(i).getId()==(loginDetails.size())){

                                                    logs = loginDetails.get(i);
                                                    break;
                                                }
                                            }

                                            if(logs !=null){
                                                String duration = dateCal(sdfDate.format(new Date()),logs.getLogin(),sdfTime.format(new Date()));

                                                System.out.println("log id"+duration);

                                                if(duration!=null){
                                                    System.out.println("User id "+userId);
                                                    System.out.println("date "+logs.getDate());
                                                    System.out.println("login "+logs.getLogin());
                                                    String logout = sdfTime.format(new Date());
                                                    String durations = duration;
                                                    System.out.println("logout"+sdfTime.format(new Date())+"-t");
                                                    System.out.println("duration "+duration+"-");
                                                    db.updateLog(userId,sdfDate.format(new Date()),logs.getLogin(),logout,durations,logs.getId(),logs.getLoglng(),gps.getLongitude()+","+gps.getLatitude());
                                                }else{
                                                    db.updateLog(userId,sdfDate.format(new Date()),logs.getLogin(),""+sdfTime.format(new Date()),"",logs.getId(),logs.getLoglng(),gps.getLongitude()+","+gps.getLatitude());
                                                }

                                            }

                                        }


                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setLoginStatus("Logout");

                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                                        String date = sdf.format(new Date());
                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setLogOutTime(""+date);
                                        mLoggedTime.setText("Last Check-out Time : "+date);


                                    }else if(status.equalsIgnoreCase("Logout")){



                                        db.insertLog(userId,sdfDate.format(new Date()),sdfTime.format(new Date()),"","",gps.getLongitude()+","+gps.getLatitude(),"");


                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setLoginStatus("Login");
                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                                        String date = sdf.format(new Date());
                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setLoginTime(""+date);
                                        mLoggedTime.setText("Last Check-in Time : "+date);

                                    }
                                }else{
                                    if(status.equalsIgnoreCase("Login")){

                                        if(db.getLoginDetailsMeet()!=null&&db.getLoginDetailsMeet().size()!=0) {

                                            meetingDetails = db.getLoginDetailsMeet();
                                        }

                                        if(meetingDetails!=null&&meetingDetails.size()!=0){
                                            // LoginDetails logs  = db.getLoginDetail(userId,sdfDate.format(new Date()));
                                            MeetingDetails logs  = new MeetingDetails();
                                            for(int i=0;i<meetingDetails.size();i++){

                                                if(meetingDetails.get(i).getUserId() == userId && meetingDetails.get(i).getDate().equalsIgnoreCase(""+sdfDate.format(new Date()))&&meetingDetails.get(i).getId()==(meetingDetails.size())){

                                                    logs = meetingDetails.get(i);
                                                    break;
                                                }
                                            }

                                            if(logs !=null){
                                                String duration = dateCal(sdfDate.format(new Date()),logs.getLogin(),sdfTime.format(new Date()));

                                                System.out.println("log id"+duration);

                                                if(duration!=null){
                                                    System.out.println("User id "+userId);
                                                    System.out.println("date "+logs.getDate());
                                                    System.out.println("login "+logs.getLogin());
                                                    String logout = sdfTime.format(new Date());
                                                    String durations = duration;
                                                    System.out.println("logout"+sdfTime.format(new Date())+"-t");
                                                    System.out.println("duration "+duration+"-");
                                                    db.updateLog(userId,sdfDate.format(new Date()),logs.getLogin(),logout,durations,logs.getId(),logs.getLoglng(),gps.getLongitude()+","+gps.getLatitude());
                                                }else{
                                                    db.updateLog(userId,sdfDate.format(new Date()),logs.getLogin(),""+sdfTime.format(new Date()),"",logs.getId(),logs.getLoglng(),gps.getLongitude()+","+gps.getLatitude());
                                                }

                                            }

                                        }

                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setMeetingLoginStatus("Logout");
                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                                        String date = sdf.format(new Date());
                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setMeetingLogOutTime(""+date);


                                    }else if(status.equalsIgnoreCase("Logout")){

                                        db.insertLogMeet(userId,sdfDate.format(new Date()),sdfTime.format(new Date()),"","",gps.getLongitude()+","+gps.getLatitude(),"");

                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setMeetingLoginStatus("Login");
                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                                        String date = sdf.format(new Date());
                                        PreferenceHandler.getInstance(LandingScreenActivity.this).setMeetingLoginTime(""+date);


                                    }
                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }


    public String dateCal(String date,String login,String logout){

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        DecimalFormat df = new DecimalFormat("00");

        Date fd=null,td=null;

        try {
            fd = sdf.parse(date+" "+login);
            td = sdf.parse(date+" "+logout);

            long diff = td.getTime() - fd.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long Hours = diff / (60 * 60 * 1000) % 24;
            long Minutes = diff / (60 * 1000) % 60;
            long Seconds = diff / 1000 % 60;

            return ""+df.format(Hours)+":"+df.format(Minutes);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }



    }


}
