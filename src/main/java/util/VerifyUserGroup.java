package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author nick
 */
public class VerifyUserGroup {

    private final static String IGM_CORE_VERIFY_USER_GROUP_URL = "https://core.igm.cumc.columbia.edu/api/verifyusergroup/?";

    public static boolean isAuthorized(String username) {
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
}
