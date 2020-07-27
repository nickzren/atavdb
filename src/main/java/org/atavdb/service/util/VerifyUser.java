package org.atavdb.service.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;

/**
 *
 * @author nick
 */

@Service
public class VerifyUser {

    private final String IGM_SEQUENCE_VERIFY_USER_URL = "https://sequence.igm.cumc.columbia.edu/api/verifyuser.php?";

    /*
        check whether user has valid account in igm sequence
     */
    public boolean isAuthorizedFromSequence(String username) {
        try {
            String command = "curl " + IGM_SEQUENCE_VERIFY_USER_URL + "netID=" + username;
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            if (response.toString().contains("true")) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
