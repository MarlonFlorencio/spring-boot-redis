package com.marlonflorencio.demo.redis.controller;

import com.marlonflorencio.demo.redis.model.User;
import com.marlonflorencio.demo.redis.service.UserService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * Redis Sample Controller
 * @author debugrammer
 * @version 1.0
 * @since 2019-11-10
 */
@RestController
@RequestMapping({"/users"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping()
//    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) throws IOException {
    public ResponseEntity<?> registerUser() throws IOException {
        User user = userService.register("Marlon");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<?> isUserBlocked(
            @PathVariable("id") String id
    ) throws ChangeSetPersister.NotFoundException {

        Optional<User> user = userService.getUser(id);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
}

