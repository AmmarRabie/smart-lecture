package cmp.sem.team8.smarlecture.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import es.dmoral.toasty.Toasty;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class LoginFragment extends Fragment implements LoginContract.Views, View.OnClickListener {

    private LoginContract.Actions mAction;
    private EditText mEmail;
    private EditText mPassword;

    private TextView mForgetPassword;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void setPresenter(LoginContract.Actions presenter) {
        mAction = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_login, container, false);
        mEmail = root.findViewById(R.id.email);
        mPassword = root.findViewById(R.id.password);
        setHasOptionsMenu(true);

        mForgetPassword = root.findViewById(R.id.loginFrag_forget_pass);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAction.forgotPassword(mEmail.getText().toString());
            }
        });

        root.findViewById(R.id.login).setOnClickListener(this);
//        root.findViewById(R.id.create_account).setOnClickListener(mSignUpListener);
        return root;
    }


    @Override
    public void showOnSuccess(String userName) {
        Toasty.normal(getContext(), "Hello " + userName, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showOnResetPasswordEmailSend() {
        Toasty.info(getContext(), "The reset email is send successfully",
                Toast.LENGTH_SHORT,true).show();
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
    public void onClick(View view) {
        mAction.login(mEmail.getText().toString(), mPassword.getText().toString());
    }
}