package model;

import java.util.List;

public class Coordinator extends Teacher implements Login {
    public Coordinator(String name, String password, List<Role> role) {
        super(name, password, role);
    }

}
