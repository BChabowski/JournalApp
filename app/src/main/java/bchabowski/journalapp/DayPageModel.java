package bchabowski.journalapp;

import android.app.Application;

import java.util.Date;
import java.util.List;

public class DayPageModel extends MainActivityModel {
    private List<PersonalNotes> personalNotes;
    private Date currDate;
    private DateHelper helper;
    private MenusHelper menusHelper;
    private DbConnectorForPersonalNotes dbC;


    public DayPageModel(Application application, Long dateEpoch){
        super(application);
        currDate = new Date(dateEpoch);
        helper = new DateHelper(currDate,getApplication().getResources());
        menusHelper = new MenusHelper(application);
        dbC = DbConnectorForPersonalNotes.getDbConnector(getApplication());
    }

    public String getDay(){
        return helper.getDay();
    }


    public String getWeekday(){
        return helper.getWeekday();
    }

    public Long getTime(){
        return currDate.getTime();
    }


    public List<PersonalNotes> getPersonalNotesLinkedToTheDayFromDb() {
        personalNotes = dbC.showNotesFromDay(currDate);
        return personalNotes;
    }

    public List<PersonalNotes> getPersonalNotes() {
        return personalNotes;
    }


}


