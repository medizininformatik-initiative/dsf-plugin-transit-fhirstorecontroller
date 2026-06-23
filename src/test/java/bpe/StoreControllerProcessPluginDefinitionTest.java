package bpe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.StoreControllerProcessPluginDefinition;
import org.junit.Test;

import dev.dsf.bpe.v2.ProcessPluginDefinition;

public class StoreControllerProcessPluginDefinitionTest {
    @Test
    public void testResourceLoading()
    {
        ProcessPluginDefinition definition = new StoreControllerProcessPluginDefinition();
        Map<String, List<String>> resourcesByProcessId = definition.getFhirResourcesByProcessId();

        var receive = resourcesByProcessId.get(StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_CREATE);
        assertNotNull(receive);
        assertEquals(8, receive.stream().filter(this::exists).count());

        var send = resourcesByProcessId.get(StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_DELETE);
        assertNotNull(send);
        assertEquals(4, send.stream().filter(this::exists).count());
    }

    private boolean exists(String file)
    {
        return getClass().getClassLoader().getResourceAsStream(file) != null;
    }

}
