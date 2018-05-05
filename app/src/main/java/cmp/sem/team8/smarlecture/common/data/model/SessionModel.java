package cmp.sem.team8.smarlecture.common.data.model;

import cmp.sem.team8.smarlecture.common.data.DataService;

/**
 * Created by AmmarRabie on 02/03/2018.
 */

public class SessionModel {
    private String id;
    private String forGroup;
    private DataService.AttendanceStatus attendanceStatus;
    private DataService.SessionStatus sessionStatus;
    private String name;
    private String secret;

    public SessionModel(String id, String forGroup, DataService.AttendanceStatus attendanceStatus, DataService.SessionStatus sessionStatus, String name) {
        this.id = id;
        this.forGroup = forGroup;
        this.attendanceStatus = attendanceStatus;
        this.sessionStatus = sessionStatus;
        this.name = name;
    }

    public SessionModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForGroup() {
        return forGroup;
    }

    public void setForGroup(String forGroup) {
        this.forGroup = forGroup;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public DataService.AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(DataService.AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public DataService.SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(DataService.SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
