package ast;

import types.Type; 

public class ExpressionStatement extends BaseStatement {

    BaseExpression exp;

    public ExpressionStatement(BaseExpression exp) {
        this.exp = exp; 
    }

    public String toCodeString() {
        if (exp == null) 
            return "";

        return String.format("%s", exp.toCodeString());
    }

    void accept(PPVisitor v) {
        v.visit(this);
    }

    Type accept(TypeCheckVisitor v) throws BaseULException {
        return v.verify(this);
    }

    Temp accept(IRGenVisitor v) throws BaseULException {
        return v.gen(this); 
    }
}