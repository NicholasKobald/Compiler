package ast;

import java.util.Collections; 

import types.Type;
import types.ArrayType;

public class VariableDeclaration extends AST {

    public Type type;
    public ULIdentifier id;

    public boolean hasValue; 

    public VariableDeclaration(Type type, ULIdentifier id) {
        this.type = type;
        this.id = id;
    }

    public String toCodeString(int indent_num) {
        //ie: int my_variable;
        return String.format("%s %s;\n", type.toCodeString(), id.toString());
    }

    void accept(PPVisitor v) {
        v.visit(this);
    }
    
    Temp accept(IRGenVisitor v) throws BaseULException {
        return v.gen(this); 
    }
}
