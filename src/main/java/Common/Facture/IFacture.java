package Common.Facture;

import Common.Objects.ObjectFacture;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFacture extends Remote {
    ObjectFacture consulterFacture(int refCommanden, String date) throws RemoteException;
}
