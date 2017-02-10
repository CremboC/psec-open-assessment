package y0001392;

/**
 * @author paulius
 */
public class Triple<K, V, T> {
	private final K left;
	private final V middle;
	private final T right;

	public Triple(K left, V middle, T right) {

		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	public K getLeft() {
		return left;
	}

	public V getMiddle() {
		return middle;
	}

	public T getRight() {
		return right;
	}
}
