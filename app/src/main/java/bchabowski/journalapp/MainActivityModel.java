package bchabowski.journalapp;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class MainActivityModel extends AndroidViewModel {
    DateHelper helper;

    public MainActivityModel(@NonNull Application application) {
        super(application);
    }
}
