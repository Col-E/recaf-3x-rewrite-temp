package software.coley.recaf.workspace.model.resource;

import com.sun.tools.attach.VirtualMachine;
import jakarta.annotation.Nonnull;
import software.coley.instrument.data.ClassLoaderInfo;
import software.coley.recaf.workspace.model.bundle.JvmClassBundle;

import java.util.Map;
import java.util.Set;

/**
 * A resource sourced from a remote {@link VirtualMachine}.
 *
 * @author Matt Coley
 */
public interface WorkspaceRemoteVmResource extends WorkspaceResource {
	/**
	 * @return Virtual machine of the remote process attached to.
	 */
	@Nonnull
	VirtualMachine getVirtualMachine();

	/**
	 * @return Map of remote classloaders.
	 */
	@Nonnull
	Map<Integer, ClassLoaderInfo> getRemoteLoaders();

	/**
	 * @return Map of {@code ClassLoader} id to the classes defined by the loader.
	 *
	 * @see #getRemoteLoaders() Classloader values, keys of which are {@link ClassLoaderInfo#getId()}.
	 */
	@Nonnull
	Map<Integer, JvmClassBundle> getJvmClassloaderBundles();
}
