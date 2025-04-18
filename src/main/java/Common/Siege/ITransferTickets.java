package Common.Siege;

import Common.Objects.ObjectFacture;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITransferTickets extends Remote {
    int transferTicketJson(String filename, ObjectFacture[] tickets) throws RemoteException;
}
