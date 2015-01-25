package SupportClass;

/**
 * Created by wuyue on 1/25/15.
 */
public class Person {
    private String name;
    private int age;
    private String food;

    public Person(String name){
        new Person(name, null, 0);
    }

    public Person(String name, String food, int age){
        this.name = name;
        this.food = food;
        this.age = age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setFood(String food){
        this.food = food;
    }
    public int getAge(){
        return this.age;
    }
    public String getName(){
        return this.name;
    }
    public String getFood(){
        return this.food;
    }
}
