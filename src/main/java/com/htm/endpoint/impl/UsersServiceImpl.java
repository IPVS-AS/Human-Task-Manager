package com.htm.endpoint.impl;

import com.htm.endpoint.UsersService;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.security.IUserManager;
import com.htm.security.UserManagerBasicImpl;
import com.htm.userdirectory.IUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Configurable

//TODO: NOCH DIE ROLLEN DER USER HINZUFÜGEN!!!!!!!
public class UsersServiceImpl implements UsersService {


    protected IUserManager userManagerTosca;

    private final String DUMMY_PASSWORD = "password";


    @Override
    public String createUser(String userId, String firstname, String lastname) {
        IUser user = null;
        JSONObject response = new JSONObject();
        try {
             user = userManagerTosca.getUser(userId);
            if (user == null) {
                user = userManagerTosca.addUser(userId, firstname, lastname, DUMMY_PASSWORD);
                System.out.println(user.getFirstName());
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
        return null;
    }

    @Override
    public String updateUser(String json, String id) {
        return null;
    }

    @Override
    public String deleteUser(String id) {
        return null;
    }


    @Autowired
    public void setiUserManager(IUserManager userManagerTosca) {
        this.userManagerTosca = userManagerTosca;
    }

    public IUserManager getiUserManager() {return userManagerTosca;}
}
