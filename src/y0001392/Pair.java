package y0001392;

/**
 * Created by Crembo on 2017-02-09.
 */
public class Pair<K, V> {
    private final K left;
    private final V right;

    Pair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    K getLeft() {
        return left;
    }
    V getRight() {
        return right;
    }
}