package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Model class represents the whole data wanted to make exported file or needed to fetched from
 * existing file
 */
public class FileModel {
    /**
     * The group this file is representing its whole data
     */
    private GroupModel group;

    /**
     * represents all group members currently existing in the group
     */
    private ArrayList<GroupMember> members;

    /**
     * All sessions in this group that should be exported or imported
     */
    private ArrayList<SessionModel> sessions;

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public ArrayList<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<GroupMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        String info = "File of " + group.toString() + '\n';
        for (GroupMember oneGroupMember : members) {
            info += "For member [" + oneGroupMember.getName() + "]:\n\t";
            info += oneGroupMember.getInSessions().toString() + "\n";
        }
        return info;
    }

    public ArrayList<SessionModel> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<SessionModel> sessions) {
        this.sessions = sessions;
    }

    /**
     * This class represents one group member. Has the user model which tell who this group member
     * is, attendance grade that tell the grade of the user depending on his contribution in sessions,
     * inSessions that is list of all sessions this user in it even he is not marked attended on it.
     */
    public static class GroupMember extends UserModel {
        /**
         * sessions that this user is added to its names list either attend in it or not
         */
        private ArrayList<SessionMember> inSessions;

        private float attendanceGrade = 0.1f;

        public GroupMember(String name, String email, String id) {
            super(name, email, id);
            inSessions = new ArrayList<>();
        }

        public GroupMember(UserModel user) {
            this(user.getName(), user.getEmail(), user.getId());
        }

        public ArrayList<SessionMember> getInSessions() {
            return inSessions;
        }

        public void setInSessions(ArrayList<SessionMember> inSessions) {
            this.inSessions = inSessions;
        }

        public float getAttendanceGrade() {
            return attendanceGrade;
        }

        public void setAttendanceGrade(float attendanceGrade) {
            this.attendanceGrade = attendanceGrade;
        }
    }

    /**
     * This class represents contribution of one member in one session (attendance, notes about him at this session)
     * in addition to the session itself.
     * NOTE: make this class doesn't inherit from any classes because there is redundancy of UserModel
     * i know from group member who this user is and this is duplicated many because this is a list
     * make it only hold info about the session itself (attend - notes only)
     */
    public static class SessionMember {
        private String sessionId;
        private boolean isAttend;
        private ArrayList<NoteModel> notes;

        public SessionMember(String sessionId, boolean isAttend, List<NoteModel> notes) {
            this.sessionId = sessionId;
            this.isAttend = isAttend;
            this.notes = new ArrayList<>();
            this.notes.addAll(notes);
        }

        public SessionMember() {
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public boolean isAttend() {
            return isAttend;
        }

        public void setAttend(boolean attend) {
            isAttend = attend;
        }

        public ArrayList<NoteModel> getNotes() {
            return notes;
        }

        public void setNotes(ArrayList<NoteModel> notes) {
            this.notes = notes;
        }
    }
}
