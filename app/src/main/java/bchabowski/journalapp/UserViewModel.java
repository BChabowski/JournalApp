package bchabowski.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;


public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<User> user;
    private User u;
    private boolean isSigned = false;
    private UserDatabase db = UserDatabase.getDb(getApplication());


    public UserViewModel(@NonNull Application application) {
        super(application);
    }



    public User getUser(){
        loadUser();
        return u;
    }

    private void loadUser(){
        u = db.notesAndUserDAO().readUser();
    }

    public boolean isSignedUp(){

        int i = db.notesAndUserDAO().isUserCreated();
        if(i>0) isSigned = true;
        return isSigned;
    }

    public void addUser(String login, String pass, String favaut){

    }


    public boolean resetPassword(String typedFavouriteAuthor){

        if(true){
            return true;
        }
        return false;
    }

    public boolean login(String login, String password){
        User userFromTable = db.notesAndUserDAO().readUser();


        return false;
    }
}
