package software.coley.recaf.services.compile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Compiler results wrapper.
 *
 * @author Matt Coley
 */
public class CompilerResult {
	private final CompileMap compilations;
	private final List<CompilerDiagnostic> diagnostics;
	private final Throwable exception;

	/**
	 * @param exception
	 * 		Error thrown when attempting to compile.
	 */
	public CompilerResult(@Nonnull Throwable exception) {
		this(null, Collections.emptyList(), exception);
	}

	/**
	 * @param compileMap
	 * 		Compilation results.
	 * @param diagnostics
	 * 		Compilation problem diagnostics.
	 */
	public CompilerResult(@Nonnull CompileMap compileMap, @Nonnull List<CompilerDiagnostic> diagnostics) {
		this(compileMap, diagnostics, null);
	}

	private CompilerResult(@Nullable CompileMap compilations,
						   @Nonnull List<CompilerDiagnostic> diagnostics,
						   @Nullable Throwable exception) {
		this.compilations = compilations;
		this.exception = exception;
		this.diagnostics = diagnostics;
	}

	/**
	 * @return Compilation results.
	 * May be {@code null} when there are is an {@link #getException()}.
	 */
	@Nullable
	public CompileMap getCompilations() {
		return compilations;
	}

	/**
	 * @return Compilation problem diagnostics.
	 * May be {@code null} when there are is an {@link #getException()}.
	 */
	@Nullable
	public List<CompilerDiagnostic> getDiagnostics() {
		return diagnostics;
	}

	/**
	 * @return Error thrown when attempting to compile.
	 * May be {@code null} when compilation was a success.
	 */
	@Nullable
	public Throwable getException() {
		return exception;
	}
}
