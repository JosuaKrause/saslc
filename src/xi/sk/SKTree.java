package xi.sk;

/**
 * Interface for traversable SK syntax trees.
 * 
 * @author Leo Woerteler
 */
public interface SKTree {

    /**
     * This method performs a post-order traversal of the SK syntax tree,
     * notifying the visitor about all encountered nodes.
     * 
     * @param v
     *            visitor
     */
    public void traverse(final SKVisitor v);
}
