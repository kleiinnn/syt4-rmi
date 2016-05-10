package klein.rmi.engine;
import klein.rmi.client.ComputeClient;
import klein.rmi.compute.SolutionCallback;
import klein.rmi.compute.task.Task;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by markus on 10/05/16.
 */
public class ComputeEngineTest {

    @Test
    public void testExecuteTask() throws Exception {
        TestSolutionCallback mockedCallback = mock(TestSolutionCallback.class);

        ComputeEngine engine = new ComputeEngine();
        engine.runEngine();

        ComputeClient client = new ComputeClient("localhost");
        TestTask task = new TestTask((SolutionCallback<Integer>) UnicastRemoteObject.exportObject(mockedCallback, 0));
        client.executeTask(task);

        verify(mockedCallback).getSolution(1);
    }

    private static class TestSolutionCallback implements SolutionCallback<Integer> {
        public void getSolution(Integer solution) throws RemoteException { }
    }

    private static class TestTask implements Task<Integer> {
        private SolutionCallback<Integer> callback;

        public TestTask(SolutionCallback<Integer> callback) {
            this.callback = callback;
        }

        public SolutionCallback<Integer> getCallback() {
            return callback;
        }

        public Integer execute() {
            return 1;
        }
    }
}