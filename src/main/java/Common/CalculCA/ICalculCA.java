package Common.CalculCA;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICalculCA  extends Remote {
    float getCA(String date) throws RemoteException;
}
