package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 26/03/2018.
 */

/**
 * Data representation of one group in the system
 */
public class GroupModel {

    private String name;
    private String id;
    private String ownerId;

    public GroupModel(String name, String id, String ownerId) {
        this.name = name;
        this.id = id;
        this.ownerId = ownerId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

}
