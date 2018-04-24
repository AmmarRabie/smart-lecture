package cmp.sem.team8.smarlecture.common.data.model;

import java.util.ArrayList;

/**
 * Created by AmmarRabie on 26/03/2018.
 */

public class GroupModel {

    private String name;
    private String id;
    private String ownerId;
    private ArrayList<InvitedUserModel> usersList; // list of ids of the users
    private ArrayList<String> sessionsIds;

    public GroupModel(String name, String id, String ownerId) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
    }

    public GroupModel(String name, String id, String ownerId, ArrayList<InvitedUserModel> usersList, ArrayList<String> sessionsIds) {
        this(name, id, ownerId);
        this.usersList = usersList;
        this.sessionsIds = sessionsIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<InvitedUserModel> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<InvitedUserModel> usersList) {
        this.usersList = usersList;
    }

    public String getOwnerId() {
        return ownerId;
    }


    public ArrayList<String> getSessionsIds() {
        return sessionsIds;
    }

    public void setSessionsIds(ArrayList<String> sessionsIds) {
        this.sessionsIds = sessionsIds;
    }
}
