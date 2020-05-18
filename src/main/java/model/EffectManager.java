package model;

import java.sql.ResultSet;
import java.sql.SQLException;
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

            ResultSet rs = DBManager.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String effect = rs.getString("effect");

                id2EffectMap.put(id, effect);
            }

            rs.close();
        } catch (Exception e) {
        }
    }

    public static String getEffectById(int id) {
        return id2EffectMap.get(id);
    }
}
