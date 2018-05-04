package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 04/05/2018.
 */

/**
 * Data representation of the one message of a group. these are data that group members are
 * notified with when data coming.
 */
public class GroupMessageModel extends GroupModel {
    private String messageId;
    private String body;
    private String title;

    public GroupMessageModel(String messageId, String groupId, String name, String ownerId) {
        super(name, groupId, ownerId);
        this.messageId = messageId;
    }

    public GroupMessageModel(String messageId, GroupModel group, String body, String title) {
        super(group.getName(), group.getId(), group.getOwnerId());
        this.body = body;
        this.title = title;
        this.messageId = messageId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageId() {
        return messageId;
    }
}
