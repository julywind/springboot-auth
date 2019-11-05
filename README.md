## springboot-auth
  This is a convenient tool to add authorization to apis in a springboot project.
  
### usage
steps to use:  
1.add dependency in pom.xml
```
    <dependency>
        <groupId>com.github.julywind</groupId>
        <artifactId>springboot-auth</artifactId>
        <version>1.0.2</version>
    </dependency>
```
2.add annotation EnableJSecurity to SpringbootApp
```
    @SpringBootApplication
    @EnableJSecurity
    public class MyApplication {
        public static void main(String[] args) {
            SpringApplication.run(MyApplication.class);
        }
    }
```
3. add a class to implements interface AuthUser<T>, T is your user model,
such as User
```
    public class User {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
```
```
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
``` 
4. create a controller class,
the annotation 'Authorized' can be used to either Controller class or method,
when on Controller Class, equal to add it to all the apis in this file.
param role,can be empty if no need. 
```
    @Authorized(role = "myRequiringRoleName")
    @RestController
    public class MyController {
        // you can get authorized user by param annotation CurrentUser
        @GetMapping("/")
        public Object index(@CurrentUser User user){
            return user;
        }

        // use this annotation to skip some api from controller with Authorized
        @GetMapping("/")
        @SkipAuthorize 
        public Object index(@CurrentUser User user){
            return user;
        }
    }
```
5. create a ControllerAdvice to handle the authorize exception
```
    @ControllerAdvice
    @RestController
    public class MyControllerAdvice{
        @ExceptionHandler(value = AuthenticationFailedException.class)
        @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
        public String unAuthorized(AuthenticationFailedException exception){
            return exception.toString();
        }
    }
```

6. test

`# curl http://localhost:8080`
```
 com.github.julywind.auth.exception.AuthenticationFailedException
```
`# curl http://localhost:8080?code=1`
```
 {
    username: "admin",
    password: "password",
 }

```
ok. 

Author: marty, welcome to improve it together.

Thanks for Yueyang Wang;
