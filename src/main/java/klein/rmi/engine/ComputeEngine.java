/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package klein.rmi.engine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import klein.rmi.compute.Compute;
import klein.rmi.compute.SolutionCallback;
import klein.rmi.compute.task.Task;

/**
 * ComputeEngine is the reference implementation of @{link Compute} and executes tasks.
 */
public class ComputeEngine implements Compute {

    /**
     * Create a new ComputeEngine.
     */
    public ComputeEngine() {
        super();
    }

    public <T> void executeTask(Task<T> t, SolutionCallback<T> callback) {
        new Thread(new TaskExecutor<>(t, callback)).start();
    }

    /**
     * Run the engine and export it as and RMI remote object.
     */
    public void runEngine() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Compute stub =
                    (Compute) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }

    /**
     * Main server class.
     * Creates and runs a ComputeEngine
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ComputeEngine engine = new ComputeEngine();
        engine.runEngine();
    }

    /**
     * TaskExecutor is responsible for the actual task execution.
     * @param <T> task return type
     */
    private static class TaskExecutor<T> implements Runnable {
        private Task<T> task;
        private SolutionCallback<T> callback;

        /**
         * Create a new TaskExecutor instance with the given task and callback.
         * @param task task which should be executed
         * @param callback callback which should receive the calculation result
         */
        private TaskExecutor(Task<T> task, SolutionCallback<T> callback) {
            this.task = task;
            this.callback = callback;
        }

        /**
         * Execute the calculation and return pass the solution to the client via the callback.
         */
        @Override
        public void run() {
            T solution = task.execute();
            try {
                callback.getSolution(solution);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
