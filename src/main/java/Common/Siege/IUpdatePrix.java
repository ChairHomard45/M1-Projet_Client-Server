package Common.Siege;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface IUpdatePrix extends Remote {
    int updatePrix(HashMap<String, Float> dictionary) throws RemoteException;
}
