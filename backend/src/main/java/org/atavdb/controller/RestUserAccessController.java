package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.model.MessageResponse;
import org.atavdb.util.LDAP;
import org.atavdb.util.SessionManager;
import org.atavdb.util.VerifyUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nick
 */
@RestController
@RequestMapping("/api")
public class RestUserAccessController {

    @GetMapping("/authenticate")
    public boolean signin(String username, String password, HttpSession session) {
        SessionManager.clearSession4Search(session);

        if (username != null && password != null
                && LDAP.isMCAccountValid(username, password)) {
            session.setAttribute("username", username);

            return true;
        }

        return false;
    }

    @GetMapping("/authorize")
    public boolean signin(String username, HttpSession session) {
        if (username != null
                && VerifyUser.isAuthorizedFromSequence(username)) {
            session.setAttribute("sequence_authorized", true);
            return true;
        }

        return false;
    }

    @GetMapping("/signout")
    public boolean signout(HttpSession session) {
        session.invalidate();

        // signout succeed
        return true;
    }
}
