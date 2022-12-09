package software.coley.recaf.info;

import software.coley.recaf.info.builder.FileInfoBuilder;

/**
 * Basic implementation of WAR file info.
 *
 * @author Matt Coley
 */
public class BasicWarFileInfo extends BasicZipFileInfo implements WarFileInfo {
	/**
	 * @param builder
	 * 		Builder to pull information from.
	 */
	public BasicWarFileInfo(FileInfoBuilder<?> builder) {
		super(builder);
	}
}
