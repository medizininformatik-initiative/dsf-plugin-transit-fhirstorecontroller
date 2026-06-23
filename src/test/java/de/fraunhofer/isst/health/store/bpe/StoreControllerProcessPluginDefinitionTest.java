package de.fraunhofer.isst.health.store.bpe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.fraunhofer.isst.health.store.StoreControllerConstants;
import de.fraunhofer.isst.health.store.StoreControllerProcessPluginDefinition;
import dev.dsf.bpe.v2.ProcessPluginDefinition;

public class StoreControllerProcessPluginDefinitionTest
{
	@Test
	public void testResourceLoading()
	{
		ProcessPluginDefinition definition = new StoreControllerProcessPluginDefinition();
		Map<String, List<String>> resourcesByProcessId = definition.getFhirResourcesByProcessId();

		var create = resourcesByProcessId.get(StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_CREATE);
		assertNotNull(create);
		assertEquals(8, create.stream().filter(this::exists).count());

		var delete = resourcesByProcessId.get(StoreControllerConstants.PROCESS_NAME_FULL_STORE_CONTROLLER_DELETE);
		assertNotNull(delete);
		assertEquals(4, delete.stream().filter(this::exists).count());
	}

	private boolean exists(String file)
	{
		return getClass().getClassLoader().getResourceAsStream(file) != null;
	}
}
