package com.htm.endpoint.impl;

import com.htm.db.IDataAccessProvider;
import com.htm.endpoint.IRolesService;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.security.IUserManager;
import com.htm.userdirectory.IGroup;
import com.htm.userdirectory.IUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;


import java.util.Set;

/**
 * Implementation of IRolesService
 */
@Service
@Configurable
public class RolesServiceImpl implements IRolesService {

    /**
     * Autowired user manager with methods implemented for use of OpenTOSCA
     */
    protected IUserManager userManagerTosca;

    /**
     * Autowired data access provider with methods implemented for use of OpenTOSCA
     */
    protected IDataAccessProvider dataAccessTosca;

    /**
     * Creates a new role/group
     * @param roleName
     *          name of the new role/group
     * @param genericHumanRoles
     *          generic human roles mapped to the new role/group
     * @return
     *          id of the newly crated role as JSON-String
     */
    @Override
    public String createRole(String roleName, String[] genericHumanRoles) {
        IGroup group = null;
        JSONObject response = new JSONObject();
        try {
            group = userManagerTosca.getGroup(roleName);
            // if group is null, then roleName is not taken by another role
            if (group == null) {
                group = userManagerTosca.addGroup(roleName, genericHumanRoles);
                response.put("id", group.getId());
                return response.toString();
            }
            return "taken";
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * Gets the role/group with the given id
     * @param id
     *         name of the role/group
     * @return
     *         JSON-String containing the attributes of id
     */
    @Override
    public String getRole(String id) {

        JSONObject response = new JSONObject();
        try {
            IGroup group = userManagerTosca.getGroup(id);
            // if group is null, then no roles/groups with group name id  were found
            if (group != null) {
                response.put("id", group.getId());
                response.put("rolename", group.getGroupName());
                JSONArray genericHumanRoles = new JSONArray();
                genericHumanRoles.addAll(dataAccessTosca.getGenericHumanRolesByGroup(id));
                response.put("genericHumanRoles", genericHumanRoles);
                return response.toString();
            }
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all roles/groups
     * @return
     *         JSON-Array as String with all roles/groups and their attributes
     */
    @Override
    public String getAllRoles() {
        try {
            Set<IGroup> allGroups = dataAccessTosca.getAllGroups();
            JSONArray response = new JSONArray();
            // if allGroups is null, then no roles/groups were found
            if (allGroups != null) {
                // create a JSON-Object for every role/group and add it to response
                for (IGroup group : allGroups) {
                    JSONObject j = new JSONObject();
                    j.put("id", group.getId());
                    j.put("rolename", group.getGroupName());
                    JSONArray genericHumanRoles = new JSONArray();
                    genericHumanRoles.addAll(dataAccessTosca.getGenericHumanRolesByGroup(group.getGroupName()));
                    j.put("gernericHumanRoles", genericHumanRoles);
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
     * Get all Users belonging to a specific role/group
     * @param role
     *         name of role/group
     * @return
     *         JSON-Array as String with all users and their attributes belonging to role
     */
    @Override
    public String getRoleUsers(String role) {
        try {
            Set<IUser> allUserByRole = dataAccessTosca.getUserByGroup(role);
            JSONArray response = new JSONArray();
            // if allUserByRole is null, then no users were found
            if (allUserByRole != null) {
                // create a JSON-Object for every user and add it to response
                for (IUser user : allUserByRole) {
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
     * Update role/group with given values
     * @param id
     *          name of role/group that is updated
     * @param genericHumanRoles
     *          new geniric human roles mapped to id
     * @return
     *          true if update was successful
     */
    @Override
    public boolean updateRole(String id, String[] genericHumanRoles) {

        boolean response = false;
        try {
            response = dataAccessTosca.updateGroup(id, genericHumanRoles);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Deletes a role/group with the given group name
     * @param id
     *         name of role/group
     * @return
     *         true if deletion was successful
     */
    @Override
    public boolean deleteRole(String id) {

        boolean deleted = false;
        try {
            deleted = userManagerTosca.deleteGroup(id);
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
