package bchabowski.journalapp;

import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class LoginFragmentsViewModel extends MainActivityModel {
    private InputMethodManager imm;
    private DbConnectorForUser db;


    public LoginFragmentsViewModel(Application application) {
        super(application);
        imm = (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);
        db = DbConnectorForUser.getDbConnector(getApplication());
    }

    public InputMethodManager getInputMethodManager() {
        return imm;
    }

    public void showKeyboard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void setPinQuestionAndAnswer(final String pin, final String question, final String answer) {
        User u = readUser();
        u.setPinCode(pin);
        u.setPinQuestion(question);
        u.setPinAnswer(answer);
        db.updateUser(u);
    }

    public void resetPin() {
        db.resetPin();
    }

    public boolean comparePinTo(String input) {

        User u = readUser();
        String pinFromTable = u.getPinCode();
        if (pinFromTable.equals(input)) return true;
        return false;
    }

    public boolean comparePinAnswerTo(final String input) {
        User u = readUser();
        if(u.getPinAnswer().equals(input)) return true;
        return false;

    }

    public String getPinQuestion() {
        User u = readUser();
        return u.getPinQuestion();
    }

}
