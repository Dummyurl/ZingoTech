package app.zingo.employeemanagement.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import app.zingo.employeemanagement.R;

public class EmployerOptions extends AppCompatActivity {

    CardView mNotification,mPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_employer_options);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Management Options");

            mNotification = (CardView)findViewById(R.id.notification);
            mPdf = (CardView)findViewById(R.id.payslip);

            mNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent notify = new Intent(EmployerOptions.this,NotificationListActivity.class);
                    startActivity(notify);
                }
            });

            mPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent notify = new Intent(EmployerOptions.this,CreatePaySlip.class);
                    startActivity(notify);
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:

                EmployerOptions.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
