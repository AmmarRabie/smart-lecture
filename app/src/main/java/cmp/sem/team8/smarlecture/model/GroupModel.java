package cmp.sem.team8.smarlecture.model;

import java.util.ArrayList;

/**
 * Created by AmmarRabie on 26/03/2018.
 */


@Deprecated
public class GroupModel {

    String name;
    String id;
    ArrayList<String> studentsNames;

    public GroupModel() {
    }

    public GroupModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getStudentsNames() {
        return studentsNames;
    }

    public void setStudentsNames(ArrayList<String> studentsNames) {
        this.studentsNames = studentsNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}