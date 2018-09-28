package app.zingo.employeemanagement.Model;

/**
 * Created by ZingoHotels Tech on 11-09-2018.
 */

public class MeetingDetails {

    int id;
    int userId;
    String date;
    String login;
    String logout;
    String duration;
    String loglng;
    String outlnglt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLoglng() {
        return loglng;
    }

    public void setLoglng(String loglng) {
        this.loglng = loglng;
    }

    public String getOutlnglt() {
        return outlnglt;
    }

    public void setOutlnglt(String outlnglt) {
        this.outlnglt = outlnglt;
    }
}
