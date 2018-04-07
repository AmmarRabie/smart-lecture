package cmp.sem.team8.smarlecture;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by AmmarRabie on 04/04/2018.
 */

public class SmartLecture extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
//        FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(Long.MAX_VALUE);

    }


}
