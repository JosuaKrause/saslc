package xi.go.cst.prim;

import java.util.Map;
import java.util.Set;

import xi.go.cst.Node;
import xi.go.cst.Thunk;

/**
 * Primitive node.
 * 
 * @author Leo
 */
public abstract class Prim extends Node {

    @Override
    public Thunk link(final Map<String, Thunk> defs, final Set<String> linked) {
        return null;
    }

}
