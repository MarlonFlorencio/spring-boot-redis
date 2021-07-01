package com.marlonflorencio.demo.redis.controller;

import com.marlonflorencio.demo.redis.controller.request.RegisterUserRequest;
import com.marlonflorencio.demo.redis.model.User;
import com.marlonflorencio.demo.redis.service.user.UserMsgPackService;
import com.marlonflorencio.demo.redis.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMsgPackService userMsgPackService;

    @GetMapping("performance")
    public ResponseEntity<?> peformance() {
        userMsgPackService.peformance();
        userService.peformance();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping()
   public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequest request) throws IOException {
        User user = userService.register(
                request.getFirstName(),
                request.getLastName()
        );
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id)  {

        Optional<User> user = userService.getUser(id);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
}

