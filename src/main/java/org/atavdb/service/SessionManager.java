package org.atavdb.service;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

/**
 *
 * @author nick
 */
@Service
public class SessionManager {
    public void clearSession4Search(HttpSession session) {
        session.removeAttribute("query");
        session.removeAttribute("queryType");
        session.removeAttribute("maxAF");
        session.removeAttribute("phenotype");
        session.removeAttribute("isHighQualityVariant");
        session.removeAttribute("isUltraRareVariant");
        session.removeAttribute("isPublicAvailable");
        session.removeAttribute("error");
    }
}
