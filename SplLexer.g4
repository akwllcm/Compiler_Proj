lexer grammar SplLexer;

// Keywords
INT    : 'int';
STRUCT : 'struct';
RETURN : 'return';
IF     : 'if';
ELSE   : 'else';
WHILE  : 'while';

// Operators，最长匹配
ASSIGN : '=';
PLUS   : '+';
MINUS  : '-';
MUL    : '*';
DIV    : '/';
MOD    : '%';
POW    : '^';

LE     : '<=';
GE     : '>=';
EQ     : '==';
NEQ    : '!=';
AND    : '&&';
OR     : '||';
LT     : '<';
GT     : '>';
NOT    : '!';
DOT    : '.';

SEMI   : ';';
COMMA  : ',';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';

// Identifier
Identifier : [a-zA-Z_][a-zA-Z0-9_]*;


Number  : '0' 
        | [1-9][0-9]*;


WS : [ \t\r\n]+ -> skip;

// Comments 
LINE_COMMENT : '//' .*? ('\n' | EOF) -> skip;
BLOCK_COMMENT : '/*' .*? '*/'   -> skip;