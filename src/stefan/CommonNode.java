package stefan;

import java.util.Map;

public interface CommonNode {

	/*
	 * Each node must return a unique integer ID. New nodes must not use a
	 * previously assigned ID. Note, that 'java.lang.Object.hashCode()' is not
	 * sufficient.
	 */
	int getId();

	/*
	 * Return the kind as enumerated in 'stefan.Kind'. The methods below should
	 * throw a 'java.lang.UnsupportedOperationException' if called on a node of
	 * inadequate kind.
	 */
	Kind getKind();

	/*
	 * Valid for Kind: prim. Return the primitive represented by this node, as
	 * enumerated in 'stefan.Prim'.
	 */
	Prim getPrim();

	/*
	 * Valid for Kind: num. Return the value of the literal represented by this
	 * node.
	 */
	int getNum();

	/*
	 * Valid for Kind: bln. Return the value of the literal represented by this
	 * node.
	 */
	boolean getBln();

	/*
	 * Valid for Kind: str. Return the value of the literal represented by this
	 * node.
	 */
	String getStr();

	/*
	 * Valid for Kind: name & lam. For a variable node, return the name of the
	 * variable. For a λ-abstraction, return the name of the bound variable,
	 * i.e., "x" for 'λx.e'.
	 */
	String getName();

	/*
	 * Valid for Kind: app. Return left child of an application, i.e., 'e1' in
	 * the case of 'e1 e2'.
	 */
	CommonNode getLeft();

	/*
	 * Valid for Kind: app. Return right child of an application, i.e., 'e2' in
	 * the case of 'e1 e2'.
	 */
	CommonNode getRight();

	/*
	 * Valid for Kind: lam & let. Return the body of the expression, i.e., 'e'
	 * for 'λx.e', and 'e3' for 'let x = e1, y = e2 in e3'.
	 */
	CommonNode getBody();

	/*
	 * Valid for Kind: let & module. Return the names and bodies of all defined
	 * functions defined by the module or let-in block.
	 */
	Map<String, ? extends CommonNode> getDefs();

	/* Valid for Kind: lst. Return whether the list is empty. */
	boolean isEmpty();

	/* Valid for Kind: lst. Return the head of a non-empty list. */
	CommonNode getHead();

	/* Valid for Kind: lst. Return the tail of a non-empty list. */
	CommonNode getTail();

}
