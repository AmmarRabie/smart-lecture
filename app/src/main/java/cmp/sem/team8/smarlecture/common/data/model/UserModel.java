package cmp.sem.team8.smarlecture.common.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AmmarRabie on 02/03/2018.
 */

public class UserModel implements Serializable{

    private String id;
    private String name;
    private String email;


    public UserModel(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public UserModel(String name, String email, String id, ArrayList<String> groupInvitations) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
