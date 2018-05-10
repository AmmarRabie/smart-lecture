package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 23/04/2018.
 */

/**
 * Data represents the session with additional info about the owner of this session (group) and
 * the group this session is related to
 */
public class SessionForUserModel extends SessionModel {
    private GroupModel group;
    private UserModel owner;

    public SessionForUserModel(SessionModel session, GroupModel group, UserModel owner) {
        super(session.getId(), session.getForGroupId(), session.getAttendanceStatus(), session.getSessionStatus(), session.getName());
        this.group = group;
        this.owner = owner;
    }

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }
}
