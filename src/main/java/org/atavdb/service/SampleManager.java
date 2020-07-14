package org.atavdb.service;

import org.atavdb.global.Enum.Ancestry;
import org.atavdb.global.Enum.Gender;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import org.atavdb.model.Sample;

/**
 *
 * @author nick
 */
public class SampleManager {

    // date point for clearing cached data
    private static LocalDate currentDate4AllSample = LocalDate.now();
    private static LocalDate currentDate4PublicAvailableSample = LocalDate.now();

    // authorized user init all sample map , key is phenotype and value is list of samples associated
    private static HashMap<String, ArrayList<Sample>> allSampleMap = new HashMap<>();
    private static HashMap<String, ArrayList<Sample>> publicAvailableSampleMap = new HashMap<>();

    public static void init(FilterManager filter) {
        if (getMap(filter).isEmpty()
                || checkSampleCount(filter)) {
            initAllSampleFromDB(filter);

            // trigger to clear cached data when sample count mismatch
            VariantManager.clearCachedData(filter);
        }
    }

    private static boolean checkSampleCount(FilterManager filter) {
        // reset sample data & clear cached data once a day
        if (getCurrentDate(filter).isEqual(LocalDate.now())) {
            return false;
        } else {
            resetCurrentDate(filter);
        }

        try {
            // only if sample count mismatch then reset sample data & clear cached data
            String sqlCode = "SELECT count(*) as count FROM sample "
                    + "WHERE"
                    + filter.getSampleSQL()
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
        } catch (Exception e) {
        }

        return false;
    }

    private static void initAllSampleFromDB(FilterManager filter) {
        getMap(filter).clear();
        getMap(filter).put("", new ArrayList<>());

        try {
            String sqlCode = "SELECT sample_id,seq_gender,experiment_id,broad_phenotype,ancestry,available_control_use FROM sample "
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
                String ancestryStr = rs.getString("ancestry");
                Ancestry ancestry = ancestryStr == null ? Ancestry.NA : Ancestry.valueOf(ancestryStr);
                byte availableControlUse = rs.getByte("available_control_use");

                Sample sample = new Sample(sampleId, gender, experimentId, broadPhenotype, ancestry, availableControlUse);

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
        } catch (Exception e) {
        }
    }

    public static int getTotalSampleNum(FilterManager filter) {
        return getMap(filter).isEmpty() ? 0 : getMap(filter).get(filter.getPhenotype()).size();
    }

    public static ArrayList<Sample> getList(FilterManager filter) {
        return getMap(filter).get(filter.getPhenotype());
    }

    public static HashMap<String, ArrayList<Sample>> getMap(FilterManager filter) {
        return filter.isAvailableControlUseOnly() ? publicAvailableSampleMap : allSampleMap;
    }

    private static LocalDate getCurrentDate(FilterManager filter) {
        return filter.isAvailableControlUseOnly() ? currentDate4PublicAvailableSample : currentDate4AllSample;
    }

    private static void resetCurrentDate(FilterManager filter) {
        if (filter.isAvailableControlUseOnly()) {
            currentDate4PublicAvailableSample = LocalDate.now();
        } else {
            currentDate4AllSample = LocalDate.now();
        }
    }
}
