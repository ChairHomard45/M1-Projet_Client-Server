package Server.Article;

import Common.Article.IArticleEmployer;
import Server.Database.BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleEmployerImpl implements IArticleEmployer {
    /**
     * @param refArticle Reference de l'article
     * @param qte        quantité à ajouter
     * @return returner 0 si OK, -1 si Exception et 1 si existe déjà
     */
    @Override
    public int ajouterArticle(String refArticle, int qte) {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement pS;
        ResultSet rs;
        int qteArticle;

        try {
            pS = con.prepareStatement("SELECT * FROM article WHERE reference_article = ?");
            pS.setString(1, refArticle);
            rs = pS.executeQuery();
            rs.next();
            if (rs.getRow() == 0) return 1;
            qteArticle = rs.getInt("quantite_stock");

            pS = con.prepareStatement("UPDATE article SET quantite_stock = ? WHERE reference_article = ?");
            pS.setInt(1, qte + qteArticle);
            pS.setString(2, refArticle);
            pS.executeUpdate();

            pS.close();
            rs.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
        return 0;
    }
}
