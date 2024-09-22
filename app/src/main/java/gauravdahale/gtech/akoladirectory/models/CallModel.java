package gauravdahale.gtech.akoladirectory.models;

public class CallModel {
String username,userphone,shoptitle,calldate;

    public CallModel(String shoptitle, String username, String userphone, String calldate) {
        this.username = username;
        this.userphone = userphone;
        this.shoptitle = shoptitle;
        this.calldate = calldate;
    }

    public String getCalldate() {
        return calldate;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getShoptitle() {
        return shoptitle;
    }

    public void setShoptitle(String shoptitle) {
        this.shoptitle = shoptitle;
    }
}
