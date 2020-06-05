package model;

import global.Enum.Ethnicity;
import global.Enum.Gender;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import util.DBManager;

/**
 *
 * @author nick
 */
public class SampleManager {

    // authorized user init all sample map , key is phenotype and value is list of samples associated
    private static HashMap<String, ArrayList<Sample>> allSampleMap = new HashMap<>();
    private static HashMap<String, ArrayList<Sample>> availableControlUseSampleMap = new HashMap<>();

    public static void init(FilterManager filter) throws Exception {
        if (checkSampleCount(filter)) {
            initAllSampleFromDB(filter);
        }
    }

    private static boolean checkSampleCount(FilterManager filter) throws Exception {
        if (getMap(filter).isEmpty()) {
            return true;
        }

        String sqlCode = "SELECT count(*) as count FROM sample "
                + "WHERE"
                + filter.getSampleSQL()
                + filter.getPhenotypeSQL()
                + filter.getAvailableControlUseSQL();

        Connection connection = DBManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sqlCode);

        if (rs.next()) {
            if (getMap(filter).get("all").size() != rs.getInt("count")) {
                return true;
            }
        }

        rs.close();
        statement.close();

        return false;
    }

    private static void initAllSampleFromDB(FilterManager filter) throws Exception {
        getMap(filter).clear();
        getMap(filter).put("all", new ArrayList<>());

        String sqlCode = "SELECT sample_id,sample_name,seq_gender,experiment_id,broad_phenotype,ethnicity FROM sample "
                + "WHERE"
                + filter.getSampleSQL()
                + filter.getPhenotypeSQL()
                + filter.getAvailableControlUseSQL();

        Connection connection = DBManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sqlCode);

        while (rs.next()) {
            int sampleId = rs.getInt("sample_id");
            String genderStr = rs.getString("seq_gender");
            Gender gender = genderStr == null ? Gender.NA : Gender.valueOf(genderStr);
            int experimentId = rs.getInt("experiment_id");
            String broadPhenotype = rs.getString("broad_phenotype");
            String ethnicityStr = rs.getString("ethnicity");
            Ethnicity ethnicity = ethnicityStr == null ? Ethnicity.NA : Ethnicity.valueOf(ethnicityStr);

            Sample sample = new Sample(sampleId, gender, experimentId, broadPhenotype, ethnicity);

            if (broadPhenotype != null && !broadPhenotype.isEmpty()) {
                ArrayList<Sample> list = getMap(filter).get(broadPhenotype);
                if (list == null) {
                    list = new ArrayList<>();
                    getMap(filter).put(broadPhenotype, list);
                }

                list.add(sample);
            }

            getMap(filter).get("all").add(sample);
        }

        rs.close();
        statement.close();
    }

    public static int getTotalSampleNum(FilterManager filter) {
        return getMap(filter).isEmpty() ? 0 : getMap(filter).get("all").size();
    }

    public static ArrayList<Sample> getList(FilterManager filter) {
        return getMap(filter).get(filter.getPhenotype());
    }
    
    public static HashMap<String, ArrayList<Sample>> getMap(FilterManager filter) {
        return filter.isAvailableControlUseOnly() ? availableControlUseSampleMap : allSampleMap;
    }
}
