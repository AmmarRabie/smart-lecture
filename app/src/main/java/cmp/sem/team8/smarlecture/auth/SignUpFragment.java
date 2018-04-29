package cmp.sem.team8.smarlecture.auth;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ProfileImageUtil;
import es.dmoral.toasty.Toasty;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

public class SignUpFragment extends Fragment implements SignUpContract.Views, View.OnClickListener {


    private static final int[] foregroundColors = new int[]{0xfff14242, 0xff57c867, 0xff5379e5, 0xff9753e5, 0xff3e3e3e};
    private static final int backgroundColor = 0xffffffff;

    private SignUpContract.Actions mAction;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mConfirmPassword;
    private ImageView profileImageView;
    private ProgressDialog progressDialog = null;

    private String profileImageEncoded;

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

//        setRandomPicture();
        setFromFirebase();
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRandomPicture();
            }
        });

        profileImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 54452);
                return true;
            }
        });

        return root;
    }

    private void setFromFirebase() {
        FirebaseStorage.getInstance().getReference("profile-images/gza2.jpg").getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                profileImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                FirebaseStorage.getInstance().getReference("profile-images/newFileFromAndroidApp.jpg")
                        .putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "successsssss", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
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
                mPassword.getText().toString(), mConfirmPassword.getText().toString(), profileImageEncoded);
    }

    @Override
    public void setPresenter(SignUpContract.Actions presenter) {
        mAction = presenter;
    }

    private void setRandomPicture() {
//        int[] foregroundColors = getResources().getIntArray(R.array.randomColorsForProfilePicture);
//        int backgroundColor = getResources().getInteger(R.integer.backgroundColor);
        int randomIndex = ((int) (Math.random() * 100)) % foregroundColors.length;
        final int foregroundColor = foregroundColors[randomIndex];
        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.kaleidoscope_source);
        Bitmap bitmap = ProfileImageUtil.createProfileImage(original, foregroundColor, backgroundColor);

        // set the view
        profileImageView.setImageBitmap(bitmap);

        // get the encoded string of bitmap and save it in the variable
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        profileImageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 54452:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap selectedProfileImg = BitmapFactory.decodeFile(filePath);
                    selectedProfileImg = Bitmap.createScaledBitmap(selectedProfileImg, 250, 250, true);
                    profileImageView.setImageBitmap(selectedProfileImg);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedProfileImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    profileImageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
        }
    }
}
