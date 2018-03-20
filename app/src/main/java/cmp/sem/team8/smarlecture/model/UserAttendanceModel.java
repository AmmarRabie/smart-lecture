package cmp.sem.team8.smarlecture.model;

/**
 * Created by ramym on 3/20/2018.
 */

public class UserAttendanceModel {
    public String Name;
    public boolean checked;

    public UserAttendanceModel(String name, boolean checked) {
        Name = name;
        this.checked = checked;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
