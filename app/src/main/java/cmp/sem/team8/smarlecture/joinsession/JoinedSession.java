package cmp.sem.team8.smarlecture.joinsession;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.StudentAttendanceAdapter;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendanceFragment;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;
import cmp.sem.team8.smarlecture.quickstatistics.QuickStatisticsActivity;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendanceFragment;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionFragment;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionPresenter;

/**
 * Created by ramym on 3/17/2018.
 */

public class JoinedSession extends AppCompatActivity {

    private Button mQuestionButton;
    private Button mAttendanceButton;
    private Button mObjectivesButton;
    private Button mStartSessionButton;

    int SessionId;

    private BeginAttendancePresenter mAttendancePresenter;
    private StartSessionPresenter    mStartSessionPresenter;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined__session);

/*        mQuestionButton = findViewById(R.id.sessionActivity_question);
        mAttendanceButton = findViewById(R.id.sessionActivity_attendance);
        mObjectivesButton = findViewById(R.id.sessionActivity_objectives);*/
        context=this;
        mAttendancePresenter = null;
        mStartSessionPresenter=null;
        //
        // begin the session with generating the session.

        /////

    }


    public void onToFragmentClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            // case R.id.sessionActivity_startsession:
            //    StartSessionFragment fragment2 = (StartSessionFragment)
            //           getSupportFragmentManager().findFragmentById(R.id.contentFrame2);
            //    fragment2 = fragment2 == null ?
            //            StartSessionFragment.newInstance()
            //            : fragment2;
            //    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

            //    if (mStartSessionPresenter == null)
            //        mStartSessionPresenter = new StartSessionPresenter(fragment2);
            //    else
            //        mStartSessionPresenter.refresh();
            //    break;

            case R.id.joinedsession_write_attendance:

                View v=(LayoutInflater.from(JoinedSession.this)).inflate(R.layout.dialog,null);

                AlertDialog.Builder alert=new AlertDialog.Builder(JoinedSession.this);
                alert.setView(v);
                final EditText input=(EditText)v.findViewById(R.id.dialog_text);
                input.setText("Enter attendance code");
                alert.setCancelable(true);

                final String SessionId=getIntent().getStringExtra("sessionid");
                final String GroupnId=getIntent().getStringExtra("groupid");

                alert.setPositiveButton("join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
                        ref = ref.child("sessions").child(SessionId);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                String Secrect="";
                                String AttendanceFlag="";
                                for (DataSnapshot children : dataSnapshot.getChildren())
                                {
                                    if (children.getKey().toString().equals("attendance"))
                                        AttendanceFlag = children.getValue().toString();
                                    else if (children.getKey().toString().equals("attendancesecrect"))
                                        Secrect = children.getValue().toString();
                                }
                                if (AttendanceFlag.equals("closed"))
                                {
                                    Toast.makeText(context,"Attendance is closed ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!Secrect.equals(input.getText().toString()))
                                {
                                    Toast.makeText(context,"Secrect is Incorrect ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                int fragmentId = R.id.joinedsession_contentFrame;
                                WriteAttendanceFragment fragment2 = (WriteAttendanceFragment)
                                        getSupportFragmentManager().findFragmentById(R.id.joinedsession_contentFrame);
                                fragment2 = fragment2 == null ?
                                        WriteAttendanceFragment.newInstance()
                                        : fragment2;
                                ActivityUtils.addFragmentToActivityByTag(getSupportFragmentManager(), fragment2, fragmentId,"writeattendance");

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });

                Dialog dialogg =alert.create();
                dialogg.show();

                //if (mAttendancePresenter == null)
                //    mAttendancePresenter = new BeginAttendancePresenter(fragment2,2790306);
                //else
                //    mAttendancePresenter.refresh();
                break;

            ///////////

        }
    }
}
