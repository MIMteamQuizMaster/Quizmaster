package database.mysql;

import java.util.ArrayList;

public interface GenericDAO<T> {
    ArrayList<T> getAll();
    T getOneById(int id);
    void storeOne(T type);
}
