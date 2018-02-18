package ast;

import types.Type; 

public abstract class BaseStatement extends AST {

    void accept(PPVisitor v) {
        v.visit(this);
    }

    Type accept(TypeCheckVisitor v) throws BaseULException {
        System.out.println("Verifying a statement:");
        return v.verify(this);
    }

    public String toCodeString() {
        return "";
    }

    public String toCodeString(int indent) {
        return ""; 
    }

}