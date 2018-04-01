package cmp.sem.team8.smarlecture.quickstatistics;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.joinsession.JoinedSession;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;
import cmp.sem.team8.smarlecture.session.SessionActivity;

public class QuickStatisticsActivity extends AppCompatActivity {

    private Button BeginSession;
    private Button JoinSession;
    private Context context;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_statistics);

        context = this;

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


        // TODO: This was conflict, now the same buttons has 2 diff listeners. Update it

        /*BeginSession = (Button) findViewById(R.id.button3);
        BeginSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), SessionActivity.class);
                startActivity(intent);
            }
        });

        JoinSession = (Button) findViewById(R.id.button2);
        JoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = (LayoutInflater.from(QuickStatisticsActivity.this)).inflate(R.layout.dialog, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(QuickStatisticsActivity.this);
                alert.setView(view);
                final EditText input = (EditText) view.findViewById(R.id.dialog_text);

                alert.setCancelable(true);


                alert.setPositiveButton("join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        ref = FirebaseDatabase.getInstance().getReference();
                        ref = ref.child("sessions").child(input.getText().toString());

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    Intent i = new Intent(context, JoinedSession.class);

                                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                        if (childDataSnapshot.getKey().equals("status")) {
                                            if (childDataSnapshot.getValue().equals("closed")) {
                                                Toast.makeText(context, " Session has been closed ",
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
                                    Toast.makeText(context, " Session not exists ",
                                            Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                Dialog dialogg = alert.create();
                dialogg.show();
            }
        });*/

    }


    public void testProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    public void testForceLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(getResources().getString(R.string.intentKey_forceLogin),
                true);
        startActivity(intent);
    }


    public void connectSession(View v) {
        Intent intent = new Intent(this, JoinedSession.class);
        intent.putExtra("sessionid", "123");
        intent.putExtra("groupid","id1");
        startActivity(intent);
        /*View view = (LayoutInflater.from(QuickStatisticsActivity.this)).inflate(R.layout.dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(QuickStatisticsActivity.this);
        alert.setView(view);
        final EditText input = (EditText) view.findViewById(R.id.dialog_text);
        input.setHint("enter session id");


        alert.setCancelable(true);


        alert.setPositiveButton("join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                ref = FirebaseDatabase.getInstance().getReference();
                ref = ref.child("sessions").child(input.getText().toString());

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Intent i = new Intent(context, JoinedSession.class);

                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                if (childDataSnapshot.getKey().equals("status")) {
                                    if (childDataSnapshot.getValue().equals("closed")) {
                                        Toast.makeText(context, " Session has been closed ",
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } else if (childDataSnapshot.getKey().toString().equals("group")) {
                                    i.putExtra("groupid", childDataSnapshot.getValue().toString());
                                }
                            }

                            String SessionID = input.getText().toString();
                            i.putExtra("sessionid", 123);

                            startActivity(i);
                            dialog.cancel();

                        } else {
                            Toast.makeText(context, " Session not exists ",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        Dialog dialogg = alert.create();
        dialogg.show();*/
    }

    public void beginSession(View v) {
        Intent intent = new Intent(v.getContext(), SessionActivity.class);
        startActivity(intent);
    }
}




