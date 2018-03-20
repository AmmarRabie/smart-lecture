package cmp.sem.team8.smarlecture.session.beginattendance;

import android.media.MediaCas;
import android.text.format.Time;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendancePresenter implements BeginAttendanceContract.Actions{
    BeginAttendanceContract.Views mView;
    Integer Atime;
    DatabaseReference ref;

    int SessionId;

    int maxId=10000000;
    int minId=0;
    public BeginAttendancePresenter(BeginAttendanceContract.Views view,int SessionId) {
        mView = view;
        mView.setPresenter(this);

        Atime=null;

        this.SessionId= SessionId;
        ref= FirebaseDatabase.getInstance().getReference();

    }
    @Override
    public void start() {

    }

    @Override
    public void BeginAttendance() {

        setTime(5);
        mView.showProgressIndicator(1);

        Random rand = new Random(System.currentTimeMillis());
        //get the range, casting to long to avoid overflow problems
        long range = (long)maxId - (long)minId + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * rand.nextDouble());
        Integer randomNumber =  (int)(fraction + minId);

        DatabaseReference nref=ref.child("sessions").child(Integer.toString(SessionId)).child("attendance");

        nref.setValue("open");

        nref=ref.child("sessions").child(Integer.toString(SessionId)).child("attendancesecrect");

        nref.setValue(randomNumber.toString());

        mView.showSecrect(randomNumber);

    }

    @Override
    public void endAttendance() {

        DatabaseReference nref = ref.child("sessions").child(Integer.toString(SessionId)).child("attendance");
        nref.setValue("closed");

    }

    @Override
    public void setTime(int time) {
       // implement it later
        Atime=new Integer(5);

    }



    @Override
    public void refresh() {

    }
}
