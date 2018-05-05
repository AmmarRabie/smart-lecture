package cmp.sem.team8.smarlecture.common.data.model;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by ramym on 4/28/2018.

 import java.util.ArrayList;
 import java.util.List;

 /**
 * Created by Ramy saied 29/4/2018.
 * to represent groups of users like most attendant users ,.....
 */
public class GroupOfUsersModel {

    public String string;
    public final List<UserModel> children = new ArrayList<UserModel>();

    public GroupOfUsersModel(String string) {
        this.string = string;
    }

}