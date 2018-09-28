package app.zingo.employeemanagement.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 17-08-2018.
 */

public class ProfileDeviceMappings implements Serializable {

    @SerializedName("ProfileDeviceMappingId")
    private int ProfileDeviceMappingId;

    @SerializedName("ProfileId")
    private int ProfileId	;

    @SerializedName("DeviceId")
    private String DeviceId;

    public int getProfileDeviceMappingId() {
        return ProfileDeviceMappingId;
    }

    public void setProfileDeviceMappingId(int profileDeviceMappingId) {
        ProfileDeviceMappingId = profileDeviceMappingId;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
}
