package com.htm.db.spring;

import com.htm.dm.EHumanRoles;
import com.htm.entities.jpa.*;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.taskinstance.*;
import com.htm.taskinstance.jpa.*;
import com.htm.taskmodel.ILiteral;
import com.htm.taskmodel.ILogicalPeopleGroupDef;
import com.htm.taskmodel.ITaskModel;
import com.htm.taskmodel.ModelElementFactory;
import com.htm.taskmodel.jpa.LiteralWrapper;
import com.htm.userdirectory.IGroup;
import com.htm.userdirectory.IUser;
import com.htm.userdirectory.UserDirectoryFactory;
import com.htm.userdirectory.jpa.GroupWrapper;
import com.htm.userdirectory.jpa.UserWrapper;
import com.htm.utils.Utilities;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class implements the IDataAccessProvider in the way that it can be used for the REST-API.
 * It only implements the necessary methods and the new created ones.
 */
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

    /**
     * Gets the task with the given id from the database
     * @param tiid
     *          id of the task
     * @return
     *          ITaskInstance-Instance corresponding to given id
     * @throws DatabaseException
     * @throws IllegalArgumentException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ITaskInstance getTaskInstance(String tiid) throws DatabaseException, IllegalArgumentException {
        try {
            Query query = em.createQuery("SELECT hti FROM Humantaskinstance hti WHERE hti.id = :tiid");
            query.setParameter("tiid", Utilities.transfrom2PrimaryKey(tiid));
            // since tiid is unique there is only one result
            Humantaskinstance taskInstanceEntity = (Humantaskinstance) query.getSingleResult();

            if (taskInstanceEntity != null) {
                return this.taskInstanceFactory.createTaskInstanceFromEntity(taskInstanceEntity);
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

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

    /**
     * Gets all work items belonging to a task
     * @param tiid
     *         id of the task
     * @return
     *         all IWorkItem-Instances belonging to the task
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<IWorkItem> getWorkItems(String tiid) throws DatabaseException {
        try {

            Query query = em
                    .createQuery("SELECT wi FROM Workitem wi WHERE wi.humantaskinstance.id = :tiid");
            query.setParameter("tiid", Integer.valueOf(tiid));

            // Get the work item entities belonging to tiid and add them to the list of work items
            List<?> workItemEntities = (List<?>) query.getResultList();
            List<IWorkItem> workItems = new ArrayList<IWorkItem>();

            if (!workItemEntities.isEmpty()) {
                Iterator<?> iter = workItemEntities.iterator();

                while (iter.hasNext()) {
                    IWorkItem workItem = Utilities.createWorkItemFromEntity((Workitem) iter.next());
                    workItems.add(workItem);
                }
                return workItems;
            }
            return  null;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
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

    /**
     * Deletes a human task instance form the database
     * @param tiid
     *          id of the task instance
     * @return
     *          true if task instance was successfully deleted
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHumanTaskInstance(String tiid) throws DatabaseException {
        try {
            // delete all belonging inputParameters
            Query query = em.createQuery("DELETE FROM InputParameter i WHERE i.task.id = :id");
            query.setParameter("id", Integer.valueOf(tiid));
            query.executeUpdate();
            // delete all belonging outputParameters
            Query query1 = em.createQuery("DELETE FROM OutputParameter o WHERE o.task.id = :id");
            query1.setParameter("id", Integer.valueOf(tiid));
            query1.executeUpdate();
            // delete belonging workItem
            Query query2 = em.createQuery("DELETE FROM Workitem w WHERE w.humantaskinstance.id = :id");
            query2.setParameter("id", Integer.valueOf(tiid));
            int i = query2.executeUpdate();
            // if workItem was deleted successfully, then also delete the task instance
            if (i >= 1) {
                Query query3 = em.createQuery("DELETE FROM Humantaskinstance ti WHERE ti.id = :id");
                query3.setParameter("id", Integer.valueOf(tiid));
                int j = query3.executeUpdate();
                if (j >= 1) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
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

    /**
     * Gets the user with the given userId from the database
     * @param userId
     *          userId of the user
     * @return
     *          IUser-Instance corresponding to given userId
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
        // if userEntity is null, then a user with userId userId was not found
        if (userEntity != null) {

            return UserDirectoryFactory.newInstance().createUserFromEntity(userEntity);
        }
        return null;
    }

    /**
     * Puts a new user with the given values in the database
     * @param userId
     *          userId of user
     * @param firstname
     *          first name of user
     * @param lastname
     *          last name of user
     * @return
     *          IUser-Instance corresponding to the new user
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IUser createUser(String userId, String firstname, String lastname) {
        try {
            // if this statement is not null, then the userId is taken and the method returns null
            if (getUser(userId) == null) {
                // creating the new user
                IUser user = new UserWrapper();
                user.setUserId(userId);
                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setPassword(" ");
                em.persist(user.getAdaptee());

                IAssignedUser assignedUser = new AssignedUserWrapper();
                assignedUser.setUserID(userId);
                em.persist(assignedUser.getAdaptee());
                return user;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes the user with the given userId from the database
     * @param userId
     *          userId of the user to be deleted
     * @return
     *          true if user was successfully deleted
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String userId) throws DatabaseException {
        try {
        Query query = em.createQuery("DELETE FROM Assigneduser user WHERE user.userid = :userId");
        query.setParameter("userId", userId);
        int i = query.executeUpdate();
        if (i < 1 ) {

                throw new Exception();
        }
        Query query1 = em.createQuery("DELETE FROM User user WHERE user.userid = :userId");
        query1.setParameter("userId", userId);
        int j = query1.executeUpdate();
        if (j >= 1 ) {
            return true;
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets all users from the database
     * @return
     *         Set containing all users from the database
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<IUser> getAllUser() throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT u FROM User u");
            List<?> userEntities = query.getResultList();

            // Get the user entities and add them to the set of users.
            Set<IUser> allUsers = new HashSet<IUser>();
            if(!userEntities.isEmpty()) {
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

    /**
     * Updates the user belonging to userId with the given values
     * @param userId
     *          userId of user to be updated
     * @param firstname
     *          first name of user
     * @param lastname
     *          last name of user
     * @param groups
     *          groups which the user belongs to
     * @return
     *          true if the update was successful
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(String userId, String firstname, String lastname, String[] groups) throws DatabaseException {
        // update user with all given parameters
        try {
            Query query = em.createQuery("UPDATE User SET firstname = :firstname, lastname = :lastname WHERE userid = :userId");
            query.setParameter("firstname", firstname);
            query.setParameter("lastname", lastname);
            query.setParameter("userId", userId);
            int i = query.executeUpdate();
            if (i < 1) {
                return false;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        // add user to all groups in parameter group
        // user is still member of groups which she was before
        try {

           for (int i = 0; i < groups.length; i++) {
                addUserToGroup(userId, groups[i]);
            }
            return true;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Adds an user to a group
     * @param userId
     *         userId of the user to be added
     * @param groupName
     *          group name of the group
     * @return
     *          true if user is added to group or was already a member
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUserToGroup(String userId, String groupName) throws DatabaseException {
        IUser user = getUser(userId);
        IGroup group = getGroup(groupName);
        // get all IDs of groups where the user is already belonging to
        Query query = em.createQuery("SELECT ug.id.groupId FROM UsersGroups ug WHERE ug.id.userId = :userId");
        query.setParameter("userId", Integer.valueOf(user.getId()));
        List<?> groupIds = query.getResultList();
        // if groupIds is not null, then check for each ID if user is already a member of the new group
        if (!groupIds.isEmpty()) {
            Iterator it = groupIds.iterator();
            while (it.hasNext()) {
                // if user is already member of the new group then return true
               if (group.getId().equals(it.next().toString())) {
                    return true;
                }
            }
        }

        if ((user != null) && (group != null)) {
            // if user is not already member of the new group, add user as member of the new group and return true
            group.addMember(user);
            em.persist(group.getAdaptee());
            return true;
        }
        return false;
    }

    /**
     * Gets all users belonging to a group from database
     * @param groupName
     *          name of group whose members to be returned
     * @return
     *          set containing all users belonging to the group
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<IUser> getUserByGroup(String groupName) throws DatabaseException {
        IGroup group = getGroup(groupName);
        // get all user_IDs from users belonging to group
        Query query = em.createQuery("SELECT ug.id.userId FROM UsersGroups ug WHERE ug.id.groupId = :groupId");
        query.setParameter("groupId", Integer.valueOf(group.getId()));
        List<?> userIds = query.getResultList();

        // Get the user entities belonging to the IDs and add them to the set of users
        Set<IUser> allUserByGroup = new HashSet<IUser>();
        if (!userIds.isEmpty()) {
            Iterator it = userIds.iterator();
            UserDirectoryFactory userDirectoryFactory = UserDirectoryFactory.newInstance();
            while (it.hasNext()) {
                Query query1 = em.createQuery("SELECT u FROM User u WHERE u.id = :id");
                query1.setParameter("id", it.next());
                User userEntity = (User) query1.getSingleResult();
                allUserByGroup.add(userDirectoryFactory.createUserFromEntity(userEntity));
            }
        }
        return allUserByGroup;
    }

    /**
     * Gets all group names of groups which an user is a member of
     * @param userId
     *          userId of the user
     * @return
     *          set of group names of groups which the user is a member of
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<String> getUserAllGroups(String userId) throws DatabaseException {
        IUser user = getUser(userId);
        // get all group_IDs from groups which the user is a member of
        Query query = em.createQuery("SELECT ug.id.groupId FROM UsersGroups ug WHERE ug.id.userId = :userId");
        query.setParameter("userId", Integer.valueOf(user.getId()));
        List<?> groupIds = query.getResultList();

        // Get the group names belonging to the IDs and add them to the set of group names
        Set<String> allGroupsUser = new HashSet<String>();
        if (!groupIds.isEmpty()) {
            Iterator it = groupIds.iterator();
            while (it.hasNext()) {
                Query query1 = em.createQuery("SELECT gr.groupname FROM Group gr WHERE gr.id = :id");
                query1.setParameter("id", (Integer) it.next());
                String groupName = (String) query1.getSingleResult();
                 allGroupsUser.add(groupName);
                }
            }
        return allGroupsUser;

    }

    @Override
    public void persistGroup(IGroup group) throws DatabaseException {

    }

    /**
     * Gets the group with the given group name from the database
     * @param groupName
     *          name of the group
     * @return
     *         IGroup-Instance corresponding to the new group
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IGroup getGroup(String groupName) throws DatabaseException {

        Query query = em.createQuery("SELECT gr from Group gr where gr.groupname = :groupname");
        query.setParameter("groupname", groupName);

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

    /**
     * Puts a new group with the given values in the database
     *
     * @param groupName
     *          name of the group
     * @param genericHumanRoles
     *          name of the generic human roles to be added
     * @return
     *        IGroup-Instance corresponding to the new group
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IGroup createGroup(String groupName, String[] genericHumanRoles) throws DatabaseException {
        try {
            // if this statement is not null, then the groupName is taken and the method returns null
            if (getGroup(groupName) == null) {
                // insert new role into role table
                IGroup group = new GroupWrapper(groupName);


                // insert for every new group-genericHumanRole mapping a new entry in literal
                if(genericHumanRoles != null) {
                    for (int j = 0; j < genericHumanRoles.length; j++) {
                        EHumanRoles humanRole = checkHumanRole(genericHumanRoles[j]);
                        if (humanRole != null) {
                            ILiteral literal = new LiteralWrapper();
                            literal.setOrganizationalEntityId(groupName);
                            literal.setGenericHumanRole(humanRole);
                            em.persist(literal.getAdaptee());

                        } else {
                            throw new Exception();
                            //return null;
                        }
                    }
                }
                em.persist(group.getAdaptee());
                return group;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return null;
    }

    @Override
    public Set<String> getGroupNames() throws DatabaseException {

        return null;
    }

    /**
     * Deletes the group with the given groupName from the database
     * @param groupName
     *          name of group to be deleted
     * @return
     *          true if group was successfully deleted
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroup(String groupName) throws DatabaseException {
        try {
            // Delete all entiries from LITERAL where the group is enityidentifier
            Query query = em.createQuery("DELETE FROM Literal li WHERE li.entityidentifier = :groupname");
            query.setParameter("groupname", groupName);
            int i = query.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        try {
            // Delete the group from GROUP-Table
            Query query1 = em.createQuery("DELETE FROM Group group WHERE group.groupname = :groupname");
            query1.setParameter("groupname", groupName);
            int j = query1.executeUpdate();
            if (j >= 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Gets all groups from the database
     * @return
     *         Set containing all groups from the database
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<IGroup> getAllGroups() throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT g FROM Group g");
            List<?> groupEntities = query.getResultList();

            //Get the group entities and add them th the set of groups.
            Set<IGroup> allGroups = new HashSet<IGroup>();
            if (!groupEntities.isEmpty()) {
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

    /**
     * Updates the group belonging to groupname with the given values
     * The method overrides the old values with the new
     * @param groupname
     *          name of the group
     * @param genericHumanRole
     *          generic human roles the group is mapped to
     * @return
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroup(String groupname, String[] genericHumanRole) throws DatabaseException {
        try {
            // Delete all entries in table LITERAL, where Group is enitiyidentifier
            Query query = em.createQuery("DELETE FROM Literal li WHERE li.entityidentifier = :groupname");
            query.setParameter("groupname", groupname);
            int i = query.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
            // Now insert for every new group-genericHumanRole mapping a new entry
        try {
                for (int j = 0; j < genericHumanRole.length; j++) {
                    EHumanRoles humanRole = checkHumanRole(genericHumanRole[j]);
                    if(humanRole != null) {
                        ILiteral literal = new LiteralWrapper();
                        literal.setOrganizationalEntityId(groupname);
                        literal.setGenericHumanRole(humanRole);
                        em.persist(literal.getAdaptee());

                    } else {
                        throw new Exception();
                    }
                }
                return true;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }


    }

    @Override
    public List<ITaskInstance> getNonFinalizedTaskInstances() throws DatabaseException {
        return null;
    }

    /**
     * Gets all mapped generic human roles of a group
     * @param groupname
     *          name of the group
     * @return
     *          set of mapped generic human roles
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<String> getGenericHumanRolesByGroup(String groupname) throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT li FROM Literal li WHERE li.entityidentifier = :groupname");
            query.setParameter("groupname", groupname);
            List<?> literalEntities = query.getResultList();

            // Get the literal enitities and add their human role to the set of Human roles of role groupname
            Set<String> allHumanRolesGroup = new HashSet<>();
            if (!literalEntities.isEmpty()) {
                Iterator<?> iterator = literalEntities.iterator();
                ModelElementFactory modelElementFactory = ModelElementFactory.newInstance();
                while (iterator.hasNext()) {
                    allHumanRolesGroup.add( modelElementFactory.createLiteral((Literal) iterator.next()).getGenericHumanRole().name());
                }
            }
            return allHumanRolesGroup;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * Gets the task type wit the given name form the database
      * @param taskTypeName
     *          name of the task type
     * @return
     *          ITaskType-Instance corresponding to the new task type
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ITaskType getTaskType(String taskTypeName) throws DatabaseException {

        Query query = em.createQuery("SELECT t from TaskType t where t.taskTypeName = :taskTypeName");
        query.setParameter("taskTypeName", taskTypeName);

        //since task type names are supposed to be unique only one result is expected

        TaskType taskTypeEntity = null;
        try {
            taskTypeEntity = (TaskType) query.getSingleResult();

        } catch (Exception e) {
            return null;
        }
        if (taskTypeEntity != null) {

            Utilities.isValidClass(taskTypeEntity, TaskType.class);
            return new TaskTypeWrapper((TaskType) taskTypeEntity);
        }
        return null;
    }

    /**
     * Puts a new task type with the given values in the database
     * @param tasktypeName
     * @return
     *          ITaskType-Instance corresponding to the new task type
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ITaskType createTaskType(String tasktypeName) throws DatabaseException {
        try {
            if(getTaskType(tasktypeName) == null) {
                ITaskType taskType = new TaskTypeWrapper(tasktypeName);
                em.persist(taskType.getAdaptee());
                return taskType;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return null;
    }

    /**
     * Deletes the task type with the given tasktypeName from the database
     * @param tasktypeName
     *          name of the task type to be deleted
     * @return
     *          true if task type was successfully deleted
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTaskType(String tasktypeName) throws DatabaseException {
        Query query = em.createQuery("DELETE FROM TaskType tt WHERE tt.taskTypeName = :taskTypeName");
        query.setParameter("taskTypeName",tasktypeName);
        int i = query.executeUpdate();
        if (i >= 1) {
            return true;
        }
        return false;
    }

    /**
     * Gets all task types form the database
     * @return
     *        Set containing all task types from the database
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<ITaskType> getAllTaskTypes() throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT tt FROM TaskType tt");
            List<?> taskTypeEntities = query.getResultList();

            //Get the task type entities and add them to the set of task types
            Set<ITaskType> allTaskTypes = new HashSet<>();
            if (!taskTypeEntities.isEmpty()) {
                Iterator it = taskTypeEntities.iterator();
                while (it.hasNext()) {
                    TaskType taskType = (TaskType) it.next();
                    Utilities.isValidClass(taskType, TaskType.class);
                    allTaskTypes.add(new TaskTypeWrapper(taskType));
                }
            }
            return allTaskTypes;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Updates the task type belonging to tasktypename with the given values
     * @param tasktypename
     *          name of the task type to be updated
     * @param groups
     *          groups which are associated to task type
     * @return
     *          true if the update was successful
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTaskType(String tasktypename, String[] groups) throws DatabaseException {
        boolean updated = false;
        // add task types to all groups in parameter group
        // task type type is still associated with groups which it was before
        try {

            for (int i = 0; i < groups.length; i++) {
                addGroupToTaskType(tasktypename, groups[i]);
            }
            updated = true;
            return updated;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * Adds a group to a task Type
     * @param taskTypeName
     *         task type name of the task type
     * @param groupName
     *         group name of the group to be added
     * @return
     *         true if group is added to task type or was already mapped to it
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addGroupToTaskType(String taskTypeName, String groupName) throws DatabaseException {
        ITaskType taskType = getTaskType(taskTypeName);
        IGroup group = getGroup(groupName);
        // get all IDs of groups where the task type is already associated to
        Query query = em.createQuery("SELECT tg.id.taskTypeId FROM TaskTypeGroups tg WHERE tg.id.groupId = :groupId");
        query.setParameter("groupId", Integer.valueOf(group.getId()));
        List<?> taskTypeIds = query.getResultList();
        // if taskTypeIds is not null, then check for each ID if group is already associated with task type
        if(!taskTypeIds.isEmpty()) {
            Iterator it = taskTypeIds.iterator();
            while (it.hasNext()) {
                if (taskType.getId().equals(it.next().toString())) {
                    return true;
                }
            }
        }

        if ((taskType != null) && (group != null)) {
            // if task type is not already associated with the new group, add group as associated group and return true
            taskType.addGroup(group);
            em.persist(taskType.getAdaptee());
            return true;
        }

        return false;
    }

    /**
     * Gets all group names of groups which are mapped to task type
     * @param taskTypeName
     *          name of the task type
     * @return
     *          Set of group names associated with task type
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<String> getTaskTypeAllGroups(String taskTypeName) throws DatabaseException {
        ITaskType taskType = getTaskType(taskTypeName);
        // get all group_IDs from groups which the task type is associated to
        Query query = em.createQuery("SELECT tg.id.groupId FROM TaskTypeGroups tg WHERE tg.id.taskTypeId = :taskTypeId");
        query.setParameter("taskTypeId", Integer.valueOf(taskType.getId()));
        List<?> groupId = query.getResultList();

        //Get the group names belonging to the IDs and add them to the set of group names
        Set<String> allGroupsTaskType = new HashSet<>();
        if (!groupId.isEmpty()) {
            Iterator it = groupId.iterator();
            while (it.hasNext()) {
                Query query1 = em.createQuery("SELECT gr.groupname FROM Group gr WHERE gr.id = :id");
                query1.setParameter("id", (Integer) it.next());
                String groupname = (String) query1.getSingleResult();
                allGroupsTaskType.add(groupname);
            }
        }
        return allGroupsTaskType;
    }

    /**
     * Puts a new task with the given values in the database
     * @param name
     *          name of the task
     * @param title
     *          title of the task which will be shown to user in app
     * @param subject
     *          subject of the task which will be shown to user in app
     * @param description
     *          description of the task which will be shown to user in app
     * @param priority
     *          priority of the task
     * @return
     *          ITaskInstance corresponding to the new task
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ITaskInstance createTask(String name,String title, String subject, String description, String priority, String[] taskTypeNames) throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT ti FROM Humantaskinstance ti WHERE ti.name = :name");
            query.setParameter("name", name);
            List<?> taskInstance = query.getResultList();
            if (taskInstance.isEmpty()) {
                // create task instance
                ITaskInstance task = new TaskInstanceWrapper(name);
                Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
                task.setCreatedOn(timestamp);
                task.setPriority(Integer.valueOf(priority));
                task.setPresentationTitle(title);
                task.setPresentationSubject(subject);
                task.setPresentationDescription(description);
                em.persist(task.getAdaptee());


                //create belonging workItem
                IWorkItem workItem = new WorkItemWrapper();
                workItem.setClaimed(false);
                workItem.setGenericHumanRole(EHumanRoles.POTENTIAL_OWNER);
                workItem.setCreationTime(timestamp);
                workItem.setAssignedToEverbody(false);
                workItem.setTaskInstance(task);
                em.persist(workItem.getAdaptee());

                //map task types to task
                try {
                    for (int x = 0; x < taskTypeNames.length; x++ ) {
                        addTaskToTaskType(task.getId(), taskTypeNames[x]);
                    }
                } catch (Exception e) {
                    throw new DatabaseException(e);
                } finally {
                    return task;
                }

            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Puts a new input parameter with the given values in the database
     * @param key
     *          label of the input parameter
     * @param value
     *          value of the input parameter
     * @param tiid
     *          task to which the input parameter belongs to
     * @return
     *          true if successfully created
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInputParameter(String key, String value, String tiid) throws DatabaseException {
        try {
            ITaskInstance task = getTaskInstance(tiid);
            if (task == null) {
                return false;
            }
            IInputParameter inputParameter = new InputParameterWrapper(key);
            inputParameter.setLabel(key);
            inputParameter.setValue(value);
            inputParameter.setTask(task);
            em.persist(inputParameter.getAdaptee());
            return true;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Puts a new input parameter with the given values in the database
     * @param key
     *          label of the output parameter
     * @param value
     *          value of the output parameter
     * @param tiid
     *          task to which the input parameter belongs to
     * @return
     *          true if successfully created
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOutputParameter(String key, String value, String tiid) throws DatabaseException {
        try {
            ITaskInstance task = getTaskInstance(tiid);
            if (task == null) {
                return false;
            }
            IOutputParameter outputParameter = new OutputParameterWrapper(key);
            outputParameter.setLabel(key);
            outputParameter.setValue(value);
            outputParameter.setTask(task);
            em.persist(outputParameter.getAdaptee());
            return true;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Adds a task to a task type
     * @param tiid
     *          id of the new task
     * @param taskTypeName
     *          task type name of the task type
     * @return
     *          ture if task was successfully mapped
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTaskToTaskType(String tiid, String taskTypeName) throws DatabaseException {
        ITaskType taskType = getTaskType(taskTypeName);
        ITaskInstance task = getTaskInstance(tiid);
        // get all IDs of tasks where the task type is already associated to
        Query query = em.createQuery("SELECT tt.id.taskTypeId FROM TaskTypeTasks tt WHERE tt.id.taskId = :taskId");
        query.setParameter("taskId", Integer.valueOf(tiid));
        List<?> taskTypeIds = query.getResultList();
        // if taskTypeIds is not null, then check for each ID if task is already associated with task type
        if (!taskTypeIds.isEmpty()) {
            Iterator it = taskTypeIds.iterator();
            while (it.hasNext()) {
                if (taskType.getId().equals(it.next().toString())) {
                    return true;
                }
            }
        }

        if ((taskType != null) && (tiid != null)) {
            // if taskTypeIds is not null, then check for each ID if task is already associated with task type
            taskType.addTask(task);
            em.persist(taskType.getAdaptee());
            return true;
        }
        return false;
    }

    /**
     * Gets all input parameters belonging to a task
     * @param tiid
     *          id of the task
     * @return
     *          set of input parameter belonging to task
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<IInputParameter> getInputParametersByTask(String tiid) throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT i FROM InputParameter i WHERE i.task.id = :tiid");
            query.setParameter("tiid", Integer.valueOf(tiid));
            // get all input parameter entities which task is associated to
            List<?> inputParameterEntities = query.getResultList();
            // get the instances of input parameter and add them to set of input parameters
            Set<IInputParameter> inputParameters = new HashSet<>();
            if (!inputParameterEntities.isEmpty()) {
                Iterator it = inputParameterEntities.iterator();
                while (it.hasNext()) {
                    InputParameter input = (InputParameter) it.next();
                    Utilities.isValidClass(input, InputParameter.class);
                    inputParameters.add(new InputParameterWrapper(input));
                }
            }
            return inputParameters;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Gets all output parameters belonging to a task
     * @param tiid
     *          tiid of the task
     * @return
     *          set of output parameter belonging to task
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<IOutputParameter> getOutputParametersByTask(String tiid) throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT o FROM OutputParameter o WHERE o.task.id = :tiid");
            query.setParameter("tiid",Integer.valueOf(tiid));
            // get all output parameter entities which task is associated to
            List<?> outputParameterEntities = query.getResultList();
            // get the instances of output parameter and add them to set of output parameters
            Set<IOutputParameter> outputParameters = new HashSet<>();
            if (!outputParameterEntities.isEmpty()) {
                Iterator it = outputParameterEntities.iterator();
                while (it.hasNext()) {
                    OutputParameter output = (OutputParameter) it.next();
                    Utilities.isValidClass(output, OutputParameter.class);
                    outputParameters.add(new OutputParameterWrapper(output));
                }
                return outputParameters;
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Gets all tasks from the database
     * @return
     *          Set containing all tasks from the database
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<ITaskInstance> getAllTasks() throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT ti FROM Humantaskinstance ti");
            List <?> taskEnitities = query.getResultList();

            //Get the task entities and add them to the set of tasks
            Set<ITaskInstance> allTasks = new HashSet<>();
            if (!taskEnitities.isEmpty()) {
                Iterator it = taskEnitities.iterator();
                while (it.hasNext()){
                    allTasks.add(this.taskInstanceFactory.createTaskInstanceFromEntity((Humantaskinstance) it.next()));
                }
                return allTasks;
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * Gets all tasks from database assigned to an user
     * @param userId
     *          userId of user
     * @return
     *          set of tasks assigned to the user
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<ITaskInstance> getTasksByUser(String userId) throws DatabaseException {
        try {
            IUser user = getUser(userId);
            Query query = em.createQuery("SELECT wi.humantaskinstance.id FROM Workitem wi WHERE wi.assigneduser.userid = :userId");
            query.setParameter("userId", userId);

            List <?> taskIds = query.getResultList();

            //Get the task entities and add them to the set of tasks
            Set<ITaskInstance> allTasks = new HashSet<>();
            if (!taskIds.isEmpty()) {
                Iterator it = taskIds.iterator();
                while (it.hasNext()){
                    allTasks.add(getTaskInstance(it.next().toString()));
                }
                return allTasks;
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Gets all tasks from database assigned to a taskType
     * @param taskTypeName
     *          name of the task type
     * @return
     *          set of tasks assigned to the task type
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<ITaskInstance> getTasksByTaskType(String taskTypeName) throws DatabaseException {
        try {
            ITaskType taskType = getTaskType(taskTypeName);
            Query query = em.createQuery("SELECT tt FROM TaskTypeTasks tt WHERE tt.id.taskTypeId = :taskTypeId");
            query.setParameter("taskTypeId", Integer.valueOf(taskType.getId()));

            List <?> taskIds = query.getResultList();

            //Get the task entities and add them to the set of tasks
            Set<ITaskInstance> allTasks = new HashSet<>();
            if (!taskIds.isEmpty()) {
                Iterator it = taskIds.iterator();
                while (it.hasNext()){
                    allTasks.add(getTaskInstance((String) it.next()));
                }
                return allTasks;
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Updates the output parameters of a task with the given values
     * @param task
     *          id of the task
     * @param outputparameters
     *          values and labels of the output parameters
     * @return
     *          true if update was successful
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOutputParameter(String task, HashMap outputparameters) throws DatabaseException {
        //update each value of a label with the new value
        try {
            Set<String> labels = outputparameters.keySet();
            for (String label: labels) {
                Query query = em.createQuery("UPDATE OutputParameter SET value = :val WHERE label = :label AND task.id = :id");
                query.setParameter("val", (String) outputparameters.get(label));
                query.setParameter("label", label);
                query.setParameter("id", Integer.valueOf(task));
                int i = query.executeUpdate();
                if (i < 1) {
                    throw new Exception();
                }
            }
            return true;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * updates the task belonging to id with the given values
     * @param id
     *          id of the task to be updated
     * @param name
     *          name of the task
     * @param taskTypeNames
     *          name of the task type of the task
     * @param state
     *          state of the task
     * @param inputParameter
     *          input parameters of the task
     * @param outputParameter
     *          output parameters of the task
     * @param title
     *          title of the task shown to the user
     * @param subject
     *          subject of the task shown to the user
     * @param description
     *          description of the task shown to the user
     * @param priority
     *          priority of the task
     * @return
     *          true if update was successful
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(String id, String name, String[] taskTypeNames, String state, HashMap inputParameter, HashMap outputParameter, String title, String subject, String description, String priority, String claimed) throws DatabaseException {
        try {
            if (claimed != null) {
                claimTask(id, claimed);
            }
            // update task instance
            Query query = em.createQuery("UPDATE Humantaskinstance SET name = :name, presentationdescription = :description, " +
                    "presentationname = :title, presentationsubject = :subject ,priority= :priority WHERE id = :id");
            query.setParameter("name", name);
            query.setParameter("description", description.getBytes());
            query.setParameter("title", title);
            query.setParameter("subject", subject);
            query.setParameter("priority", Integer.valueOf(priority));
            query.setParameter("id", Integer.valueOf(id));
            int i = query.executeUpdate();
            if (i < 1) {
                throw new Exception();
            }
            //update state of task
            try {
                boolean updateState = updateState(id, state);
                if (!updateState) {
                    throw new Exception();
                }

            } catch (Exception e) {
                throw new DatabaseException(e);
            }
            // add task to task type with name in taskTypeName
            // task is still associated with task types it was before
            try {
                for (int x = 0; x < taskTypeNames.length; x++ ) {
                    addTaskToTaskType(id, taskTypeNames[x]);
                }
            } catch (Exception e) {
                throw new DatabaseException(e);
            }
           // delete input parameters if they not null
           if(inputParameter != null) {
                Query query1 = em.createQuery("DELETE FROM InputParameter i WHERE i.task.id = :id");
                query1.setParameter("id",Integer.valueOf(id));
               int j = query.executeUpdate();
               if (j < 1) {
                   throw new Exception();
               }
               Set<String> labels = inputParameter.keySet();
               for (String label : labels) {
                   createInputParameter(label,(String) inputParameter.get(label), id);
               }
           }
            // delete output parameters if they not null
            if(outputParameter != null) {
                Query query1 = em.createQuery("DELETE FROM OutputParameter o WHERE o.task.id = :id");
                query1.setParameter("id",Integer.valueOf(id));
                int j = query.executeUpdate();
                if (j < 1) {
                    throw new Exception();
                }
                Set<String> labels = outputParameter.keySet();
                for (String label : labels) {
                    createInputParameter(label,(String) outputParameter.get(label), id);
                }
            }
            return true;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * Updates the state of a task in the database
     * @param tiid
     *          id of the task
     * @param state
     *          new state of the task
     * @return
     *          true if update was successful
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateState(String tiid, String state) throws DatabaseException {
        try {
            ETaskInstanceState newState = checkState(state);
            if (newState != null) {
                //Check if it is allowed to change to the new state
                ITaskInstance task = getTaskInstance(tiid);
                if (newState.name().equals(task.getStatus().name())) {
                    return true;
                }
                boolean canBeChangedTo = task.getStatus().canBeChangedTo(newState);
                if (canBeChangedTo) {
                    Query query = em.createQuery("UPDATE Humantaskinstance SET status = :state WHERE id = :id");
                    query.setParameter("state", newState.name());
                    query.setParameter("id", Integer.valueOf(tiid));
                    int i = query.executeUpdate();
                    if (i >= 1) {
                        return true;
                    }
                    return false;
                } else {
                    throw new DatabaseException("new State is not a valid state");
                }

            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return false;
    }

    /**
     * Claims a task for a user with the given userId
     * @param tiid
     *          id of the task
     * @param userId
     *          userId of the user
     * @return
     *          true if claim was successful
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean claimTask(String tiid, String userId) throws DatabaseException {
        try {
            List<IWorkItem> workItem = getWorkItems(tiid);
            if (workItem.get(0).isClaimed()) {
                throw new DatabaseException("Task is already claimed");
            }
                Query query = em.createQuery("SELECT u FROM Assigneduser u WHERE u.userid = :userId");
                query.setParameter("userId", userId);
                Assigneduser assignedUserEntity = (Assigneduser) query.getSingleResult();
                IAssignedUser assignedUser = new AssignedUserWrapper(assignedUserEntity);
                workItem.get(0).setAssignee(assignedUser);
                workItem.get(0).setClaimed(true);
                em.persist(workItem.get(0).getAdaptee());
                return true;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * Gets all task type names which are mapped to a task
     * @param tiid
     *          id of the task
     * @return
     *          set of task type names associated with task
     * @throws DatabaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Set<String> getTaskTypeByTask(String tiid) throws DatabaseException {
        ITaskInstance task = getTaskInstance(tiid);
        //get all task type id belonging to task
        Query query = em.createQuery("SELECT tt.id.taskTypeId FROM TaskTypeTasks tt WHERE tt.id.taskId = :id");
        query.setParameter("id", Integer.valueOf(tiid));
        List<?> taskTypeIds = query.getResultList();

        //Get the task type id and add them to the set of task type names
        Set<String> allTaskTypeTasks = new HashSet<>();
        if (!taskTypeIds.isEmpty()) {
            Iterator it = taskTypeIds.iterator();
            while (it.hasNext()) {
                Query query1 = em.createQuery("SELECT t.taskTypeName FROM TaskType t WHERE t.id = :id");
                query1.setParameter("id", (Integer) it.next());
                String taskTypeName = (String) query1.getSingleResult();
                allTaskTypeTasks.add(taskTypeName);
            }
        }
        return allTaskTypeTasks;
    }


    /**
     * Checks if a value of a String belongs to a possible human role
     * @param possibleHumanRole
     *          String to be checked
     * @return
     *          corresponding human role
     */
    public EHumanRoles checkHumanRole(String possibleHumanRole){
        possibleHumanRole = possibleHumanRole.toUpperCase();
        for (EHumanRoles humanRole : EHumanRoles.values()) {
            if (humanRole.name().equals(possibleHumanRole)) {
                return humanRole;
            }
        }
        return null;
    }

    /**
     * Checks if a value of a String belongs to a possible state
     * @param possibleState
     *          String to be checked
     * @return
     *          corresponding state
     */
    public ETaskInstanceState checkState(String possibleState) {
        possibleState = possibleState.toUpperCase();
        for (ETaskInstanceState state : ETaskInstanceState.values()) {
            if (state.name().equals(possibleState)) {
                return state;
            }
        }
        return null;
    }


}
