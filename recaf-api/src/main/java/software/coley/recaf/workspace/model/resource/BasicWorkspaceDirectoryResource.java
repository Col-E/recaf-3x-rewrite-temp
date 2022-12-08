package software.coley.recaf.workspace.model.resource;

import java.nio.file.Path;

/**
 * Basic implementation of a workspace resource sourced from a directory.
 *
 * @author Matt Coley
 */
public class BasicWorkspaceDirectoryResource extends BasicWorkspaceResource implements WorkspaceDirectoryResource {
	private final Path directoryPath;

	/**
	 * @param builder
	 * 		Builder to pull info from.
	 */
	public BasicWorkspaceDirectoryResource(WorkspaceResourceBuilder builder) {
		super(builder);
		this.directoryPath = builder.getFilePath();
	}

	@Override
	public Path getDirectoryPath() {
		return directoryPath;
	}
}