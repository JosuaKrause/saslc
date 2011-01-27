package xi.runtime.ast.prim;

import java.util.Map;
import java.util.Set;

import xi.runtime.ast.Node;
import xi.runtime.ast.Thunk;

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
