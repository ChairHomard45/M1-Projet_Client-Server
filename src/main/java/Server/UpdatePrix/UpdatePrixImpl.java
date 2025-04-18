package Server.UpdatePrix;

import Common.Siege.IUpdatePrix;
import Server.Database.BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UpdatePrixImpl implements IUpdatePrix {
    /**
     * @param dictionary Dictionnaire avec les prix
     * @return status
     */
    @Override
    public int updatePrix(HashMap<String, Float> dictionary) {
        if (dictionary.isEmpty()) {
            return 1;
        }
        Connection con = BD.getInstance().getConnection();
        PreparedStatement pSCheck;

        try {
            pSCheck = con.prepareStatement(
                    "SELECT reference_article "
                            + " FROM article "
                            + " WHERE reference_article = ?"
            );
            for (Map.Entry<String, Float> entry : dictionary.entrySet()) {
                pSCheck.setString(1, entry.getKey());
                try (ResultSet rs = pSCheck.executeQuery()) {
                    if (!rs.next()) {
                        return -2;
                    }
                }
            }
            pSCheck.close();

            try (PreparedStatement pS = con.prepareStatement(
                    "UPDATE article " +
                            "SET unite_prix = ? " +
                            "WHERE reference_article = ?"
            )) {
                // Update each article's price
                for (Map.Entry<String, Float> entry : dictionary.entrySet()) {
                    pS.setFloat(1, entry.getValue());
                    pS.setString(2, entry.getKey());
                    pS.executeUpdate();
                }
            }

            return 0;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return -1;
        }
    }
}
