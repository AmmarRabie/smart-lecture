package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.StorageEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

abstract class FirebaseRepoHelper implements AppDataSource {

    protected ListenersList listeners;
    protected HashMap<Listen, ValueEventWithRef> listenersMap;

    FirebaseRepoHelper() {
        listeners = new ListenersList();
        listenersMap = new HashMap<>();
    }


    public static DatabaseReference getReference(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

    public static DatabaseReference getGroupRef(String groupId) {
        return getReference(GroupEntry.KEY_THIS).child(groupId);
    }

    public static DatabaseReference getSessionRef(String sessionId) {
        return getReference(SessionEntry.KEY_THIS).child(sessionId);
    }

    public DatabaseReference getUserRef(String userId) {
        return getReference(UserEntry.KEY_THIS).child(userId);
    }

    protected StorageReference getProfileImageRef(String imageId) {
        return FirebaseStorage.getInstance()
                .getReference(StorageEntry.FOLDER_PROFILE_IMAGES).child(imageId + ".png");
    }


    // helper methods and nested classes for handling forgetting Listen callbacks
    protected boolean isDead(Listen callback) {
        if (!callback.shouldListen()) {
            listeners.remove(callback);
            return true;
        }
        return false;
    }

    @Override
    public void forget(Listen listener) {
        ValueEventWithRef target = listenersMap.get(listener);
        if (target != null) {
            listenersMap.remove(listener); // remove from the map
            target.forget();
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).firebaseListener.equals(listener)) {
                listeners.get(i).forget();
            }
        }
    }

    protected void addNewListener(Listen callback, ValueEventListener listener, DatabaseReference reference)
    {
        ValueEventWithRef valueEventWithRef = new ValueEventWithRef(listener, reference);
        listenersMap.put(callback, valueEventWithRef);
    }

    protected static final class ListenersList extends ArrayList<ValueEventWithRef> {
        @Override
        public ValueEventWithRef remove(int index) {
            this.get(index).forget();
            return super.remove(index);
        }

        @Override
        public boolean remove(Object o) {
            ((ValueEventWithRef) o).forget();
            return super.remove(o);
        }
    }

    protected static final class ValueEventWithRef {
        ValueEventListener firebaseListener;
        DatabaseReference reference;

        public ValueEventWithRef(ValueEventListener listener, DatabaseReference reference) {
            this.firebaseListener = listener;
            this.reference = reference;
        }

        ValueEventWithRef(DatabaseReference reference) {
            this.reference = reference;
        }

        void forget() {
            if (reference == null || firebaseListener == null)
                return;
            reference.removeEventListener(firebaseListener);
            reference = null;
            firebaseListener = null;
        }

        public void setFirebaseListener(ValueEventListener firebaseListener) {
            this.firebaseListener = firebaseListener;
        }

    }
}
