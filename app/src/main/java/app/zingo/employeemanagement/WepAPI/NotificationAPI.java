package app.zingo.employeemanagement.WepAPI;

import java.util.ArrayList;

import app.zingo.employeemanagement.Model.NotificationManager;
import app.zingo.employeemanagement.Model.ProfileNotification;
import app.zingo.employeemanagement.Model.Profiles;
import app.zingo.employeemanagement.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 17-08-2018.
 */

public interface NotificationAPI {

    @POST("Calculation/SendNotificationForMultipleDeviceByProfileId")
    Call<ArrayList<String>> sendnotificationToProfile(@Header("Authorization") String auth , @Body ProfileNotification profileNotification);

    @POST("NotificationManagers")
    Call<NotificationManager> saveNotification(@Header("Authorization") String auth , @Body NotificationManager hotelNotification);

    @GET("NotificationManagers")
    Call<ArrayList<NotificationManager>> getNotification(@Header("Authorization") String authKey);



}
