package cmp.sem.team8.smarlecture.common.data.model;

import cmp.sem.team8.smarlecture.common.data.DataService;

/**
 * Created by AmmarRabie on 02/03/2018.
 */

/**
 * Data representation of a session in the system. this is the base session model
 * meaning that this class doesn't have any additional data out of session
 */
public class SessionModel {
    private String id;
    private String forGroupId;
    private DataService.AttendanceStatus attendanceStatus;
    private DataService.SessionStatus sessionStatus;
    private String name;
    private String secret;

    public SessionModel(String id, String forGroupId, DataService.AttendanceStatus attendanceStatus, DataService.SessionStatus sessionStatus, String name) {
        this.id = id;
        this.forGroupId = forGroupId;
        this.attendanceStatus = attendanceStatus;
        this.sessionStatus = sessionStatus;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForGroupId() {
        return forGroupId;
    }

    public void setForGroupId(String forGroupId) {
        this.forGroupId = forGroupId;
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
