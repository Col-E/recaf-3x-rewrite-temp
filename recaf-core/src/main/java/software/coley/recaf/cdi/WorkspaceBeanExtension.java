package software.coley.recaf.cdi;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;

/**
 * Extension that enabled {@link WorkspaceScoped} annotated beans to be registered with {@link WorkspaceBeanContext}.
 * The custom context enables some workspace-oriented operations.
 *
 * @author Matt Coley
 */
@SuppressWarnings("unused")
public class WorkspaceBeanExtension implements Extension {
	public void registerContext(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
		WorkspaceBeanContext context = WorkspaceBeanContext.getInstance();
		event.addContext(context);
	}
}