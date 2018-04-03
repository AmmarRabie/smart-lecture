package cmp.sem.team8.smarlecture.auth;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import es.dmoral.toasty.Toasty;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class SignUpFragment extends Fragment implements SignUpContract.Views, View.OnClickListener {


    private SignUpContract.Actions mAction;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mConfirmPassword;


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_signup, container, false);
        mEmail = root.findViewById(R.id.signUp_email);
        mPassword = root.findViewById(R.id.signUp_pass);
        mName = root.findViewById(R.id.signUp_name);
        mConfirmPassword = root.findViewById(R.id.signUp_confirmPass);
        setHasOptionsMenu(true);

        root.findViewById(R.id.signUp_signup).setOnClickListener(this);
        return root;
    }


    @Override
    public void showOnSuccess() {
        Toasty.success(getContext(), "Signed in successfully", Toast.LENGTH_SHORT,true).show();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showErrorMessage(String cause) {
        Toasty.error(getContext(), cause, Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAction.start();
    }

    @Override
    public void showProgressIndicator() {
        // empty for now
        Toast.makeText(getContext(), "start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressIndicator() {
        // empty for now
        Toast.makeText(getContext(), "end", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        mAction.signUp(mName.getText().toString(), mEmail.getText().toString(),
                mPassword.getText().toString(), mConfirmPassword.getText().toString());
    }

    @Override
    public void setPresenter(SignUpContract.Actions presenter) {
        mAction = presenter;
    }
}
