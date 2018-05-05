package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by Loai Ali on 4/18/2018.
 */

public class ObjectiveModel {
    private String mSessionID;
    private  String mObjectiveID;
    private String mObjectiveDescription;
    private float mObjectivesAverageRating;
    private int mNumberofUsersRatedThisObjective;


    public float getmObjectivesAverageRating() {
        return mObjectivesAverageRating;
    }

    public void setmObjectivesAverageRating(float mObjectivesRating) {
        this.mObjectivesAverageRating = mObjectivesRating;
    }

    public int getmNumberofUsersRatedThisObjective() {
        return mNumberofUsersRatedThisObjective;
    }

    public void setmNumberofUsersRatedThisObjective(int mNumberofUsersRatedThisObjective) {
        this.mNumberofUsersRatedThisObjective = mNumberofUsersRatedThisObjective;
    }

    public String getmSessionID() {
        return mSessionID;
    }

    public void setmSessionID(String mSessionID) {
        this.mSessionID = mSessionID;
    }

    public String getmObjectiveID() {
        return mObjectiveID;
    }

    public void setmObjectiveID(String mObjectiveID) {
        this.mObjectiveID = mObjectiveID;
    }

    public String getmObjectiveDescription() {
        return mObjectiveDescription;
    }

    public void setmObjectiveDescription(String mObjectiveDescription) {
        this.mObjectiveDescription = mObjectiveDescription;
    }
}
