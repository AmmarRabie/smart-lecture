package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.AppDataSource.Listen;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public abstract class FirebaseRepoHelper implements AppDataSource{

    protected ListenersList listeners;

    FirebaseRepoHelper() {
        listeners = new ListenersList();
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
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).listener.equals(listener)) {
                listeners.get(i).forget();
            }
        }
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
        ValueEventListener listener;
        DatabaseReference reference;

        public ValueEventWithRef(ValueEventListener listener, DatabaseReference reference) {
            this.listener = listener;
            this.reference = reference;
        }

        ValueEventWithRef(DatabaseReference reference) {
            this.reference = reference;
        }

        void forget() {
            if (reference == null || listener == null)
                return;
            reference.removeEventListener(listener);
            reference = null;
            listener = null;
        }

        public ValueEventListener getListener() {
            return listener;
        }

        public void setListener(ValueEventListener listener) {
            this.listener = listener;
        }


    }
}
