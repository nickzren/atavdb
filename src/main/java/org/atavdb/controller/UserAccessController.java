package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.util.LDAP;
import org.atavdb.service.util.SessionManager;
import org.atavdb.service.util.VerifyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nick
 */
@Controller
@ComponentScan("org.atavdb.service.util")
public class UserAccessController {

    @Autowired
    LDAP ldap;

    @Autowired
    VerifyUser verifyUser;
    
    @Autowired
    SessionManager sessionManager;

    @RequestMapping("/signin")
    public ModelAndView signin(String username, String password,
            HttpSession session) {
        sessionManager.clearSession4Search(session);
        ModelAndView mv = new ModelAndView("signin");

        if (username != null && password != null) {

            if (ldap.isMCAccountValid(username, password)) {
                session.setAttribute("username", username);

                if (verifyUser.isAuthorizedFromSequence(username)) {
                    session.setAttribute("sequence_authorized", true);
                }

                return new ModelAndView("redirect:/");
            } else {
                mv.addObject("error", "Invalid CUMC MC account username/password.");
            }
        }

        return mv;
    }

    @RequestMapping("/signout")
    public ModelAndView signout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
    }
}
