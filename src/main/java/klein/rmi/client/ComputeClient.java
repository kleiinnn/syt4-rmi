package klein.rmi.client;

import klein.rmi.compute.Compute;
import klein.rmi.compute.SolutionCallback;
import klein.rmi.compute.task.EulerCalculateTask;
import klein.rmi.compute.task.PiCalculateTask;
import klein.rmi.compute.task.Task;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * ComputeClient is the main client class.
 * An instance of ComputeClient wraps the RMI functionality and
 * provides and convenient interface for executing calculation tasks.
 */
public class ComputeClient {
    public static final String LOOKUP_NAME = "Compute";

    private Compute compute;

    /**
     * Creates a new ComputeClient instance and connects to the RMI server at the given hosts.
     * @param host RMI server host
     * @throws RemoteException
     * @throws NotBoundException
     */
    public ComputeClient(String host) throws RemoteException, NotBoundException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Registry registry = LocateRegistry.getRegistry(host);
        compute = (Compute) registry.lookup(LOOKUP_NAME);
    }

    /**
     * Execute the given task on the server.
     * @param task task which should be executed
     * @param callback callback which is called when the calculation is finished
     * @throws RemoteException
     */
    public void executeTask(Task task, SolutionCallback callback) throws RemoteException {
        compute.executeTask(task, callback);
    }

    /**
     * Prints usage information
     */
    private static void printUsage() {
        System.out.println("Parameters: [pi, euler] <precision> <server-host>");
    }

    /**
     * Main method for client
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // check parameter length
        if(args.length != 3) {
            System.err.println("Wrong parameters.");
            printUsage();
            return;
        }

        // create callback
        SolutionCallback callback = new SolutionCallback() {
            @Override
            public void getSolution(Object solution) throws RemoteException {
                System.out.println(solution);
                UnicastRemoteObject.unexportObject(this, true);
            }
        };

        try {
            ComputeClient client = new ComputeClient(args[2]);
            Task task = null;

            if(args[0].equals("pi")) {
                task = new PiCalculateTask(Integer.parseInt(args[1]));
            } else if(args[0].equals("euler")) {
                task = new EulerCalculateTask(Integer.parseInt(args[1]));
            } else {
                System.err.println("Wrong paramters.");
                printUsage();
                return;
            }

            // execute task
            client.executeTask(task, (SolutionCallback) UnicastRemoteObject.exportObject(callback, 0));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}