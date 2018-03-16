package cmp.sem.team8.smarlecture.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class LoginFragment extends Fragment implements LoginContract.Views, View.OnClickListener {

    private LoginContract.Actions mPresenter;
    private EditText mEmail;
    private EditText mPassword;

    private Button mForgetPassword;

    private View.OnClickListener mSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), SignupActivity.class);
            startActivityForResult(intent, LoginActivity.RC_SIGN_UP);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    };

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void setPresenter(LoginContract.Actions presenter) {
        mPresenter = presenter;
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
                mPresenter.forgotPassword(mEmail.getText().toString());
            }
        });

        root.findViewById(R.id.login).setOnClickListener(this);
        root.findViewById(R.id.create_account).setOnClickListener(mSignUpListener);
        return root;
    }


    @Override
    public void showOnSuccess() {
        Toast.makeText(getContext(), "Signed in successfully", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showOnResetPasswordEmailSend() {
        Toast.makeText(getContext(), "The reset email is send successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Override
    public void onClick(View view) {
        mPresenter.login(mEmail.getText().toString(), mPassword.getText().toString());
    }
}