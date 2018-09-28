package app.zingo.employeemanagement.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.zingo.employeemanagement.Google.TrackGPS;
import app.zingo.employeemanagement.R;

public class NotificationScreenActivity extends AppCompatActivity {

    private static TextView mLocation;
    private static EditText mDate,mProfileName;
    private static MapView mapView;
    private Button mHome;

    //Map
    private GoogleMap mMap;

    Marker marker;
    TrackGPS trackGPS;
    double mLatitude, mLongitude;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public String TAG = "MAPLOCATION",placeId;

    //intent
    String title,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_notification_screen);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mLocation = (TextView)findViewById(R.id.location_et);
            mDate = (EditText) findViewById(R.id.date_time);
            mProfileName = (EditText) findViewById(R.id.employee_name);
            mapView = (MapView)findViewById(R.id.google_map_view);
            mHome = (Button) findViewById(R.id.back_home);

            trackGPS = new TrackGPS(this);

            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            try {
                MapsInitializer.initialize(NotificationScreenActivity.this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;


                    if (ActivityCompat.checkSelfPermission(NotificationScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NotificationScreenActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.setMyLocationEnabled(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

                    if(mLatitude==0 && mLongitude==0)
                    {
                        if(trackGPS.canGetLocation())
                        {
                            mLatitude = trackGPS.getLatitude();
                            mLongitude = trackGPS.getLongitude();
                        }
                    }else{

                    }


                    LatLng latLng = new LatLng(mLatitude,mLongitude);

                    final CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                    marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.
                            defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .position(latLng));

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                }
            });



            Bundle bundle =getIntent().getExtras();
            if(bundle!=null){
                title = bundle.getString("Title");
                message = bundle.getString("Message");
            }



            if(message!=null){

                if(message.contains("=")){

                    String messageArray[] = message.split("=");
                    String type = messageArray[0];
                    String date = messageArray[1];
                    String location = messageArray[2];
                    String user = messageArray[3];

                    if(title.contains("Meeting Login")){

                        setTitle("Meeting "+type);

                    }else if(title.contains("Master Login")){
                        setTitle("Master "+type);
                    }

                    mDate.setText(date);
                    mProfileName.setText(user);

                    if(location.contains("-")){

                        String locationArray[] = location.split("-");
                        double lng = Double.parseDouble(locationArray[1]);
                       mLongitude = Double.parseDouble(locationArray[1]);
                        double lat = Double.parseDouble(locationArray[0]);
                        mLatitude = Double.parseDouble(locationArray[0]);

                        try{


                            if(mMap!=null){
                                mMap.clear();
                            }

                            LatLng meetLatLng = new LatLng(lat,lng);
                            String address = getAddress(meetLatLng);
                            mLocation.setText(address);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(meetLatLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(17).target(meetLatLng).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }

            mHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent main = new Intent(NotificationScreenActivity.this, NotificationListActivity.class);
                    startActivity(main);
                    NotificationScreenActivity.this.finish();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(NotificationScreenActivity.this, Locale.getDefault());
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
                /*sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());*/
                //System.out.println("add = "+address.getAddressLine(0));
                //result = sb.toString();
                result = address.getAddressLine(0);

                return result;
            }
            return result;
        } catch (IOException e) {
            Log.e("MapLocation", "Unable connect to Geocoder", e);
            return result;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                Intent main = new Intent(NotificationScreenActivity.this, NotificationListActivity.class);
                startActivity(main);
                NotificationScreenActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(NotificationScreenActivity.this, NotificationListActivity.class);
        startActivity(main);
        NotificationScreenActivity.this.finish();
    }
}
