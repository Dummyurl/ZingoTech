package app.zingo.employeemanagement.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import app.zingo.employeemanagement.BuildConfig;
import app.zingo.employeemanagement.R;
import app.zingo.employeemanagement.Utils.PreferenceHandler;

public class SplashScreenActivity extends AppCompatActivity {

   //UI declaration
    private static  ImageView mLogo;
    private static  TextView mAppName,mPowered,mBy,mVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_splash_screen);

           //Transparent Notification bar/tray above or equal to API 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }

            //UI initialize
            mLogo = (ImageView)findViewById(R.id.app_log);
            mAppName = (TextView) findViewById(R.id.hotel_title_text);
            mVersionCode = (TextView) findViewById(R.id.version_code);
            mPowered = (TextView) findViewById(R.id.hotel_title_sub_text);
            mBy = (TextView) findViewById(R.id.splash_hotel_title);

            //Animation
            Animation logo_anim = AnimationUtils.loadAnimation(this,R.anim.splash_transition_zoom_in);
            mLogo.startAnimation(logo_anim);
            mAppName.startAnimation(logo_anim);
            mPowered.startAnimation(logo_anim);
            mBy.startAnimation(logo_anim);

            String versioncode = BuildConfig.VERSION_NAME;
            mVersionCode.setText("Version code : "+versioncode+"");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {


                    try
                    {
                        int userId = PreferenceHandler.getInstance(SplashScreenActivity.this).getUserId();

                        if (userId!=0){

                            if (userId==153||userId==182){

                                Intent login = new Intent(SplashScreenActivity.this,EmployerOptions.class);
                                startActivity(login);
                                SplashScreenActivity.this.finish();
                            }else{
                                Intent login = new Intent(SplashScreenActivity.this,LandingScreenActivity.class);
                                startActivity(login);
                                SplashScreenActivity.this.finish();
                            }



                        }else{

                            Intent login = new Intent(SplashScreenActivity.this,LoginScreenActivity.class);
                            startActivity(login);
                            SplashScreenActivity.this.finish();

                        }

                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }, 1200);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
