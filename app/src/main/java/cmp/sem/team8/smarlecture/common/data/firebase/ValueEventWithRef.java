package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AmmarRabie on 06/05/2018.
 */

/**
 * represents the value event listener with its reference
 */
class ValueEventWithRef extends EventWithRefBase {
    private ValueEventListener firebaseListener;

    ValueEventWithRef(DatabaseReference reference) {
        super(reference);
    }

    ValueEventWithRef(DatabaseReference reference, ValueEventListener firebaseListener) {
        super(reference);
        this.firebaseListener = firebaseListener;
    }

    @Override
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
