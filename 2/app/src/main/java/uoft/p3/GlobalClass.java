package uoft.p3;

import android.app.Application;

import java.util.Stack;

/**
 * Created by wuyue on 1/26/15.
 */
public class GlobalClass extends Application{

    public Stack<String> locationStack = new Stack<String>();
    public String lastLocation;


    public void addLocation(String location) {
        locationStack.add(location);
        savethelast(location);
    }

    public Stack<String> getStack(){
        return locationStack;
    }
    public void CleanUnSaveList(){
        locationStack.clear();
    }
    public int getStackSize(){
        return locationStack.size();
    }
    public String peekStack(){
        String temp = locationStack.peek();
        savethelast(temp);
        return locationStack.pop();
    }
    public void savethelast(String temp){
        lastLocation = temp;
    }
    public String getLastLocation(){
        return lastLocation;
    }
}
