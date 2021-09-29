package org.atavdb.model;

import org.atavdb.util.DBManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
/**
 *
 * @author nick
 */

public class EffectManager {

    private static HashMap<Integer, String> id2EffectMap = new HashMap<>();
    private static HashMap<Integer, String> id2ImpactMap = new HashMap<>();
    
    private static void initDefaultEffectSet() throws Exception {
        String sql = "SELECT * FROM effect_ranking";

        Connection connection = DBManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String effect = rs.getString("effect");
            String impact = rs.getString("impact");

            id2EffectMap.put(id, effect);
            id2ImpactMap.put(id, impact);
        }

        rs.close();
        statement.close();
    }

    public static String getEffectById(int id) throws Exception {
        if (id2EffectMap.isEmpty()) {
            initDefaultEffectSet();
        }

        return id2EffectMap.get(id);
    }
    
    public static String getImpactById(int id) {
        return id2ImpactMap.get(id);
    }
}
