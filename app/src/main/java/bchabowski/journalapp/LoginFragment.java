package bchabowski.journalapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements Colourable {
    private EditText pinField;
    private TextView forgottenPassword;
    private LoginFragmentsViewModel model;
    private String pin;
    private View view;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        model = new LoginFragmentsViewModel(getActivity().getApplication());
        pinField = view.findViewById(R.id.pinField);
        forgottenPassword = view.findViewById(R.id.forgottenPassword);

        pinField.requestFocus();
        model.showKeyboard();
        observePinField();
        setForgottenPassListener();
        return view;
    }

    private void observePinField() {
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    pin = pinField.getText().toString();

                    tryToLogIn(pin);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void tryToLogIn(String pinCode) {
        boolean isPinCorrect = model.comparePinTo(pinCode);
        if (isPinCorrect) {
            model.getInputMethodManager().hideSoftInputFromWindow(view.getWindowToken(), 0);
            Intent i = new Intent(getContext(), MainActivity.class);
            i.putExtra("pass_ok", "");
            startActivity(i);
        } else
            Toast.makeText(getContext().getApplicationContext(), R.string.invalid_pin, Toast.LENGTH_LONG).show();
    }

    private void setForgottenPassListener() {
        forgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment resetPassword = new ResetPasswordFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.LoginLayout, resetPassword).commit();
            }
        });
    }


    @Override
    public void setColours() {
        forgottenPassword.setTextColor(model.getTextColour());
        view.setBackgroundColor(model.getBackgroundColour());
//        forgottenPassword.getRootView().setBackgroundColor(model.getBackgroundColour());
    }
}
