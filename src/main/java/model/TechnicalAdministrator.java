package model;

import java.util.List;

public class TechnicalAdministrator extends User {

    public TechnicalAdministrator(int userId, String firstname, String lastname) {
        super(userId, firstname, lastname);
    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }

}
