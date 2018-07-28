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


@Service
@Configurable

//TODO: NOCH DIE ROLLEN DER USER HINZUFÜGEN!!!!!!!
public class UsersServiceImpl implements IUsersService {


    protected IUserManager userManagerTosca;

    protected IDataAccessProvider dataAccessTosca;

    private final String DUMMY_PASSWORD = "password";


    @Override
    public String createUser(String userId, String firstname, String lastname) {
        IUser user = null;
        JSONObject response = new JSONObject();
        try {
             user = userManagerTosca.getUser(userId);
            if (user == null) {
                user = userManagerTosca.addUser(userId, firstname, lastname, DUMMY_PASSWORD);
                response.put("id", user.getId());
                return response.toString();
            }
            return "taken";
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return  null;
    }

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
                return response.toString();
            }
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAllUsers() {
        try {
            Set<IUser> allUsers = dataAccessTosca.getAllUser();
            JSONArray response = new JSONArray();
            if (allUsers != null) {
                for (IUser user : allUsers) {
                    JSONObject j = new JSONObject();
                    j.put("id", user.getId());
                    j.put("userId", user.getUserId());
                    j.put("firstname", user.getFirstName());
                    j.put("lastname", user.getLastName());
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
    public boolean updateUser(String firstname, String lastname, String[] groups, String id) {
        boolean response = false;
        try {
            response = dataAccessTosca.updateUser(id, firstname, lastname, groups);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return response;
    }

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
