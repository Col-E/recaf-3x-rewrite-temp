package software.coley.recaf.workspace.model.bundle;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import software.coley.recaf.behavior.Closing;
import software.coley.recaf.info.Info;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Base bundle type.
 *
 * @param <I>
 * 		Bundle value type.
 *
 * @author Matt Coley
 */
public interface Bundle<I extends Info> extends Map<String, I>, Iterable<I>, Closing {
	/**
	 * History stack for the given item key.
	 *
	 * @param key
	 * 		Item key.
	 *
	 * @return History of item.
	 */
	@Nullable
	Stack<I> getHistory(String key);

	/**
	 * @return Keys of items that have been modified <i>(Containing any history values)</i>.
	 */
	@Nonnull
	Set<String> getDirtyKeys();

	/**
	 * @param key Item key.
	 * @return {@code true} if the item has a history. Any such item will also be present in {@link #getDirtyKeys()}.
	 */
	boolean hasHistory(String key);

	/**
	 * If the given item isn't part of the bundle, it is added and no historical record is kept.
	 * Otherwise, the existing item's history is incremented.
	 *
	 * @param info Item to update.
	 */
	void incrementHistory(I info);

	/**
	 * Decrement the history of the value associated with the key.
	 * The value removed in this operation then replaces the value of the item map.
	 *
	 * @param key
	 * 		Item key.
	 */
	void decrementHistory(String key);

	/**
	 * @param listener
	 * 		Listener to add.
	 */
	void addBundleListener(BundleListener<I> listener);

	/**
	 * @param listener
	 * 		Listener to remove.
	 */
	void removeBundleListener(BundleListener<I> listener);
}
