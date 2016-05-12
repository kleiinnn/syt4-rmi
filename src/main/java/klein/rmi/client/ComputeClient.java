package klein.rmi.client;

import klein.rmi.compute.Compute;
import klein.rmi.compute.SolutionCallback;
import klein.rmi.compute.task.EulerCalculateTask;
import klein.rmi.compute.task.PiCalculateTask;
import klein.rmi.compute.task.Task;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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

    public void executeTask(Task task, SolutionCallback callback) throws RemoteException {
        compute.executeTask(task, callback);
    }

    private static void printUsage() {
        System.out.println("Parameters: [pi, euler] <precision> <server-host>");
    }

    public static void main(String[] args) {
        if(args.length != 3) {
            System.err.println("Wrong paramters.");
            printUsage();
            return;
        }

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

            client.executeTask(task, (SolutionCallback) UnicastRemoteObject.exportObject(callback, 0));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}