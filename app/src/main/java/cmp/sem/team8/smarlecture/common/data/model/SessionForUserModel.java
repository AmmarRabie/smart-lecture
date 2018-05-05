package cmp.sem.team8.smarlecture.common.data.model;

import cmp.sem.team8.smarlecture.common.data.DataService;

/**
 * Created by AmmarRabie on 23/04/2018.
 */

public class SessionForUserModel {
    private String id;
    private DataService.SessionStatus status;
    private DataService.AttendanceStatus attendanceStatus;
    private String sessionName;

    private String forGroupId;
    private String forGroupName;

    private String ownerId;
    private String ownerName;

    public SessionForUserModel(String id,
                               DataService.SessionStatus status,
                               DataService.AttendanceStatus attendanceStatus,
                               String sessionName,
                               String forGroupId,
                               String forGroupName,
                               String ownerId,
                               String ownerName) {
        this.id = id;
        this.status = status;
        this.attendanceStatus = attendanceStatus;
        this.sessionName = sessionName;
        this.forGroupId = forGroupId;
        this.forGroupName = forGroupName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public String getId() {
        return id;
    }

    public DataService.SessionStatus getStatus() {
        return status;
    }

    public DataService.AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getForGroupId() {
        return forGroupId;
    }

    public String getForGroupName() {
        return forGroupName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
