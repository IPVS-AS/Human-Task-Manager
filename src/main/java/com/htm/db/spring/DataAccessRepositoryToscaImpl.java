package com.htm.db.spring;

import com.htm.dm.EHumanRoles;
import com.htm.entities.jpa.Group;
import com.htm.entities.jpa.Literal;
import com.htm.entities.jpa.User;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.taskinstance.*;
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
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

    /**
     * Gets the user with the given userId from the database
     * @param userId
     *          userId of the user
     * @return
     *          IUser-Instance corresponding to given userId
     * @throws DatabaseException
     */
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
    @Transactional
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
                return user;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete the user with the given userId from the database
     * @param userId
     *          userId of the user to be deleted
     * @return
     *          true if user was successfully deleted
     * @throws DatabaseException
     */
    @Override
    @Transactional
    public boolean deleteUser(String userId) throws DatabaseException {
        IUser user = getUser(userId);
        Query query = em.createQuery("DELETE FROM User user WHERE user.userid = :userId");
        query.setParameter("userId", userId);
        int i = query.executeUpdate();
        if (i >= 1 ) {
            return true;
        }
        return false;
    }

    /**
     * Get all users from the database
     * @return
     *         Set containing all users from the database
     * @throws DatabaseException
     */
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

    /**
     * updates the user belonging to userId with the given values
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
    @Transactional
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
    @Transactional
    public boolean addUserToGroup(String userId, String groupName) throws DatabaseException {
        IUser user = getUser(userId);
        IGroup group = getGroup(groupName);
        // get all IDs of groups where the user is already belonging to
        Query query = em.createQuery("SELECT ug.id.groupId FROM UsersGroups ug WHERE ug.id.userId = :userId");
        query.setParameter("userId", Integer.valueOf(user.getId()));
        List<?> groupIds = query.getResultList();
        // if groupIds is not null, then check for each ID if user is already a member of the new group
        if (groupIds != null) {
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
     * gets all users belonging to a group from database
     * @param groupName
     *          name of group whose members to be returned
     * @return
     *          Set containing all users belonging to the group
     * @throws DatabaseException
     */
    @Override
    @Transactional
    public Set<IUser> getUserByGroup(String groupName) throws DatabaseException {
        IGroup group = getGroup(groupName);
        // get all user_IDs from users belonging to group
        Query query = em.createQuery("SELECT ug.id.userId FROM UsersGroups ug WHERE ug.id.groupId = :groupId");
        query.setParameter("groupId", Integer.valueOf(group.getId()));
        List<?> userIds = query.getResultList();

        // Get the user entities belonging to the IDs and add them to the set of users
        Set<IUser> allUserByGroup = new HashSet<IUser>();
        if (userIds != null) {
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
     *          Set of group names of groups which the user is a member of
     * @throws DatabaseException
     */
    @Override
    @Transactional
    public Set<String> getUserAllGroups(String userId) throws DatabaseException {
        IUser user = getUser(userId);
        // get all group_IDs from groups which the user is a member of
        Query query = em.createQuery("SELECT ug.id.groupId FROM UsersGroups ug WHERE ug.id.userId = :userId");
        query.setParameter("userId", Integer.valueOf(user.getId()));
        List<?> groupIds = query.getResultList();

        // Get the group names belonging to the IDs and add them to the set of group names
        Set<String> allGroupsUser = new HashSet<String>();
        if (groupIds != null) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
     * @param genericHumanRoles
     * @return
     *        IGroup-Instance corresponding to the new group
     * @throws DatabaseException
     */
    @Override
    @Transactional
    public IGroup createGroup(String groupName, String[] genericHumanRoles) throws DatabaseException {
        try {
            // if this statement is not null, then the groupName is taken and the method returns null
            if (getGroup(groupName) == null) {
                // insert new role into role table
                IGroup group = new GroupWrapper(groupName);
                em.persist(group.getAdaptee());

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
                            return null;
                        }
                    }
                }
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

    /**
     * Delete the group with the given groupName from the database
     * @param groupName
     *          name of group to be deleted
     * @return
     *          true if group was successfully deleted
     * @throws DatabaseException
     */
    @Override
    @Transactional
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
     * Get all groups from the database
     * @return
     *         Set containing all groups from the database
     * @throws DatabaseException
     */
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
    /**
     * updates the user belonging to userId with the given values
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
    @Transactional
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
                        return false;
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
     *          Set of mapped generic human roles
     * @throws DatabaseException
     */
    @Override
    @Transactional
    public Set<String> getGenericHumanRolesByGroup(String groupname) throws DatabaseException {
        try {
            Query query = em.createQuery("SELECT li FROM Literal li WHERE li.entityidentifier = :groupname");
            query.setParameter("groupname", groupname);
            List<?> literalEntities = query.getResultList();

            // Get the literal enitities and add their human role to the set of Human roles of role groupname
            Set<String> allHumanRolesGroup = new HashSet<>();
            if (literalEntities != null) {
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
     * Checks if a value of a String belongs to a possible human role
     * @param possibleHumanRole
     *          Stirng to be checked
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


}
