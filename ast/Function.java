package ast;

import types.Type; 

public class Function extends AST {

    public FunctionDeclaration declaration;
    public FunctionBody body;

    public void fillFunction(FunctionDeclaration declaration, FunctionBody body) {
        this.declaration = declaration;
        this.body = body;
    }

    public String toString() {
        return this.declaration.toString() + "\n" + this.body.toString();
    }

    public Type type() {
        return this.declaration.type; 
    }

    /*
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (!(obj instanceof Function)) return false; 
        
        return this.declaration.equals(obj.declaration); 
    }
    */
    void accept(PPVisitor v) {
        v.visit(this);
    }
    
    void accept(TypeCheckVisitor v) throws BaseULException {
        v.verify(this); 
    }
}