package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 24/04/2018.
 */

/**
 * This class represents one group invitation
 */
public class GroupInvitationModel {
    private String groupId;
    private String ownerId;
    private String groupName;
    private String ownerName;

    public GroupInvitationModel(String groupId, String ownerId, String groupName, String ownerName) {
        this.groupId = groupId;
        this.ownerId = ownerId;
        this.groupName = groupName;
        this.ownerName = ownerName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
