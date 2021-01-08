package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Role{
    STUDENT("Student"),
    TEACHER("Teacher"),
    COORDINATOR("Coordinator"),
    ADMINISTRATOR("Administrator"),
    TECHNICAL_ADMINISTRATOR("Technical Administrator");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public static Role getRole(String value) {
        for(Role r: Role.values()) {
            if(r.roleName.equals(value)) {
                return r;
            }
        }
        return null;// not found
    }

}
