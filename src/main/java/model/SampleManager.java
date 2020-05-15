package model;

import global.Enum.Gender;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import util.DBManager;

/**
 *
 * @author nick
 */
public class SampleManager {

    private static HashMap<String, ArrayList<Sample>> map = new HashMap<>();

    public static void init(FilterManager filter) throws Exception {
        if (checkSampleCount(filter)) {
            initAllSampleFromDB(filter);
        }
    }

    private static boolean checkSampleCount(FilterManager filter) throws Exception {
        if (map.isEmpty()) {
            return true;
        }

        String sqlCode = "SELECT count(*) as count FROM sample "
                + "WHERE sample_finished=1 AND sample_failure=0"
                + " AND sample_type!='custom_capture'"
                + filter.getPhenotypeSQL();

        ResultSet rs = DBManager.executeQuery(sqlCode);

        if (rs.next()) {
            if (map.get("all").size() != rs.getInt("count")) {
                return true;
            }
        }

        rs.close();

        return false;
    }

    private static void initAllSampleFromDB(FilterManager filter) throws Exception {
        map.clear();
        map.put("all", new ArrayList<>());

        String sqlCode = "SELECT sample_id,sample_name,seq_gender,experiment_id,broad_phenotype FROM sample "
                + "WHERE sample_finished=1 AND sample_failure=0"
                + " AND sample_type!='custom_capture'"
                + filter.getPhenotypeSQL();

        ResultSet rs = DBManager.executeQuery(sqlCode);

        while (rs.next()) {
            int sampleId = rs.getInt("sample_id");
            String seqGender = rs.getString("seq_gender");
            Gender gender = Gender.NA;
            if (seqGender != null) {
                gender = Gender.valueOf(seqGender);
            }
            int experimentId = rs.getInt("experiment_id");
            String broadPhenotype = rs.getString("broad_phenotype");

            Sample sample = new Sample(sampleId, gender, experimentId, broadPhenotype);

            if (broadPhenotype != null && !broadPhenotype.isEmpty()) {
                ArrayList<Sample> list = map.get(broadPhenotype);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(broadPhenotype, list);
                }

                list.add(sample);
            }

            map.get("all").add(sample);
        }

        rs.close();
    }

    public static int getTotalSampleNum() {
        return map.isEmpty() ? 0 : map.get("all").size();
    }

    public static ArrayList<Sample> getList(FilterManager filter) {
        return map.get(filter.getPhenotype());
    }
}
