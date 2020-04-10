package model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import util.DBManager;

/**
 *
 * @author nick
 */
public class SampleManager {

    private static ArrayList<Sample> sampleList = new ArrayList<>();
    private static HashMap<Integer, Sample> sampleMap = new HashMap<>();
    private static HashSet<String> sampleNameSet = new HashSet<>();

    public static void init() {
        if (checkSampleCount()) {
            initAllSampleFromDB();
        }
    }

    private static boolean checkSampleCount() {
        if (sampleList.isEmpty()) {
            return true;
        }

        String sqlCode = "SELECT count(*) as count FROM sample "
                + "WHERE sample_type != 'custom_capture' "
                + "and sample_finished = 1 "
                + "and sample_failure = 0 "
                + "and sample_name not like 'SRR%'";

        try {
            ResultSet rs = DBManager.executeQuery(sqlCode);

            if (rs.next()) {
                if (sampleList.size() != rs.getInt("count")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    private static void initAllSampleFromDB() {
        String sqlCode = "SELECT * FROM sample "
                + "WHERE sample_type != 'custom_capture' "
                + "and sample_finished = 1 "
                + "and sample_failure = 0 "
                + "and sample_name not like 'SRR%'";

        try {
            ResultSet rs = DBManager.executeQuery(sqlCode);

            while (rs.next()) {
                int sampleId = rs.getInt("sample_id");
                String familyId = rs.getString("sample_name").trim();
                String individualId = rs.getString("sample_name").trim();

                if (!sampleNameSet.contains(individualId)) {
                    sampleNameSet.add(individualId);
                } else {
                    // do not allow duplicate samples
                    continue;
                }

                String paternalId = "0";
                String maternalId = "0";
                byte sex = 1; // male
                byte pheno = 1; // control
                String sampleType = rs.getString("sample_type").trim();
                String captureKit = rs.getString("capture_kit").trim();

                Sample sample = new Sample(sampleId, familyId, individualId,
                        paternalId, maternalId, sex, pheno, sampleType, captureKit);

                sampleList.add(sample);
                sampleMap.put(sampleId, sample);
            }

            rs.close();
        } catch (Exception e) {
        }
    }

    public static int getTotalSampleNum() {
        return sampleList.size();
    }

    public static ArrayList<Sample> getList() {
        return sampleList;
    }
}
