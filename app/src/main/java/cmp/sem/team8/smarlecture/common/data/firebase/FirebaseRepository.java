/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cmp.sem.team8.smarlecture.common.data.firebase;


import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.model.GroupModel;
import cmp.sem.team8.smarlecture.model.SessionModel;
import cmp.sem.team8.smarlecture.model.UserModel;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class FirebaseRepository implements AppDataSource {


    @Override
    public void getUser(String userId, Get<UserModel> callback) {

    }

    @Override
    public void listenUser(String userId, Listen<UserModel> callback) {

    }

    @Override
    public void insertUser(UserModel userModel, Insert<String> callback) {

    }

    @Override
    public void updateUserName(String userId, String newName, Update callback) {

    }

    @Override
    public void getGroupById(String groupId, Get<GroupModel> callback) {

    }

    @Override
    public void getGroupSessions(String groupId, Get<ArrayList<SessionModel>> callback) {

    }

    @Override
    public void deleteGroupById(String groupId, Delete callback) {

    }

    @Override
    public void updateGroup(GroupModel updatingValues, Update callback) {

    }

    @Override
    public void updateGroupById(String groupId, String newGroupName, Update callback) {

    }

    @Override
    public void deleteNamesList(String groupId, Delete callback) {

    }

    @Override
    public void deleteNameOfNamesList(String groupId, String nameId, Delete callback) {

    }

    @Override
    public void updateNameOfNamesList(String groupId, String nameId, String newName, Update callback) {

    }

    @Override
    public void insertNameInNamesList(String groupId, String userId, Insert<String> callback) {

    }

    @Override
    public void getSessionById(String sessionId, Get<SessionModel> callback) {

    }

    @Override
    public void getSessionStatus(String sessionId, Get<SessionStatus> callback) {

    }

    @Override
    public void getAttendanceStatus(String sessionId, Get<AttendanceStatus> callback) {

    }

    @Override
    public void updateSession(SessionModel sessionModel, Update callback) {

    }

    @Override
    public void updateSessionStatus(String sessionId, SessionStatus status, Update callback) {

    }

    @Override
    public void updateAttendanceStatus(String sessionId, AttendanceStatus status, Update callback) {

    }

    @Override
    public void updateSessionSecret(String sessionId, String secret, Update callback) {

    }

    @Override
    public void forget(Listen listener) {

    }
}
