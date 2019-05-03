package bchabowski.journalapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {
    MainActivityModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new MainActivityModel(getApplication());
        setTheme(model.getTheme());
        setContentView(R.layout.activity_login);

        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction().replace(R.id.LoginLayout,loginFragment).commit();

    }

    @Override
    public void onBackPressed() {    }


}
