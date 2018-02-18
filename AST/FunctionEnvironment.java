package ast; 

import java.util.*;

import ast.RedefinitionException;
import ast.ULIdentifier;
import ast.UndeclaredIdentifierException;
import ast.VariableDeclaration;
import types.Type; 
import types.VoidType; 

public class FunctionEnvironment {

    GlobalEnvironment globals;
    Type returnType;
    /* List of local params */ 
    LinkedList<Param> usedParams;
    /* List of local variables */ 
    LinkedList<VariableDeclaration> usedVariables;  

    /*
     * A function environment gets initialized with a function declaration,
     * injecting all of the formalParams* into the environment.
     * Additionally, the function environment is passed a reference to the global environment
     * for checking the existence of external functions called. 
     */
    public FunctionEnvironment(FunctionDeclaration fd, 
                               VariableDeclarationList varDecList, 
                               GlobalEnvironment globals) 
                               throws RedefinitionException {
        this.returnType = fd.type; 
        this.globals = globals;
        this.usedParams = new LinkedList<Param>();
        this.usedVariables = new LinkedList<VariableDeclaration>();
        for (Param p: fd.params) {
            this.add(p); 
        }
        for (VariableDeclaration varDec: varDecList) {
            this.addVarDeclaration(varDec); 
        }
    }

    public void add(Param p) throws RedefinitionException {
        if (this.isUsed(p))
            throw new RedefinitionException("Duplicate Param Ids", p.id.getLineNumber());
        if (p.type instanceof VoidType)
            throw new RedefinitionException("This SHOULD BE A VOIDTYPE EXCEPTION BUT IM Z", p.id.getLineNumber());
            usedParams.add(p); 
    }

    public void addVarDeclaration(VariableDeclaration vardec)
                                  throws RedefinitionException {
        if (this.isUsed(vardec))
            throw new RedefinitionException("Duplicate Variable Declaration", vardec.id.getLineNumber());
        if (vardec.type instanceof VoidType)
            throw new RedefinitionException("This SHOULD BE A VOIDTYPE EXCEPTION BUT IM Z", vardec.id.getLineNumber());

        this.usedVariables.add(vardec); 
    }
    public boolean isUsed(AST p) {
        /*
        * TODO: learn generics (kidding, I'll just keep telling everyone how 
        * bad of a programming language Java is) 
        */
        ULIdentifier target = null; // java is honestly a garbage language 
        if (p instanceof Param) {
            Param param = (Param)p; 
            target = param.id; 
        } else if (p instanceof VariableDeclaration) {
            VariableDeclaration varDec = (VariableDeclaration)p;
            target = varDec.id; 
        }
    
        // System.out.println("Checking: " + target.toString());
        for (Param seen: this.usedParams) {
            if (target.equals(seen.id)) {
                return true; 
            }
        }
        for (VariableDeclaration seen: this.usedVariables) {
            if (target.equals(seen.id)) {
                return true; 
            }
        }  
        return false;
    }

    public Type getVariableType(ULIdentifier target) throws UndeclaredIdentifierException {
        for (Param seen: this.usedParams) {
            if (target.equals(seen.id)) {
                return seen.type; 
            }
        }
        for (VariableDeclaration seen: this.usedVariables) {
            if (target.equals(seen.id)) {
                return seen.type; 
            }
        }
        String err = String.format("Use of undeclared identifier %s", target.toString());  
        throw new UndeclaredIdentifierException(err, target.getLineNumber()); 
    }
}