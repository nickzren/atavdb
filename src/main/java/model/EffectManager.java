package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import util.DBManager;

/**
 *
 * @author nick
 */
public class EffectManager {

    public static final String TMP_EFFECT_ID_TABLE = "tmp_effect_id";
    
    // system defualt values
    private static HashMap<Integer, String> id2EffectMap = new HashMap<>();
    private static HashMap<String, Integer> impactEffect2IdMap = new HashMap<>();
    // potential problem here for the same effect name 
    private static HashMap<String, Integer> effect2IdMap = new HashMap<>();
    
    // user input values
    private static HashSet<String> inputEffectSet = new HashSet<>();

    private static final String HIGH_IMPACT = "('HIGH')";
    private static final String MODERATE_IMPACT = "('HIGH'),('MODERATE')";
    private static final String LOW_IMPACT = "('HIGH'),('MODERATE'),('LOW')";
    private static final String MODIFIER_IMPACT = "('HIGH'),('MODERATE'),('LOW'),('MODIFIER')";

    public static int MISSENSE_VARIANT_ID;
    public static HashSet<Integer> LOF_EFFECT_ID_SET = new HashSet<>();
    public static HashSet<String> LOF_EFFECT_SET = new HashSet<>();
    
    private static boolean isUsed = false;

    public static void init() throws SQLException {
        initDefaultEffectSet();
    }

    private static void initDefaultEffectSet() {
        try {
            String sql = "SELECT * FROM effect_ranking";

            ResultSet rs = DBManager.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String effect = rs.getString("effect");

                id2EffectMap.put(id, effect);
                effect2IdMap.put(effect, id);

                if (effect.equals("missense_variant")) {
                    MISSENSE_VARIANT_ID = id;
                }
            }
            
            rs.close();
        } catch (Exception e) {
        }
    }

    public static String getEffectById(int id) {
        return id2EffectMap.get(id);
    }

    public static int getIdByEffect(String effect) {
        return effect2IdMap.get(effect);
    }
    
    public static boolean isLOF(int effectID) {
        return LOF_EFFECT_ID_SET.contains(effectID);
    }
    
    public static boolean isLOF(String effect) {
        return LOF_EFFECT_SET.contains(effect);
    }
    
    public static boolean isEffectContained(String effect) {
        return inputEffectSet.isEmpty() || inputEffectSet.contains(effect);
    }
    
    public static boolean isUsed() {
        return isUsed;
    }
}
