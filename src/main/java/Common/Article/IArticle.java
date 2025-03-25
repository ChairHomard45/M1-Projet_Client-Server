package Common.Article;

import Common.Objects.ObjectArticle;

import java.util.List;

public interface IArticle
{
    ObjectArticle getInfoArticle(String refArticle);
    List<String> getRefsArticles(String refFamille);
}
