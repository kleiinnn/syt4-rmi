package klein.rmi.compute;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by markus on 06/05/16.
 */
public interface SolutionCallback<T> extends Remote, Serializable {
    void getSolution(T solution) throws RemoteException;
}
