package app.zingo.employeemanagement.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.employeemanagement.FirebaseServices.SharedPrefManager;
import app.zingo.employeemanagement.Model.ProfileDeviceMappings;
import app.zingo.employeemanagement.Model.Profiles;
import app.zingo.employeemanagement.R;
import app.zingo.employeemanagement.Utils.Constants;
import app.zingo.employeemanagement.Utils.CustomToast;
import app.zingo.employeemanagement.Utils.DataBaseHelper;
import app.zingo.employeemanagement.Utils.PreferenceHandler;
import app.zingo.employeemanagement.Utils.ThreadExecuter;
import app.zingo.employeemanagement.Utils.Util;
import app.zingo.employeemanagement.WepAPI.ProfileAPI;
import app.zingo.employeemanagement.WepAPI.ProfileDeviceMappingAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenActivity extends AppCompatActivity {

    //UI
    private static View view;
    private static EditText mUserName,mUserPassword;
    private static CheckBox mShowHidePassword;
    private static TextView mForgotPassword;
    private static Button mLogin;
    private static FrameLayout loginLayout;
    private static Animation shakeAnimation;

    DataBaseHelper db = new DataBaseHelper(LoginScreenActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_login_screen);

            //UI initialize
            view = (View) findViewById(R.id.login_layout);
            mUserName = (EditText)findViewById(R.id.user_name);
            mUserPassword = (EditText)findViewById(R.id.user_password);
            mShowHidePassword = (CheckBox) findViewById(R.id.show_hide_password);
            mForgotPassword = (TextView) findViewById(R.id.forgot_password);
            mLogin = (Button) findViewById(R.id.log_in_btn);
            loginLayout = (FrameLayout) findViewById(R.id.login_layout);

            mForgotPassword.setVisibility(View.GONE);

            // Load ShakeAnimation
            shakeAnimation = AnimationUtils.loadAnimation(LoginScreenActivity.this,
                    R.anim.shake);


            mShowHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton button,
                                             boolean isChecked) {

                    // If it is checked then show password else hide password
                    if (isChecked) {

                        mShowHidePassword.setText(R.string.hide_pwd);// change checkbox text

                        mUserPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        mUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
                    } else {
                        mShowHidePassword.setText(R.string.show_pwd);// change checkbox text

                        mUserPassword.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password

                    }

                }
            });

            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validateLogin();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void validateLogin(){

        String getUserName = mUserName.getText().toString();
        String getPassword = mUserPassword.getText().toString();

        if(getUserName==null||getUserName.isEmpty()||getUserName.equals("")){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Apply activity transition
                loginLayout.startAnimation(shakeAnimation);
            } else {
                // Swap without transition
            }

            new CustomToast().Show_Toast(LoginScreenActivity.this,view, "Enter User name.");
        }else if(getPassword==null||getPassword.isEmpty()||getPassword.equals("")){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Apply activity transition
                loginLayout.startAnimation(shakeAnimation);
            } else {
                // Swap without transition
            }
            new CustomToast().Show_Toast(LoginScreenActivity.this,view, "Enter Password.");
        }else{

            try{

                if(Util.isNetworkAvailable(LoginScreenActivity.this)){

                    login(getUserName,getPassword);

                }else{

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Apply activity transition
                        loginLayout.startAnimation(shakeAnimation);
                    } else {
                        // Swap without transition
                    }
                    new CustomToast().Show_Toast(LoginScreenActivity.this,view, "No Internet");

                }



            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    private void login( final String username, final String password) throws Exception{

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Profiles profiles = new Profiles();
        profiles.setUserName(username);
        profiles.setPassword(password);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                ProfileAPI apiService = Util.getClient().create(ProfileAPI.class);




                Call<ArrayList<Profiles>> call = apiService.getProfileByUserNameAndPassword(Constants.auth_string,profiles);

                call.enqueue(new Callback<ArrayList<Profiles>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Profiles>> call, Response<ArrayList<Profiles>> response) {
                        try {
                            int statusCode = response.code();
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            if (statusCode == 200 || statusCode == 201) {

                                ArrayList<Profiles> profilesArrayList = response.body();//-------------------should not be list------------
                                if (profilesArrayList!=null && profilesArrayList.size()!=0) {
                                    
                                    Profiles responseBody = profilesArrayList.get(0);


                                    if(responseBody!=null){

                                        String token = SharedPrefManager.getInstance(LoginScreenActivity.this).getDeviceToken();
                                        System.out.println("token"+token);

                                        ProfileDeviceMappings profileDeviceMappings = new ProfileDeviceMappings();
                                        profileDeviceMappings.setDeviceId(token);
                                        profileDeviceMappings.setProfileId(responseBody.getProfileId());


                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginScreenActivity.this);
                                        SharedPreferences.Editor spe = sp.edit();
                                        spe.putInt(Constants.USER_ID, responseBody.getProfileId());
                                        PreferenceHandler.getInstance(LoginScreenActivity.this).setUserId(responseBody.getProfileId());
                                        PreferenceHandler.getInstance(LoginScreenActivity.this).setUserFullName(responseBody.getFirstName());
                                        PreferenceHandler.getInstance(LoginScreenActivity.this).setPhoneNumber(responseBody.getPhoneNumber());
                                        PreferenceHandler.getInstance(LoginScreenActivity.this).setUserName(responseBody.getUserName());

                                        addDeviceId(profileDeviceMappings);



                                    }else{

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            // Apply activity transition
                                            loginLayout.startAnimation(shakeAnimation);
                                        } else {
                                            // Swap without transition
                                        }
                                        new CustomToast().Show_Toast(LoginScreenActivity.this,view, "Something went wrong");
                                    }


                                }else{

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        // Apply activity transition
                                        loginLayout.startAnimation(shakeAnimation);
                                    } else {
                                        // Swap without transition
                                    }
                                    new CustomToast().Show_Toast(LoginScreenActivity.this,view, "Login Credentials was wrong!");


                                }
                            }else {
                                if (progressDialog!=null)
                                    progressDialog.dismiss();
                                Toast.makeText(LoginScreenActivity.this, "Login failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            Toast.makeText(LoginScreenActivity.this, "Please try after some time", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Profiles>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    public void addDeviceId(final ProfileDeviceMappings hm)
    {
        final ProgressDialog dialog = new ProgressDialog(LoginScreenActivity.this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                String authenticationString = Util.getToken(LoginScreenActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                ProfileDeviceMappingAPI hotelOperation = Util.getClient().create(ProfileDeviceMappingAPI.class);
                Call<ProfileDeviceMappings> response = hotelOperation.addProfileDevice(hm);

                response.enqueue(new Callback<ProfileDeviceMappings>() {
                    @Override
                    public void onResponse(Call<ProfileDeviceMappings> call, Response<ProfileDeviceMappings> response) {
                        System.out.println("GetHotelByProfileId = "+response.code());

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200||response.code() == 201||response.code() == 202||response.code() == 204)
                        {
                            try{
                                System.out.println("registered");
                                ProfileDeviceMappings deviceMappings = response.body();

                                if(deviceMappings!=null){

                                    PreferenceHandler.getInstance(LoginScreenActivity.this).setMappingId(deviceMappings.getProfileDeviceMappingId());

                                    if(PreferenceHandler.getInstance(LoginScreenActivity.this).getUserId()==153||PreferenceHandler.getInstance(LoginScreenActivity.this).getUserId()==182){
                                        Intent title = new Intent(LoginScreenActivity.this,EmployerOptions.class);
                                        title.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        title.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                        startActivity(title);
                                        LoginScreenActivity.this.finish();
                                        overridePendingTransition(R.anim.left_enter,R.anim.right_out);
                                    }else{
                                        Intent title = new Intent(LoginScreenActivity.this,LandingScreenActivity.class);
                                        title.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        title.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                        startActivity(title);
                                        LoginScreenActivity.this.finish();
                                        overridePendingTransition(R.anim.left_enter,R.anim.right_out);
                                    }


                                }



                            }catch (Exception e){
                                e.printStackTrace();
                            }




                        }else if(response.code() == 404){


                        }else
                        {

                            Toast.makeText(LoginScreenActivity.this,"Please try after some time",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ProfileDeviceMappings> call, Throwable t) {
                        System.out.println("Failed");
                        System.out.println(" Exception = "+t.getMessage());
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(LoginScreenActivity.this,"Please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

}
