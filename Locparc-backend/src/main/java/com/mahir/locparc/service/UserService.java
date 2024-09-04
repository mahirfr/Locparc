package com.mahir.locparc.service;

import com.mahir.locparc.dao.UserDao;
import com.mahir.locparc.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean isAdminOrLender(User user) {
        return user.getRole().getName().equals("ROLE_ADMIN") ||
                user.getRole().getName().equals("ROLE_LENDER");
    }

    public boolean isAdmin(User user) {
        return user.getRole().getName().equals("ROLE_ADMIN");
    }



}
