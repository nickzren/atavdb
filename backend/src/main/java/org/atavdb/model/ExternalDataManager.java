package org.atavdb.model;

import org.atavdb.util.DBManager;
import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private static final String IGMAF_TABLE = "igm_af.variant_051023";
    private static final String IGMAF_SUBSET_TABLE = "igm_af.variant_subset_051023";
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
            String sql = "SELECT af FROM " + GENOMEASIA_TABLE + chr
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
            String sql = "SELECT global_AF FROM " + GNOMAD_GENOME_TABLE + chr
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

    public static void setIGMAF(Variant var, SearchFilter filter) {
        try {
            String table = filter.isAvailableControlUseOnly() ? IGMAF_SUBSET_TABLE : IGMAF_TABLE;

            String sql = "SELECT ac,an,af,ns,nhom FROM " + table + " WHERE chr=? AND variant_id=?";

            Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, var.getChr());
            preparedStatement.setInt(2, var.getVariantId());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                var.setAC(rs.getInt("ac"));
                var.setAN(rs.getInt("an"));
                var.setAF(rs.getFloat("af"));
                var.setNS(rs.getInt("ns"));
                var.setNHOM(rs.getInt("nhom"));
            } else {
                var.setIsValid(false);
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
        }
    }

    /*
        check if IGM AF table has been populated or not
    */
    public static boolean isIGMAFEmpty(SearchFilter filter) {
        try {
            String table = filter.isAvailableControlUseOnly() ? IGMAF_SUBSET_TABLE : IGMAF_TABLE;
            String sql = "SELECT * from " + table + " limit 1";
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return false;
            }

            rs.close();
            statement.close();
        } catch (SQLException ex) {
        }

        return true;
    }

    public static float getTOPMED(String chr, int pos, String ref, String alt) {
        float af = Data.FLOAT_NA;

        if (chr.equalsIgnoreCase("Y") || chr.equalsIgnoreCase("MT")) { // not support Y and MT regions
            return af;
        }

        try {
            String sql = "SELECT af FROM " + TOPMED_TABLE + chr
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
