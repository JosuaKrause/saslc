package xi.go.cst;

import java.util.Map;
import java.util.Set;

import xi.go.cst.prim.Function;

/**
 * A node of the expression tree.
 * 
 * @author Leo
 * @author Joschi
 */
public abstract class Node {

    /**
     * Checks whether this is an application node.
     * 
     * @return {@code true}, if this is an application, {@code false} otherwise
     */
    public boolean isApp() {
        return false;
    }

    /**
     * Checks whether this is a value.
     * 
     * @return {@code true}, if this is a value, {@code false} otherwise
     */
    public boolean isValue() {
        return false;
    }

    /**
     * Checks whether this is a character.
     * 
     * @return {@code true}, if this is a character, {@code false} otherwise
     */
    public boolean isChar() {
        return false;
    }

    /**
     * Checks whether this is a reference.
     * 
     * @return {@code true}, if this is a reference, {@code false} otherwise
     */
    public boolean isRef() {
        return false;
    }

    /**
     * Getter for the left child of an application node.
     * 
     * @return left child
     */
    public Thunk getLeft() {
        throw new UnsupportedOperationException("No Application node.");
    }

    /**
     * Getter for the right child of an application node.
     * 
     * @return right child
     */
    public Thunk getRight() {
        throw new UnsupportedOperationException("No Application node.");
    }

    /**
     * Getter for the function definition of a function node.
     * 
     * @return function definition
     */
    public Function.Def getFunction() {
        throw new UnsupportedOperationException("No Function.");
    }

    @Override
    public abstract String toString();

    /**
     * Links the application tree, i.e. replaces all references with the
     * corresponding expressions.
     * 
     * @param defs
     *            definitions
     * @param linked
     *            set of already linked definitions to avoid loops
     * @return a new thunk that this node's thunk should be replaced with, or
     *         {@code null}
     */
    public abstract Thunk link(Map<String, Thunk> defs, Set<String> linked);

    /**
     * @return Whether the node should be shared for visualization.
     */
    public abstract boolean shareNode();
}
