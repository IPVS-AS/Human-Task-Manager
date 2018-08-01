package com.htm.endpoint.impl;

import com.htm.db.IDataAccessProvider;
import com.htm.endpoint.IUsersService;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.security.IUserManager;
import com.htm.userdirectory.IUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementation of IUsersService
 */
@Service
@Configurable
public class UsersServiceImpl implements IUsersService {


    /**
     * Autowired user manager with methods implemented for use of OpenTOSCA
     */
    protected IUserManager userManagerTosca;

    /**
     * Autowired data access provider with methods implemented for use of OpenTOSCA
     */
    protected IDataAccessProvider dataAccessTosca;

    /**
     * Dummy password since this implementation does not support passwords
     */
    private final String DUMMY_PASSWORD = "password";


    /**
     * Creates a new user
     * @param userId
     *          userId of the new user
     * @param firstname
     *          first name of the new user
     * @param lastname
     *          last name of the new user
     * @param groups
     *          groups which the new user belongs to
     * @return
     *          id of the newly crated user as JSON-String
     */
    @Override
    public String createUser(String userId, String firstname, String lastname, String[] groups) {
        IUser user = null;
        JSONObject response = new JSONObject();
        try {
             user = userManagerTosca.getUser(userId);
            // if user is null, then userId is not taken by another role
            if (user == null) {
                user = userManagerTosca.addUser(userId, firstname, lastname, DUMMY_PASSWORD);
                response.put("id", user.getId());
                if (groups != null) {
                    for (int i = 0; i < groups.length; i++) {
                        dataAccessTosca.addUserToGroup(user.getUserId(), groups[i]);
                    }
                }

                return response.toString();
            }
            return "taken";
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * Gets the user with the given id
     * @param id
     *         userId of the user
     * @return
     *         JSON-String containing the attributes of id
     */
    @Override
    public String getUser(String id) {
        JSONObject response = new JSONObject();
        try {
            IUser user = userManagerTosca.getUser(id);
            if (user != null) {
                response.put("id", user.getId());
                response.put("userId", user.getUserId());
                response.put("firstname", user.getFirstName());
                response.put("lastname", user.getLastName());
                response.put("roles", dataAccessTosca.getUserAllGroups(id));
                return response.toString();
            }
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all users
     * @return
     *         JSON-Array as String with all users and their attributes
     */
    @Override
    public String getAllUsers() {
        try {
            Set<IUser> allUsers = dataAccessTosca.getAllUser();
            JSONArray response = new JSONArray();
            // if allUsers is null, then no users were found
            if (allUsers != null) {
                // create a JSON-Object for every user and add it to response
                for (IUser user : allUsers) {
                    JSONObject j = new JSONObject();
                    j.put("id", user.getId());
                    j.put("userId", user.getUserId());
                    j.put("firstname", user.getFirstName());
                    j.put("lastname", user.getLastName());
                    j.put("roles", dataAccessTosca.getUserAllGroups(user.getUserId()));
                    response.add(j);
                }
                return response.toString();
            }
            return null;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update user with given values
     * @param id
     *          userId of the user to be updated
     * @param firstname
     *          first name of the user
     * @param lastname
     *          last name of the user
     * @param groups
     *          groups which the user belongs to
     * @return
     *          true if update was successful
     */
    @Override
    public boolean updateUser(String firstname, String lastname, String[] groups, String id) {
        boolean response = false;
        try {
            response = dataAccessTosca.updateUser(id, firstname, lastname, groups);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Deletes an user with the given userId
     * @param id
     *         userId of user to be deleted
     * @return
     *         true if deletion was successful
     */
    @Override
    public boolean deleteUser(String id) {

        boolean deleted = false;
        try {
            deleted = userManagerTosca.deleteUser(id);
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return deleted;
    }


    @Autowired
    public void setiUserManager(IUserManager userManagerTosca) {
        this.userManagerTosca = userManagerTosca;
    }

    public IUserManager getiUserManager() {return userManagerTosca;}

    @Autowired
    public void setIDataAccessProvider(IDataAccessProvider dataAccessTosca) {
        this.dataAccessTosca = dataAccessTosca;
    }

    public IDataAccessProvider getIDataAccessProvider() {return dataAccessTosca;}
}
