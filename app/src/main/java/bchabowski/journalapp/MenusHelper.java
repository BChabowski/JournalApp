package bchabowski.journalapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.TextView;
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


    public void setCharTarget(final Activity currContext, int theme){
       Integer charTarget = dbForUser.readUser().getCharThreshold();
       String currTarget = "\t"+context.getResources().getString(R.string.current_target);

        AlertDialog.Builder alert = new AlertDialog.Builder(currContext);
        final EditText editText  = new EditText(currContext);
        editText.setKeyListener(DigitsKeyListener.getInstance(false,false));
        editText.setHint(R.string.zero_means_no_target);

        alert.setTitle(R.string.set_daily_char_target);

        if(charTarget>0) alert.setMessage(currTarget+" " +charTarget.toString());

        alert.setView(editText);

        alert.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String target = editText.getText().toString();
                if(target.matches("\\d+")){
                    setDailyCharTarget(Integer.valueOf(target));
                    if(currContext.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){
                    (currContext).recreate();
                    }
                    String info = context.getResources().getString(R.string.target_set);
                    Toast.makeText(currContext,info + " " + target,Toast.LENGTH_LONG).show();

                }
                else Toast.makeText(currContext.getApplicationContext(),R.string.error_setting_target,Toast.LENGTH_LONG).show();
            }
        });
        alert.show();

    }

}
