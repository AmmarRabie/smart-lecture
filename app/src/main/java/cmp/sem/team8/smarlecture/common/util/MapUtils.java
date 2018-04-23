package cmp.sem.team8.smarlecture.common.util;

import java.util.HashMap;

import cmp.sem.team8.smarlecture.model.GroupModel;


/**
 * Created by AmmarRabie on 26/03/2018.
 */

public class MapUtils {


    public static HashMap<String, Object> toHashMap(GroupModel model) {
        HashMap<String, Object> modelHashedMap = new HashMap<>();

        modelHashedMap.put("name", model.getName());
        modelHashedMap.put("id", model.getId());
        modelHashedMap.put("namesList", model.getStudentsNames());

        return modelHashedMap;
    }
}
