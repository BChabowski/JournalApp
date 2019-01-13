package bchabowski.journalapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment implements View.OnClickListener, Colourable {
    EditText pinAnswer;
    TextView pinQuestion;
    LoginFragmentsViewModel model;
    private View view;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        model = new LoginFragmentsViewModel(getActivity().getApplication());
        pinAnswer = view.findViewById(R.id.pinAnswer);
        pinQuestion = view.findViewById(R.id.pinQuestion);
        Button reset = view.findViewById(R.id.resetPassButton);
        reset.setOnClickListener(this);
        pinQuestion.setText(model.getPinQuestion());
        return view;
    }

    @Override
    public void onClick(View v){
        String answer = pinAnswer.getText().toString();
        tryToResetPassword(answer);
    }

    private void tryToResetPassword(String answer){
        boolean isAnswerCorrect = model.comparePinAnswerTo(answer);
        if(isAnswerCorrect) {
            model.resetPin();
            Intent i =new Intent(getContext(), MainActivity.class);
            startActivity(i);
        }
        else Toast.makeText(getContext(),"złaodpowiedź",Toast.LENGTH_LONG).show();
    }

    @Override
    public void setColours() {
        pinQuestion.setTextColor(model.getTextColour());
        view.setBackgroundColor(model.getBackgroundColour());
    }
}
