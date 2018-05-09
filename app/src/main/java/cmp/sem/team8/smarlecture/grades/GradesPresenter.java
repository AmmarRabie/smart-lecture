package cmp.sem.team8.smarlecture.grades;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.data.mock.MockRepo;
import cmp.sem.team8.smarlecture.common.data.model.GroupStatisticsModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;

import static java.lang.Math.round;

/**
 * Created by ramym on 5/1/2018.
 */


public class GradesPresenter implements GradesContract.Actions {

    GradesContract.Views mView;
    ArrayList<UserGradeModel> mList;

    DataService mDataBase;
    DataService mTestDataBase;

    GradesPresenter(GradesContract.Views view) {
        this.mView = view;

        mView.setPresenter(this);

        mDataBase = FirebaseRepository.getInstance();
        mTestDataBase = MockRepo.getInstance();
    }

    @Override
    public void start() {

        mView.showProgressIndicator();
        getGroupGrade();
        mView.hideProgressIndicator();
    }

    @Override
    public void getGroupGrade() {

        mDataBase.getGroupGrade(mView.getGroupId(), new DataService.Get<ArrayList<UserGradeModel>>() {
            @Override
            public void onDataNotAvailable() {
                mView.hideActivityLoading();
                mView.showEmptyView();
            }

            @Override
            public void onDataFetched(ArrayList<UserGradeModel> data) {
                if (data.size() != 0) {
                    mView.hideActivityLoading();
                    mView.hideEmptyView();
                    mView.showGradesOfGroup(data);
                } else {
                    mView.hideActivityLoading();
                    mView.showEmptyView();
                }
            }
        });
    }


    @Override
    public void setGroupGrade() {

        try {
            mView.showProgressIndicator();
            final int highest_grade = mView.getHighestGrade();
            final int lowest_grade = mView.getLowestGrade();

            if (lowest_grade >= highest_grade) {
                mView.hideProgressIndicator();
                mView.showErrorMessage("lowest grade cant be greater than the highest grade");
                return;
            }


            mDataBase.getGroupAndItsSessionNameList(mView.getGroupId(), new DataService.Get<GroupStatisticsModel>() {
                @Override
                public void onDataNotAvailable() {
                    mView.showErrorMessage(" There is No Sessions to assign Grades ");
                    mView.hideActivityLoading();
                    mView.showEmptyView();
                }

                @Override
                public void onDataFetched(GroupStatisticsModel data) {

                    ArrayList<String> GroupMem = data.getGroupMembers();
                    ArrayList<ArrayList<String>> SessionMem = data.getSessionMembers();

                    final ArrayList<Pair> mylist = new ArrayList<>();
                    for (int i = 0; i < GroupMem.size(); i++) {
                        int counter = 0;

                        for (int j = 0; j < SessionMem.size(); j++) {
                            for (int k = 0; k < SessionMem.get(j).size(); k++) {
                                if (GroupMem.get(i).equals(SessionMem.get(j).get(k))) {
                                    counter++;
                                }
                            }
                        }

                        mylist.add(new Pair(GroupMem.get(i), counter));
                    }


                    Collections.sort(mylist);

                    ArrayList<String> ids = new ArrayList<>();
                    ArrayList<Integer> grades = new ArrayList<>();

                    if ((mylist.get(mylist.size() - 1)).element1 != (mylist.get(0)).element1) {
                        double slope = (double) (highest_grade - lowest_grade) / ((mylist.get(mylist.size() - 1)).element1 - (mylist.get(0)).element1);
                        double b = ((lowest_grade - (slope * mylist.get(0).element1)));

                        // line equation ax+b   a  =  slope.

                        for (int i = mylist.size() - 1; i >= 0; i--) {
                            mylist.get(i).element1 = (int) round((b + ((mylist.get(i).element1 * slope))));
                            ids.add(mylist.get(i).element0);
                            grades.add(mylist.get(i).element1);
                        }

                        mDataBase.updateGroupGrades(mView.getGroupId(), ids, grades, new DataService.Update() {
                            @Override
                            public void onUpdateSuccess() {

                                getGroupGrade();
                                mView.hideProgressIndicator();
                            }
                        });
                    } else {
                        for (int i = mylist.size() - 1; i >= 0; i--) {
                            mylist.get(i).element1 = lowest_grade;
                            ids.add(mylist.get(i).element0);
                            grades.add(mylist.get(i).element1);
                        }

                        mDataBase.updateGroupGrades(mView.getGroupId(), ids, grades, new DataService.Update() {
                            @Override
                            public void onUpdateSuccess() {

                                getGroupGrade();
                                mView.hideProgressIndicator();
                            }
                        });
                    }
                }


            });
        } catch (NullPointerException e) {
            mView.showErrorMessage(e.getMessage());
            mView.hideProgressIndicator();
        }
    }


    public class Pair implements Comparable<Pair> {

        private String element0;
        private int element1;

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
            return (this.getElement1() - o.getElement1());
        }
    }

}
