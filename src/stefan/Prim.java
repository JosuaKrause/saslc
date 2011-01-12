package stefan;

/* This enumerates the primitives used by the CommonNode interface. */

public enum Prim {

    // combinators introduced by the compiler, not used currently...
    S(3), K(2), Y(1), B(3), C(3), I(1), U(2),

    // arithmetic primitives: ~ + - * / %
    Neg(1), Add(2), Sub(2), Mul(2), Div(2), Mod(2),

    // comparison primitives: < > <= >= == !=
    Lt(2), Gt(2), Le(2), Ge(2), Eq(2), Ne(2),

    // boolean primitives: ! & | if-then-else
    Not(1), And(2), Or(2), Cond(3),

    // list primitives: : hd tl
    Char(1), Cons(2), Hd(1), Tl(1),

    // strictness operator: !>
    Seq(2);

    // The arity of a primitive, this will be useful later on...
    public final int arity;

    Prim(final int a) {
        arity = a;
    }

}
