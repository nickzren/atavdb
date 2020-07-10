package org.atavdb.service;

import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nick
 */
public class ExternalDataManager {

    private static final String EXAC_TABLE = "exac.variant_r03_2015_09_16";
    private static final String GENOMEASIA_TABLE = "genomeasia.variant_chr";
    private static final String GNOMAD_EXOME_TABLE = "gnomad_2_1.exome_variant";
    private static final String GNOMAD_GENOME_TABLE = "gnomad_2_1.genome_variant_chr";
    private static final String GME_TABLE = "gme.variant";
    private static final String IRANOME_TABLE = "iranome.variant";
    private static final String TOPMED_TABLE = "topmed.variant_chr";

    public static float getExAC(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        try {
            String sql = "SELECT global_af FROM " + EXAC_TABLE
                    + " WHERE chr=? AND pos=? AND ref_allele=? AND alt_allele=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("global_af");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }

    public static float getGenomeAsia(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        if (chr.equalsIgnoreCase("MT")) { // not support MT regions
            return af;
        }

        try {
            String sql = "SELECT af FROM " + GENOMEASIA_TABLE
                    + " WHERE chr=? AND pos=? AND ref=? AND alt=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("af");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }

    public static float getGenoADExome(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        try {
            String sql = "SELECT global_AF FROM " + GNOMAD_EXOME_TABLE
                    + " WHERE chr=? AND pos=? AND ref=? AND alt=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("global_AF");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }

    public static float getGenoADGenome(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        try {
            String sql = "SELECT global_AF FROM " + GNOMAD_GENOME_TABLE
                    + " WHERE chr=? AND pos=? AND ref=? AND alt=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("global_AF");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }

    public static float getGME(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        if (chr.equalsIgnoreCase("Y") || chr.equalsIgnoreCase("MT")) { // not support Y or MT regions
            return af;
        }

        try {
            String sql = "SELECT af FROM " + GME_TABLE
                    + " WHERE chr=? AND pos=? AND ref=? AND alt=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("af");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }

    public static float getIRANOME(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        if (chr.equalsIgnoreCase("MT")) { // not support Y or MT regions
            return af;
        }

        try {
            String sql = "SELECT af FROM " + IRANOME_TABLE
                    + " WHERE chr=? AND pos=? AND ref=? AND alt=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("af");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }

    public static float getTOPMED(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        if (chr.equalsIgnoreCase("Y") || chr.equalsIgnoreCase("MT")) { // not support Y and MT regions
            return af;
        }

        try {           
            String sql = "SELECT af FROM " + TOPMED_TABLE
                    + " WHERE chr=? AND pos=? AND ref=? AND alt=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chr);
            preparedStatement.setInt(2, pos);
            preparedStatement.setString(3, ref);
            preparedStatement.setString(4, alt);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                af = rs.getFloat("af");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }

        return af;
    }
}
