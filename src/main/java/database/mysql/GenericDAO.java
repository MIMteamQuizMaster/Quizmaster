package database.mysql;

import java.util.ArrayList;

public interface GenericDAO<T> {
    public ArrayList<T> getAll();
    public T getOneById(int id);
    public void storeOne(T type);
}
