package model;

import java.util.List;

public abstract class User implements Login {
    private String name;
    private String password;
    private List<Role> role;

    public User(String name, String password, List<Role> role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
}
