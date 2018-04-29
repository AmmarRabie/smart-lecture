package cmp.sem.team8.smarlecture.common.data.mock;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserAttendanceModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;
import cmp.sem.team8.smarlecture.model.ObjectiveModel;

/**
 * Created by AmmarRabie on 24/04/2018.
 */

public class MockRepo implements AppDataSource {

    private static MockRepo INSTANCE = null;

    public static MockRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockRepo();
        }
        return INSTANCE;
    }
    private MockRepo(){}

    @Override
    public void getUser(final String userId, final Get<UserModel> callback) {

    }

    @Override
    public void insertUser(final UserModel userModel, final Insert<Void> callback) {

    }

    @Override
    public void updateUserName(String userId, String newName, final Update callback) {

    }


    @Override
    public void listenUser(String userId, final Listen<UserModel> callback) {

    }

    @Override
    public void getSessionsForUser(String userId, Get<SessionForUserModel> callback, boolean withClosed, boolean withOpened, boolean withNotActive) {

    }

    @Override
    public void getSessionsForUser(String userId, Get<SessionForUserModel> callback) {

    }

    @Override
    public void getGroupInvitationsForUser(String userId, Get<GroupInvitationModel> callback) {
        callback.onDataFetched(new GroupInvitationModel(
                "1",
                "1",
                "CMP_SEM_Software-Engineering",
                "Ahmed Hamdy"
        ));

        callback.onDataFetched(new GroupInvitationModel(
                "2",
                "1",
                "CMP_SEM_Computer-Graphics",
                "Ahmed Hamdy"
        ));

        callback.onDataFetched(new GroupInvitationModel(
                "3",
                "2",
                "CMP_Team8-micro",
                "Ammar Alsayed"
        ));

        callback.onDataFetched(new GroupInvitationModel(
                "3",
                "2",
                "CMP_Team8-software-engineering",
                "Ammar Alsayed"
        ));
    }


    @Override
    public void getSessionsOfGroup(String groupId, Get<ArrayList<SessionModel>> callback) {

    }

    @Override
    public void inviteUserToGroup(String email, final String groupId, final Insert<UserModel> callback) {

    }


    @Override
    public void acceptFollowingGroup(String userId, String groupId, Update callback) {
        callback.onUpdateSuccess();
    }

    @Override
    public void refuseFollowingGroup(String userId, String groupId, final Update callback) {
        callback.onUpdateSuccess();
    }

/*    @Override
    public void getSessionsForUser(String userId, final Get<ArrayList<SessionForUserModel>> callback, final boolean withClosed, final boolean withOpened, final boolean withNotActive) {
        ArrayList<SessionForUserModel> mockResult = new ArrayList<>();

        mockResult.add(new SessionForUserModel("1"
                , SessionStatus.OPEN
                , AttendanceStatus.NOT_ACTIVATED
                , "Lec 5"
                , "1"
                , "CMP_SEM_computer-graphics"
                , "1"
                , "Ahmed Hamdy"));

        mockResult.add(new SessionForUserModel("2"
                , SessionStatus.CLOSED
                , AttendanceStatus.CLOSED
                , "Lec 4"
                , "1"
                , "CMP_SEM_computer-graphics"
                , "1"
                , "Ahmed Hamdy"));

        mockResult.add(new SessionForUserModel("3"
                , SessionStatus.OPEN
                , AttendanceStatus.OPEN
                , "dynamic memory"
                , "2"
                , "CMP_ACM-level-2"
                , "2"
                , "ACM Egypt"));

        mockResult.add(new SessionForUserModel("4"
                , SessionStatus.OPEN
                , AttendanceStatus.CLOSED
                , "number theory"
                , "2"
                , "CMP_ACM-level-3"
                , "2"
                , "ACM Egypt"));

        mockResult.add(new SessionForUserModel("5"
                , SessionStatus.NOT_ACTIVATED
                , AttendanceStatus.NOT_ACTIVATED
                , "making micro diagram"
                , "3"
                , "CMP_Team-8"
                , "3"
                , "Ammar Alsayed"));

        mockResult.add(new SessionForUserModel("5"
                , SessionStatus.NOT_ACTIVATED
                , AttendanceStatus.NOT_ACTIVATED
                , "Review code of micro"
                , "3"
                , "CMP_Team-8"
                , "3"
                , "Ammar Alsayed"));
        callback.onDataFetched(mockResult);
    }


    @Override
    public void getSessionsForUser(String userId, Get<ArrayList<SessionForUserModel>> callback) {
        getSessionsForUser(userId, callback, true, true, true);
    }*/


    @Override
    public void getUsersListOfGroup(String groupId, Get<ArrayList<UserAttendanceModel>> callback) {


    }

    @Override
    public void getJoinedSessionInfo(String sessionID, String groupID, Get<SessionForUserModel> callback) {

    }

    @Override
    public void listenForsessionStatus(String sessionID, Listen<String> callback) {

    }

    @Override
    public void getObjectives(String sessionID, Get<ArrayList<ObjectiveModel>> callback) {

    }

    @Override
    public void updateObjectivesRating(String sessionID, String objectiveID, Float newObjectiveRating,Integer newNumberUsersRated,Update callback) {

    }

    @Override
    public void insertObjective(String sessionID, ObjectiveModel addedObjective, Insert<Void> callback) {

    }

    @Override
    public void forget(Listen listener) {

    }

}







