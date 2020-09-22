package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.util.LDAP;
import org.atavdb.util.SessionManager;
import org.atavdb.util.VerifyUser;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nick
 */
@Controller
@ComponentScan("org.atavdb.service.util")
public class UserAccessController {

    @RequestMapping("/signin")
    public ModelAndView signin(String username, String password,
            HttpSession session) {
        SessionManager.clearSession4Search(session);
        ModelAndView mv = new ModelAndView("signin");

        if (username != null && password != null) {

            if (LDAP.isMCAccountValid(username, password)) {
                session.setAttribute("username", username);

                if (VerifyUser.isAuthorizedFromSequence(username)) {
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
