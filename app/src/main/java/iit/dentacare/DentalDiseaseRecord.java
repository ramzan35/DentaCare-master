package iit.dentacare;

import java.util.Date;

/**
 * Created by Ramzan Dieze on 6/1/2017.
 */

public class DentalDiseaseRecord {

    private String userId;
    private Date  brushingTime;
    private String status;
    private Date  date;

    public DentalDiseaseRecord(String userId, Date  brushingTime, String status, Date  date){
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

    public String getBrushingTime() {
        return brushingTime.toString();
    }

    public void setBrushingTime(Date brushingTime) {
        this.brushingTime = brushingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date  date) {
        this.date = date;
    }

}
