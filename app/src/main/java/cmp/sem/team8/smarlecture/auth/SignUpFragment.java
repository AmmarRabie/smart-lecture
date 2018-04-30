package cmp.sem.team8.smarlecture.auth;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ProfileImageUtil;
import es.dmoral.toasty.Toasty;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class SignUpFragment extends Fragment implements SignUpContract.Views, View.OnClickListener {
    private static final String TAG = "SignUpFragment";
    private static final int GALLERY_REQUEST = 1254;
    private static final int CAMERA_REQUEST = 546;

    private SignUpContract.Actions mAction;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mConfirmPassword;
    private ImageView profileImageView;
    private ProgressDialog progressDialog = null;

    private byte[] currImageBytes;

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
        profileImageView = root.findViewById(R.id.signUp_profileImage);
        setHasOptionsMenu(true);

        root.findViewById(R.id.signUp_signup).setOnClickListener(this);

//        setFromFirebase();
        setRandomPicture();
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] selections = {
                        "Camera", "Gallery", "Set Random profile"
                };

                AlertDialog.Builder selectionDialogBuilder = new AlertDialog.Builder(getContext());
                selectionDialogBuilder.setTitle("Set new image from");
                selectionDialogBuilder.setItems(selections, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: // Camera
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                break;
                            case 1: // Gallery
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                                break;
                            case 2: // random
                                setRandomPicture();
                                break;
                        }
                    }
                });
                selectionDialogBuilder.show();
            }
        });

        return root;
    }

    @Override
    public void showOnSuccess() {
        Toasty.success(getContext(), getString(R.string.mes_signedInSuccess), Toast.LENGTH_SHORT, true).show();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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

    @Override
    public void showProgressIndicator() {
        if (progressDialog == null)
            progressDialog = ProgressDialog.show(getContext(),
                    null, getString(R.string.mes_signingUp_indicator)
                    , true, false);
        else
            progressDialog.show();

    }

    @Override
    public void hideProgressIndicator() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        mAction.signUp(mName.getText().toString(), mEmail.getText().toString(),
                mPassword.getText().toString(), mConfirmPassword.getText().toString(), currImageBytes);
    }

    @Override
    public void setPresenter(SignUpContract.Actions presenter) {
        mAction = presenter;
    }

    private void setRandomPicture() {
        Bitmap randBitmap = ProfileImageUtil.createRandomImage(getContext(), 250, 250);
        updateImageBytes(randBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case CAMERA_REQUEST:
                Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                if (photo == null) {
                    Log.e(TAG, "onActivityResult: data can't be casted to Bitmap");
                    return;
                }
                updateImageBytes(photo);
                break;
            case GALLERY_REQUEST:
                Uri selectedImage = imageReturnedIntent.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap selectedProfileImg = BitmapFactory.decodeFile(filePath);
                updateImageBytes(selectedProfileImg);
                break;
        }
    }

    /**
     * Update the view with scaled version of the new image.
     * Update the bytes that will be send to the presenter as the profile image of the user
     * so it should be called every time user change his profile image
     *
     * @param newBitmap new image in bitmap
     */
    private void updateImageBytes(Bitmap newBitmap) {
        // scale the bitmap and update the view
        newBitmap = Bitmap.createScaledBitmap(newBitmap, 250, 250, true);
        profileImageView.setImageBitmap(newBitmap);

        // save compressed value of image
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        currImageBytes = byteArrayOutputStream.toByteArray();
    }
}
