lexer grammar ulNoActions;

T17 : '(' ;
T18 : ')' ;
T19 : ',' ;
T20 : '[' ;
T21 : ']' ;
T22 : '{' ;
T23 : '}' ;
T24 : ';' ;
T25 : 'print' ;
T26 : 'println' ;
T27 : '=' ;
T28 : 'return' ;
T29 : '*' ;
T30 : '+' ;
T31 : '-' ;
T32 : '<' ;
T33 : '==' ;

// $ANTLR src "ulNoActions.g" 151
IF	: 'if'
    ;


// $ANTLR src "ulNoActions.g" 155
WHILE : 'while'
      ;


// $ANTLR src "ulNoActions.g" 159
ELSE : 'else'
     ;


// $ANTLR src "ulNoActions.g" 163
TYPE    : 'int'
        | 'string'
        | 'float'
        | 'char'
        | 'void'
        | 'boolean'
        ;


// $ANTLR src "ulNoActions.g" 172
TRUE:  'true'   ;
// $ANTLR src "ulNoActions.g" 173
FALSE: 'false'  ;


// $ANTLR src "ulNoActions.g" 176
INTEGERCONST : ('0'..'9')+
             ;

// $ANTLR src "ulNoActions.g" 179
FLOATCONST : ('0'..'9')+'.'('0'..'9')+
           ;

// $ANTLR src "ulNoActions.g" 182
CHARCONST    : '\''('a'..'z'|'A'..'Z'|'_'|' '|'0'..'9'|'.'|','|'!')'\''
             ;


// $ANTLR src "ulNoActions.g" 186
STRINGCONST  : '"'('a'..'z'|'A'..'Z'|'_'|' '|'0'..'9'|'.'|','|'!')*'"'
             ;


// $ANTLR src "ulNoActions.g" 190
ID	: ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;



// $ANTLR src "ulNoActions.g" 195
WS      : ( '\t' | ' ' | ('\r' | '\n') )+ { $channel = HIDDEN;}
        ;


// $ANTLR src "ulNoActions.g" 199
COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n')? { $channel = HIDDEN;}
        ;