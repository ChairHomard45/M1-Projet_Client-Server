package Server.Article;

import Common.Article.IArticle;
import Common.Objects.ObjectArticle;
import Server.Database.BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ArticleImpl implements IArticle
{
    /**
     * @param refArticle référence de l'article
     * @return un objet Article avec les informations
     */
    @Override
    public synchronized ObjectArticle getInfoArticle(String refArticle)
    {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement ps;
        ResultSet rs;

        try
        {
            ps = con.prepareStatement(
                "SELECT * " +
                    "FROM article " +
                    "WHERE reference_article = ?"
            );
            ps.setString(1, refArticle);
            rs = ps.executeQuery();
            rs.next();

            if(rs.getRow() == 0) return null;

            ObjectArticle objA = new ObjectArticle(
                rs.getString("reference_article"),
                rs.getString("nom_article"),
                rs.getString("famille_article"),
                rs.getFloat("unite_prix"),
                rs.getInt("quantite_stock")
            );

            rs.close();
            ps.close();

            return objA;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param refFamille référence de la famille d'articles
     * @return une liste des références d'articles de cette famille
     */
    @Override
    public synchronized Dictionary<String, String> getRefsArticles(String refFamille)
    {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement ps;
        ResultSet rs;

        try
        {
            ps = con.prepareStatement(
                "SELECT reference_article, nom_article " +
                    "FROM article " +
                    "WHERE famille_article = ? AND quantite_stock > 0"
            );
            ps.setString(1, refFamille);
            rs = ps.executeQuery();

            Hashtable<String, String> refsArticles = new Hashtable<>();

            while (rs.next())
            {
                refsArticles.put(rs.getString("reference_article"), rs.getString("nom_article"));
            }

            rs.close();
            ps.close();

            return refsArticles;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return un dictionnaire des références d'articles
     */
    @Override
    public synchronized Dictionary<String, String> getRefsArticles()
    {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement ps;
        ResultSet rs;

        try
        {
            ps = con.prepareStatement(
                    "SELECT reference_article, nom_article " +
                            "FROM article " +
                            "WHERE quantite_stock > 0"
            );
            rs = ps.executeQuery();

            Hashtable<String, String> refsArticles = new Hashtable<>();

            while (rs.next())
            {
                refsArticles.put(rs.getString("reference_article"), rs.getString("nom_article"));
            }

            rs.close();
            ps.close();

            return refsArticles;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
