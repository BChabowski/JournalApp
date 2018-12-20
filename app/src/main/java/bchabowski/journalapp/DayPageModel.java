package bchabowski.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DayPageModel extends AndroidViewModel {
    private List<PersonalNotes> personalNotes;
    private Date currDate, beginningOfTheDay, endOfTheDay;
    private DateHelper helper;
    private UserDatabase db;


    public DayPageModel(Application application, Long dateEpoch){
        super(application);
        currDate = new Date(dateEpoch);
        helper = new DateHelper(currDate,application.getResources());
        String tmp = helper.getDateWithoutHours()+" 00:00:00";
        this.beginningOfTheDay = helper.parseStringToDate(tmp);
        tmp = helper.getDateWithoutHours()+" 23:59:59";
        endOfTheDay = helper.parseStringToDate(tmp);
        db = UserDatabase.getDb(getApplication());
    }

    public String getDay(){
        int i = Integer.valueOf(helper.getDay());
        return String.valueOf(i)+".";
    }

    public String getMonthAndYear(){
        return helper.getMonth()+"  "+helper.getYear();
    }

    public String getWeekday(){
        return helper.getWeekday();
    }

    public Long getTime(){
        return currDate.getTime();
    }


    public List<PersonalNotes> getPersonalNotesLinkedToTheDayFromDb() {
        //move to dbconnectorforPersonalNotes
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    personalNotes = db.notesAndUserDAO().readAllNotesBetween(beginningOfTheDay,endOfTheDay);
                    return null;
                }

            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return personalNotes;
    }

    public List<PersonalNotes> getPersonalNotes() {
        return personalNotes;
    }

    private void avg(){
        //prototype avg char counter, to be moved to another class
        List<Integer> ints = null;
        try {
            ints = new AsyncTask<Void,Void,List<Integer>>(){

                @Override
                protected List<Integer> doInBackground(Void... voids) {
                    return db.notesAndUserDAO().readAllCharCounts();
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer sum = 0;
        Double avg = 0.0;
        if(ints!=null) {
            for (int i : ints) {
                sum+=i;
            }
            avg = Double.valueOf(sum)/Double.valueOf(ints.size());
        }
        Toast.makeText(getApplication(),avg.toString(),Toast.LENGTH_LONG).show();
    }

    public void chooseOnClickListener(int id){
        switch(id) {
            case R.id.export_to_txt:
                Toast.makeText(getApplication(),R.string.export_to_txt,Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_colour:
                Toast.makeText(getApplication(),R.string.change_background_colour,Toast.LENGTH_SHORT).show();
                break;

        }
    }

}


