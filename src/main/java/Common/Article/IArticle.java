package Common.Article;

import Common.Objects.ObjectArticle;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Dictionary;
import java.util.List;

public interface IArticle extends Remote
{
    ObjectArticle getInfoArticle(String refArticle) throws RemoteException;
    List<String> getRefsArticles(String refFamille) throws RemoteException;
    Dictionary<String, String> getRefsArticles() throws RemoteException;
}
