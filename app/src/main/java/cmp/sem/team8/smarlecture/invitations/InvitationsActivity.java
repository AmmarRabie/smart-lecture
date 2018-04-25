package cmp.sem.team8.smarlecture.invitations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.data.mock.MockRepo;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

public class InvitationsActivity extends AppCompatActivity {


    private InvitationsPresenter invitationsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        InvitationsFragment invitationsFragment = (InvitationsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (invitationsFragment == null) {
            invitationsFragment = InvitationsFragment.newInstance();


            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    invitationsFragment, R.id.contentFrame);
        }

        // Create the presenter
        invitationsPresenter = new InvitationsPresenter(FirebaseAuthService.getInstance(), FirebaseRepository.getInstance(), invitationsFragment);
    }
}
