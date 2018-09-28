package app.zingo.employeemanagement.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 17-08-2018.
 */

public class ProfileNotification {

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("Title")
    private String Title;

    @SerializedName("Message")
    private String Message;

    @SerializedName("ServerId")
    private String ServerId;

    @SerializedName("SenderId")
    private String SenderId;

    @SerializedName("ProfileId")
    private int ProfileId;

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getServerId() {
        return ServerId;
    }

    public void setServerId(String serverId) {
        ServerId = serverId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }
}
