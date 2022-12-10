package software.coley.recaf.util;

import jakarta.annotation.Nonnull;
import org.objectweb.asm.Type;

/**
 * General number parsing and other operations.
 *
 * @author Matt Coley
 */
public class NumberUtil {
	/**
	 * @param input
	 * 		Text input that represents a number.
	 * 		<ul>
	 * 		<li>Numbers ending with {@code F} are parsed as {@link Float}.</li>
	 * 		<li>Numbers ending with {@code D} are parsed as {@link Double}.</li>
	 * 		<li>Numbers ending with {@code L} are parsed as {@link Long}.</li>
	 * 		</ul>
	 *
	 * @return Number parsed from text.
	 * Can be an {@link Integer}, {@link Long}, {@link Float}, or {@link Double}.
	 *
	 * @throws NumberFormatException
	 * 		When the input cannot be parsed.
	 */
	@SuppressWarnings("")
	@Nonnull
	public static Number parse(@Nonnull String input) {
		String text = input.trim().toUpperCase();
		Number value;
		if (text.indexOf('.') > 0) {
			value = parseDecimal(text);
		} else {
			if (text.endsWith("L") && text.startsWith("0X"))
				value = Long.parseLong(text.substring(2, text.indexOf("L")), 16);
			else if (text.endsWith("L"))
				value = Long.parseLong(text.substring(0, text.indexOf("L")));
			else if (text.startsWith("0X"))
				value = Integer.parseInt(text.substring(2), 16);
			else if (text.endsWith("F"))
				value = Float.parseFloat(text.substring(0, text.indexOf("F")));
			else if (text.endsWith("D") || text.contains("."))
				value = Double.parseDouble(text.substring(0, text.indexOf("D")));
			else
				value = Integer.parseInt(text);
		}
		return value;
	}

	/**
	 * @param text
	 * 		Text input that represents a decimal number.
	 *
	 * @return Decimal number, either {@link Float} or {@link Double}.
	 */
	@Nonnull
	private static Number parseDecimal(@Nonnull String text) {
		if (text.endsWith("F"))
			return Float.parseFloat(text.substring(0, text.indexOf("F")));
		else if (text.endsWith("D") || text.contains("."))
			return Double.parseDouble(text.substring(0, text.indexOf("D")));
		else
			return Double.parseDouble(text);
	}

	/**
	 * @param type1
	 * 		Numeric type. Must be a primitive.
	 * @param type2
	 * 		Numeric type. Must be a primitive.
	 *
	 * @return Widest primitive.
	 */
	@Nonnull
	public static Type getWidestType(@Nonnull Type type1, @Nonnull Type type2) {
		if (type1.getSort() > type2.getSort())
			return type1;
		return type2;
	}

