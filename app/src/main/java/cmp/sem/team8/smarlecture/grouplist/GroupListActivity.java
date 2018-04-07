package cmp.sem.team8.smarlecture.grouplist;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.joinsession.JoinedSession;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;

public class GroupListActivity extends AppCompatActivity {


    private GroupListPresenter mGroupListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        setTitle("Groups");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

        GroupListFragment grouplistfragment = (GroupListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (grouplistfragment == null) {
            grouplistfragment = GroupListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    grouplistfragment, R.id.contentFrame);
        }

        mGroupListPresenter = new GroupListPresenter(grouplistfragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_grouplist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionGroupList_joinSession:
//                return false; // make the fragment handle this instead
                joinSession();
                return true;
            case R.id.optionGroupList_profile:
                Intent profileActivity = new Intent(this, ProfileActivity.class);
                startActivity(profileActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void joinSession() {
        View view = (LayoutInflater.from(this)).inflate(R.layout.dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setTitle("Join Session");
        final EditText input = (EditText) view.findViewById(R.id.dialog_text);
        alert.setPositiveButton("join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref = ref.child("sessions").child(input.getText().toString());

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Intent i = new Intent(GroupListActivity.this, JoinedSession.class);

                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                if (childDataSnapshot.getKey().equals("status")) {
                                    if (childDataSnapshot.getValue().equals("closed")) {
                                        Toast.makeText(GroupListActivity.this, " Session has been closed ",
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } else if (childDataSnapshot.getKey().toString().equals("group")) {
                                    i.putExtra("groupid", childDataSnapshot.getValue().toString());
                                }
                            }

                            String SessionID = input.getText().toString();
                            i.putExtra("sessionid", SessionID);

                            startActivity(i);
                            dialog.cancel();

                        } else {
                            Toast.makeText(GroupListActivity.this, " Session not exists ",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        alert.show();
    }
}
