package cmp.sem.team8.smarlecture.common.data.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ramym on 4/28/2018.
 */

public class GroupStatisticsModel {
    private ArrayList<String> groupMembers;
    private ArrayList<ArrayList<String>> sessionMembers;

    public GroupStatisticsModel(ArrayList<String> groupMembers,  ArrayList<ArrayList<String>> sessionMembers) {
        this.groupMembers = groupMembers;
        this.sessionMembers = sessionMembers;
    }

    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(ArrayList<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setSessionMembers( ArrayList<ArrayList<String>> mem)
    {
        this.sessionMembers=mem;
    }
    public  ArrayList<ArrayList<String>> getSessionMembers()
    {
        return this.sessionMembers;
    }
}