	/**
	 * Compare two numeric values, regardless of their type.
	 *
	 * @param right
	 * 		First value.
	 * @param left
	 * 		Second value.
	 *
	 * @return Comparison of {@code X.compare(left, right)}
	 */
	public static int cmp(@Nonnull Number left, @Nonnull Number right) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (right instanceof Double || left instanceof Double) {
			return Double.compare(left.doubleValue(), right.doubleValue());
		} else if (right instanceof Float || left instanceof Float) {
			return Float.compare(left.floatValue(), right.floatValue());
		} else if (right instanceof Long || left instanceof Long) {
			return Long.compare(left.longValue(), right.longValue());
		} else {
			return Integer.compare(left.intValue(), right.intValue());
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Difference value.
	 */
	@Nonnull
	public static Number sub(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Double || first instanceof Double) {
			return first.doubleValue() - second.doubleValue();
		} else if (second instanceof Float || first instanceof Float) {
			return first.floatValue() - second.floatValue();
		} else if (second instanceof Long || first instanceof Long) {
			return first.longValue() - second.longValue();
		} else {
			return first.intValue() - second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Sum value.
	 */
	@Nonnull
	public static Number add(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Double || first instanceof Double) {
			return first.doubleValue() + second.doubleValue();
		} else if (second instanceof Float || first instanceof Float) {
			return first.floatValue() + second.floatValue();
		} else if (second instanceof Long || first instanceof Long) {
			return first.longValue() + second.longValue();
		} else {
			return first.intValue() + second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Product value.
	 */
	@Nonnull
	public static Number mul(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Double || first instanceof Double) {
			return first.doubleValue() * second.doubleValue();
		} else if (second instanceof Float || first instanceof Float) {
			return first.floatValue() * second.floatValue();
		} else if (second instanceof Long || first instanceof Long) {
			return first.longValue() * second.longValue();
		} else {
			return first.intValue() * second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Divided value.
	 */
	@Nonnull
	public static Number div(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Double || first instanceof Double) {
			return first.doubleValue() / second.doubleValue();
		} else if (second instanceof Float || first instanceof Float) {
			return first.floatValue() / second.floatValue();
		} else if (second instanceof Long || first instanceof Long) {
			return first.longValue() / second.longValue();
		} else {
			return first.intValue() / second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Remainder value.
	 */
	@Nonnull
	public static Number rem(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Double || first instanceof Double) {
			return first.doubleValue() % second.doubleValue();
		} else if (second instanceof Float || first instanceof Float) {
			return first.floatValue() % second.floatValue();
		} else if (second instanceof Long || first instanceof Long) {
			return first.longValue() % second.longValue();
		} else {
			return first.intValue() % second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Value where matching bits remain.
	 */
	@Nonnull
	public static Number and(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Long || first instanceof Long) {
			return first.longValue() & second.longValue();
		} else {
			return first.intValue() & second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Value where all active bits remain.
	 */
	@Nonnull
	public static Number or(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Long || first instanceof Long) {
			return first.longValue() | second.longValue();
		} else {
			return first.intValue() | second.intValue();
		}
	}

	/**
	 * @param first
	 * 		First value.
	 * @param second
	 * 		Second value.
	 *
	 * @return Value where non-matching bits remain.
	 */
	@Nonnull
	public static Number xor(@Nonnull Number first, @Nonnull Number second) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (second instanceof Long || first instanceof Long) {
			return first.longValue() ^ second.longValue();
		} else {
			return first.intValue() ^ second.intValue();
		}
	}

	/**
	 * @param value
	 * 		Numeric value.
	 *
	 * @return Negated value.
	 */
	@Nonnull
	public static Number neg(@Nonnull Number value) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (value instanceof Double) {
			return -value.doubleValue();
		} else if (value instanceof Float) {
			return -value.floatValue();
		} else if (value instanceof Long) {
			return -value.longValue();
		} else {
			return -value.intValue();
		}
	}

	/**
	 * @param value
	 * 		Numeric value.
	 * @param shift
	 * 		Value to shift by.
	 *
	 * @return Shifted value.
	 */
	@Nonnull
	public static Number shiftLeft(@Nonnull Number value, @Nonnull Number shift) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (value instanceof Long) {
			return value.longValue() << shift.longValue();
		} else {
			return value.intValue() << shift.intValue();
		}
	}

	/**
	 * @param value
	 * 		Numeric value.
	 * @param shift
	 * 		Value to shift by.
	 *
	 * @return Shifted value.
	 */
	@Nonnull
	public static Number shiftRight(@Nonnull Number value, @Nonnull Number shift) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (value instanceof Long) {
			return value.longValue() >> shift.longValue();
		} else {
			return value.intValue() >> shift.intValue();
		}
	}

	/**
	 * @param value
	 * 		Numeric value.
	 * @param shift
	 * 		Value to shift by.
	 *
	 * @return Shifted value.
	 */
	@Nonnull
	public static Number shiftRightU(@Nonnull Number value, @Nonnull Number shift) {
		// Check for widest types first, go down the type list to narrower types until reaching int.
		if (value instanceof Long) {
			return value.longValue() >>> shift.longValue();
		} else {
			return value.intValue() >>> shift.intValue();
		}
	}
}