package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupMessagesEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.StorageEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

/**
 * Helper class for {@link FirebaseRepository}. simplify the logic  of listening and forgetting
 * Also define the get reference for basic entries
 */
abstract class FirebaseRepoHelper implements DataService {

    private HashMap<Listen, EventWithRefBase> listenersMap;

    FirebaseRepoHelper() {
        listenersMap = new HashMap<>();
    }


    static DatabaseReference getReference(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

    static DatabaseReference getGroupRef(String groupId) {
        return getReference(GroupEntry.KEY_THIS).child(groupId);
    }

    static DatabaseReference getSessionRef(String sessionId) {
        return getReference(SessionEntry.KEY_THIS).child(sessionId);
    }

    static DatabaseReference getUserRef(String userId) {
        return getReference(UserEntry.KEY_THIS).child(userId);
    }

    static DatabaseReference getGroupMessagesRef(String groupId) {
        return getReference(GroupMessagesEntry.KEY_THIS).child(groupId);
    }

    static StorageReference getProfileImageRef(String imageId) {
        return FirebaseStorage.getInstance()
                .getReference(StorageEntry.FOLDER_PROFILE_IMAGES).child(imageId + ".png");
    }


    @Override
    public void forget(Listen listener) {
        EventWithRefBase target = listenersMap.get(listener);
        if (target != null) {
            listenersMap.remove(listener); // remove from the map
            target.forget();
        }
    }

    void addNewListener(Listen callback, ValueEventListener listener, DatabaseReference reference) {
        EventWithRefBase valueEventWithRef = new ValueEventWithRef(reference, listener);
        listenersMap.put(callback, valueEventWithRef);
    }

    void addNewListener(Listen callback, ChildEventListener listener, DatabaseReference reference) {
        EventWithRefBase valueEventWithRef = new ChildEventWithRef(reference, listener);
        listenersMap.put(callback, valueEventWithRef);
    }
}
