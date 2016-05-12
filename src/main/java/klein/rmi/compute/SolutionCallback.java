package klein.rmi.compute;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An instance of SolutionCallback serves as a callback for remote calculations and receives the solution.
 */
public interface SolutionCallback<T> extends Remote, Serializable {
    /**
     * Asynchronous callback method for calculation tasks.
     * @param solution task solution
     * @throws RemoteException
     */
    void getSolution(T solution) throws RemoteException;
}
