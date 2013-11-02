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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.htm.db.IDataAccessProvider;
import com.htm.dm.EHumanRoles;
import com.htm.exceptions.AuthenticationException;
import com.htm.exceptions.AuthorizationException;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.exceptions.UserException;
import com.htm.taskinstance.IAssignedUser;
import com.htm.taskinstance.IWorkItem;
import com.htm.utils.SessionUtils;
import com.htm.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class AuthorizationManager implements IAuthorizationManager {

    private static final Logger log = Utilities.getLogger(AuthorizationManager.class);

    @Autowired
    private IDataAccessProvider dataAccessProvider;


    @Override
    public IAssignedUser authorizeTaskQueryAction(EActions action)
            throws HumanTaskManagerException {
        // TODO Everybody can execute queries, improve that !!!
        IDataAccessProvider dap = this.dataAccessProvider;
        /* Get the id of the user that is currently logged in */
        String userId = SessionUtils.getCurrentUser();
        return dap.getAssignedUser(userId);

    }

    @Override
    public void authorizeTaskParentAction(String userId, String tiid,
                                          EActions action) throws AuthenticationException,
            IllegalArgumentException, DatabaseException, AuthorizationException {

        log.debug("Authorize task client action - Action: '" + action
                + "' User id: '" + userId + "'");
        boolean isAuthorized = false;

        switch (action) {
            case CREATE_TASK_INSTANCE:
                // TODO currently every valid user can create a task instance
                isAuthorized = true;
                break;
            case EXIT:
                isAuthorized = hasRoleForPerformingAction(userId, tiid, action);
                break;
            default:
                break;
        }

        if (!isAuthorized) {
            String errorMsg = "Authorize task parent action - User '" + userId
                    + "' has not the proper roles to perform the action "
                    + action + " for task instance '" + tiid + "'";
            log.error(errorMsg);
            throw new AuthorizationException(errorMsg);
        }
    }

    @Override
    public IAssignedUser authorizeTaskClientAction(String tiid,
                                                   EActions action) throws AuthorizationException,
            AuthenticationException, IllegalArgumentException,
            DatabaseException, UserException {
        IDataAccessProvider dap = this.dataAccessProvider;

        /* Get the id of the user that is currently logged in */
        String userId = SessionUtils.getCurrentUser();
        /* Check if the user is authorized to perform the action */
        log.debug("Authorize task client action - Action: '" + action
                + "' User id: '" + userId + "'");
        boolean isAuthorized = hasRoleForPerformingAction(userId, tiid, action);
        log.debug("Authorize task client action - Is user authorized: "
                + isAuthorized);
        if (!isAuthorized) {
            String errorMsg = "Authorize task client action - User '" + userId
                    + "' has not the proper roles to perform the action "
                    + action + " for task instance '" + tiid + "'";
            log.error(errorMsg);
            throw new AuthorizationException(errorMsg);
        }
        return dap.getAssignedUser(userId);
    }

    // TODO is implementation correct?
    // TODO reduce redundancy
    @Override
    public IAssignedUser authorizeTaskClientAction2(String tiid,
                                                    String userId, EActions action) throws AuthorizationException,
            AuthenticationException, IllegalArgumentException,
            DatabaseException, UserException {
        IDataAccessProvider dap = this.dataAccessProvider;

        /* Check if the user is authorized to perform the action */
        log.debug("Authorize task client action - Action: '" + action
                + "' User id: '" + userId + "'");
        boolean isAuthorized = hasRoleForPerformingAction(userId, tiid, action);
        log.debug("Authorize task client action - Is user authorized: "
                + isAuthorized);
        if (!isAuthorized) {
            String errorMsg = "Authorize task client action - User '" + userId
                    + "' has not the proper roles to perform the action "
                    + action + " for task instance '" + tiid + "'";
            log.error(errorMsg);
            throw new AuthorizationException(errorMsg);
        }
        return dap.getAssignedUser(userId);
    }

    protected boolean hasRoleForPerformingAction(String userId,
                                                        String tiid, EActions action) throws IllegalArgumentException,
            DatabaseException {

        IDataAccessProvider dap = this.dataAccessProvider;

        if (dap.getTaskInstance(tiid) == null) {
            throw new IllegalArgumentException("The task instance " + tiid
                    + " can not be found.");
        }

        IAssignedUser assignedUser = dap.getAssignedUser(userId);

        if (assignedUser != null) {
            /* Get the roles that are required to perform the action */
            EHumanRoles[] roles = AuthorizationUtils.actionRolesMap.get(action);
            log.debug("Authorize task client action - One of the following roles is required to "
                    + "perform the action: "
                    + Utilities.concateArrayElementsToString(roles));
            if (roles == null) {
                throw new IllegalArgumentException(
                        "No user roles were defined "
                                + "for task client action " + action);
            }
            /*
                * Get all work items that associated the user with the given task
                * instance
                */
            List<IWorkItem> workItemsOfUserForTI = dap.getWorkItems(tiid,
                    assignedUser);
            log.debug("Authorize task client action - Checking if the user possesses the required work items for executing the action.");
            log.debug("Authorize task client action - "
                    + "Number of work items assigned to user "
                    + workItemsOfUserForTI.size());

            /*
                * The algorithm checks for each work item of the user if the user
                * has the proper human role that is required to perform the action.
                * When the first role of the user meets the required role the
                * algorithm terminates. If ANYBODY is allowed to perform the action
                * and the user has at least one work item assigned return true.
                */
            Iterator<IWorkItem> iter = workItemsOfUserForTI.iterator();
            while (iter.hasNext()) {
                IWorkItem workItem = (IWorkItem) iter.next();
                EHumanRoles workItemRole = workItem.getGenericHumanRole();
                log.debug("Authorize task client action - User has role: " + workItem.getGenericHumanRole());
                for (EHumanRoles role : roles) {
                    /*
                          * Either the user has the required role or ANYBODY is
                          * allowed to execute the action
                          */
                    if (workItemRole.equals(role)
                            || role.equals(EHumanRoles.ANYBODY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<EHumanRoles> getRolesOfUser(String tiid, String userId)
            throws HumanTaskManagerException {
        List<EHumanRoles> roles = null;
        log.debug("Get roles of user '" + userId + "' for task '" + tiid + "'");
        IDataAccessProvider dap = this.dataAccessProvider;
        IAssignedUser assignedUser;
        try {
            assignedUser = dap.getAssignedUser(userId);
            if (assignedUser != null) {
                List<IWorkItem> workItemsOfUserForTI = dap.getWorkItems(tiid,
                        assignedUser);
                if (workItemsOfUserForTI.size() > 0) {
                    roles = new ArrayList<EHumanRoles>();
                    for (IWorkItem workItem : workItemsOfUserForTI) {
                        log.debug("User has role: " + workItem.getGenericHumanRole());
                        // TODO anybody
                        roles.add(workItem.getGenericHumanRole());

                    }
                }
            }
        } catch (DatabaseException e) {
            log.error("Cannot load work items of user '" + userId + "' for task '" + tiid + "'", e);
            throw new HumanTaskManagerException("Cannot load work items of user '" + userId + "' for task '" + tiid + "'", e);
        }

        return roles;
    }

}
