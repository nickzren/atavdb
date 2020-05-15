package model;

import global.Enum.Gender;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DBManager;

/**
 *
 * @author nick
 */
public class SampleManager {

    private static ArrayList<Sample> sampleList = new ArrayList<>();

    public static void init(FilterManager filter) throws Exception {
        if (checkSampleCount(filter)) {
            initAllSampleFromDB(filter);
        }
    }

    private static boolean checkSampleCount(FilterManager filter) throws Exception {
        if (sampleList.isEmpty()) {
            return true;
        }

        String sqlCode = "SELECT count(*) as count FROM sample "
                + "WHERE sample_finished=1 AND sample_failure=0"
                + " AND sample_type!='custom_capture'"
                + filter.getPhenotypeSQL();

        ResultSet rs = DBManager.executeQuery(sqlCode);

        if (rs.next()) {
            if (sampleList.size() != rs.getInt("count")) {
                return true;
            }
        }

        rs.close();

        return false;
    }

    private static void initAllSampleFromDB(FilterManager filter) throws Exception {
        sampleList.clear();

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

            sampleList.add(sample);
        }

        rs.close();
    }

    public static int getTotalSampleNum() {
        return sampleList.size();
    }

    public static ArrayList<Sample> getList() {
        return sampleList;
    }
}
