package software.coley.recaf.info.annotation;

import java.util.List;

/**
 * Outline of a potential value for {@link AnnotationElement#getElementValue()}.
 *
 * @author Matt Coley
 */
public interface AnnotationArrayReference {
	/**
	 * @return List of values.
	 */
	List<Object> getValues();
}
