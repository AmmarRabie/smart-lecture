package cmp.sem.team8.smarlecture.profile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DrawableUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class ProfileFragment extends Fragment implements ProfileContract.Views {

    private ProfileContract.Actions mPresenter;

    private EditText mName;
    private TextView mEmail;
    private Button mChangePass;
    private Button mEditName;
    private Button mSignOut;

    private AlertDialog changePasswordDialog;

    private boolean mEditState = false;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void setPresenter(ProfileContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_profile, container, false);

        mName = root.findViewById(R.id.profileFrag_name);
        mEmail = root.findViewById(R.id.profileFrag_email);
        mChangePass = root.findViewById(R.id.profileFrag_changePassword);
        mEditName = root.findViewById(R.id.profileFrag_editName);
        mSignOut = root.findViewById(R.id.profileFrag_signOut);

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.signOut();
            }
        });

        mEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditState) {
                    mPresenter.changeName(mName.getText().toString());
                } else {
                    mEditState = true;
                    mEditName.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_save));
                    mName.setEnabled(true);
                }
            }
        });


        AlertDialog.Builder changePasswordDialogBuilder = new AlertDialog.Builder(getActivity());
        changePasswordDialogBuilder.setTitle("password change");
        final LinearLayout rootView = buildDialogLayout();
        changePasswordDialogBuilder.setView(rootView);

        changePasswordDialogBuilder.setPositiveButton("change",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPass = ((EditText) rootView.getChildAt(0)).getText().toString();
                        String confirmPass = ((EditText) rootView.getChildAt(1)).getText().toString();
                        mPresenter.changePassword(newPass, confirmPass);
                    }
                });

        changePasswordDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        changePasswordDialog = changePasswordDialogBuilder.create();

        mChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog.show();
            }
        });

        return root;
    }


    @Override
    public void showOnSuccess() {
        Toast.makeText(getContext(),
                "Name changed successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnChangeNameSuccess() {
        Toast.makeText(getContext(), "name changed successfully", Toast.LENGTH_SHORT).show();
        mEditState = false;
        mEditName.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_edit));
        mName.setEnabled(false);
    }

    @Override
    public void showOnChangePassSuccess() {
        Toast.makeText(getContext(),
                "Password changed successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnSignOutSuccess() {
        Toast.makeText(getContext(), "signed out successfully", Toast.LENGTH_SHORT).show();

        // direct user to the log in screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // end this activity
        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public void showUserInfo(String name, String email) {
        mName.setText(name);
        mEmail.setText(email);
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

    private LinearLayout buildDialogLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText pass = new EditText(getContext());
        pass.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        pass.setHint("New password");
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        EditText confirmPass = new EditText(getContext());
        confirmPass.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        confirmPass.setHint("Confirm password");
        confirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        layout.addView(pass, 0);
        layout.addView(confirmPass, 1);

        return layout;
    }
}