package cmp.sem.team8.smarlecture.model;

import java.io.Serializable;

/**
 * Created by AmmarRabie on 02/03/2018.
 */

public class UserModel implements Serializable{

    String name;
    String email;

    String identity;

    public UserModel(String name, String email, String identity) {
        this.name = name;
        this.email = email;
        this.identity = identity;
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
