package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by AmmarRabie on 06/05/2018.
 */

class ChildEventWithRef extends EventWithRefBase {
    private ChildEventListener childEventListener;

    ChildEventWithRef(DatabaseReference reference) {
        super(reference);
    }

    public ChildEventWithRef(DatabaseReference reference, ChildEventListener childEventListener) {
        super(reference);
        this.childEventListener = childEventListener;
    }

    @Override
    void forget() {
        if (reference == null || childEventListener == null)
            return;
        reference.removeEventListener(childEventListener);
        reference = null;
        childEventListener = null;
    }
}
