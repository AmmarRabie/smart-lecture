package cmp.sem.team8.smarlecture.common.data.model;

import android.support.annotation.NonNull;

/**
 * Created by ramym on 5/1/2018.
 */

public class UserGradeModel extends UserModel implements Comparable<UserGradeModel> {

    String grade;

    public UserGradeModel(String name, String email, String id,String grade) {
        super(name, email, id);
        this.grade=grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public int compareTo(@NonNull UserGradeModel o) {
        return (Integer.parseInt(this.getGrade())-Integer.parseInt(o.getGrade()));
    }
}
