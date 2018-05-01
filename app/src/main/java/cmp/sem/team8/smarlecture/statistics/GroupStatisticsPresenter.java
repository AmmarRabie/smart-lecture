package cmp.sem.team8.smarlecture.statistics;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.data.mock.MockRepo;
import cmp.sem.team8.smarlecture.common.data.model.GroupStatisticsModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by ramym on 4/27/2018.
 */

public class GroupStatisticsPresenter implements GroupStatisticsContract.Actions{
    double sessionPercentage;
    ArrayList<SessionModel> sessionList;
    AppDataSource mDatabase;
    GroupStatisticsContract.Views mView;
    GroupStatisticsModel data;
    MockRepo  mTestDatabase;

    int mode=0;   // to be used to sort the list to avoid listeners are Asincronized   worst=0;  best=1;


    GroupStatisticsPresenter(GroupStatisticsContract.Views mView)
    {
        this.mView=mView;
        this.mView.setPresenter(this);
        mDatabase=new FirebaseRepository();
        data=null;
        mTestDatabase=MockRepo.getInstance();
    }
    @Override
    public void start() {
        mView.showProgressIndicator();
        String groupId=mView.getGroupID();
        mDatabase.getGroupAndItsSessionNameList(groupId, new AppDataSource.Get<GroupStatisticsModel>() {
            @Override
            public void onDataFetched(GroupStatisticsModel data_coming) {
                data=data_coming;
                groupAttendancePercentage();
                MostAndWorstAttendanceUsers();
                mView.hideProgressIndicator();
            }
        });
    }

    @Override
    public void groupAttendancePercentage() {

        double counter1=0;
        double counter2=0;

        ArrayList<ArrayList<String>> SessionsMem=data.getSessionMembers();
        ArrayList<String>  GroupMem  = data.getGroupMembers();

        for(int i=0;i<data.getSessionMembers().size();i++)
        {
            counter1 += SessionsMem.get(i).size();
            counter2 += GroupMem.size();
        }
        mView.showAttendancePercentage(counter1/counter2);
    }



    @Override
    public void MostAndWorstAttendanceUsers() {

        ArrayList<String> GroupMem=data.getGroupMembers();
        ArrayList<ArrayList<String>> SessionMem=data.getSessionMembers();

        final ArrayList<Pair> mylist=new ArrayList<>();
        for (int i=0;i<GroupMem.size();i++)
        {
            int counter=0;

            for (int j=0;j<SessionMem.size();j++)
            {
                for (int k=0;k<SessionMem.get(j).size();k++)
                {
                    if (GroupMem.get(i).equals(SessionMem.get(j).get(k)))
                    {
                        counter++;
                    }
                }
            }

            mylist.add(new Pair(GroupMem.get(i),counter));
        }


        Collections.sort(mylist);

       final ArrayList<UserGradeModel> mostusers=new ArrayList<>();
       final ArrayList<UserGradeModel> worstusers=new ArrayList<>();
        if (mylist.size()>10)
        {
            for (int i = 0; i <10; i++)
            {
                    mDatabase.getUserGrade(mylist.get(i).getElement1(),mylist.get(i).getElement0(), new AppDataSource.Get<UserGradeModel>()
                    {
                        @Override
                        public void onDataFetched(UserGradeModel data) {
                            worstusers.add(data);

                            if (worstusers.size()==10) {
                                Collections.sort(worstusers);
                                mView.showWorstAttendantUsers(worstusers);
                            }
                        }
                    });
            }

            for (int i = mylist.size()-1; i>=mylist.size()-10; i--)
             {
            mDatabase.getUserGrade(mylist.get(i).getElement1(),mylist.get(i).getElement0(), new AppDataSource.Get<UserGradeModel>(){
                @Override
                public void onDataFetched(UserGradeModel data) {
                    mostusers.add(data);

                    if (mostusers.size()==10) {
                        Collections.sort(mostusers);
                        Collections.reverse(mostusers);
                        mView.showMostAttendantUsers(mostusers);
                    }
                }
            });
             }
        }
        else
        {
            for (int i = 0; i <mylist.size(); i++)
            {
                    mDatabase.getUserGrade(mylist.get(i).getElement1(),mylist.get(i).getElement0(), new AppDataSource.Get<UserGradeModel>(){
                        @Override
                        public void onDataFetched(UserGradeModel data) {
                            worstusers.add(data);

                            if (worstusers.size()==mylist.size()) {
                                Collections.sort(worstusers);
                                mView.showWorstAttendantUsers(worstusers);
                                Collections.reverse(worstusers);
                                mView.showMostAttendantUsers(worstusers);
                            }
                        }
                    });
            }
        }



    }


    public class Pair implements Comparable<Pair> {

        private final String element0;
        private final int element1;

        public Pair(String element0, int element1) {
            this.element0 = element0;
            this.element1 = element1;
        }

        public String getElement0() {
            return element0;
        }

        public int getElement1() {
            return element1;
        }


        @Override
        public int compareTo(@NonNull Pair o) {
            return (this.getElement1()-o.getElement1());
        }
    }
}
