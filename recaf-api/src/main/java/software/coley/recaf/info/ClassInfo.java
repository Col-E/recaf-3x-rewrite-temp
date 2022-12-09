package software.coley.recaf.info;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import software.coley.recaf.info.annotation.Annotated;
import software.coley.recaf.info.builder.AbstractClassInfoBuilder;
import software.coley.recaf.info.member.FieldMember;
import software.coley.recaf.info.member.MethodMember;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Outline of a class.
 *
 * @author Matt Coley
 * @see JvmClassInfo For JVM classes.
 * @see AndroidClassInfo For Android classes.
 */
public interface ClassInfo extends Info, Annotated, Accessed {
	/**
	 * @return Name of the source file the class was compiled from.
	 * May be {@code null} when there is no debug data attached to the class.
	 */
	@Nullable
	String getSourceFileName();

	/**
	 * @return List of implemented interfaces.
	 */
	@Nonnull
	List<String> getInterfaces();

	/**
	 * @return Super-name of the class.
	 * May be {@code null} for {@link java.lang.annotation.Annotation} classes.
	 */
	@Nullable
	String getSuperName();

	/**
	 * @return Package the class resides in.
	 * May be {@code null} for classes in the default package.
	 */
	@Nullable
	default String getPackageName() {
		String className = getName();
		int packageIndex = className.lastIndexOf('/');
		if (packageIndex <= 0) return null;
		return className.substring(0, packageIndex);
	}

	/**
	 * @return Stream of all parent types, where the {@link #getSuperName()} is first if present,
	 * followed by any {@link #getInterfaces()}.
	 */
	default Stream<String> parentTypesStream() {
		return Stream.concat(
				Stream.ofNullable(getSuperName()),
				getInterfaces().stream()
		);
	}

	/**
	 * @return Signature containing generic information. May be {@code null}.
	 */
	@Nullable
	String getSignature();

	/**
	 * @return Name of outer class that this is declared in, if this is an inner class.
	 * {@code null} when this class is not an inner class.
	 */
	@Nullable
	String getOuterClassName();

	/**
	 * @return Name of the outer method that this is declared in, as an anonymous inner class.
	 * {@code null} when this class is not an inner anonymous class.
	 *
	 * @see #getOuterMethodDescriptor() Descriptor of outer method
	 */
	@Nullable
	String getOuterMethodName();

	/**
	 * @return Descriptor of the outer method that this is declared in, as an anonymous inner class.
	 * {@code null} when this class is not an inner anonymous class.
	 *
	 * @see #getOuterMethodName() Name of outer method.
	 */
	@Nullable
	String getOuterMethodDescriptor();

	/**
	 * Breadcrumbs of the outer class.
	 * This List <strong>MUST</strong> be sorted in order of the outermost first.
	 * The last element is the outer of the class itself.
	 * <br>
	 * For an example, if our class is 'C' then this list will be [A, B]:
	 * <pre>
	 * class A {
	 *     class B {
	 *         class C {}  // This class
	 *     }
	 * }
	 * </pre>
	 *
	 * @return Breadcrumbs of the outer class.
	 */
	@Nonnull
	List<String> getOuterClassBreadcrumbs();

	/**
	 * @return List of declared inner classes.
	 */
	@Nonnull
	List<InnerClassInfo> getInnerClasses();

	/**
	 * @return List of declared fields.
	 */
	@Nonnull
	List<FieldMember> getFields();

	/**
	 * @return List of declared methods.
	 */
	@Nonnull
	List<MethodMember> getMethods();

	/**
	 * @return Stream of declared fields.
	 */
	@Nonnull
	default Stream<FieldMember> fieldStream() {
		return Stream.of(this).flatMap(self -> self.getFields().stream());
	}

	/**
	 * @return Stream of declared methods.
	 */
	@Nonnull
	default Stream<MethodMember> methodStream() {
		return Stream.of(this).flatMap(self -> self.getMethods().stream());
	}

	/**
	 * @param action
	 * 		Action to run if this is a JVM class.
	 */
	void acceptIfJvmClass(Consumer<JvmClassInfo> action);

	/**
	 * @param action
	 * 		Action to run if this is an Android class.
	 */
	void acceptIfAndroidClass(Consumer<AndroidClassInfo> action);

	/**
	 * @param action
	 * 		Action to run.
	 */
	default void acceptClass(Consumer<ClassInfo> action) {
		action.accept(this);
	}

	/**
	 * @param predicate
	 * 		Predicate to run if this is a JVM class.
	 *
	 * @return {@code true} when the predicate passes.
	 * {@code false} when it does not, or the class is not a JVM class.
	 */
	boolean testIfJvmClass(Predicate<JvmClassInfo> predicate);

	/**
	 * @param predicate
	 * 		Predicate to run if this is an Android class.
	 *
	 * @return {@code true} when the predicate passes.
	 * {@code false} when it does not, or the class is not an Android class.
	 */
	boolean testIfAndroidClass(Predicate<AndroidClassInfo> predicate);

	/**
	 * @param predicate
	 * 		Predicate to run.
	 *
	 * @return Predicate evaluation.
	 */
	default boolean testClass(Predicate<ClassInfo> predicate) {
		return predicate.test(this);
	}

	/**
	 * @param function
	 * 		Mapping function.
	 * @param <R>
	 * 		Function return type.
	 *
	 * @return Mapped value.
	 */
	default <R> R mapClass(Function<ClassInfo, R> function) {
		return function.apply(this);
	}

	/**
	 * @return New builder wrapping this class information.
	 */
	default AbstractClassInfoBuilder<?> toBuilder() {
		return AbstractClassInfoBuilder.forClass(this);
	}

	@Override
	default ClassInfo asClass() {
		return this;
	}

	@Override
	default FileInfo asFile() {
		throw new IllegalStateException("Class cannot be cast to generic file");
	}

	/**
	 * @return Self cast to JVM class.
	 */
	@Nonnull
	JvmClassInfo asJvmClass();

	/**
	 * @return Self cast to Android class.
	 */
	@Nonnull
	AndroidClassInfo asAndroidClass();

	@Override
	default boolean isClass() {
		return true;
	}

	@Override
	default boolean isFile() {
		return false;
	}

	/**
	 * @return {@code true} if self is a JVM class.
	 */
	boolean isJvmClass();

	/**
	 * @return {@code true} if self is an Android class.
	 */
	boolean isAndroidClass();
}
