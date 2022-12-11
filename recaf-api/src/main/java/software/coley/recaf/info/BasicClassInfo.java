package software.coley.recaf.info;

import jakarta.annotation.Nonnull;
import software.coley.recaf.info.annotation.AnnotationInfo;
import software.coley.recaf.info.annotation.TypeAnnotationInfo;
import software.coley.recaf.info.builder.AbstractClassInfoBuilder;
import software.coley.recaf.info.member.BasicFieldMember;
import software.coley.recaf.info.member.BasicMember;
import software.coley.recaf.info.member.FieldMember;
import software.coley.recaf.info.member.MethodMember;
import software.coley.recaf.info.properties.Property;
import software.coley.recaf.info.properties.PropertyContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Basic implementation of class info.
 *
 * @author Matt Coley
 * @see BasicJvmClassInfo
 * @see BasicAndroidClassInfo
 */
public abstract class BasicClassInfo implements ClassInfo {
	private final PropertyContainer properties;
	private final String name;
	private final String superName;
	private final List<String> interfaces;
	private final int access;
	private final String signature;
	private final String sourceFileName;
	private final List<AnnotationInfo> annotations;
	private final List<TypeAnnotationInfo> typeAnnotations;
	private final String outerClassName;
	private final String outerMethodName;
	private final String outerMethodDescriptor;
	private final List<InnerClassInfo> innerClasses;
	private final List<FieldMember> fields;
	private final List<MethodMember> methods;
	private List<String> breadcrumbs;

	protected BasicClassInfo(AbstractClassInfoBuilder<?> builder) {
		this(builder.getName(),
				builder.getSuperName(),
				builder.getInterfaces(),
				builder.getAccess(),
				builder.getSignature(),
				builder.getSourceFileName(),
				builder.getAnnotations(),
				builder.getTypeAnnotations(),
				builder.getOuterClassName(),
				builder.getOuterMethodName(),
				builder.getOuterMethodDescriptor(),
				builder.getInnerClasses(),
				builder.getFields(),
				builder.getMethods(),
				builder.getPropertyContainer());
	}

	protected BasicClassInfo(@Nonnull String name, String superName, @Nonnull List<String> interfaces, int access,
							 String signature, String sourceFileName,
							 @Nonnull List<AnnotationInfo> annotations,
							 @Nonnull List<TypeAnnotationInfo> typeAnnotations,
							 String outerClassName, String outerMethodName,
							 String outerMethodDescriptor,
							 @Nonnull List<InnerClassInfo> innerClasses,
							 @Nonnull List<FieldMember> fields, @Nonnull List<MethodMember> methods,
							 @Nonnull PropertyContainer properties) {
		this.name = name;
		this.superName = superName;
		this.interfaces = interfaces;
		this.access = access;
		this.signature = signature;
		this.sourceFileName = sourceFileName;
		this.annotations = annotations;
		this.typeAnnotations = typeAnnotations;
		this.outerClassName = outerClassName;
		this.outerMethodName = outerMethodName;
		this.outerMethodDescriptor = outerMethodDescriptor;
		this.innerClasses = innerClasses;
		this.fields = fields;
		this.methods = methods;
		this.properties = properties;
		// Link fields/methods to self
		Stream.concat(fields.stream(), methods.stream())
				.filter(member -> member instanceof BasicMember)
				.map(member -> (BasicMember) member)
				.forEach(member -> member.setDeclaringClass(this));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSuperName() {
		return superName;
	}

	@Override
	public List<String> getInterfaces() {
		return interfaces;
	}

	@Override
	public int getAccess() {
		return access;
	}

	@Override
	public String getSignature() {
		return signature;
	}

	@Override
	public String getSourceFileName() {
		return sourceFileName;
	}

	@Override
	public List<AnnotationInfo> getAnnotations() {
		return annotations;
	}

	@Override
	public List<TypeAnnotationInfo> getTypeAnnotations() {
		return typeAnnotations;
	}

	@Override
	public String getOuterClassName() {
		return outerClassName;
	}

	@Override
	public String getOuterMethodName() {
		return outerMethodName;
	}

	@Override
	public String getOuterMethodDescriptor() {
		return outerMethodDescriptor;
	}

	@Override
	public List<String> getOuterClassBreadcrumbs() {
		if (breadcrumbs == null) {
			int maxOuterDepth = 10;
			breadcrumbs = new ArrayList<>();
			String currentOuter = getOuterClassName();
			int counter = 0;
			while (currentOuter != null) {
				if (++counter > maxOuterDepth) {
					breadcrumbs.clear(); // assuming some obfuscator is at work, so breadcrumbs might be invalid.
					break;
				}
				breadcrumbs.add(0, currentOuter);
				String targetOuter = currentOuter;
				currentOuter = innerClasses.stream()
						.filter(i -> i.getInnerClassName().equals(targetOuter))
						.map(InnerClassInfo::getOuterClassName)
						.findFirst().orElse(null);
			}
		}
		return breadcrumbs;
	}

	@Override
	public List<InnerClassInfo> getInnerClasses() {
		return innerClasses;
	}

	@Override
	public List<FieldMember> getFields() {
		return fields;
	}

	@Override
	public List<MethodMember> getMethods() {
		return methods;
	}

	@Override
	public <V> void setProperty(Property<V> property) {
		properties.setProperty(property);
	}

	@Override
	public void removeProperty(String key) {
		properties.removeProperty(key);
	}

	@Override
	public Map<String, Property<?>> getProperties() {
		return properties.getProperties();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BasicClassInfo other = (BasicClassInfo) o;

		if (access != other.access) return false;
		if (!properties.equals(other.properties)) return false;
		if (!name.equals(other.name)) return false;
		if (!Objects.equals(superName, other.superName)) return false;
		if (!interfaces.equals(other.interfaces)) return false;
		if (!Objects.equals(signature, other.signature)) return false;
		if (!Objects.equals(sourceFileName, other.sourceFileName)) return false;
		if (!annotations.equals(other.annotations)) return false;
		if (!typeAnnotations.equals(other.typeAnnotations)) return false;
		if (!Objects.equals(outerClassName, other.outerClassName)) return false;
		if (!Objects.equals(outerMethodName, other.outerMethodName)) return false;
		if (!Objects.equals(outerMethodDescriptor, other.outerMethodDescriptor)) return false;
		if (!innerClasses.equals(other.innerClasses)) return false;
		if (!fields.equals(other.fields)) return false;
		return methods.equals(other.methods);
	}

	@Override
	public int hashCode() {
		int result = properties.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (superName != null ? superName.hashCode() : 0);
		result = 31 * result + interfaces.hashCode();
		result = 31 * result + access;
		result = 31 * result + (signature != null ? signature.hashCode() : 0);
		result = 31 * result + (sourceFileName != null ? sourceFileName.hashCode() : 0);
		result = 31 * result + annotations.hashCode();
		result = 31 * result + typeAnnotations.hashCode();
		result = 31 * result + (outerClassName != null ? outerClassName.hashCode() : 0);
		result = 31 * result + (outerMethodName != null ? outerMethodName.hashCode() : 0);
		result = 31 * result + (outerMethodDescriptor != null ? outerMethodDescriptor.hashCode() : 0);
		result = 31 * result + innerClasses.hashCode();
		result = 31 * result + fields.hashCode();
		result = 31 * result + methods.hashCode();
		return result;
	}
}
