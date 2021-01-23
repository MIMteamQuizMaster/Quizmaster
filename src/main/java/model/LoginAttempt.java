package model;

public class LoginAttempt {
    int id;
    String ip;
    String password;
    String date;

    public LoginAttempt(int id, String ip, String date) {
        this.id = id;
        this.ip = ip;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
