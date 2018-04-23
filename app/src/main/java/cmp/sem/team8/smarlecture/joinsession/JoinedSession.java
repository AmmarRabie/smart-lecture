package cmp.sem.team8.smarlecture.joinsession;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.rateobjectives.RateObjectivesActivity;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

/**
 * Created by ramym on 3/17/2018.
 */

public class JoinedSession extends AppCompatActivity {

    String GroupID;
    String SessionId;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem WriteAttendanceTab;
    PagerAdapter pageAdapter;
    private BeginAttendancePresenter mAttendancePresenter;
    private SessionInfoPresenter mSessionInfoPresenter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_joined__session);

        context = this;

        mAttendancePresenter = null;

        mSessionInfoPresenter = null;

        SessionId=getIntent().getStringExtra(getString(R.string.IKey_sessionId));

        GroupID = getIntent().getStringExtra(getString(R.string.IKey_groupId));

        tabLayout = findViewById(R.id.joined_session_tablayout);

        WriteAttendanceTab = findViewById(R.id.joined_session_attendance);

        viewPager = findViewById(R.id.joined_session_viewPager);

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),SessionId);

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        /////

    }


   /* public void onToFragmentClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            // case R.id.sessionActivity_startsession:
            //    SessionInfoFragment fragment2 = (SessionInfoFragment)
            //           getSupportFragmentManager().findFragmentById(R.id.contentFrame2);
            //    fragment2 = fragment2 == null ?
            //            SessionInfoFragment.newInstance()
            //            : fragment2;
            //    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

            //    if (mSessionInfoPresenter == null)
            //        mSessionInfoPresenter = new SessionInfoPresenter(fragment2);
            //    else
            //        mSessionInfoPresenter.refresh();
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
    }*/

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i =new Intent(context, RateObjectivesActivity.class);
        i.putExtra("session_id",SessionId);
        startActivity(i);
    }*/
}
