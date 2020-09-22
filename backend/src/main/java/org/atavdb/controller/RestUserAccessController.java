package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.model.MessageResponse;
import org.atavdb.util.LDAP;
import org.atavdb.util.SessionManager;
import org.atavdb.util.VerifyUser;
import org.springframework.http.HttpStatus;
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

    @RequestMapping("/signin")
    public MessageResponse signin(String username, String password,
            HttpSession session) {
        SessionManager.clearSession4Search(session);
        ModelAndView mv = new ModelAndView("signin");

        if (username != null && password != null) {

            if (LDAP.isMCAccountValid(username, password)) {
                session.setAttribute("username", username);

                if (VerifyUser.isAuthorizedFromSequence(username)) {
                    session.setAttribute("sequence_authorized", true);
                }

                return new MessageResponse(HttpStatus.OK.value(), "Login success.");
            } else {
                // return login failed
                return new MessageResponse(HttpStatus.NOT_FOUND.value(),
                        "Invalid CUMC MC account username/password.");
            }
        }

        return null;
    }

    @RequestMapping("/signout")
    public MessageResponse signout(HttpSession session) {
        session.invalidate();

        // signout succeed
        return new MessageResponse(HttpStatus.OK.value(), "Logout success.");
    }
}
