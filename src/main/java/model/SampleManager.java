package model;

import global.Enum.Ethnicity;
import global.Enum.Gender;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import util.DBManager;

/**
 *
 * @author nick
 */
public class SampleManager {

    private static LocalDate currentDate = LocalDate.now();

    // authorized user init all sample map , key is phenotype and value is list of samples associated
    private static HashMap<String, ArrayList<Sample>> allSampleMap = new HashMap<>();
    private static HashMap<String, ArrayList<Sample>> publicAvailableSampleMap = new HashMap<>();

    public static void init(FilterManager filter) throws Exception {
        if (checkSampleCount(filter)) {
            initAllSampleFromDB(filter);

            // trigger to clear cached data when sample count mismatch
            VariantManager.clearCachedData(filter);
        }
    }

    private static boolean checkSampleCount(FilterManager filter) throws Exception {
        // init sample data
        if (getMap(filter).isEmpty()) {
            return true;
        }

        // reset sample data & clear cached data once a day
        if (currentDate.isEqual(LocalDate.now())) {
            return false;
        } else {
            currentDate = LocalDate.now();
        }

        // only if sample count mismatch then reset sample data & clear cached data
        String sqlCode = "SELECT count(*) as count FROM sample "
                + "WHERE"
                + filter.getSampleSQL()
                + filter.getPhenotypeSQL()
                + filter.getAvailableControlUseSQL();

        Connection connection = DBManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sqlCode);

        if (rs.next()) {
            if (getMap(filter).get("").size() != rs.getInt("count")) {
                return true;
            }
        }

        rs.close();
        statement.close();

        return false;
    }

    private static void initAllSampleFromDB(FilterManager filter) throws Exception {
        getMap(filter).clear();
        getMap(filter).put("", new ArrayList<>());

        String sqlCode = "SELECT sample_id,seq_gender,experiment_id,broad_phenotype,ethnicity,available_control_use FROM sample "
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
            byte availableControlUse = rs.getByte("available_control_use");

            Sample sample = new Sample(sampleId, gender, experimentId, broadPhenotype, ethnicity, availableControlUse);

            if (broadPhenotype != null && !broadPhenotype.isEmpty()) {
                ArrayList<Sample> list = getMap(filter).get(broadPhenotype);
                if (list == null) {
                    list = new ArrayList<>();
                    getMap(filter).put(broadPhenotype, list);
                }

                list.add(sample);
            }

            getMap(filter).get("").add(sample);
        }

        rs.close();
        statement.close();
    }

    public static int getTotalSampleNum(FilterManager filter) {
        return getMap(filter).isEmpty() ? 0 : getMap(filter).get("").size();
    }

    public static ArrayList<Sample> getList(FilterManager filter) {
        return getMap(filter).get(filter.getPhenotype());
    }

    public static HashMap<String, ArrayList<Sample>> getMap(FilterManager filter) {
        return filter.isAvailableControlUseOnly() ? publicAvailableSampleMap : allSampleMap;
    }
}
