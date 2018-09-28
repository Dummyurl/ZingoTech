package app.zingo.employeemanagement.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.employeemanagement.Adapter.NotificationManagerAdapter;
import app.zingo.employeemanagement.Model.NotificationManager;
import app.zingo.employeemanagement.R;
import app.zingo.employeemanagement.Utils.PreferenceHandler;
import app.zingo.employeemanagement.Utils.ThreadExecuter;
import app.zingo.employeemanagement.Utils.Util;
import app.zingo.employeemanagement.WepAPI.NotificationAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity {

    private static RecyclerView mNotificationList;
    private static Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_notification_list);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Notification List");

            mNotificationList = (RecyclerView) findViewById(R.id.notification_list);
            mLogout = (Button) findViewById(R.id.log_out);

            getNotification();

            mLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent logout = new Intent(NotificationListActivity.this,LoginScreenActivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PreferenceHandler.getInstance(NotificationListActivity.this).clear();
                    startActivity(logout);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getNotification(){
        final ProgressDialog progressDialog = new ProgressDialog(NotificationListActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                NotificationAPI apiService =
                        Util.getClient().create(NotificationAPI.class);
                String authenticationString = Util.getToken(NotificationListActivity.this);
                Call<ArrayList<NotificationManager>> call = apiService.getNotification(authenticationString);

                call.enqueue(new Callback<ArrayList<NotificationManager>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NotificationManager>> call, Response<ArrayList<NotificationManager>> response) {

                        int statusCode = response.code();
                        if (progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                        if (statusCode == 200) {

                            try{
                               ArrayList<NotificationManager> list =  response.body();
                                ArrayList<NotificationManager> nfm = new ArrayList<>();


                                if(list != null && list.size() != 0)
                                {

                                    for(int i=0;i<list.size();i++){

                                        if(list.get(i).getNotificationText().contains("Master Login")||list.get(i).getNotificationText().contains("Meeting Login")){

                                            nfm.add(list.get(i));
                                       }
                                    }

                                    if(nfm!=null && nfm.size()!=0){

                                        Collections.reverse(nfm);
                                        NotificationManagerAdapter adapter = new NotificationManagerAdapter(NotificationListActivity.this, nfm);
                                        mNotificationList.setAdapter(adapter);



                                    }else{
                                        Toast.makeText(NotificationListActivity.this, "No Notifications", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else
                                {

                                    Toast.makeText(NotificationListActivity.this, "No Notifications", Toast.LENGTH_SHORT).show();

                                }
                            }catch(Exception e){
                                e.printStackTrace();

                            }


                        }else {

                            Toast.makeText(NotificationListActivity.this, " Failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NotificationManager>> call, Throwable t) {

                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                        Toast.makeText(NotificationListActivity.this, "Something went wrong due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:

                NotificationListActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
