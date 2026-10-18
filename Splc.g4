grammar Splc;

@parser::members {
    private framework.project2.ErrorReporter errorReporter;

    public void setErrorReporter(framework.project2.ErrorReporter reporter) {
        this.errorReporter = reporter;
    }

    /**
     * Report a grammar-specific missing-token error.
     *
     * line is already the 0-indexed line required by Project 2.
     */
    protected void reportMissing(int tokenType, int line) {
        if (errorReporter != null) {
            errorReporter.reportMissing(tokenType, line);
        }
    }

    /**
     * Return the 0-indexed line of the parser's current lookahead token.
     */
    protected int currentLine() {
        return Math.max(0, getCurrentToken().getLine() - 1);
    }
}


/* ==================== Program ==================== */

program
    : globalDef* EOF
    ;

globalDef
    : structDef
    | functionDef
    | globalVarDef
    ;


/* ==================== Type ====================
int a;
struct Point p
 */

type
    : INT
    | STRUCT Identifier
    ;


/* ==================== Struct ====================
struct Point {
int x;
int y;
};

 */

structDef
    : STRUCT Identifier
      LBRACE fieldDef+
      RBRACE SEMI
    ;

fieldDef
    : variableDecl SEMI
    ;


/* ==================== Variable ==================== */

variableDecl
    : type Identifier arrayDimension*
    ;

/*数组维度使用
int b[10];
int matrix[2][3];
*/
arrayDimension   
    : LBRACK Number RBRACK
    ;

globalVarDef
    : variableDecl SEMI
    ;

localVarDef
    : variableDecl (ASSIGN expression)? SEMI
    ;


/* ==================== Function ==================== */

functionDef
    : type Identifier
      LPAREN parameterList? RPAREN
      block
    ;

/*
 * TODO (Error Production):
 *
 * Project 2 contains one grammar-specific recovery case in parameterList
 * that cannot be diagnosed as the required Missing COMMA reliably by the
 * generic recovery strategy alone.  Extend this rule according to the spec.
 */
parameterList
    : parameter ( COMMA parameter | {reportMissing(SplcParser.COMMA, currentLine());} parameter)*
    ;

parameter
    : variableDecl
    ;
/*
int add(int a, int b) {
return a + b;
}
int get(int a[10]) {
return a[0];
}
*/


/* ==================== Statement ==================== */

block
    : LBRACE statement* RBRACE
    ;

statement
    : block
    | localVarDef
    | ifStatement
    | whileStatement
    | returnStatement
    | expressionStatement
    ;

ifStatement
    : IF LPAREN expression RPAREN
      block
      (ELSE block)?
    ;

whileStatement
    : WHILE LPAREN expression RPAREN
      block
    ;

returnStatement
    : RETURN expression SEMI
    ;

expressionStatement
    : expression SEMI
    ;


/* ==================== Expression ==================== */
/*
 * TODO:
 * Complete the expression rule according to the Project 2 specification.
 *
 * The temporary Number-only rule keeps the starter grammar compilable.


优先级	运算符	结合性
1	函数调用、数组访问、成员访问	左
2	POW	右
3	一元 PLUS、MINUS、NOT	右
4	STAR、DIV、MOD	左
5	PLUS、MINUS	左
6	LT、LE、GT、GE	左
7	EQ、NEQ	左
8	AND	左
9	OR	左
10	ASSIGN	右
 */

expression
    : Identifier LPAREN argumentList? RPAREN         
    | expression LBRACK expression RBRACK             
    | expression DOT Identifier                      
    | <assoc=right> expression POW expression         
    | PLUS expression                                
    | MINUS expression                                
    | NOT expression                                  
    | expression STAR expression                      
    | expression DIV expression                       
    | expression MOD expression                       
    | expression PLUS expression                      
    | expression MINUS expression                     
    | expression LT expression                        
    | expression LE expression                        
    | expression GT expression                        
    | expression GE expression                        
    | expression EQ expression                        
    | expression NEQ expression                       
    | expression AND expression                       
    | expression OR expression                        
    | <assoc=right> expression ASSIGN expression      
    | LPAREN expression RPAREN                        
    | Number                                          
    | Identifier                                      
    ;

argumentList
    : expression (COMMA expression)*
    ;


/* ==================== Lexer ==================== */

INT : 'int';
STRUCT : 'struct';
IF : 'if';
ELSE : 'else';
WHILE : 'while';
RETURN : 'return';

LE : '<=';
GE : '>=';
EQ : '==';
NEQ : '!=';
AND : '&&';
OR : '||';
ASSIGN : '=';
PLUS : '+';
MINUS : '-';
STAR : '*';
DIV : '/';
MOD : '%';
POW : '^';
LT : '<';
GT : '>';
NOT : '!';

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
COMMA : ',';
DOT : '.';

Number
    : '0'
    | [1-9] [0-9]*
    ;

Identifier
    : [a-zA-Z_] [a-zA-Z0-9_]*
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
