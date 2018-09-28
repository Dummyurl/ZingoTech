package app.zingo.employeemanagement.WepAPI;

import java.util.ArrayList;

import app.zingo.employeemanagement.Model.Profiles;
import app.zingo.employeemanagement.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 17-08-2018.
 */

public interface ProfileAPI {

    @POST(API.GET_PROFILE_BY_USER_NAME_AND_PASSWORD)
    Call<ArrayList<Profiles>> getProfileByUserNameAndPassword(@Header("Authorization") String authKey, @Body Profiles body);

}
