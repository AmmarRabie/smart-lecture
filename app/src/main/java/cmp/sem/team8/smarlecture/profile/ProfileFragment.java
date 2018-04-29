package cmp.sem.team8.smarlecture.profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;
import es.dmoral.toasty.Toasty;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class ProfileFragment extends Fragment implements ProfileContract.Views {

    private ProfileContract.Actions mAction;

    private EditText mNameView;
    private TextView mEmailView;
    private Button mChangePassView;
    private Button mEditNameView;
    private Button mSignOutView;
    private ImageView mProfileImageView;

    private AlertDialog changePasswordDialog;

    private boolean mEditState = false;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void setPresenter(ProfileContract.Actions presenter) {
        mAction = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_profile, container, false);

        mNameView = root.findViewById(R.id.profileFrag_name);
        mEmailView = root.findViewById(R.id.profileFrag_email);
        mChangePassView = root.findViewById(R.id.profileFrag_changePassword);
        mEditNameView = root.findViewById(R.id.profileFrag_editName);
        mSignOutView = root.findViewById(R.id.profileFrag_signOut);
        mProfileImageView = root.findViewById(R.id.profileFrag_profileImage);

        mSignOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAction.signOut();
            }
        });

        mEditNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditState) {
                    mAction.changeName(mNameView.getText().toString());
                } else {
                    mEditState = true;
                    mEditNameView.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_save));
                    mNameView.setEnabled(true);
                }
            }
        });


        final AlertDialog.Builder changePasswordDialogBuilder = new AlertDialog.Builder(getActivity());
        changePasswordDialogBuilder.setTitle(getString(R.string.dTitle_changePass));
        final LinearLayout rootView = buildDialogLayout();
        changePasswordDialogBuilder.setView(rootView);

        changePasswordDialogBuilder.setPositiveButton(getString(R.string.dAction_change),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPass = ((EditText) rootView.getChildAt(0)).getText().toString();
                        String confirmPass = ((EditText) rootView.getChildAt(1)).getText().toString();
                        mAction.changePassword(newPass, confirmPass);
                    }
                });

        changePasswordDialogBuilder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        changePasswordDialog = changePasswordDialogBuilder.create();

        mChangePassView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog.show();
            }
        });

        return root;
    }


    @Override
    public void showOnChangeNameSuccess() {
        Toast.makeText(getContext(), getString(R.string.mes_nameChanged), Toast.LENGTH_SHORT).show();
        mEditState = false;
        mEditNameView.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_edit));
        mNameView.setEnabled(false);
    }

    @Override
    public void showOnChangePassSuccess() {
        Toasty.success(getContext(),
                getString(R.string.mes_passwordChanged), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showOnSignOutSuccess() {
        // direct user to the log in screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // end this activity
        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public void showUserInfo(UserModel user) {
        mNameView.setText(user.getName());
        mEmailView.setText(user.getEmail());
        if (user.getProfileImage() == null) // do nothing if the user is and old user with no image
            return;
        byte[] imgByte = user.getProfileImage();
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        imgBitmap = Bitmap.createScaledBitmap(imgBitmap, 250, 250, true);
        mProfileImageView.setImageBitmap(imgBitmap);
    }

    @Override
    public void showErrorMessage(String cause) {
        Toasty.error(getContext(), cause, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAction.start();
    }

    private LinearLayout buildDialogLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText pass = new EditText(getContext());
        pass.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        pass.setHint(getString(R.string.newPass));
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass.setHintTextColor(getContext().getResources().getColor(android.R.color.secondary_text_dark));
        pass.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));

        EditText confirmPass = new EditText(getContext());
        confirmPass.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        confirmPass.setHint(getString(R.string.confirmPass));
        confirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPass.setHintTextColor(getContext().getResources().getColor(android.R.color.secondary_text_dark));
        confirmPass.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));

        layout.addView(pass, 0);
        layout.addView(confirmPass, 1);

        return layout;
    }
}