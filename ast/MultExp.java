package ast;

import types.Type;


public class MultExp extends BinaryExpression { 

    public MultExp(BaseExpression exp, BaseExpression exp1) {
        super(exp, exp1);
        this.operator = "*"; 
    }

    Type accept(TypeCheckVisitor v) throws BaseULException {
        return v.verify(this);
    }

    Temp accept(IRGenVisitor v) throws BaseULException {
        return v.gen(this); 
    }
}