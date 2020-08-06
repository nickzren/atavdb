package org.atavdb.service.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.atavdb.service.util.DBManager;
import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author nick
 */
@Service
@ComponentScan("org.atavdb.service")
public class GeneManager {

    @Autowired
    DBManager dbManager;

    private final String HGNC_MAP_FILE = "/hgnc_complete_set_to_GRCh37.87.tsv.gz";
    private HashMap<String, String> geneMap = new HashMap<>();

    public String getChr(String gene) throws Exception {
        gene = getNameByAlternativeName(gene);
        
        String sql = "SELECT chrom FROM hgnc WHERE gene=?";

        Connection connection = dbManager.getConnection();
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

    public GeneManager() throws IOException {
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
    }

    public String getNameByAlternativeName(String gene) {
        return geneMap.getOrDefault(gene, gene);
    }

    public boolean isValid(String gene) throws Exception {
        return !getChr(gene).equals(Data.STRING_NA);
    }
}
