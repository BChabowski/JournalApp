package bchabowski.journalapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccount2Fragment extends CreateAccountFragment implements Colourable {
    private EditText questionField, answerField;
    private Button setPin;
    private View view;
    private LoginFragmentsViewModel model;
    private String pin;

    public CreateAccount2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_account2, container, false);
        setHasOptionsMenu(true);
        model = new LoginFragmentsViewModel(getActivity().getApplication());
        Bundle bundle = getArguments();
        pin = bundle.getString("pin");

        questionField = view.findViewById(R.id.pinQuestionField);
        answerField = view.findViewById(R.id.pinAnswerField);
        setPin = view.findViewById(R.id.setPinButton);

        questionField.requestFocus();
        model.showKeyboard();
        setPinListener();
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            CreateAccountFragment createAccount = new CreateAccountFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.AddPinLayout, createAccount).commit();
            return true;
        }
        return false;
    }

    private void setPinListener(){
        setPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionField.getText().toString();
                String answer = answerField.getText().toString();
                Toast.makeText(getContext(),pin+question+answer,Toast.LENGTH_LONG).show();
                model.setPinQuestionAndAnswer(pin,question,answer);
                model.getInputMethodManager().hideSoftInputFromWindow(v.getWindowToken(),0);
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void setColours() {
        view.setBackgroundColor(model.getBackgroundColour());
//        questionField.getRootView().setBackgroundColor(model.getBackgroundColour());
    }
}
