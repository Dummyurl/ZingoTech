package app.zingo.employeemanagement.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.zingo.employeemanagement.Activity.NotificationScreenActivity;
import app.zingo.employeemanagement.Model.NotificationManager;
import app.zingo.employeemanagement.R;
import app.zingo.employeemanagement.Utils.PreferenceHandler;
import app.zingo.employeemanagement.Utils.ThreadExecuter;
import app.zingo.employeemanagement.Utils.Util;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 18-08-2018.
 */

public class NotificationManagerAdapter extends RecyclerView.Adapter<NotificationManagerAdapter.ViewHolder>{

    private Activity context;
    private ArrayList<NotificationManager> list;

    public NotificationManagerAdapter(Activity context, ArrayList<NotificationManager> list) {

        this.context = context;
        this.list = list;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        try{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_notification_manager, parent, false);

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try{
            final NotificationManager dto = list.get(position);

            String title = list.get(position).getNotificationText();
            String message = list.get(position).getNotificationFor();

            if(message!=null){

                if(message.contains("=")){

                    String messageArray[] = message.split("=");
                    String type = messageArray[0];
                    String date = messageArray[1];
                    String location = messageArray[2];
                    String user = messageArray[3];
                    String meeting;

                    if(messageArray.length>4){

                            String client = messageArray[4];
                            String purpose = messageArray[5];
                            String remark = messageArray[6];



                            System.out.println("Meeting Client="+client);
                            System.out.println("Meeting purpose="+purpose);
                            System.out.println("Meeting Remark="+remark);

                            holder.mClient.setText(client);
                            holder.mPurpose.setText(purpose);
                            holder.mRemarks.setText(remark);


                    }

                    if(title.contains("Meeting Login")){

                        holder.mTitle.setText("Meeting "+type);
                        holder.mMeetingNotification.setVisibility(View.VISIBLE);

                    }else if(title.contains("Master Login")){
                        holder.mTitle.setText("Master "+type);
                        holder.mMeetingNotification.setVisibility(View.GONE);
                    }

                    holder.mTime.setText(date);
                    holder.mEmployee.setText(user);

                    if(location.contains("-")){

                        String locationArray[] = location.split("-");
                        double lng = Double.parseDouble(locationArray[1]);
                        double lat = Double.parseDouble(locationArray[0]);

                        try{


                            LatLng meetLatLng = new LatLng(lat,lng);
                            String address = getAddress(meetLatLng);
                            holder.mPlace.setText(address);


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }



            holder.mNotificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        Intent intent = new Intent(context, NotificationScreenActivity.class);
                        intent.putExtra("Title",dto.getNotificationText());
                        intent.putExtra("Message",dto.getNotificationFor());
                        context.startActivity(intent);



                }
            });
        }catch (Exception e){

        }



    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {


        CardView mNotificationLayout;
        TextView mTitle,mEmployee,mTime,mPlace,mClient,mPurpose,mRemarks;
        LinearLayout mMeetingNotification;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            try{
                mNotificationLayout = (CardView) itemView.findViewById(R.id.notification_layout);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mEmployee = (TextView) itemView.findViewById(R.id.employee_name_adapter);
                mTime = (TextView) itemView.findViewById(R.id.time_date);
                mPlace = (TextView) itemView.findViewById(R.id.place_name);
                mClient = (TextView) itemView.findViewById(R.id.client_name);
                mPurpose = (TextView) itemView.findViewById(R.id.purpose);
                mRemarks = (TextView) itemView.findViewById(R.id.remarks_meeting);
                mMeetingNotification = (LinearLayout) itemView.findViewById(R.id.meeting_notify);

            }catch(Exception e){
                e.printStackTrace();
            }



        }
    }

    private String getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(",");
                }

                result = address.getAddressLine(0);

                return result;
            }
            return result;
        } catch (IOException e) {
            Log.e("MapLocation", "Unable connect to Geocoder", e);
            return result;
        }

    }

}
