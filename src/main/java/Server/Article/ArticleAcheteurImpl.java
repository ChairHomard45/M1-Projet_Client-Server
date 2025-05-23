package Server.Article;

import Common.Article.IArticleAcheteur;
import Common.Objects.ObjectArticle;
import Server.Database.BD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleAcheteurImpl implements IArticleAcheteur {

    /**
     * @param refArticle reference Article
     * @param qte        Quantité à acheter
     * @return Objet Article acheté
     */
    @Override
    public synchronized ObjectArticle acheterArticle(int refCommande, String refArticle, int qte) {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement pS;
        ResultSet rs;
        ObjectArticle article = null;

        try {
            // Check if Article en Stock demander
            pS = con.prepareStatement("SELECT * FROM article WHERE reference_article = ?");
            pS.setString(1, refArticle);
            rs = pS.executeQuery();
            rs.next();
            if (rs.getInt("quantite_stock") < qte) return null;
            float montant = rs.getFloat("unite_prix");
            int stock = rs.getInt("quantite_stock") - qte;

            // Recuperer le montant de la commande actuelle
            pS = con.prepareStatement("SELECT * FROM commande WHERE reference_commande = ?");
            pS.setInt(1, refCommande);
            rs = pS.executeQuery();
            rs.next();
            if (rs.getString("status_commande").equals("Terminer")) return null;
            float montant_commande = rs.getFloat("montant_commande");

            // Decrementer le stock de l'article acheter
            pS = con.prepareStatement("UPDATE article SET quantite_stock = ? WHERE reference_article = ?");
            pS.setInt(1, stock);
            pS.setString(2, refArticle);
            pS.executeUpdate();

            // Check si le lien existe déjà
            pS = con.prepareStatement("SELECT * FROM contient WHERE reference_commande = ? AND reference_article = ?");
            pS.setInt(1, refCommande);
            pS.setString(2, refArticle);
            rs = pS.executeQuery();
            rs.next();

            if (rs.getRow() == 0) {
                // Ajouter le lien entre la commande et l'article dans la base
                pS = con.prepareStatement("INSERT INTO contient VALUES(?,?,?)");
                pS.setString(1, refArticle);
                pS.setInt(2, refCommande);
                pS.setInt(3, qte);
            } else {
                // Ajoute une quantiter si existe déjà (Utilise le select de la commande précédente)
                pS = con.prepareStatement("UPDATE contient SET qte = ? WHERE reference_commande = ? AND reference_article = ?");
                pS.setInt(1, rs.getInt("qte") + qte);
                pS.setInt(2, refCommande);
                pS.setString(3, refArticle);
                qte = rs.getInt("qte") + qte;
            }
            pS.executeUpdate();


            // Update Commande montant
            pS = con.prepareStatement("Update commande set montant_commande = ? WHERE reference_commande = ?");
            pS.setFloat(1, CalculFloat(montant, montant_commande));
            pS.setInt(2, refCommande);
            pS.executeUpdate();

            // Recuperer Info Article
            pS = con.prepareStatement("SELECT * FROM article WHERE reference_article = ?");
            pS.setString(1, refArticle);
            rs = pS.executeQuery();
            rs.next();

            article = new ObjectArticle(refArticle, rs.getString("nom_article"), rs.getString("famille_article"), rs.getFloat("unite_prix"), qte);
            pS.close();
            rs.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
        return article;
    }

    /**
     * @param refCommande reference de commande
     * @return returner 0 si OK, -1 si Exception et 1 si existe déjà
     */
    @Override
    public synchronized int creerCommande(int refCommande) {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement pS;
        ResultSet rs;
        try {

            // Check si Commande existe
            pS = con.prepareStatement("SELECT * FROM commande WHERE reference_commande = ?");
            pS.setInt(1, refCommande);
            rs = pS.executeQuery();
            rs.next();

            if (rs.getRow() != 0) {
                if (Objects.equals(rs.getString("status_commande"), "Terminer")){
                    return 2;
                }
                return 1;
            }

            pS = con.prepareStatement("INSERT INTO commande VALUES(?,?,?)");
            pS.setInt(1, refCommande);
            pS.setFloat(2, 0.00f);
            pS.setString(3, "En Cours");
            pS.executeUpdate();
            pS.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }

        return 0;
    }

    public synchronized List<ObjectArticle> consulterCommande(int refCommande) throws RemoteException{
        Connection con = BD.getInstance().getConnection();
        PreparedStatement pS;
        ResultSet rs;
        List<ObjectArticle> articles = new ArrayList<>();

        try {
            pS = con.prepareStatement(
                    "SELECT status_commande " +
                            "FROM commande WHERE reference_commande = ?"
            );
            pS.setInt(1, refCommande);
            rs = pS.executeQuery();
            rs.next();
            if (Objects.equals(rs.getString("status_commande"), "Terminer")){
                return null;
            }
            pS.close();
            rs.close();

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
                articles.add(
                        new ObjectArticle(
                                rs.getString("reference_article"),
                                rs.getString("nom_article"),
                                rs.getString("famille_article"),
                                rs.getFloat("unite_prix"),
                                rs.getInt("qte")
                        )
                );
            }

            rs.close();
            pS.close();

            return articles;
        } catch (SQLException er){
            er.printStackTrace();
        }

        return null;
    }

    private float CalculFloat(float a, float b) {
        BigDecimal c = BigDecimal.ZERO;
        c = c.add(BigDecimal.valueOf(a));
        c = c.add(BigDecimal.valueOf(b));
        return c.setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
