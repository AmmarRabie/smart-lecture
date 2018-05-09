package cmp.sem.team8.smarlecture.common.data.firebase;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by AmmarRabie on 06/05/2018.
 */

abstract class EventWithRefBase {
    DatabaseReference reference;

    EventWithRefBase(DatabaseReference reference){
        this.reference = reference;
    }
    abstract void forget();
}
