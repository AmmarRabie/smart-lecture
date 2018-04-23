package cmp.sem.team8.smarlecture.model;

import java.util.ArrayList;

/**
 * Created by AmmarRabie on 02/03/2018.
 */
@Deprecated
public class SessionModel {
    private String mSessionID;
    private String mName;
    private String mGroupID;
    private String mSessionStatus;
    private String mAttendanceStatus;
    private ArrayList<String> mStudentsList;

    public String getmAttendanceStatus() {
        return mAttendanceStatus;
    }

    public void setmAttendanceStatus(String mAttendanceStatus) {
        this.mAttendanceStatus = mAttendanceStatus;
    }

    public ArrayList<String> getmStudentsList() {
        return mStudentsList;
    }

    public void setmStudentsList(ArrayList<String> mStudentsList) {
        this.mStudentsList = mStudentsList;
    }

    public String getmSessionID() {
        return mSessionID;
    }

    public void setmSessionID(String mSessionID) {
        this.mSessionID = mSessionID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmGroupID() {
        return mGroupID;
    }

    public void setmGroupID(String mGroupID) {
        this.mGroupID = mGroupID;
    }

    public String getmSessionStatus() {
        return mSessionStatus;
    }

    public void setmSessionStatus(String mSessionStatus) {
        this.mSessionStatus = mSessionStatus;
    }


}