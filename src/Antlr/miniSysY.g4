grammar miniSysY;
program
   : compUnit
   ;

compUnit
   : (funcDef | decl)+
   ;

decl
   : constDecl
   | varDecl
   ;

constDecl
   : CONST bType constDef (COMMA constDef)* SEMICOLON
   ;

bType
   : INT
   ;

constDef
   : IDENT (LBRACKT constExp RBRACKT)* ASSIGN constInitVal
   ;

constInitVal
   : constExp
   | (LBRACE (constInitVal (COMMA constInitVal)*)? RBRACE)
   ;

varDecl
   : bType varDef (COMMA varDef)* SEMICOLON
   ;

varDef
   : IDENT (LBRACKT constExp RBRACKT)* (ASSIGN initVal)?
   ;

initVal
   : exp
   | (LBRACE (initVal (COMMA initVal)*)? RBRACE)
   ;

funcDef
   : funcType IDENT LPAR funcFParams? RPAR block
   ;

funcType
   : VOID
   | INT
   ;

funcFParams
   : funcFParam (COMMA funcFParam)*
   ;

funcFParam
   : bType IDENT (LBRACKT RBRACKT (LBRACKT exp RBRACKT)*)?
   ;

block
   : LBRACE blockItem* RBRACE
   ;

blockItem
   : constDecl
   | varDecl
   | stmt
   ;

stmt
   : assign_stmt
   | exp_stmt
   | block
   | if_stmt
   | while_stmt
   | break_stmt
   | continue_stmt
   | return_stmt
   ;

assign_stmt
   : lVal ASSIGN exp SEMICOLON
   ;

exp_stmt
   : exp? SEMICOLON
   ;

if_stmt
   : IF LPAR cond RPAR stmt (ELSE stmt)?
   ;

while_stmt
   : WHILE LPAR cond RPAR stmt
   ;

break_stmt
   : BREAK SEMICOLON
   ;

continue_stmt
   : CONTINUE SEMICOLON
   ;

return_stmt
   : RETURN (exp)? SEMICOLON
   ;

exp
   : addExp
   ;

cond
   : lOrExp
   ;

lVal
   : IDENT (LBRACKT exp RBRACKT)*
   ;

primaryExp
   : (LPAR exp RPAR)
   | lVal
   | number
   ;

number
   : DECIMAL_CONST
   | OCTAL_CONST
   | HEXADECIMAL_CONST
   ;

unaryExp
   : primaryExp
   | callee
   | (unaryOp unaryExp)
   ;

callee
   : IDENT LPAR funcRParams? RPAR
   ;

unaryOp
   : PLUS
   | MINUS
   | NOT
   ;

funcRParams
   : param (COMMA param)*
   ;

param
   : exp
   | STRING
   ;

mulExp
   : unaryExp (mulOp unaryExp)*
   ;

mulOp
   : MUL
   | DIV
   | MOD
   ;

addExp
   : mulExp (addOp mulExp)*
   ;

addOp
   : PLUS
   | MINUS
   ;

relExp
   : addExp (relOp addExp)*
   ;

relOp
   : LT
   | GT
   | LE
   | GE
   ;

eqExp
   : relExp (eqOp relExp)*
   ;

eqOp
   : EQ
   | NEQ
   ;

lAndExp
   : eqExp (AND eqExp)*
   ;

lOrExp
   : lAndExp (OR lAndExp)*
   ;

constExp
   : addExp
   ;


CONST
   : 'const'
   ;

INT
   : 'int'
   ;

VOID
   : 'void'
   ;

IF
   : 'if'
   ;

ELSE
   : 'else'
   ;

WHILE
   : 'while'
   ;

BREAK
   : 'break'
   ;

CONTINUE
   : 'continue'
   ;

RETURN
   : 'return'
   ;

IDENT
   : [_a-zA-Z]
   | [_a-zA-Z] [_a-zA-Z0-9]+
   ;

DECIMAL_CONST
   : [1-9]
   | [1-9] [0-9]+
   ;

OCTAL_CONST
   : '0'
   | ('0' [0-7]+)
   ;

HEXADECIMAL_CONST
   : ('0x' | '0X') [a-fA-F0-9]+
   ;

STRING
   : DOUBLE_QUOTE REGULAR_CHAR*? DOUBLE_QUOTE
   ;

fragment REGULAR_CHAR
   : (ESC | .)
   ;

fragment ESC
   : '\\"' | '\\\\'
   ;

PLUS
   : '+'
   ;

MINUS
   : '-'
   ;

NOT
   : '!'
   ;

MUL
   : '*'
   ;

DIV
   : '/'
   ;

MOD
   : '%'
   ;

ASSIGN
   : '='
   ;

EQ
   : '=='
   ;

NEQ
   : '!='
   ;

LT
   : '<'
   ;

GT
   : '>'
   ;

LE
   : '<='
   ;

GE
   : '>='
   ;

AND
   : '&&'
   ;

OR
   : '||'
   ;

LPAR
   : '('
   ;

RPAR
   : ')'
   ;

LBRACE
   : '{'
   ;

RBRACE
   : '}'
   ;

LBRACKT
   : '['
   ;

RBRACKT
   : ']'
   ;

COMMA
   : ','
   ;

SEMICOLON
   : ';'
   ;

DOUBLE_QUOTE
   : '"'
   ;

WS
   : [ \r\n\t]+ -> skip
   ;

LINE_COMMENT
   : '//' ~ [\r\n]* -> skip
   ;

MULTILINE_COMMENT
   : '/*' .*? '*/' -> skip
   ;

