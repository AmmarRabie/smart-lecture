package cmp.sem.team8.smarlecture.group.sessionslist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cmp.sem.team8.smarlecture.grouplist.GroupListContract;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.*;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class SessionListPresenter implements SessionListContract.Actions {
    private static final String TAG = "SessionListPresenter";

    private DatabaseReference mGroupRef;
    private final String GROUP_ID;
    private AppDataSource mDataSource;
    private SessionListContract.Views mView;

    private static final int minId = 0;
    private static final int maxId = 10000000;


    public SessionListPresenter(SessionListContract.Views view, final String groupID,AppDataSource dataSource) {
        mView = view;
        mDataSource=dataSource;
        GROUP_ID = groupID;
        mGroupRef = null;
        if (groupID == null) {
            Log.e(TAG, "StudentListPresenter: group passed as null");
            return;
        }
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.handleOfflineStates();


        FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            mGroupRef = FirebaseDatabase.getInstance().
                                    getReference(SessionEntry.KEY_THIS);
                            addSessions();
                        } else {
                            Log.e(TAG, "onDataChange: the Gruop presenter is called with invalid group id");
                            mView.showOnErrorMessage("group doesn't exist");
                            mGroupRef = null;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mGroupRef = null;
                        mView.showOnErrorMessage(databaseError.getMessage());
                    }
                });
    }


    private String generateUniqueId() {
        Random rand = new Random(System.currentTimeMillis());
        //get the range, casting to long to avoid overflow problems
        long range = (long) maxId - (long) minId + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * rand.nextDouble());
        Integer randomNumber = (int) (fraction + minId);

        return randomNumber.toString();
    }


    @Override
    public void addSession(final String sessionName) {
        if (sessionName == null || sessionName.isEmpty())
            mView.showOnErrorMessage("Session can't have an empty name");

        final String sessionID = generateUniqueId();
        mDataSource.addSession(GROUP_ID, sessionID, sessionName, new AppDataSource.Insert<Void>() {
            @Override
            public void onDataInserted(Void feedback) {
                mView.OnAddSuccess(new cmp.sem.team8.smarlecture.common.data.model.SessionModel(sessionID,GROUP_ID, AppDataSource.AttendanceStatus.NOT_ACTIVATED, AppDataSource.SessionStatus.NOT_ACTIVATED,sessionName));
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });
      /*  DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS);
        DatabaseReference newsessionRef = sessionsRef.child(sessionID);
        //final String sessionID=newsessionRef.getKey();
        final cmp.sem.team8.smarlecture.common.data.model.SessionModel addedSession = new cmp.sem.team8.smarlecture.common.data.model.SessionModel();
        addedSession.setSessionStatus(AppDataSource.SessionStatus.NOT_ACTIVATED);
       // addedSession.setmStudentsList(new ArrayList<String>());
        addedSession.setId(sessionID);
        addedSession.setAttendanceStatus(AppDataSource.AttendanceStatus.NOT_ACTIVATED);
        addedSession.setForGroup(GROUP_ID);
        addedSession.setName(sessionName);


        newsessionRef.child(SessionEntry.KEY_FOR_GROUP_ID).setValue(addedSession.getForGroup());
        newsessionRef.child(SessionEntry.KEY_FOR_SESSION_NAME_).setValue(addedSession.getName());
        newsessionRef.child(SessionEntry.KEY_SESSION_STATUS).setValue(addedSession.getSessionStatus());
        newsessionRef.child(SessionEntry.KEY_ATTENDANCE_STATUS).setValue(addedSession.getAttendanceStatus()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    mView.OnAddSuccess(addedSession);
                else {
                    if (mView != null)
                        mView.showOnErrorMessage(task.getException().toString());
                }
            }
        });*/
    }

    @Override
    public void editSession(final String sessionName, final String sessionID) {
        if (mGroupRef == null) return;
        if (sessionID == null || sessionID.isEmpty()) {
            mView.showOnErrorMessage("Session doesn't have an ID ");
            return;
        }
        if (sessionName == null || sessionName.isEmpty()) {
            mView.showOnErrorMessage("Session can't have an empty name");
            return;

        }
        mDataSource.editSession(sessionID, sessionName, mView.getOfflineState(), new AppDataSource.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.OnEditSuccess(sessionID,sessionName);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });
      /*  final boolean isOffline = mView.getOfflineState();
        if (isOffline) {
            mView.OnEditSuccess(sessionID, sessionName);
        }
        mGroupRef.child(sessionID).child(SessionEntry.KEY_FOR_SESSION_NAME_).setValue(sessionName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            if (!isOffline)
                                mView.OnEditSuccess(sessionID, sessionName);
                            else mView.showOnErrorMessage(task.getException().getMessage());
                    }
                });*/
    }

    @Override
    public void deleteSession(final String sessionID) {
        if (sessionID == null || sessionID.isEmpty()) {
            mView.showOnErrorMessage("This Session doesn't have an ID");
            return;
        }
        mDataSource.deleteSession(sessionID, mView.getOfflineState(), new AppDataSource.Delete() {
            @Override
            public void onDeleted() {
                mView.OnDeleteSuccess(sessionID);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });
      /*  final boolean isOffline = mView.getOfflineState();
        if (isOffline)
            mView.OnDeleteSuccess(sessionID);
        mGroupRef.child(sessionID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!isOffline)
                        mView.OnDeleteSuccess(sessionID);
                } else {
                    mView.showOnErrorMessage(task.getException().getMessage());
                }
            }
        });
*/

    }

    private void addSessions() {
        if (mGroupRef == null) {
            return;
        }

        mDataSource.getGroupSessions(GROUP_ID, new AppDataSource.Get<ArrayList<cmp.sem.team8.smarlecture.common.data.model.SessionModel>>() {
            @Override
            public void onDataFetched(ArrayList<cmp.sem.team8.smarlecture.common.data.model.SessionModel> data) {
                mView.showSessionsList(data);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });
       /* Query groupSessions = FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS).
                orderByChild(SessionEntry.KEY_FOR_GROUP_ID).equalTo(GROUP_ID);


        groupSessions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SessionModel> sessionsList = new ArrayList<SessionModel>();

                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists())
                        continue;
                    String sessionId = sessionSnapshot.getKey();
                    SessionModel sessionModel = sessionSnapshot.getValue(SessionModel.class);
                    sessionModel.setmSessionID(sessionId);

                    sessionModel.setmName(sessionSnapshot.child(SessionEntry.KEY_FOR_SESSION_NAME_).getValue(String.class));
                    sessionModel.setmSessionStatus(sessionSnapshot.child(SessionEntry.KEY_SESSION_STATUS).getValue(String.class));

                    sessionModel.setmGroupID(GROUP_ID);
                    sessionModel.setmAttendanceStatus(sessionSnapshot.child(SessionEntry.KEY_ATTENDANCE_STATUS).getValue(String.class));

                    HashMap<String, String> list = (HashMap<String, String>) sessionSnapshot.child(SessionEntry.KEY_NAMES_LIST.toString()).getValue();
                    //if students list is empty list will be equal to null
                    if (list != null) {
                        ArrayList<String> students = new ArrayList<>(list.values());
                        sessionModel.setmStudentsList(students);
                    } else {
                        sessionModel.setmStudentsList(new ArrayList<String>());

                    }
                    //sessionModel.setmStudentsList(sessionSnapshot.child(SessionEntry.KEY_NAMES_LIST).getValue(ArrayList.class));
                    sessionsList.add(sessionModel);
                }
                mView.showSessionsList(sessionsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null)
                    mView.showOnErrorMessage(databaseError.getMessage());
            }
        });

/*
        mGroupRef.child(SessionEntry.KEY_THIS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, Object>> listNames = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> listStatus = new ArrayList<HashMap<String, Object>>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists())
                        continue;
                    if(GROUP_ID.equals(child.child(SessionEntry.KEY_FOR_GROUP_ID))){
                        String key=child.getKey();
                        String name=child.getKey();
                        String status=mGroupRef.child(SessionEntry.);

                    }
                    String key = child.getKey();
                    String name = child.getValue(String.class);
                    HashMap<String, Object> thisStudent = new HashMap<>();
                    thisStudent.put("key", key);
                    thisStudent.put("name", name);
                    list.add(thisStudent);
                }
                mView.showSessionsList(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void end() {

    }
}
