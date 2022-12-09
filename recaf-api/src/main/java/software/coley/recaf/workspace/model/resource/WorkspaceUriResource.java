package software.coley.recaf.workspace.model.resource;

import jakarta.annotation.Nonnull;
import software.coley.recaf.info.FileInfo;

import java.net.URI;

/**
 * A resource sourced from content denoted by an {@link java.net.URI}.
 *
 * @author Matt Coley
 */
public interface WorkspaceUriResource extends WorkspaceResource {
	/**
	 * @return Information about the file loaded from.
	 */
	@Nonnull
	FileInfo getFileInfo();

	/**
	 * @return URI of which the contents of this resource originate from.
	 */
	@Nonnull
	URI getUri();
}
