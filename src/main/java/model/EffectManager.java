package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import util.DBManager;

/**
 *
 * @author nick
 */
public class EffectManager {

    private static HashMap<Integer, String> id2EffectMap = new HashMap<>();

    public static void init() throws SQLException {
        if (id2EffectMap.isEmpty()) {
            initDefaultEffectSet();
        }
    }

    private static void initDefaultEffectSet() {
        try {
            String sql = "SELECT * FROM effect_ranking";

            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String effect = rs.getString("effect");

                id2EffectMap.put(id, effect);
            }
            
            rs.close();
            statement.close();
        } catch (Exception e) {
        }
    }

    public static String getEffectById(int id) {
        return id2EffectMap.get(id);
    }
}
