package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author nick
 */
public class VerifyUser {

    private final static String IGM_CORE_VERIFY_USER_GROUP_URL = "https://core.igm.cumc.columbia.edu/api/verifyusergroup/?";
    private final static String IGM_SEQUENCE_VERIFY_USER_URL = "https://sequence.igm.cumc.columbia.edu/api/verifyuser.php?";
    
    /*
        check whether user has atavdb group in igm core
     */
    public static boolean isAuthorizedFromCore(String username) {
        try {
            URL url = new URL(IGM_CORE_VERIFY_USER_GROUP_URL + "username=" + username + "&groupname=atavdb");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                if (response.toString().contains("true")) {
                    return true;
                }
            }
        } catch (Exception ex) {
        }

        return false;
    }

    /*
        check whether user has valid account in igm sequence
     */
    public static boolean isAuthorizedFromSequence(String username) {
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
