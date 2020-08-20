package org.atavdb.service.util;

import javax.servlet.http.HttpSession;

/**
 *
 * @author nick
 */
public class SessionManager {
    public static void clearSession4Search(HttpSession session) {
        session.removeAttribute("query");
        session.removeAttribute("queryType");
        session.removeAttribute("maf");
        session.removeAttribute("phenotype");
        session.removeAttribute("isHighQualityVariant");
        session.removeAttribute("isUltraRareVariant");
        session.removeAttribute("isPublicAvailable");
        session.removeAttribute("error");
    }
}
