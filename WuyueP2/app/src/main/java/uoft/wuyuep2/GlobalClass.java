package uoft.wuyuep2;

import android.app.Application;

import java.util.ArrayList;

import SupportClass.Person;

/**
 * Created by wuyue on 1/26/15.
 */
public class GlobalClass extends Application{
    public ArrayList<Person> unSavePersonList = new ArrayList<Person>();

//    public void addPerson


    public void addPerson(Person somePerson) {
        unSavePersonList.add(somePerson);
    }

    public ArrayList<Person> getUnSaveList(){
        return unSavePersonList;
    }
    public void CleanUnSaveList(){
        unSavePersonList.clear();
    }
}
