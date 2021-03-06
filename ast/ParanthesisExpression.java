package ast;

import types.Type; 


public class ParanthesisExpression extends BaseExpression {

    BaseExpression wrapped_expr;

    public ParanthesisExpression(BaseExpression e) {
        this.wrapped_expr = e; 
        this.lineNumber = e.getLineNumber();
    }
    
    public String toCodeString() {
        return String.format("(%s)", this.wrapped_expr.toCodeString()); 
    }

    Type accept(TypeCheckVisitor v) throws BaseULException {
        return v.verify(this);
    }

    Temp accept(IRGenVisitor v) throws BaseULException {
        return v.gen(this); 
    }
}