package org.atavdb.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.atavdb.util.DBManager;
import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author nick
 */
public class GeneManager {

    private static final String HGNC_MAP_FILE = "/hgnc_complete_set_to_GRCh37.87.tsv.gz";
    private static HashMap<String, String> geneMap = new HashMap<>();

    static {
        try {
            Resource resource = new ClassPathResource(HGNC_MAP_FILE);
            GZIPInputStream in = new GZIPInputStream(resource.getInputStream());
            Reader decoder = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(decoder);

            String line = "";
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    String[] tmp = line.split("\t");
                    for (String gene : tmp[1].split("\\|")) {
                        if (geneMap.containsKey(gene)) {
                            geneMap.remove(gene);
                        } else {
                            geneMap.put(gene, tmp[0]);
                        }
                    }
                }
            }

            br.close();
            decoder.close();
            in.close();
        } catch (IOException e) {
        }
    }

    public static String getChr(String gene) throws Exception {
        gene = getNameByAlternativeName(gene);

        String sql = "SELECT chrom FROM hgnc WHERE gene=?";

        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, gene);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            return rs.getString("chrom");
        }

        rs.close();
        preparedStatement.close();

        return Data.STRING_NA;
    }

    public static String getNameByAlternativeName(String gene) {
        return geneMap.getOrDefault(gene, gene);
    }

    public static boolean isValid(String gene) throws Exception {
        return !getChr(gene).equals(Data.STRING_NA);
    }
}
