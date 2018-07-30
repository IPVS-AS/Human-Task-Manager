/*
 * Copyright 2012 Bangkok Project Team, GRIDSOLUT GmbH + Co.KG, and
 * University of Stuttgart (Institute of Architecture of Application Systems)
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.htm.security;

import com.htm.db.IDataAccessProvider;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.userdirectory.IGroup;
import com.htm.userdirectory.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the IUserManager in the way that it can be used for the REST-API.
 *
 */

@Transactional
public class UserManagerToscaImpl implements IUserManager {

    @Autowired
    protected IDataAccessProvider dataAccessTosca;


    @Override
    public IUser addUser(String userId, String firstName, String lastName, String password) throws HumanTaskManagerException {
        IUser user = dataAccessTosca.createUser(userId, firstName, lastName);
        return user;
    }

    @Override
    public IGroup addGroup(String groupName, String[] genericHumanRoles) throws HumanTaskManagerException {
        return dataAccessTosca.createGroup(groupName, genericHumanRoles);
    }

    @Override
    public boolean deleteGroup(String groupName) throws HumanTaskManagerException {
        return dataAccessTosca.deleteGroup(groupName);
    }

    @Override
    public IGroup getGroup(String groupName) throws HumanTaskManagerException {
        return dataAccessTosca.getGroup(groupName);
    }

    @Override
    public IUser getUser(String userId) throws HumanTaskManagerException {
       return dataAccessTosca.getUser(userId);
    }

    @Override
    public boolean changePassword(String userId, String newPassword) throws HumanTaskManagerException {
        return false;
    }

    @Override
    public boolean deleteUser(String userid) throws HumanTaskManagerException {
        return dataAccessTosca.deleteUser(userid);
    }
}
