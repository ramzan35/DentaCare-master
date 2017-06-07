package iit.dentacare;

import java.util.Date;

/**
 * Created by Ramzan Dieze on 6/1/2017.
 */

public class DentalDiseaseRecord {

    private String userId;
    private int brushingTime;
    private Status status;
    private Date  date;

    public DentalDiseaseRecord(String userId, int  brushingTime, Status status, Date  date){
        this.userId = userId;
        this.brushingTime = brushingTime;
        this.status = status;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBrushingTime() {
        return brushingTime;
    }

    public void setBrushingTime(int brushingTime) {
        this.brushingTime = brushingTime;
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date  date) {
        this.date = date;
    }

    static class Status{

        int upLeft;
        int upRight;
        int downLeft;
        int downRight;


        public Status(int upLeft,int upRight,int downleft,int downRight){
            this.upLeft = upLeft;
            this.upRight = upRight;
            this.downLeft = downleft;
            this.downRight = downRight;
        }

        public String toString(){//overriding the toString() method
            return "upLeft="+upLeft+" upRight="+upRight+" downLeft="+downLeft+" downRight="+downRight;
        }
    }

}
