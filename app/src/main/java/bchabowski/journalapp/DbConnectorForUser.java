package bchabowski.journalapp;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class DbConnectorForUser {
    private UserDatabase db;
    private static DbConnectorForUser connInstance;

    private DbConnectorForUser(Context context){
        db = UserDatabase.getDb(context);
    }

    public static DbConnectorForUser getDbConnector(Context context){
        if(connInstance==null){
            connInstance = new DbConnectorForUser(context);
        }
        return connInstance;
    }

    public User readUser() {
        User user = null;
        try {
            user = new AsyncTask<Void, Void, User>() {
                @Override
                protected User doInBackground(Void... voids) {
                    return db.notesAndUserDAO().readUser();
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(user==null){
            firstRun();
            readUser();
        }
        return user;
    }

    private void firstRun() {
        final User newUser = new User("","","",false,0,Color.WHITE);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.notesAndUserDAO().insertUser(newUser);

                return null;
            }
        }.execute();
    }

    public void updateUser(User user){
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... users) {
                db.notesAndUserDAO().updateUser(users[0]);
                return null;
            }
        }.execute(user);
    }

    public void resetPin(){
        User u = readUser();
        u.setPinCode("");
        u.setPinQuestion("");
        u.setPinAnswer("");
        updateUser(u);
    }



}
