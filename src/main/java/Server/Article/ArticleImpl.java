package Server.Article;

import Common.Article.IArticle;
import Common.Objects.ObjectArticle;
import Server.Database.BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleImpl implements IArticle
{
    @Override
    public ObjectArticle getInfoArticle(String refArticle)
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

            if(!rs.next())
            {
                return null;
            }

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

    @Override
    public List<String> getRefsArticles(String refFamille)
    {
        Connection con = BD.getInstance().getConnection();
        PreparedStatement ps;
        ResultSet rs;

        try
        {
            ps = con.prepareStatement(
                "SELECT reference_article " +
                    "FROM article " +
                    "WHERE famille_article = ? AND quantite_stock > 0"
            );
            ps.setString(1, refFamille);
            rs = ps.executeQuery();

            List<String> refsArticles = new ArrayList<>();

            while (rs.next())
            {
                refsArticles.add(rs.getString("reference_article"));
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
