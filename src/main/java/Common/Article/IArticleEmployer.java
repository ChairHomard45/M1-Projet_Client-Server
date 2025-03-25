package Common.Article;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IArticleEmployer extends Remote
{
    int ajouterArticle(String refArticle, int qte) throws RemoteException;
}
