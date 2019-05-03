package bchabowski.journalapp;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportToTxt {


    private String prepareFile(PersonalNotes note) {
        DateHelper helper = new DateHelper(note.getTimestamp());
        StringBuilder builder = new StringBuilder();
        builder.append(helper.getDate());
        if (!note.getTags().equals("")) {
            builder.append("\n");
            builder.append("#: " + note.getTags());
        }
        builder.append("\n\n");
        builder.append(note.getContent());
        return builder.toString();
    }


    public void exportToTxt(Context context, PersonalNotes note) {
        String toExport = prepareFile(note);
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File path = new File(Environment.getExternalStorageDirectory() + File.separator + "Journal App");
        if (!path.isDirectory()) path.mkdir();
        File file = new File(path, note.getTimestamp().toString() + ".txt");

        try {

            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(toExport);
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(context.getApplicationContext(), R.string.saved_to_txt, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
