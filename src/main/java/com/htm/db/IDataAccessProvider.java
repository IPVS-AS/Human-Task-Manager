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

package com.htm.db;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.htm.dm.EHumanRoles;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.taskinstance.*;
import com.htm.taskmodel.ILogicalPeopleGroupDef;
import com.htm.taskmodel.ITaskModel;
import com.htm.userdirectory.IGroup;
import com.htm.userdirectory.IUser;

import javax.xml.crypto.Data;

public interface IDataAccessProvider {

    public final static boolean JUNIT_TEST = true;

    public boolean deleteAllWorkItems() throws DatabaseException;

    public boolean deleteAllTaskInstances() throws DatabaseException;


    public static class Factory {

        //private static IDataAccessProvider dataAccessProvider = null;

       // public static IDataAccessProvider newInstance() {

//			/* Singleton - Only one instance of the data access provider can be created */
//			if (dataAccessProvider == null) {
//				if (JUNIT_TEST) {
////					BeanFactory beanFac = new XmlBeanFactory(new FileSystemResource(System.getProperty("user.dir")+"/src/test/resources/services-spring.xml"));
//					dataAccessProvider = new DataAccessProviderJpaJUnit();
//				} else {
//					dataAccessProvider = DatabaseAccessProviderJPA.newInstance();
//				}
//
//			}
//			return dataAccessProvider;
            // TODO introduce properties to maintain "lightness"
       //     return null;
      //  }
    }

    //Only for testing purposes
    public abstract void open();

    public abstract void beginTx();

    public abstract void commitTx() throws DatabaseException;

    public abstract void rollbackTx();

    public abstract boolean isTxActive();

    public abstract void close();

    public abstract ILogicalPeopleGroupDef getLogicalPeopleGroupDef(String name)
            throws DatabaseException;

    public Map<String, ILogicalPeopleGroupDef> getLogicalPeopleGroupDefs()
            throws DatabaseException;

    public abstract ITaskModel getHumanTaskModel(String modelName)
            throws DatabaseException;

    public abstract Map<String, ITaskModel> getHumanTaskModels()
            throws DatabaseException;

    public abstract ITaskInstance getTaskInstance(String tiid)
            throws DatabaseException, IllegalArgumentException;

    //public abstract Set<String> getHumanTaskInstanceIds(String modelName) throws DatabaseException;

    public abstract void persistTaskModel(ITaskModel taskModel) throws DatabaseException;

    public abstract void persistLogicalPeopleGroupDef(ILogicalPeopleGroupDef lpgDef) throws DatabaseException;

    public abstract boolean deleteLogicalPeopleGroupDef(String lpgDefName)
            throws DatabaseException;

    public abstract boolean deleteHumanTaskModel(String modelName)
            throws DatabaseException;

    public List<IWorkItem> getWorkItems(ITaskInstance taskInstance) throws DatabaseException;

    public List<IWorkItem> getWorkItems(String tiid) throws DatabaseException;

    public List<IWorkItem> getWorkItems(String tiid, IAssignedUser user) throws DatabaseException;

    public List<IWorkItem> getWorkItems(ITaskInstance taskInstance, IAssignedUser user) throws DatabaseException;

    public IWorkItem getWorkItem(String tiid, IAssignedUser user, EHumanRoles role) throws DatabaseException;

    public boolean deleteWorkItem(String wiid) throws DatabaseException;

    public abstract void persistHumanTaskInstance(ITaskInstance taskInstance) throws HumanTaskManagerException;

    public abstract boolean deleteHumanTaskInstance(String tiid) throws DatabaseException;

    public abstract void persistWorkItem(IWorkItem workItem) throws DatabaseException;

    public abstract void persistWorkItems(List<IWorkItem> workItems) throws DatabaseException;

    public abstract IAssignedUser getAssignedUser(String userid) throws DatabaseException;

    public boolean assginedUserExists(String userid) throws DatabaseException;

