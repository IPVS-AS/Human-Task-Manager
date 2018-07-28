package com.htm.db.spring;

import com.htm.dm.EHumanRoles;
import com.htm.entities.jpa.Group;
import com.htm.entities.jpa.User;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.taskinstance.*;
import com.htm.taskmodel.ILogicalPeopleGroupDef;
import com.htm.taskmodel.ITaskModel;
import com.htm.userdirectory.IGroup;
import com.htm.userdirectory.IUser;
import com.htm.userdirectory.UserDirectoryFactory;
import com.htm.userdirectory.jpa.GroupWrapper;
import com.htm.userdirectory.jpa.UserDirectoryFactoryJPA;
import com.htm.userdirectory.jpa.UserWrapper;
import com.htm.utils.Utilities;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.*;


@Component
public class DataAccessRepositoryToscaImpl implements DataAccessRepositoryCustom {

    @PersistenceContext
    protected EntityManager em;

    protected Logger log;


    @Autowired
    private TaskInstanceFactory taskInstanceFactory;

    /**
     * Configure the entity manager to be used
     *
     * @param em
     */
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean deleteAllWorkItems() throws DatabaseException {
        return false;
    }

    @Override
    public boolean deleteAllTaskInstances() throws DatabaseException {
        return false;
    }

    @Override
    public void open() {

    }

    @Override
    public void beginTx() {

    }

    @Override
    public void commitTx() throws DatabaseException {

    }

    @Override
    public void rollbackTx() {

    }

    @Override
    public boolean isTxActive() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public ILogicalPeopleGroupDef getLogicalPeopleGroupDef(String name) throws DatabaseException {
        return null;
    }

    @Override
    public Map<String, ILogicalPeopleGroupDef> getLogicalPeopleGroupDefs() throws DatabaseException {
        return null;
    }

    @Override
    public ITaskModel getHumanTaskModel(String modelName) throws DatabaseException {
        return null;
    }

    @Override
    public Map<String, ITaskModel> getHumanTaskModels() throws DatabaseException {
        return null;
    }

    @Override
    public ITaskInstance getTaskInstance(String tiid) throws DatabaseException, IllegalArgumentException {
        return null;
    }

    @Override
    public void persistTaskModel(ITaskModel taskModel) throws DatabaseException {

    }

    @Override
    public void persistLogicalPeopleGroupDef(ILogicalPeopleGroupDef lpgDef) throws DatabaseException {

    }

    @Override
    public boolean deleteLogicalPeopleGroupDef(String lpgDefName) throws DatabaseException {
        return false;
    }

    @Override
    public boolean deleteHumanTaskModel(String modelName) throws DatabaseException {
        return false;
    }

    @Override
    public List<IWorkItem> getWorkItems(ITaskInstance taskInstance) throws DatabaseException {
        return null;
    }

    @Override
    public List<IWorkItem> getWorkItems(String tiid) throws DatabaseException {
        return null;
    }

    @Override
    public List<IWorkItem> getWorkItems(String tiid, IAssignedUser user) throws DatabaseException {
        return null;
    }

    @Override
    public List<IWorkItem> getWorkItems(ITaskInstance taskInstance, IAssignedUser user) throws DatabaseException {
        return null;
    }

    @Override
    public IWorkItem getWorkItem(String tiid, IAssignedUser user, EHumanRoles role) throws DatabaseException {
        return null;
    }

    @Override
    public boolean deleteWorkItem(String wiid) throws DatabaseException {
        return false;
    }

    @Override
    public void persistHumanTaskInstance(ITaskInstance taskInstance) throws HumanTaskManagerException {

    }

    @Override
    public boolean deleteHumanTaskInstance(String tiid) throws DatabaseException {
        return false;
    }

    @Override
    public void persistWorkItem(IWorkItem workItem) throws DatabaseException {

    }

    @Override
    public void persistWorkItems(List<IWorkItem> workItems) throws DatabaseException {

    }

    @Override
    public IAssignedUser getAssignedUser(String userid) throws DatabaseException {
        return null;
    }

    @Override
    public boolean assginedUserExists(String userid) throws DatabaseException {
        return false;
    }

    @Override
    public List<IWorkItem> query(String whereClause, int maxResults) throws DatabaseException {
        return null;
    }

    @Override
    public List<IWorkItem> query(String whereClause) throws DatabaseException {
        return null;
    }

