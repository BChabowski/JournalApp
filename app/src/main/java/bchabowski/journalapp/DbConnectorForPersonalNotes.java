package bchabowski.journalapp;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DbConnectorForPersonalNotes {
    private UserDatabase db;
    private static DbConnectorForPersonalNotes connInstance;


    private DbConnectorForPersonalNotes(Context context){
        db = UserDatabase.getDb(context);
    }

    public static DbConnectorForPersonalNotes getDbConnector(Context context){
        if(connInstance==null){
            connInstance = new DbConnectorForPersonalNotes(context);
        }
        return connInstance;
    }

    public void savePersonalNote(PersonalNotes personalNote) {
        //PersonalNotes[] notes = {personalNote};
        new AsyncTask<PersonalNotes, Void, Void>() {
            @Override
            protected Void doInBackground(PersonalNotes... notes) {
                db.notesAndUserDAO().insertNote(notes[0]);
                return null;
            }
        }.execute(personalNote);
    }

    public void updatePersonalNote(PersonalNotes note){
        new AsyncTask<PersonalNotes, Void, Void>() {
            @Override
            protected Void doInBackground(PersonalNotes... notes) {
                db.notesAndUserDAO().updateNote(notes[0]);
                return null;
            }
        }.execute(note);
    }

    public PersonalNotes readPersonalNote(long entryId){
        PersonalNotes note = null;
        try {
            note = new AsyncTask<Long, Void, PersonalNotes>() {

                @Override
                protected PersonalNotes doInBackground(Long... ids) {
                    return db.notesAndUserDAO().readNoteById(ids[0]);
                }
            }.execute(entryId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(note==null)
            return new PersonalNotes(new Date(0L),"",0,0);
        else return note;
    }


    public Integer readCharCountFromDay(Date day){
        Date from = getBeginningOfTheDay(day);
        Date to = getEndOfTheDay(day);
        Integer count = 0;
        try {
            count = new AsyncTask<Date, Void, Integer>() {
                @Override
                protected Integer doInBackground(Date... dates) {
                    List<Integer> charCounts = db.notesAndUserDAO().readCharCountsByDate(dates[0],dates[1]);
                    Integer sum = 0;
                    for (int i : charCounts) {
                        sum += i;
                    }
                    return sum;
                }
            }.execute(from,to).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count;
    }

    public Integer readWordCountFromDay(Date day){
        Date from = getBeginningOfTheDay(day);
        Date to = getEndOfTheDay(day);
        Integer count = 0;
        try {
            count = new AsyncTask<Date, Void, Integer>() {
                @Override
                protected Integer doInBackground(Date... dates) {
                    List<Integer> wordCounts = db.notesAndUserDAO().readWordCountsByDate(dates[0],dates[1]);
                    Integer sum = 0;
                    for (int i : wordCounts) {
                        sum += i;
                    }
                    return sum;
                }
            }.execute(from,to).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<PersonalNotes> showAllBetween(Date from,Date to){
        Date beginningOfTheDay, endOfTheDay;
        beginningOfTheDay = getBeginningOfTheDay(from);
        endOfTheDay = getEndOfTheDay(to);

        Date[] dates = {beginningOfTheDay,endOfTheDay};

        List<PersonalNotes> notes = null;
        try {
            notes = new AsyncTask<Date, Void, List<PersonalNotes>>() {
                @Override
                protected List<PersonalNotes> doInBackground(Date... dates) {
                    return db.notesAndUserDAO().readAllNotesBetween(dates[0],dates[1]);
                }
            }.execute(dates).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(notes==null) return Collections.emptyList();
        return notes;
    }
    public List<PersonalNotes> showAll(){

        List<PersonalNotes> notes = null;
        try {
            notes = new AsyncTask<Void, Void, List<PersonalNotes>>() {
                @Override
                protected List<PersonalNotes> doInBackground(Void... voids) {
                    return db.notesAndUserDAO().readAllNotes();
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(notes==null) return Collections.emptyList();
        return notes;
    }


    public List<PersonalNotes> showNotesFromDay(Date day){
        return showAllBetween(day,day);
    }

    public List<PersonalNotes> showNotesFromMonth(Date day){
        DateHelper helper = new DateHelper(day);
        Date monthBegins, monthEnds;
        monthBegins = helper.getFirstDayOfMonth();
        monthEnds = helper.getLastDayOfMonth();
        return showAllBetween(monthBegins,monthEnds);
    }


    private Date getBeginningOfTheDay(Date day){
        DateHelper helper = new DateHelper(day);
        Date beginningOfTheDay;
        String tmp = helper.getDateWithoutHours()+" 00:00:00";
        beginningOfTheDay = helper.parseStringToDate(tmp);
        return beginningOfTheDay;
    }

    private Date getEndOfTheDay(Date day){
        DateHelper helper = new DateHelper(day);
        Date endOfTheDay;
        String tmp = helper.getDateWithoutHours()+" 23:59:59";
        endOfTheDay = helper.parseStringToDate(tmp);
        return endOfTheDay;
    }
}
