package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by markus on 06/05/16.
 */
public interface SolutionCallback<T> extends Remote {
    void getSolution(T solution) throws RemoteException;
}
