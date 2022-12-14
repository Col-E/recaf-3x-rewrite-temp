package software.coley.recaf.services.decompile.cfr;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.util.CfrVersionInfo;
import org.benf.cfr.reader.util.DecompilerComment;
import software.coley.recaf.services.decompile.AbstractJvmDecompiler;
import software.coley.recaf.services.decompile.DecompileResult;
import software.coley.recaf.util.ReflectUtil;
import software.coley.recaf.workspace.model.Workspace;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * CFR decompiler implementation.
 *
 * @author Matt Coley
 */
@ApplicationScoped
public class CfrDecompiler extends AbstractJvmDecompiler {
	public static final String NAME = "CFR";
	private final CfrConfig config;

	/**
	 * New CFR decompiler instance.
	 */
	@Inject
	public CfrDecompiler(CfrConfig config) {
		super(NAME, CfrVersionInfo.VERSION);
		this.config = config;
	}

	@Override
	public DecompileResult decompile(Workspace workspace, String name, byte[] bytecode) {
		ClassSource source = new ClassSource(workspace, name, bytecode);
		SinkFactoryImpl sink = new SinkFactoryImpl();
		CfrDriver driver = new CfrDriver.Builder()
				.withClassFileSource(source)
				.withOutputSink(sink)
				.withOptions(config.toMap())
				.build();
		driver.analyse(Collections.singletonList(name));
		String decompile = sink.getDecompilation();
		if (decompile == null)
			return new DecompileResult(null, sink.getException(), DecompileResult.ResultType.FAILURE);
		return new DecompileResult(decompile, null, DecompileResult.ResultType.SUCCESS);
	}

	static {
		try {
			// Rewrite CFR comments to not say "use --option" since this is not a command line context.
			Field field = ReflectUtil.getDeclaredField(DecompilerComment.class, "comment");
			ReflectUtil.quietSet(DecompilerComment.RENAME_MEMBERS, field, "Duplicate member names detected");
			ReflectUtil.quietSet(DecompilerComment.ILLEGAL_IDENTIFIERS, field, "Illegal identifiers detected");
			ReflectUtil.quietSet(DecompilerComment.MALFORMED_SWITCH, field, "Recovered potentially malformed switches");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
