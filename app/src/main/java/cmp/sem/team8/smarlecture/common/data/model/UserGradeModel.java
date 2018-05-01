package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by ramym on 5/1/2018.
 */

public class UserGradeModel extends UserModel {

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
}
