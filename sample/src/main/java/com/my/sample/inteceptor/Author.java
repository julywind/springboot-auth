package com.my.sample.inteceptor;

import com.my.sample.pojo.User;
import com.github.julywind.auth.AuthUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Author implements AuthUser<User> {
    // to getUser from request
    @Override
    public User getUser(HttpServletRequest request) {
        // custom your verify processor
        if(request.getParameter("code")!=null) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("password");
            return user;
        }
        return null;
    }

    // check user permissions
    @Override
    public boolean isAuthorized(String requiredRole, User user) {
        // custom your permission check handler
        System.out.println(requiredRole);
        System.out.println(user);
        return user!=null;
    }
}
