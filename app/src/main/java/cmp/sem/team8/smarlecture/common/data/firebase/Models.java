package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AmmarRabie on 25/04/2018.
 */

/**
 * <B><I>
 * Don't Use This class
 * </B></I>
 * <P>
 * The aim of this class was to isolate the firebase data structures -> it define the classes as
 * it stored in the firebase. Every class represents an entry with all its childes data only
 * (with any joins).<P>
 * This class will not be used now. We will make use of it at the situation our firebase database
 * will become very large with many data entries, so at this situation we will need such class to
 * concentrate our work in theses classes then another class will take the task of converting these
 * models to user view models.<P>
 * <B>Not completed</B>
 */
final class Models {
    final static class Group
    {
        private Group(){}

        String groupId;
        String ownerId;
        String name;
        ArrayList<HashMap<String,Boolean>> namesList;

        static Group fromSnapshot(DataSnapshot groupSnapshot)
        {
            Group result = new Group();
            result.groupId = groupSnapshot.getKey();
            result.name = groupSnapshot.child(FirebaseContract.GroupEntry.KEY_NAME).getValue(String.class);
            result.ownerId = groupSnapshot.child(FirebaseContract.GroupEntry.KEY_OWNER_ID).getValue(String.class);
            for (DataSnapshot dataSnapshot : groupSnapshot.child(FirebaseContract.GroupEntry.KEY_NAMES_LIST).getChildren())
                result.namesList.add(dataSnapshot.getValue(HashMap.class));
            return result;
        }
    }


    final static class Session
    {

    }

    final static class User
    {

    }


}
