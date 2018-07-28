package com.htm.endpoint.impl;

import com.htm.db.IDataAccessProvider;
import com.htm.endpoint.IRolesService;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.security.IUserManager;
import com.htm.userdirectory.IGroup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Configurable
//TODO: Verwernden der generic human roles!!!!
public class RolesServiceImpl implements IRolesService {

    protected IUserManager userManagerTosca;

    protected IDataAccessProvider dataAccessTosca;

    @Override

    public String createRole(String roleName, String[] genericHumanRoles) {
        IGroup group = null;
        JSONObject response = new JSONObject();
        try {
            group = userManagerTosca.getGroup(roleName);
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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getRole(String id) {

        JSONObject response = new JSONObject();
        try {
            IGroup group = userManagerTosca.getGroup(id);
            if (group != null) {
                response.put("id", group.getId());
                response.put("rolename", group.getGroupName());
                response.put("genericHumanRoles", "noch hinzufügen");
                return response.toString();
            }
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAllRoles() {
        try {
            Set<IGroup> allGroups = dataAccessTosca.getAllGroups();
            JSONArray response = new JSONArray();
            if (allGroups != null) {

                for (IGroup group : allGroups) {
                    JSONObject j = new JSONObject();
                    j.put("id", group.getId());
                    j.put("rolename", group.getGroupName());
                    j.put("genericHumanRoles", "noch hinzufügen");

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

    @Override
    public String getRoleUsers() {
        return null;
    }

    @Override
    public String updateRole(String json, String id) {
        return null;
    }

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
