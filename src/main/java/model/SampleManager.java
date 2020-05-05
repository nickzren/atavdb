package model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import util.DBManager;

/**
 *
 * @author nick
 */
public class SampleManager {

    private static ArrayList<Sample> sampleList = new ArrayList<>();
    private static HashMap<Integer, Sample> sampleMap = new HashMap<>();

    public static void init() {
        if (checkSampleCount()) {
            initAllSampleFromDB();
        }
    }

    private static boolean checkSampleCount() {
        if (sampleList.isEmpty()) {
            return true;
        }

        String sqlCode = "SELECT count(*) as count FROM sample WHERE sample_finished = 1 AND sample_failure = 0";

        try {
            ResultSet rs = DBManager.executeQuery(sqlCode);

            if (rs.next()) {
                if (sampleList.size() != rs.getInt("count")) {
                    return true;
                }
            }
            
            rs.close();
        } catch (Exception e) {
        }

        return false;
    }

    private static void initAllSampleFromDB() {
        sampleList.clear();
        
        String sqlCode = "SELECT * FROM sample WHERE sample_finished = 1 AND sample_failure = 0";

        try {
            ResultSet rs = DBManager.executeQuery(sqlCode);

            while (rs.next()) {
                int sampleId = rs.getInt("sample_id");
                String familyId = rs.getString("sample_name").trim();
                String individualId = rs.getString("sample_name").trim();
                String paternalId = "0";
                String maternalId = "0";
                String seqGender = rs.getString("seq_gender").trim();
                byte sex = 1; // male
                if(seqGender.equals("F")) {
                    sex = 2;
                }
                byte pheno = 1; // control
                String sampleType = rs.getString("sample_type").trim();
                String captureKit = rs.getString("capture_kit").trim();
                int experimentId = rs.getInt("experiment_id");
                String broadPhenotype = rs.getString("broad_phenotype").trim();

                Sample sample = new Sample(sampleId, familyId, individualId,
                        paternalId, maternalId, sex, pheno, sampleType, captureKit
                        , experimentId, broadPhenotype);

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
    
    public static Sample getSample(int sampleId) {
        return sampleMap.get(sampleId);
    }
}
