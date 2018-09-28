package app.zingo.employeemanagement.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.zingo.employeemanagement.Adapter.LoginDetailsAdapter;
import app.zingo.employeemanagement.Model.LatLngDistance;
import app.zingo.employeemanagement.Model.LoginDetails;
import app.zingo.employeemanagement.Model.MeetingDetails;
import app.zingo.employeemanagement.R;
import app.zingo.employeemanagement.Utils.DataBaseHelper;
import app.zingo.employeemanagement.Utils.PreferenceHandler;

public class ProfileActivity extends AppCompatActivity {

    private static TextView mName,mWorkedDays,mWorkedHours,mTravelDistance,mTarget;
    private static RecyclerView mLoginDetails;

    DataBaseHelper db = new DataBaseHelper(ProfileActivity.this);
    ArrayList<LoginDetails> loginDetails;
    ArrayList<MeetingDetails> meetingDetails;
    ArrayList<LatLngDistance> latLng;
    int userId,workedDays=0,workedHours=0,target=0,totalHours=0,totalMinutes=0;
    double travelDistance=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_profile);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Profile");

            mName = (TextView)findViewById(R.id.user_name);
            mWorkedDays = (TextView)findViewById(R.id.worked_days);
            mWorkedHours = (TextView)findViewById(R.id.worked_hours);
            mTravelDistance = (TextView)findViewById(R.id.travel_distance);
            mTarget = (TextView)findViewById(R.id.target_complete);

            mLoginDetails = (RecyclerView) findViewById(R.id.login_details);

            mName.setText(""+ PreferenceHandler.getInstance(ProfileActivity.this).getUserFullName());
            userId = PreferenceHandler.getInstance(ProfileActivity.this).getUserId();

            latLng = new ArrayList<>();

            if(db.getLoginDetails()!=null&&db.getLoginDetails().size()!=0){

                ArrayList<LoginDetails> loginDetailsList = db.getLoginDetails();
                loginDetails = new ArrayList<>();

                for(int i=0;i<loginDetailsList.size();i++){

                    if(loginDetailsList.get(i).getUserId()==userId){
                         loginDetails.add(loginDetailsList.get(i));

                        LatLngDistance lng = new LatLngDistance();
                        lng.setDate(loginDetailsList.get(i).getDate());
                        lng.setTime(loginDetailsList.get(i).getLogin());
                        lng.setLnglat(loginDetailsList.get(i).getLoglng());
                        lng.setType("Master");
                        latLng.add(lng);

                        lng = new LatLngDistance();
                        lng.setDate(loginDetailsList.get(i).getDate());
                        lng.setTime(loginDetailsList.get(i).getLogout());
                        lng.setLnglat(loginDetailsList.get(i).getOutlnglt());
                        lng.setType("Master");
                        latLng.add(lng);
                    }


                }

            }

            if(db.getLoginDetailsMeet()!=null&&db.getLoginDetailsMeet().size()!=0){

                ArrayList<MeetingDetails> loginDetailsList = db.getLoginDetailsMeet();
                meetingDetails = new ArrayList<>();

                for(int i=0;i<loginDetailsList.size();i++){

                    if(loginDetailsList.get(i).getUserId()==userId){
                        meetingDetails.add(loginDetailsList.get(i));
                        LatLngDistance lng = new LatLngDistance();
                        lng.setDate(loginDetailsList.get(i).getDate());
                        lng.setTime(loginDetailsList.get(i).getLogin());
                        lng.setLnglat(loginDetailsList.get(i).getLoglng());
                        lng.setType("Meet");
                        latLng.add(lng);

                        lng = new LatLngDistance();
                        lng.setDate(loginDetailsList.get(i).getDate());
                        lng.setTime(loginDetailsList.get(i).getLogout());
                        lng.setLnglat(loginDetailsList.get(i).getOutlnglt());
                        lng.setType("Meet");
                        latLng.add(lng);

                    }


                }

            }


            if(loginDetails!=null&&loginDetails.size()!=0){

                LoginDetailsAdapter adapter = new LoginDetailsAdapter(ProfileActivity.this,loginDetails);
                mLoginDetails.setAdapter(adapter);



                for(int i=0;i<loginDetails.size();i++){

                    int wd = 0;
                    String duration = loginDetails.get(i).getDuration();
                    int hour = 0,minute = 0;



                    if(duration!=null&&!duration.isEmpty()){
                        if(duration.contains(":")){
                            String dur[] = duration.split(":");
                            hour = Integer.parseInt(dur[0]);
                            minute = Integer.parseInt(dur[1]);
                        }
                    }
                    int totalSeconds = ((hour * 60) + minute) * 60 ;
                    int hours = totalSeconds / 3600;  // Be sure to use integer arithmetic
                    int minutes = ((totalSeconds) / 60) % 60;

                    totalHours = totalHours+hours;
                    totalMinutes = totalMinutes+minutes;


                    for(int j=0;j<i;j++){


                        if(loginDetails.get(i).getDate().equalsIgnoreCase(loginDetails.get(j).getDate())){
                            wd =1;
                            break;
                        }
                    }

                    if(wd==0){

                        workedDays=workedDays+1;
                    }


                }

                mWorkedDays.setText(""+workedDays);

                String hourDisplay="",minDisplay="";

                if(totalHours<10){
                    hourDisplay = "0"+totalHours;
                }else{
                    hourDisplay = ""+totalHours;
                }

                if(totalMinutes<10){
                    minDisplay = "0"+totalMinutes;
                }else{
                    minDisplay = ""+totalMinutes;
                }
                mWorkedHours.setText(hourDisplay+":"+minDisplay);
              //  mTravelDistance.setText(new DecimalFormat("##.##").format(travelDistance)+"Km");
                System.out.println(" Km = "+new DecimalFormat("##.##").format(travelDistance));

            }

            if(meetingDetails!=null&&meetingDetails.size()!=0){

                mTarget.setText(""+meetingDetails.size());

            }else{
                mTarget.setText("0");
            }

            if(latLng!=null&&latLng.size()!=0){

                Collections.sort(latLng, new Comparator() {
                    @Override
                    public int compare(Object o, Object t1) {
                        LatLngDistance latlng = (LatLngDistance) o;
                        LatLngDistance latlng1 = (LatLngDistance) t1;
                        return latlng.getDate().compareTo(latlng1.getDate());
                    }
                });

                Collections.sort(latLng, new Comparator() {
                    @Override
                    public int compare(Object o, Object t1) {
                        LatLngDistance latlng = (LatLngDistance) o;
                        LatLngDistance latlng1 = (LatLngDistance) t1;
                        return latlng.getTime().compareTo(latlng1.getTime());
                    }
                });


                for(int i=1;i<latLng.size();i++){

                    System.out.println("Type = "+latLng.get(i).getType());

                    String inLngLt = latLng.get(i).getLnglat();
                    String outLngLt = latLng.get((i-1)).getLnglat();

                    if(inLngLt!=null&&!inLngLt.isEmpty()&&outLngLt!=null&&!outLngLt.isEmpty()){

                        String in[]={},out[]={};

                        if(inLngLt.contains(",")){
                            in = inLngLt.split(",");
                        }

                        if(outLngLt.contains(",")){
                            out = outLngLt.split(",");
                        }

                        if(in!=null&&in.length!=0&&out!=null&&out.length!=0){

                            double km = distance(Double.parseDouble(in[1]),Double.parseDouble(in[0]),Double.parseDouble(out[1]),Double.parseDouble(out[0]));

                            travelDistance = travelDistance + km;

                        }
                    }

                }

                mTravelDistance.setText(new DecimalFormat("##.##").format(travelDistance)+"Km");


            }else{
                mTravelDistance.setText(new DecimalFormat("##.##").format(travelDistance)+"Km");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }




    private double distance(double lat1, double lon1, double lat2, double lon2) {

        if(lat1 == lat2 && lon1 == lon2){
            return  0;
        }else{

            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;

            dist = dist * 1.609344;
            return (dist);

        }



    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                Intent main = new Intent(ProfileActivity.this, LandingScreenActivity.class);
                startActivity(main);
                ProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(ProfileActivity.this, LandingScreenActivity.class);
        startActivity(main);
        ProfileActivity.this.finish();
    }
}
