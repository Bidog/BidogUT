package SupportClass;

/**
 * Created by wuyue on 1/25/15.
 */
public class Person {
    private String name;
    private String age;
    private String food;

    public Person(String name){
        new Person(name, null, "0");
    }

    public Person(String name, String food, String age){
        this.name = name;
        this.food = food;
        this.age = age;
    }

    public void setAge(String age){
        this.age = age;
    }

    public void setFood(String food){
        this.food = food;
    }
    public String getAge(){
        return this.age;
    }
    public String getName(){
        return this.name;
    }
    public String getFood(){
        return this.food;
    }
    public String toString(){
        return " Name : "+this.name + "     Age : "+this.age + "\n Favorite Food: " + this.food;
    }
}
