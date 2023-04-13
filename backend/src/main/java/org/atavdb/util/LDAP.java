package org.atavdb.util;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author nick
 */
public class LDAP {

    private static final String LDAP_SERVER = "ldaps://rbwdcmc001.mc.cumc.columbia.edu:636";

    /*
        check whether user has a valid CUMC MC account
    */
    public static boolean isMCAccountValid(String username, String password) {
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put(Context.PROVIDER_URL, LDAP_SERVER);
            props.put(Context.SECURITY_PRINCIPAL, "MC\\" + username);
            props.put(Context.SECURITY_CREDENTIALS, password);

            InitialDirContext context = new InitialDirContext(props);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
