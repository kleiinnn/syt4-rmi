package klein.rmi.client;

import klein.rmi.compute.Compute;
import klein.rmi.compute.task.Task;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by markus on 10/05/16.
 */
public class ComputeClient {
    public static final String LOOKUP_NAME = "Compute";

    private Compute compute;

    public ComputeClient(String host) throws RemoteException, NotBoundException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Registry registry = LocateRegistry.getRegistry(host);
        compute = (Compute) registry.lookup(LOOKUP_NAME);
    }

    public void executeTask(Task task) throws RemoteException {
        compute.executeTask(task);
    }
}