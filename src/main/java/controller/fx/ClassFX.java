package controller.fx;

import javafx.beans.property.*;
import model.Class;
import model.Teacher;

public class ClassFX {
    private Class c;

    public ClassFX(Class c) {
        this.c = c;
    }

    public Class getClassObject(){
        return this.c;
    }

    public int getDbId() {
        return new SimpleIntegerProperty(this.c.getDbId()).get();
    }

    public IntegerProperty dbIdProperty() {
        return new SimpleIntegerProperty(this.c.getDbId());
    }

    public void setDbId(int dbId) {
        this.c.setDbId(dbId);
    }

    public String getName() {
        return new SimpleStringProperty(this.c.getName()).get();
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(this.c.getName());
    }

    public void setName(String name) {
        this.c.setName(name);
    }

    public Teacher getTeacher() {
        return (Teacher) new SimpleObjectProperty<>(this.c.getTeacher()).get();
    }

    public void setTeacher(Teacher teacher) {
        this.c.setTeacher(teacher);
    }

}
