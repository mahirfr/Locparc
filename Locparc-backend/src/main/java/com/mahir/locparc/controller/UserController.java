package com.mahir.locparc.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.dao.UserDao;
import com.mahir.locparc.model.Role;
import com.mahir.locparc.model.User;
import com.mahir.locparc.security.config.JwtService;
import com.mahir.locparc.security.exceptions.ResourceNotFoundException;
import com.mahir.locparc.service.EmailService;
import com.mahir.locparc.service.PasswordGenerator;
import com.mahir.locparc.view.UserView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;

    public UserController(UserDao userDao,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          PasswordGenerator passwordGenerator,
                          EmailService emailService) {
        this.userDao           = userDao;
        this.passwordEncoder   = passwordEncoder;
        this.jwtService        = jwtService;
        this.passwordGenerator = passwordGenerator;
        this.emailService      = emailService;
    }

    @GetMapping("/admin/users")
    @JsonView({UserView.class})
    public List<User> getAll() {
        return userDao.findAll();
    }


    @GetMapping("/admin/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> optionalUser = userDao.findById(id);

        return optionalUser.map(user ->
                new ResponseEntity<>(user, HttpStatus.OK)).orElseThrow(() ->
                new ResourceNotFoundException("Utilisateur avec l'idéntifiant "
                        + id + " n'existe pas dans la base de données"));
    }


    @GetMapping("/admin/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userDao.findByEmail(email);

        return optionalUser.map(user ->
                new ResponseEntity<>(user, HttpStatus.OK)).orElseThrow(() ->
                new ResourceNotFoundException("Utilisateur avec l'email "
                        + email + " n'existe pas dans la base de données"));
    }

    @PostMapping("/admin/users/multiple")
    @Transactional
    public ResponseEntity<List<User>> createUsers(@RequestBody User[] users) {

        List<User> savedUsers = new ArrayList<>();

        if (users.length > 0) {
            try {
                for (User user : users) {
                    user.setActive(true);
                    user.setFirstPassword(passwordGenerator.generatePassayPassword());
                    user.setPassword(passwordEncoder.encode(user.getFirstPassword()));
                    user.setRole(new Role(3));
                    savedUsers.add(userDao.save(user));
                    emailService.sendEmail(user.getEmail(),
                            "Création de compte", // Subject
                            "Votre compte a été créé, voici votre mot de passe: \n" // Body
                                    + user.getFirstPassword());
                }
                return new ResponseEntity<>(savedUsers, HttpStatus.CREATED);
            } catch (Exception e) {
                throw new RuntimeException("Echec dans la création des utilisateurs. ", e);
            }
        }
        throw new IllegalArgumentException("Vous devez fournir au moins un utilisateur");
    }


    @PostMapping("/admin/users")
    @Transactional
    public ResponseEntity<User> createUser(@RequestBody User user) {

        try {
            user.setActive(true);
            user.setFirstPassword(passwordGenerator.generatePassayPassword());
            user.setPassword(passwordEncoder.encode(user.getFirstPassword()));
            user.setRole(new Role(3));
            userDao.save(user);
            emailService.sendEmail(user.getEmail(),
                    "Création de compte",
                    "Votre compte a été créé, voici votre mot de passe: \n"
                            + user.getFirstPassword());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Echec dans la création de l'utilisateur. ", e);
        }
    }

    @PostMapping("/admin/lender")
    @Transactional
    public ResponseEntity<User> createLender(@RequestBody User user) {

        try {
            user.setActive(true);
            user.setFirstPassword(passwordGenerator.generatePassayPassword());
            user.setPassword(passwordEncoder.encode(user.getFirstPassword()));
            user.setRole(new Role(2));
            emailService.sendEmail(user.getEmail(),
                    "Création de compte",
                    "Votre compte a été créé, voici votre mot de passe: \n"
                            + user.getFirstPassword());
            return new ResponseEntity<>(userDao.save(user), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Echec dans la création de loueur. ", e);
        }

    }


    @PostMapping("/admin/admin")
    @Transactional
    public ResponseEntity<User> createAdmin(@RequestBody User user) {

        try {
            user.setActive(true);
            user.setFirstPassword(passwordGenerator.generatePassayPassword());
            user.setPassword(passwordEncoder.encode(user.getFirstPassword()));
            user.setRole(new Role(1));
            emailService.sendEmail(user.getEmail(),
                    "Création de compte",
                    "Votre compte a été créé, voici votre mot de passe: \n"
                            + user.getFirstPassword());
            return new ResponseEntity<>(userDao.save(user), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Echec dans la création d'un admin . ", e);
        }

    }

    @PutMapping("/admin/edit-profile/{id}")
    @Transactional
    public ResponseEntity<User> adminEditUser(
            @PathVariable Long id,
            @RequestBody  User user
    ) {
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();

            try {
                if (user.getFirstName() != null)
                    userToUpdate.setFirstName     (user.getFirstName     ());
                if (user.getLastName() != null)
                    userToUpdate.setLastName      (user.getLastName      ());
                if (user.getEmail() != null)
                    userToUpdate.setEmail         (user.getEmail         ());
                if (user.getPhone() != null)
                    userToUpdate.setPhone         (user.getPhone         ());
                userToUpdate.setActive            (user.isActive         ());
                if (user.getRole() != null)
                    userToUpdate.setRole          (user.getRole          ());
                if (user.getAddress() != null)
                    userToUpdate.setAddress       (user.getAddress       ());

                userDao.save(userToUpdate);

                return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
            } catch (Exception e) {
                throw new RuntimeException("Echéc dans la modification du profil ", e);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/edit-profile/{id}")
    @Transactional
    public ResponseEntity<User> editUser(
            @PathVariable Long id,
            @RequestBody User user,
            @RequestHeader("Authorization") String jwt
    ) {

        Optional<User> optionalUserJwt = userDao.findByEmail(jwtService.extractEmail(jwt));
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();

            // If user tries to modify other user
            if (optionalUserJwt.isPresent()) {
                if (optionalUserJwt.get().getId() != userToUpdate.getId())
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            if (!userToUpdate.isActive()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            try {
                if (user.getFirstName() != null)
                    userToUpdate.setFirstName(user.getFirstName());
                if (user.getLastName() != null)
                    userToUpdate.setLastName(user.getLastName());
                if (user.getPhone() != null)
                    userToUpdate.setPhone(user.getPhone());
                if (!user.getPassword().equals(userToUpdate.getPassword()))
                    userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
                if (user.getAddress() != null)
                    userToUpdate.setAddress(user.getAddress());

                userDao.save(userToUpdate);

                return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
            } catch (Exception e) {
                throw new RuntimeException("Echec dans la modification de profil. ", e);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/admin/users/{id}")
    @Transactional
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            try {
                userDao.delete(optionalUser.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
