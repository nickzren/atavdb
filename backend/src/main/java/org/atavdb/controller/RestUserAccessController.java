package org.atavdb.controller;

import org.atavdb.util.LDAP;
import org.atavdb.util.VerifyUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nick
 */
@RestController
@RequestMapping("/api")
public class RestUserAccessController {

    @GetMapping("/authenticate")
    public boolean signin(String username, String password) {
        if (username != null && password != null
                && LDAP.isMCAccountValid(username, password)) {
            return true;
        }

        return false;
    }

    @GetMapping("/authorize")
    public boolean signin(String username) {
        if (username != null
                && VerifyUser.isAuthorizedFromSequence(username)) {
            return true;
        }

        return false;
    }
}
