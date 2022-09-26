lexer grammar GifDSLLexer;

options { caseInsensitive=true; }

INDENT        : [ \t]+ ;
COMMENT       : '//' ~[\r\n]* -> mode(OPTIONS_MODE);
DEFINE        : 'DEFINE' -> mode(OPTIONS_MODE);
IF            : 'IF' -> mode(OPTIONS_MODE);
LOOP          : 'LOOP' -> mode(OPTIONS_MODE);
WITH          : 'WITH' -> mode(OPTIONS_MODE);
FUNCTION_NAME : [a-z]+[a-z0-9]* -> mode(OPTIONS_MODE);

mode OPTIONS_MODE;
AS            : 'AS' ;
IN            : 'IN' ;
ON            : 'ON' ;
COLON         : ':' ;
BRACKET_START : '(' ;
BRACKET_SEP   : ',' ;
BRACKET_END   : ')' ;
OPERATOR      : '+' | '-' | '*' | '/' ;
COMPARE       : '>=' | '<=' | '>' | '<' | '=' ;
VARIABLE      : [a-z]+[a-z0-9]* ;
NUMBER        : '-'?[0-9]+ ;
TEXT          : '"' ~[\r\n"]* '"' ;
SP            : [ \t] -> channel(HIDDEN);
NL            : [\r\n]+ -> mode(DEFAULT_MODE);