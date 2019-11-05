package com.my.sample.controller;

import com.github.julywind.auth.anno.SkipAuthorize;
import com.my.sample.pojo.User;
import com.github.julywind.auth.anno.Authorized;
import com.github.julywind.auth.anno.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Authorized(role = "myRoles")
@RestController
public class MyController {
    // you can get authorized user by param annotation CurrentUser
    @GetMapping("/")
    public Object index(@CurrentUser User user){
        return user;
    }

    // use this annotation to skip some api from controller with Authorized
    @GetMapping("/test")
    @SkipAuthorize
    public Object index2(){
        return "test";
    }
}
