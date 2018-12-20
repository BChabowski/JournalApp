package bchabowski.journalapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountFragment extends Fragment {
    private EditText pinField, repeatPinField;
    private String pinCode;
    private LoginFragmentsViewModel model;

    public CreateAccountFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        model = new LoginFragmentsViewModel(getActivity().getApplication());
        setHasOptionsMenu(true);

        pinField = view.findViewById(R.id.pinField);
        repeatPinField = view.findViewById(R.id.repeatPinField);

        pinField.requestFocus();
        model.showKeyboard();
        observePinFields();
        User u = model.readUser();
        Toast.makeText(getContext(),u.getPinCode(),Toast.LENGTH_LONG).show();
        return view;
    }

    private void observePinFields(){
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    pinCode = s.toString();
                    //method to unfocus pinCode. Needed to hide last digit
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            repeatPinField.requestFocus();
                        }
                    },30);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        repeatPinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==4) {
                String secondPinCode = s.toString();

                if (pinCode.equals(secondPinCode)) {
                    CreateAccount2Fragment createAccount2 = new CreateAccount2Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("pin",pinCode);
                    createAccount2.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.AddPinLayout, createAccount2).commit();
                } else {
                    Toast.makeText(getContext(), R.string.pins_dont_match, Toast.LENGTH_LONG).show();
                }
            }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }


}
