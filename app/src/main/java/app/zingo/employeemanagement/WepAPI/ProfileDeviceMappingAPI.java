package app.zingo.employeemanagement.WepAPI;

import java.util.ArrayList;

import app.zingo.employeemanagement.Model.ProfileDeviceMappings;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 17-08-2018.
 */

public interface ProfileDeviceMappingAPI {

    @POST("ProfileDeviceMappings")
    Call<ProfileDeviceMappings> addProfileDevice(@Body ProfileDeviceMappings body);

    @DELETE("ProfileDeviceMappings/{id}")
    Call<ProfileDeviceMappings> deleteDeviceId(@Path("id") int id);
}
