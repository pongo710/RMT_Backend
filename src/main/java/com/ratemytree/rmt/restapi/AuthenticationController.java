package com.ratemytree.rmt.restapi;

import com.ratemytree.rmt.user.User;
import com.ratemytree.rmt.user.UserDTO;
import com.ratemytree.rmt.user.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * David Schilling - davejs92@gmail.com
 */
@Controller
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public HttpEntity<UserDTO> login(Principal principal) {

        User user = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
    }
}
