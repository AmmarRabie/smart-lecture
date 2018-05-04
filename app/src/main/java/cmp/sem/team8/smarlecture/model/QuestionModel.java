package cmp.sem.team8.smarlecture.model;

/**
 * Created by Loai Ali on 4/18/2018.
 */

public class QuestionModel {
    private String mSessionID;
    private  String mQuestionID;
    private String mQuestionDescription;
   // private float mObjectivesAverageRating;
   // private int mNumberofUsersRatedThisObjective;


   // public float getmObjectivesAverageRating() {
       // return mObjectivesAverageRating;
   // }

//    public void setmObjectivesAverageRating(float mObjectivesRating) {
//        this.mObjectivesAverageRating = mObjectivesRating;
//    }

//    public int getmNumberofUsersRatedThisObjective() {
//        return mNumberofUsersRatedThisObjective;
//    }

//    public void setmNumberofUsersRatedThisObjective(int mNumberofUsersRatedThisObjective) {
//        this.mNumberofUsersRatedThisObjective = mNumberofUsersRatedThisObjective;
//    }

    public String getmSessionID() {
        return mSessionID;
    }

    public void setmSessionID(String mSessionID) {
        this.mSessionID = mSessionID;
    }

    public String getmQuestionID() {
        return mQuestionID;
    }

    public void setmQuestionID(String mObjectiveID) {
        this.mQuestionID = mObjectiveID;
    }

    public String getmQuestionDescription() {
        return mQuestionDescription;
    }

    public void setmQuestionDescription(String mObjectiveDescription) {
        this.mQuestionDescription = mObjectiveDescription;
    }
}
