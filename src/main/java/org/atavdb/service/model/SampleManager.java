package org.atavdb.service.model;

import org.atavdb.service.util.DBManager;
import org.atavdb.model.SearchFilter;
import org.atavdb.global.Enum.Ancestry;
import org.atavdb.global.Enum.Gender;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.atavdb.model.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 *
 * @author nick
 */
@Service
@ComponentScan("org.atavdb.service")
public class SampleManager {

    @Autowired
    DBManager dbManager;

    @Autowired
    VariantManager variantManager;

    // date point for clearing cached data
    private LocalDate currentDate4AllSample = LocalDate.now();
    private LocalDate currentDate4PublicAvailableSample = LocalDate.now();

    // authorized user init all sample map , key is phenotype and value is list of samples associated
    private HashMap<String, ArrayList<Sample>> allSampleMap = new HashMap<>();
    private HashMap<String, ArrayList<Sample>> publicAvailableSampleMap = new HashMap<>();

    public void init(SearchFilter filter, HttpSession session) throws Exception {
        if (getMap(filter).isEmpty()
                || checkSampleCount(filter)) {
            initAllSampleFromDB(filter);

            // trigger to clear cached data when sample count mismatch
            variantManager.clearCachedData(filter);
        }
        
        session.setAttribute("sampleCount", getTotalSampleNum(filter));
    }

    private boolean checkSampleCount(SearchFilter filter) throws Exception {
        // reset sample data & clear cached data once a day
        if (getCurrentDate(filter).isEqual(LocalDate.now())) {
            return false;
        } else {
            resetCurrentDate(filter);
        }

        // only if sample count mismatch then reset sample data & clear cached data
        String sqlCode = "SELECT count(*) as count FROM sample "
                + "WHERE"
                + filter.getSampleSQL()
                + filter.getAvailableControlUseSQL();

        Connection connection = dbManager.getConnection();
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

    private void initAllSampleFromDB(SearchFilter filter) throws Exception {
        getMap(filter).clear();
        getMap(filter).put("", new ArrayList<>());

        String sqlCode = "SELECT sample_id,seq_gender,experiment_id,broad_phenotype,ancestry,available_control_use FROM sample "
                + "WHERE"
                + filter.getSampleSQL()
                + filter.getPhenotypeSQL()
                + filter.getAvailableControlUseSQL();

        Connection connection = dbManager.getConnection();
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
    }

    public int getTotalSampleNum(SearchFilter filter) {
        return getMap(filter).isEmpty() ? 0
                : getMap(filter).getOrDefault(filter.getPhenotype(), new ArrayList<>()).size();
    }

    public ArrayList<Sample> getList(SearchFilter filter) {
        return getMap(filter).get(filter.getPhenotype());
    }

    public HashMap<String, ArrayList<Sample>> getMap(SearchFilter filter) {
        return filter.isAvailableControlUseOnly() ? publicAvailableSampleMap : allSampleMap;
    }

    private LocalDate getCurrentDate(SearchFilter filter) {
        return filter.isAvailableControlUseOnly() ? currentDate4PublicAvailableSample : currentDate4AllSample;
    }

    private void resetCurrentDate(SearchFilter filter) {
        if (filter.isAvailableControlUseOnly()) {
            currentDate4PublicAvailableSample = LocalDate.now();
        } else {
            currentDate4AllSample = LocalDate.now();
        }
    }
}
