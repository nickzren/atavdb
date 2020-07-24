package org.atavdb.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 *
 * @author nick
 */
@Service
@ComponentScan("org.atavdb.service")
public class EffectManager {

    @Autowired
    DBManager dbManager;
    
    private HashMap<Integer, String> id2EffectMap = new HashMap<>();

    private void initDefaultEffectSet() {
        try {
            String sql = "SELECT * FROM effect_ranking";

            Connection connection = dbManager.getConnection();
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

    public String getEffectById(int id) {
        if (id2EffectMap.isEmpty()) {
            initDefaultEffectSet();
        }
        
        return id2EffectMap.get(id);
    }
}
