package Server.Facture;

import Common.Facture.IFactureAcheteur;
import Common.Objects.ObjectArticle;
import Common.Objects.ObjectFacture;
import Server.Database.BD;
import Server.Utils.JSONAppend;
import Server.Utils.PathsClass;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FactureAcheteurImpl implements IFactureAcheteur {
    /**
     * Payer la commande et créer une facture.
     * @param refCommande Reférence de la commande à payer
     */
    @Override
    public int payerFacture(int refCommande, String modePaiement) {
        Connection con = BD.getInstance().getConnection();
        float montCom = 0;
        String dateActuel = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        PreparedStatement pS;
        ResultSet rs;

        try {
            pS = con.prepareStatement(
                    "SELECT status_commande " +
                            "FROM commande WHERE reference_commande = ?"
            );
            pS.setInt(1, refCommande);
            rs = pS.executeQuery();
            rs.next();
            if (Objects.equals(rs.getString("status_commande"), "Terminer")){
                return 1;
            }
            pS.close();
            rs.close();

            pS = con.prepareStatement(
                    "UPDATE commande " +
                            "SET status_commande = ? " +
                            "WHERE reference_commande = ?"
            );
            pS.setString(1, "Terminer");
            pS.setInt(2, refCommande);
            pS.executeUpdate();
            pS.close();

            List<ObjectArticle> articles = new ArrayList<>();

            pS = con.prepareStatement(
                    "SELECT DISTINCT co.reference_commande,co.montant_commande,con.qte,ar.* " +
                            "FROM commande AS co, article AS ar, contient AS con " +
                            "WHERE co.reference_commande=con.reference_commande " +
                            "AND con.reference_article=ar.reference_article " +
                            "AND co.reference_commande = ?;"
            );
            pS.setInt(1, refCommande);
            rs = pS.executeQuery();

            while (rs.next()) {
                montCom = rs.getFloat("montant_commande");
                articles.add(
                        new ObjectArticle(
                                rs.getString("reference_article"),
                                rs.getString("nom_article"),
                                rs.getString("famille_article"),
                                rs.getFloat("unite_prix"),
                                rs.getInt("quantite_stock")
                        )
                );
            }

            rs.close();
            pS.close();

            ObjectFacture objectFacture = new ObjectFacture(refCommande, montCom, dateActuel, modePaiement, articles);
            JSONAppend.insertJSONObject(objectFacture.toJSON(), PathsClass.getJSONFilePath(dateActuel));
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
