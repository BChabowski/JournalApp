package bchabowski.journalapp;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivityModel extends AndroidViewModel {
    private DbConnectorForUser dbU;
    private DbConnectorForPersonalNotes dbPN;
    private Date date;
    private DateHelper helper;
    private MenusHelper menusHelper;
    private CharCounterWithDbConn counter;

    public MainActivityModel(@NonNull Application application) {
        super(application);
        dbU = DbConnectorForUser.getDbConnector(getApplication());
        dbPN = DbConnectorForPersonalNotes.getDbConnector(getApplication());
        date = new Date();
        helper = new DateHelper(date,application.getResources());
        //getAppContext???
        menusHelper = new MenusHelper(application);
        counter = new CharCounterWithDbConn(dbPN);
    }

    //dbU access methods


    public boolean isProtected() {
        User user = readUser();
        if(user==null) return false;
        String pass = user.getPinCode();
        if (!pass.equals("")) return true;
        return false;
    }

    public void setDate(Date date){
        this.date = date;
        helper.setDate(date);
    }

    public String getMonthNameAndYear(){
        return helper.getMonthName()+"  "+helper.getYear();
    }

    public Date createDateWithCurrentHour(Date clicked){
        helper.setDate(clicked);
        String dateString = helper.getDateWithoutHours();
        Date currDate = new Date();
        helper.setDate(currDate);
        dateString += " " + helper.getHour();
        Date finalDate = helper.parseStringToDate(dateString);
        return finalDate;
    }

    public User readUser() {
       return dbU.readUser();
    }

    //options menu method

    public void usePass(Context context){
        Intent i;
        if(readUser().getPinCode().equals("")){
            i = new Intent(getApplication(),AddPin.class);
            context.startActivity(i);
        }
        else {
            dbU.resetPin();
        }
    }

    public void setCharTarget(Context context){
        menusHelper.setCharTarget(context);
    }

    public int getCharTarget(){
        return dbU.readUser().getCharThreshold();
    }

    public boolean isCharTargetSet(){
        if(getCharTarget()>0)
            return true;
        return false;
    }

    public ArrayList<Integer> getCounts(Date firstOfMonth) {
        return counter.getAllCharCountsFromMonth(firstOfMonth);
    }

    public Event greenEvent(int day){
        return new Event(Color.GREEN,createDateFromInt(day).getTime());
    }
    public Event redEvent(int day){
        return new Event(Color.RED,createDateFromInt(day).getTime());
    }
    public Event blueEvent(int day){
        return new Event(Color.BLUE,createDateFromInt(day).getTime());
    }

    private Date createDateFromInt(int day){
        String dateString = day+"/"+getMonthAndYear();
        return helper.parseStringWithoutHoursToDate(dateString);
    }

    private String getMonthAndYear(){
        return helper.getMonth()+"/"+helper.getYear();
    }


    public void changeBackgroundColour(){
        menusHelper.changeBackgroundColour();
    }

    protected int getBackgroundColour(){
        return menusHelper.getBackgroundColour();
    }

    public int getBackgroundColourForMain() {
        if(menusHelper.getBackgroundColour()== Color.DKGRAY)
            return Color.GRAY;
        return Color.WHITE;
    }

    public int getTextColour(){
        return menusHelper.getTextColour();
    }

}
