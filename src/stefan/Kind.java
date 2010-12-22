package stefan;

/* This enumerates the node kinds used by the stefan.CommonNode interface. */

public enum Kind {

    module, // a subSASL module
    app,    // an application
    prim,   // a primitive, see stefan.Prim
    num,    // a literal number
    bln,    // a literal boolean
    // chr,    // a literal character
    str,    // a literal string
    lst,    // a literal list

    lam,    // a lambda abstraction
    name,   // a name
    let     // a let-in block

}