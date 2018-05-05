package cmp.sem.team8.smarlecture.common.data.mock;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
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
    private ArrayList<UserModel> users;
    private ArrayList<SessionModel> sessions;
    private ArrayList<GroupModel> groups;

    private MockRepo() {
        users = new ArrayList<>();
        sessions = new ArrayList<>();
        groups = new ArrayList<>();
        insertDummyUsers();
        insertDummyGroups();
        insertDummySessions();
    }

    public static MockRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockRepo();
        }
        return INSTANCE;
    }

    private void insertDummySessions() {
        sessions.add(new SessionModel(
                "L4-1",
                "L4",
                AttendanceStatus.OPEN,
                SessionStatus.OPEN,
                "Abdomen"
        ));
        sessions.add(new SessionModel(
                "L4-2",
                "L4",
                AttendanceStatus.CLOSED,
                SessionStatus.OPEN,
                "NueroAnatomy"
        ));
        sessions.add(new SessionModel(
                "L4-3",
                "L4",
                AttendanceStatus.CLOSED,
                SessionStatus.CLOSED,
                "Thorax"
        ));
    }

    private void insertDummyGroups() {
        // Ahmed Hamdy
        groups.add(new GroupModel(
                "CMP2.1-SEM-Computer Graphics",
                "L1",
                "11" // Ahmed hamdy
        ));
        groups.add(new GroupModel(
                "CMP2.2-SEM-software engineering",
                "L2",
                "11" // Ahmed hamdy
        ));
        groups.add(new GroupModel(
                "CMP4.1-CD-software engineering",
                "L3",
                "11" // Ahmed hamdy
        ));
        // medhat
        groups.add(new GroupModel(
                "Kasr Alainy - second",
                "L4",
                "15" // medhat
        ));

        // Teams and out of education groups
        // [TODO]: Insert here dummy data of groups, delete your name after your additions {Ramy - Loai - Youssry}
        groups.add(new GroupModel(
                "CMP2.2-Team8 micro",
                "S1",
                "1" // Ammar Alsayed
        ));
        groups.add(new GroupModel(
                "CMP2.2-Team8 software engineering",
                "S2",
                "1" // Ammar Alsayed
        ));
        groups.add(new GroupModel(
                "CMP2.2-Team8 Signals",
                "S3",
                "1" // Ammar Alsayed
        ));
        groups.add(new GroupModel(
                "Family-gom3h Dars Tafsir",
                "S4",
                "1" // Ammar Alsayed
        ));
    }

    private void insertDummyUsers() {
        users.add(new UserModel(
                "Ammar Alsayed",
                "ammaralsayed55@gmail.com",
                "1"
        ));

        users.add(new UserModel(
                "Loai Ali",
                "loaiali@gmail.com",
                "2"
        ));

        users.add(new UserModel(
                "Ramy Mohammed Saied",
                "ramy.m.saied@gmail.com",
                "3"
        ));

        users.add(new UserModel(
                "Mahmoud Youssry",
                "mahmoudyoussry@gmail.com",
                "4"
        ));

        users.add(new UserModel(
                "Omar Samir Galal",
                "omarsamir@gmail.com",
                "5"
        ));

        users.add(new UserModel(
                "Abdo Kaseb",
                "abdokaseb@gmail.com",
                "6"
        ));

        users.add(new UserModel(
                "karim Omar",
                "karimomar@gmail.com",
                "7"
        ));

        users.add(new UserModel(
                "Ahmed Maher",
                "ahmedmaher@gmail.com",
                "8"
        ));

        users.add(new UserModel(
                "Ahmed Ibrahim",
                "ahmedibrahim@gmail.com",
                "9"
        ));

        users.add(new UserModel(
                "Ibrahim Akrab",
                "ibrahimakrab@gmail.com",
                "10"
        ));

        users.add(new UserModel(
                "Ahmed Hamdy",
                "ahmedhamdy@gmail.com",
                "11"
        ));

        users.add(new UserModel(
                "Magda Fayek",
                "magdafayek@gmail.com",
                "12"
        ));

        users.add(new UserModel(
                "Hesham Ibrahim",
                "heshamibrahim@gmail.com",
                "13"
        ));

        users.add(new UserModel(
                "Ahmed Gomaa",
                "ahmedgomma@gmail.com",
                "14"
        ));

        users.add(new UserModel(
                "medhat morssy",
                "medhatmorssy@gmail.com",
                "15"
        ));
    }

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
    public void getUsersListOfGroup(String groupId, Get<ArrayList<InvitedUserModel>> callback) {

    }

    @Override
    public void getUsersListOfGroupTemp(String groupId, Get<ArrayList<UserAttendanceModel>> callback) {

    }

    @Override
    public void getSessionStatus(String sessionId, Get<SessionStatus> callback) {
    }

    @Override
    public void getJoinedSessionInfo(String sessionID, String groupID, Get<SessionForUserModel> callback) {}
  
    @Override
    public void setAttendanceStatus(String sessionId, AttendanceStatus status, Update callback) {
    }

    @Override

    public void listenForSessionStatus(String sessionID, Listen<String> callback) {

    }

    @Override
    public void getObjectives(String sessionID, Get<ArrayList<ObjectiveModel>> callback) {
    }

    public void setSessionSecret(String sessionId, String secret, Update callback) {

    }

    @Override
    public Listen ListenSessionMembers(String sessionId, Listen<MemberModel> callback) {

        ArrayList<NoteModel> notes = new ArrayList<>();
        notes.add(new NoteModel(
                "1",
                "tutorial bonus += 2"
        ));
        notes.add(new NoteModel(
                "2",
                "tutorial bonus += 1"
        ));
        notes.add(new NoteModel(
                "3",
                "tutorial bonus += 1.5"
        ));

        callback.onDataReceived(new MemberModel(
                users.get(1),
                true,
                notes
        ));

        callback.onDataReceived(new MemberModel(
                users.get(0),
                false,
                notes
        ));

        callback.onDataReceived(new MemberModel(
                users.get(2),
                false,
                notes
        ));

        return callback;
    }

    @Override
    public void setMemberAttendance(String sessionId, String memberId, boolean isAttend, Update callback) {
    }

    @Override
    public void updateObjectivesRating(String sessionID, String objectiveID, Float newObjectiveRating,Integer newNumberUsersRated,Update callback) {
    }
  
    @Override
    public void addNote(String sessionId, String memberId, String noteText, Insert<NoteModel> callback) {
    }

    @Override
    public void insertObjective(String sessionID, String addedObjectiveDescription,boolean isOffline, Insert<ObjectiveModel> callback) {
    }
  
    @Override 
    public void deleteNote(String sessionId, String memberId, String noteId, Delete callback) {
    }

    @Override
    public void editObjective(String objectiveID, String sessionID,String objectiveDescription,boolean isOffline,Update callback) {

    }

    @Override
    public void deleteObjective(String objectiveID, String sesisonID, boolean isOffline, Delete callback) {

    }

    @Override
    public void getGroupSessions(String groupId, Get<ArrayList<SessionModel>> callback) {

    }

    @Override
    public void addSession(String groupId,String sessionId,String sessionName,Insert<Void> callback) {

    }

    @Override
    public void editSession(String sessionId, String sessionName,boolean isOffline, Update callback) {

    }

    @Override
    public void deleteSession(String sessoinId, boolean isOffline, Delete callback) {

    }

    @Override
    public void deleteGroupById(String groupId, boolean isOffline,Delete callback) {

    }

    @Override
    public void addGroup(String userId, String groupName,boolean isOffline, Insert<String> callback) {

    }

    @Override
    public void updateGroup(String groupId, String groupName, boolean isOffline, Update callback) {

    }

    @Override
    public void getGroupId(String sessionId, Get<String> callback) {

    }

    @Override
    public void getGroupsForUser(String userId, Get<ArrayList<GroupModel>> callback) {

    }

    @Override
    public void forget(Listen listener) {
    }


}







