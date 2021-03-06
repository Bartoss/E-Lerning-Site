package com.bartolomeo.CRUD.controller;

import com.bartolomeo.CRUD.jwt.JwtTokenProvider;
import com.bartolomeo.CRUD.model.CourseStudent;
import com.bartolomeo.CRUD.model.Role;
import com.bartolomeo.CRUD.model.User;
import com.bartolomeo.CRUD.service.CourseService;
import com.bartolomeo.CRUD.service.CourseStudentService;
import com.bartolomeo.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseStudentService courseStudentService;

    @PostMapping("/api/user/registration")
    public ResponseEntity<?> register(@RequestBody User user){
        if(userService.findByUsername(user.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole(Role.STUDENT);
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/api/user/login")
    public ResponseEntity<?> getUser(Principal principal){
        if(principal == null){
            //This should be ok http status because here will be logout path.
            return ResponseEntity.ok(principal);
        }
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        User user = userService.findByUsername(authenticationToken.getName());
        user.setToken(tokenProvider.generateToken(authenticationToken));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/api/user/enroll")
    public ResponseEntity<?> enrollCourse(@RequestBody CourseStudent courseStudent){
        return new ResponseEntity<>(courseStudentService.saveCourseStudent(courseStudent), HttpStatus.CREATED);
    }

    @GetMapping("/api/user/courses")
    public ResponseEntity<?> getAllCourses(){
        return ResponseEntity.ok(courseService.findAllCourses());
    }
}
