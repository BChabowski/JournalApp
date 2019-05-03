package bchabowski.journalapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddPin extends AppCompatActivity {
    MainActivityModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new MainActivityModel(getApplication());
        setTheme(model.getTheme());
        setContentView(R.layout.activity_add_pin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateAccountFragment createAccount = new CreateAccountFragment();
        fragmentManager.beginTransaction().replace(R.id.AddPinLayout,createAccount).commit();
    }


}
