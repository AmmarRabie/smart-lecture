package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by ramym on 3/20/2018.
 */

public class UserAttendanceModel {
    public String Name;
    public boolean checked;
    public String key;

    public UserAttendanceModel(String name,String key, boolean checked) {
        Name = name;
        this.checked = checked;
        this.key=key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
