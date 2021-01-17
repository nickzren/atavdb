package org.atavdb.controller;

import javax.servlet.http.HttpSession;
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
    public boolean signin(String username, String password, HttpSession session) {
        if (username != null && password != null
                && LDAP.isMCAccountValid(username, password)) {
            session.setAttribute("authenticatedUser", true);
            return true;
        }

        return false;
    }

    @GetMapping("/authenticated")
    public Boolean authenticated(HttpSession session) {
        return (Boolean) session.getAttribute("authenticatedUser");
    }

    @GetMapping("/authorize")
    public boolean signin(String username, HttpSession session) {
        if (username != null
                && VerifyUser.isAuthorizedFromSequence(username)) {
            session.setAttribute("authorizedUser", true);
            return true;
        }

        return false;
    }

    @GetMapping("/authorized")
    public Boolean authorized(HttpSession session) {
        return (Boolean) session.getAttribute("authorizedUser");
    }

    @GetMapping("/signout")
    public void signout(HttpSession session) {
        session.invalidate();
    }
}