    @Override
    public List<ITaskInstance> getMyTasks(String genericHumanRole, Set<ETaskInstanceState> states, Timestamp createdOn, String whereClause) throws DatabaseException {
        return null;
    }

    @Override
    public Set<String> getUserIdsByGroup(String groupName) throws DatabaseException {
        return null;
    }

    @Override
    public void persistUser(IUser user) throws DatabaseException {

    }

    @Override
    @Transactional
    public IUser getUser(String userId) throws DatabaseException {
        Query query = em.createQuery("SELECT user FROM User user WHERE user.userid = :userId");
        query.setParameter("userId", userId);

         //since user ids are supposed to be unique only one result is expected
        User userEntity = null;
        try {
            userEntity = (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        if (userEntity != null) {

           return UserDirectoryFactory.newInstance().createUserFromEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional
    public IUser createUser(String userId, String firstname, String lastname) {
        try {
            if (getUser(userId) == null) {
                IUser user = new UserWrapper();
                user.setUserId(userId);
                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setPassword(" ");
                em.persist(user.getAdaptee());
                return user;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteUser(String userId) throws DatabaseException {
        Query query = em.createQuery("DELETE FROM User user WHERE user.userid = :userId");
        query.setParameter("userId", userId);
        int i = query.executeUpdate();
        if (i >= 1 ) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Set<IUser> getAllUser() throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT u FROM User u");
            List<?> userEntities = query.getResultList();

            // Get the user entities and add them to the set of users.
            Set<IUser> allUsers = new HashSet<IUser>();
            if(userEntities != null) {
                Iterator<?> iterator = userEntities.iterator();
                UserDirectoryFactory userDirectoryFactory = UserDirectoryFactory.newInstance();
                while (iterator.hasNext()) {

                    allUsers.add((userDirectoryFactory.createUserFromEntity((User) iterator.next())));
                }
            }
            return allUsers;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    @Transactional
    public boolean updateUser(String userId, String firstname, String lastname, String[] groups) throws DatabaseException {
        try {
            Query query = em.createQuery("UPDATE User SET firstname = :firstname, lastname = :lastname WHERE userid = :userId");
            query.setParameter("firstname", firstname);
            query.setParameter("lastname", lastname);
            query.setParameter("userId", userId);

            int i = query.executeUpdate();
            if (i >= 1 ) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void persistGroup(IGroup group) throws DatabaseException {

    }

    @Override
    @Transactional
    public IGroup getGroup(String groupName) throws DatabaseException {

        Query query = em.createNativeQuery("SELECT gr from Group gr where gr.groupname = :groupName");
        query.setParameter("groupName", groupName);

        //since group names are supposed to be unique only one result is expected

            Group groupEntity = null;
        try {
            groupEntity = (Group) query.getSingleResult();

        } catch (Exception e) {
            return null;
        }
        if (groupEntity != null) {
            return UserDirectoryFactory.newInstance().createGroupFromEntity(groupEntity);
        }
            return null;

    }

    @Override
    @Transactional
    public IGroup createGroup(String groupName, String[] genericHumanRoles) throws DatabaseException {
        try {
            if (getGroup(groupName) == null) {
                IGroup group = new GroupWrapper(groupName);
                em.persist(group.getAdaptee());
                return group;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<String> getGroupNames() throws DatabaseException {

        return null;
    }

    @Override
    @Transactional
    public boolean deleteGroup(String groupName) throws DatabaseException {
        Query query = em.createQuery("DELETE FROM Group group WHERE group.groupname = :groupName");
        query.setParameter("groupName", groupName);
        int i = query.executeUpdate();
        if (i >= 1 ) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Set<IGroup> getAllGroups() throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT g FROM Group g");
            List<?> groupEntities = query.getResultList();

            //Get the group entities and add them th the set of groups.
            Set<IGroup> allGroups = new HashSet<IGroup>();
            if (groupEntities != null) {
                Iterator<?> iterator = groupEntities.iterator();
                UserDirectoryFactory userDirectoryFactory = UserDirectoryFactory.newInstance();
                while (iterator.hasNext()) {
                    allGroups.add((userDirectoryFactory.createGroupFromEntity((Group) iterator.next())));
                }
            }
            return allGroups;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    @Transactional
    public boolean updateGroup(String groupname, String genericHumanRole) throws DatabaseException {
        return false;
    }

    @Override
    public List<ITaskInstance> getNonFinalizedTaskInstances() throws DatabaseException {
        return null;
    }


}
