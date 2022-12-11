package software.coley.recaf.info;

import me.coley.cafedude.classfile.VersionConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import software.coley.recaf.info.builder.JvmClassInfoBuilder;
import software.coley.recaf.test.TestClassUtils;
import software.coley.recaf.test.dummy.AccessibleFields;
import software.coley.recaf.util.ByteHeaderUtil;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link JvmClassInfo}
 */
class JvmClassInfoTest {
	static JvmClassInfo accessibleFields;

	@BeforeAll
	static void setup() throws IOException {
		accessibleFields = TestClassUtils.fromRuntimeClass(AccessibleFields.class);
	}

	@Test
	void getBytecode() {
		assertNotNull(accessibleFields.getBytecode(),
				"Missing bytecode value");
		assertTrue(ByteHeaderUtil.match(accessibleFields.getBytecode(), ByteHeaderUtil.CLASS),
				"Bytecode header is not CAFEBABE");
	}

	@Test
	void getVersion() {
		assertTrue(accessibleFields.getVersion() >= VersionConstants.JAVA11,
				"Invalid class file version, should be >= Java 11");
	}

	@Test
	void acceptIfJvmClass() {
		int[] usage = new int[1];
		accessibleFields.acceptIfJvmClass(cls -> usage[0]++);
		assertEquals(1, usage[0], "Accept consumer was not used");
	}

	@Test
	void acceptIfAndroidClass() {
		int[] usage = new int[1];
		accessibleFields.acceptIfAndroidClass(cls -> usage[0]++);
		assertEquals(0, usage[0], "Accept consumer was used when it should not have been");
	}

	@Test
	void testIfJvmClass() {
		assertTrue(accessibleFields.testIfJvmClass(cls -> true),
				"Predicate<JVM> should have been called");
	}

	@Test
	void testIfAndroidClass() {
		assertFalse(accessibleFields.testIfAndroidClass(cls -> true),
				"Predicate<Android> should not have been called");
	}

	@Test
	void asJvmClass() {
		assertDoesNotThrow(() -> {
			assertEquals(accessibleFields, accessibleFields.asJvmClass(), "JVM.asJvm() should yield self");
		});
	}

	@Test
	void asAndroidClass() {
		assertThrows(Throwable.class, () -> accessibleFields.asAndroidClass(), "JVM.asAndroid() should fail");
	}

	@Test
	void isJvmClass() {
		assertTrue(accessibleFields.isJvmClass());
	}

	@Test
	void isAndroidClass() {
		assertFalse(accessibleFields.isAndroidClass());
	}

	@Test
	void toBuilder() {
		// Direct copy
		JvmClassInfo builderDirectCopy = accessibleFields.toBuilder()
				.build();
		assertEquals(accessibleFields, builderDirectCopy,
				"Direct copy via builder should have same class equality");

		// With modification
		JvmClassInfo builderModifiedCopy = accessibleFields.toBuilder()
				.withName("Modified")
				.build();
		assertNotEquals(accessibleFields, builderModifiedCopy,
				"Direct copy via builder should have same class equality");
	}
}