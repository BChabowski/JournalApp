package bchabowski.journalapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TagsMessageBox {
    PersonalNotes pn = PersonalNotes.getEmptyPersonalNote();
    String tags;
    EditText input;
    EditText mess;
    TextView txtv;
    Class<?> genericClass;

    public void addTagsMessagebox(final Context context,  int message, PersonalNotes note, Class<?> caller){
        pn = note;
        genericClass = caller;
        tags = pn.getTags();
        input = new EditText(context);
        input.setText(tags);
        String s = context.getResources().getString(message);
        final AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
        msgbox.setMessage(s);
        msgbox.setView(input);
        msgbox.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tags = input.getText().toString();
                pn.setTags(tags);
                txtv.setText(tags);
                //Toast.makeText(context,tags,Toast.LENGTH_LONG).show();
            }
        });
        msgbox.setNegativeButton(R.string.cancel, null);
        msgbox.create();
        msgbox.show();

    }

    public void findTagsMessagebox(final Context context, final int message, final Class<?> activityClass){

        input = new EditText(context);
        final AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
        msgbox.setTitle(message);
        msgbox.setView(input);
        msgbox.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tags = input.getText().toString();
                String trimmed = tags.trim();
//                if(trimmed.equals("")){
//                    Toast.makeText(context.getApplicationContext(),R.string.empty_tags_search_field,Toast.LENGTH_LONG).show();
//                }
//                else {
                    Intent i = new Intent(context, activityClass);
                    i.putExtra("tagsString", trimmed);
                    context.startActivity(i);
//                }
            }
        });
//        msgbox.setNegativeButton(R.string.cancel, null);
        msgbox.create();
        msgbox.show();

    }

    public String getTags(){
        return tags;
    }

}
