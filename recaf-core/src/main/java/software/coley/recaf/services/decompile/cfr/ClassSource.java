package software.coley.recaf.services.decompile.cfr;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import software.coley.recaf.info.JvmClassInfo;
import software.coley.recaf.util.visitors.ClassHollowingVisitor;
import software.coley.recaf.workspace.model.Workspace;
import software.coley.recaf.workspace.query.QueryResult;

import java.util.Collection;
import java.util.Collections;

/**
 * CFR class source. Provides access to workspace clases.
 *
 * @author Matt
 */
public class ClassSource implements ClassFileSource {
	private final Workspace workspace;
	private final String targetClassName;
	private final byte[] targetClassBytecode;

	/**
	 * Constructs a CFR class source.
	 *
	 * @param workspace
	 * 		Workspace to pull classes from.
	 * @param targetClassName
	 * 		Name to override.
	 * @param targetClassBytecode
	 * 		Bytecode to override.
	 */
	public ClassSource(Workspace workspace, String targetClassName, byte[] targetClassBytecode) {
		this.workspace = workspace;
		this.targetClassName = targetClassName;
		this.targetClassBytecode = targetClassBytecode;
	}

	@Override
	public void informAnalysisRelativePathDetail(String usePath, String specPath) {
	}

	@Override
	public Collection<String> addJar(String jarPath) {
		return Collections.emptySet();
	}

	@Override
	public String getPossiblyRenamedPath(String path) {
		return path;
	}

	@Override
	public Pair<byte[], String> getClassFileContent(String inputPath) {
		String className = inputPath.substring(0, inputPath.indexOf(".class"));
		byte[] code;
		if (className.equals(targetClassName)) {
			code = targetClassBytecode;
		} else {
			QueryResult<JvmClassInfo> result = workspace.findJvmClass(className);
			code = result.isEmpty() ? null : result.getItem().getBytecode();

			// Simply CFR's work-load by gutting supporting class internals
			if (code != null) {
				ClassWriter writer = new ClassWriter(0);
				ClassHollowingVisitor hollower = new ClassHollowingVisitor(writer);
				new ClassReader(code).accept(hollower, ClassReader.SKIP_CODE);
				code = writer.toByteArray();
			}
		}
		return new Pair<>(code, inputPath);
	}
}
