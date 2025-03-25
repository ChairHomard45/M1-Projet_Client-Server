package Common.Article;

import Common.Objects.ObjectArticle;

public interface IArticleAcheteur {
    ObjectArticle acheterArticle(int refCommande, String refArticle, int qte);
    int creerCommande(int refCommande);
}
