package ast;

import types.Type; 

public abstract class BaseStatement extends AST {

    void accept(PPVisitor v) {
        v.visit(this);
    }

    Type accept(TypeCheckVisitor v) throws BaseULException {
        return v.verify(this);
    }

    Temp accept(IRGenVisitor v) throws BaseULException {
        return v.gen(this); 
    }
    
    public String toCodeString() {
        return "";
    }

    public String toCodeString(int indent) {
        return ""; 
    }
}
