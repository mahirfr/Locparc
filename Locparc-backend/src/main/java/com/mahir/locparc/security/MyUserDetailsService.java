package com.mahir.locparc.security;

import com.mahir.locparc.dao.UserDao;
import com.mahir.locparc.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    public MyUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optional = userDao.findByEmail(email);

        if(optional.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur n'existe pas");
        }

        return new MyUserDetails(optional.get());
    }
}