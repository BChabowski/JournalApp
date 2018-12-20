package bchabowski.journalapp;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


public class MainActivityModel extends AndroidViewModel {
    private DbConnectorForUser db;

    public MainActivityModel(@NonNull Application application) {
        super(application);
        db = DbConnectorForUser.getDbConnector(getApplication());
    }

    //db access methods


    public boolean isProtected() {
        User user = readUser();
        if(user==null) return false;
        String pass = user.getPinCode();
        if (!pass.equals("")) return true;
        return false;
    }

    public User readUser() {
       return db.readUser();
    }

    //options menu method

    public void chooseOnClickListener(int id, Context context){
        Intent i;
        switch(id){
            case R.id.use_pass:
                if(readUser().getPinCode().equals("")){
                    i = new Intent(getApplication(),AddPin.class);
                    context.startActivity(i);
                }
                else {
                    db.resetPin();
                }
        }
    }

}
