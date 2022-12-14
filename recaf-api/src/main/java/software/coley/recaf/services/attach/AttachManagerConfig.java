package software.coley.recaf.services.attach;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.coley.recaf.config.BasicConfigContainer;
import software.coley.recaf.services.ServiceConfig;
import software.coley.recaf.services.file.RecafDirectoriesConfig;

import java.nio.file.Path;

/**
 * Config for {@link AttachManager}.
 *
 * @author Matt Coley
 */
@ApplicationScoped
public class AttachManagerConfig extends BasicConfigContainer implements ServiceConfig {
	private final RecafDirectoriesConfig directories;

	@Inject
	public AttachManagerConfig(RecafDirectoriesConfig directories) {
		super(AttachManager.SERVICE_ID + CONFIG_SUFFIX);
		this.directories = directories;
	}

	/**
	 * @return Mirror of {@link RecafDirectoriesConfig#getAgentDirectory()}.
	 */
	public Path getAgentDirectory() {
		return directories.getAgentDirectory();
	}
}
