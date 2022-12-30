package com.elijah.onlinebankingapp.security.service;

import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.security.model.Role;
import com.elijah.onlinebankingapp.security.model.UserModel;

import java.util.List;

public interface UserService {

    UserModel saveUser(UserModel userModel) throws DataAlreadyExistException;
    Role saveRole(Role role) throws DataAlreadyExistException;
    void addRoleToUser(String username,String roleName) throws DataNotFoundException, DataAlreadyExistException;
    UserModel getUser(String username) throws DataNotFoundException;
    List<UserModel> getUsers();
}
