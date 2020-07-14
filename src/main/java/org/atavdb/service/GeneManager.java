package org.atavdb.service;

import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author nick
 */
public class GeneManager {

    public static String getChr(String gene) {
        try {
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
        } catch (Exception e) {
            return Data.STRING_NA;
        }

        return Data.STRING_NA;
    }

    public static boolean isValid(String gene) {
        if (getChr(gene).equals(Data.STRING_NA)) {
            return false;
        }

        return true;
    }
}
