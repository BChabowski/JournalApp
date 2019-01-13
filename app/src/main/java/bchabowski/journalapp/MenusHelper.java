package bchabowski.journalapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.Toast;


public class MenusHelper {
   private DbConnectorForUser dbForUser;
   private DbConnectorForPersonalNotes dbForNotes;
   private CharCounter counter = new CharCounter();
   private Context context;

   public MenusHelper(Context ctx){
       context = ctx;
       dbForUser = DbConnectorForUser.getDbConnector(context);
       dbForNotes = DbConnectorForPersonalNotes.getDbConnector(context);
   }

    public int getBackgroundColour(){
        return dbForUser.readUser().getBackgroundColour();
    }

    public int getTextColour(){
       int backgroundColour = dbForUser.readUser().getBackgroundColour();
        if(backgroundColour==Color.WHITE)
            return Color.BLACK;
        return Color.WHITE;
    }

    public void changeBackgroundColour(){
        User u = dbForUser.readUser();
        if(u.getBackgroundColour()==Color.WHITE){
            u.setBackgroundColour(Color.DKGRAY);
        }
        else u.setBackgroundColour(Color.WHITE);
        dbForUser.updateUser(u);
    }

    public void setDailyCharTarget(int target){

       User u = dbForUser.readUser();
       u.setCharTarget(target);
       dbForUser.updateUser(u);
    }


    public void setCharTarget(final Context currContext){
        AlertDialog.Builder alert = new AlertDialog.Builder(currContext);
        final EditText editText  = new EditText(currContext);
        editText.setKeyListener(DigitsKeyListener.getInstance(false,false));
        editText.setHint(R.string.zero_means_no_target);
        alert.setTitle(R.string.set_daily_char_target);
        alert.setView(editText);
        alert.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = editText.getText().toString();
                if(s.matches("\\d+")){
                    setDailyCharTarget(Integer.valueOf(s));
                }
                else Toast.makeText(currContext,R.string.error_setting_target,Toast.LENGTH_LONG).show();
            }
        });
        alert.show();
    }

}
