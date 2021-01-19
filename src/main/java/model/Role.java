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

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;

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
