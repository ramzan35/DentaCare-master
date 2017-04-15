package iit.dentacare;

/**
 * Created by Ramzan Dieze on 08/04/2017.
 */

public class User {

    String userId;
    String orientation;
    int acceleration;

    public User(){}

    public User(String userId, String orientation, int acceleration){
        this.userId = userId;
        this.orientation = orientation;
        this.acceleration = acceleration;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrientation() {
        return orientation;
    }

    public int getAcceleration() {
        return acceleration;
    }
}
