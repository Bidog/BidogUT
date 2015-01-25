package SupportClass;

import java.util.ArrayList;

/**
 * Created by wuyue on 1/25/15.
 */
public class memoryService {
    private final ArrayList<Person> unSavePersonList = new ArrayList();

    public void addPerson(Person person){
        this.unSavePersonList.add(person);
    }
}
