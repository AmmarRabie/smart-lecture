package cmp.sem.team8.smarlecture.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;
import es.dmoral.toasty.Toasty;

public class GroupMessagesActivity extends AppCompatActivity implements GroupMessagesContract.Views {

    private GroupMessagesContract.Actions mActions;
    private GroupMessagesRecyclerAdapter groupMessagesRecyclerAdapter;

    private RecyclerView groupMessagesRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messages);

        groupMessagesRecyclerView = findViewById(R.id.groupMessagesActivity_list);
        groupMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String groupId = getIntent().getStringExtra(getString(R.string.IKey_groupId));

        groupMessagesRecyclerAdapter = new GroupMessagesRecyclerAdapter(new ArrayList<GroupMessageModel>());
        groupMessagesRecyclerView.setAdapter(groupMessagesRecyclerAdapter);

        new GroupMessagesPresenter(FirebaseRepository.getInstance(), this, groupId);
    }

    @Override
    public void setPresenter(GroupMessagesContract.Actions presenter) {
        mActions = presenter;
    }


    @Override
    public void showGroupMessages(ArrayList<GroupMessageModel> groupMessages) {
        groupMessagesRecyclerAdapter.swapList(groupMessages);
    }

    @Override
    public void showErrorMessage(String message) {
        Toasty.error(this, message, Toast.LENGTH_LONG,true).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActions.start();
    }
}