    //TODO add functionality for querying complex data (now only simple types are supported)
    public abstract List<IWorkItem> query(String whereClause, int maxResults) throws DatabaseException;

    public abstract List<IWorkItem> query(String whereClause) throws DatabaseException;

    public abstract List<ITaskInstance> getMyTasks(String genericHumanRole, Set<ETaskInstanceState> states, Timestamp createdOn, String whereClause) throws DatabaseException;

    /* Access to the user directory */

    public abstract Set<String> getUserIdsByGroup(String groupName) throws DatabaseException;

    public void persistUser(IUser user) throws DatabaseException;

    public IUser getUser(String userId) throws DatabaseException;

    public IUser createUser(String userId, String firstname, String lastname) throws DatabaseException;

    public boolean deleteUser(String userId) throws DatabaseException;

    public Set<IUser> getAllUser() throws DatabaseException;

    public boolean updateUser(String userId, String firstname, String lastname, String[] groups) throws DatabaseException;

    public boolean addUserToGroup(String userId, String groupName) throws DatabaseException;

    public Set<IUser> getUserByGroup(String groupName) throws DatabaseException;

    public Set<String> getUserAllGroups(String userId) throws DatabaseException;

    public void persistGroup(IGroup group) throws DatabaseException;

    public IGroup getGroup(String groupName) throws DatabaseException;

    public IGroup createGroup(String groupName, String[] genericHumanRoles) throws DatabaseException;

    public Set<String> getGroupNames() throws DatabaseException;

    public boolean deleteGroup(String groupName) throws DatabaseException;

    public Set<IGroup> getAllGroups() throws DatabaseException;

    public boolean updateGroup(String groupname, String[] genericHumanRole) throws DatabaseException;

    public List<ITaskInstance> getNonFinalizedTaskInstances() throws DatabaseException;

    public Set<String> getGenericHumanRolesByGroup(String groupname) throws DatabaseException;

    public ITaskType getTaskType(String taskTypeName) throws DatabaseException;

    public ITaskType createTaskType(String tasktypeName) throws DatabaseException;

    public boolean deleteTaskType(String tasktypeName) throws DatabaseException;

    public Set<ITaskType> getAllTaskTypes() throws DatabaseException;

    public boolean updateTaskType(String tasktypename, String[] groups) throws DatabaseException;

    public boolean addGroupToTaskType(String taskTypeName, String groupName) throws DatabaseException;

    public Set<String> getTaskTypeAllGroups(String taskTypeName) throws DatabaseException;

    public ITaskInstance createTask(String name, String title, String subject, String description,
                                    String priority, String[] taskTypeNames) throws DatabaseException;

    public boolean createInputParameter(String key, String value, String tiid) throws DatabaseException;

    public boolean createOutputParameter(String key, String value, String tiid) throws DatabaseException;

    public boolean addTaskToTaskType(String tiid, String taskTypeName) throws DatabaseException;

    public Set<IInputParameter> getInputParametersByTask(String tiid) throws DatabaseException;

    public Set<IOutputParameter> getOutputParametersByTask(String tiid) throws DatabaseException;

    public Set<ITaskInstance> getAllTasks() throws DatabaseException;

    public Set<ITaskInstance> getTasksByUser(String userId) throws DatabaseException;

    public Set<ITaskInstance> getTasksByTaskType(String taskTypeName) throws DatabaseException;

    public boolean updateOutputParameter(String task, HashMap outputparameters) throws DatabaseException;

    public boolean updateTask(String id,String name, String[] taskTypeNames, String status, HashMap inputParameter,
                              HashMap outputParameter, String title, String subject, String description, String priority, String claimed) throws DatabaseException;

    public boolean updateState(String tiid, String state) throws DatabaseException;

    public boolean claimTask(String tiid, String userId) throws DatabaseException;

    public Set<String> getTaskTypeByTask (String tiid) throws DatabaseException;
}