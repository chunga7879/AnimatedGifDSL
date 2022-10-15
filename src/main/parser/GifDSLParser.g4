parser grammar GifDSLParser;
options { tokenVocab=GifDSLLexer; }

program          : START_NL? (statement NL)* EOF;

statement        : INDENT? (function | with_statement | return_statement | control | COMMENT) ;

control          : control_type COLON ;
control_type     : define_statement | loop_statement | if_statement ;

define_statement : DEFINE define_function define_target? define_params? ;
define_function  : VARIABLE ;
define_target    : VARIABLE ;
define_params    : DEFINE_WITH BRACKET_START VARIABLE (BRACKET_SEP VARIABLE)* BRACKET_END ;

if_statement     : IF BRACKET_START comparison BRACKET_END ;
comparison       : arithmetic COMPARE arithmetic ;
loop_statement   : LOOP loop_variable IN (range | VARIABLE) ;
loop_variable    : VARIABLE ;
range            : BRACKET_START NUMBER BRACKET_SEP NUMBER BRACKET_END ;

function         : FUNCTION_NAME function_target? function_on? function_as? ;
function_target  : expression ;
function_on      : ON VARIABLE ;
function_as      : AS VARIABLE ;
with_statement   : WITH VARIABLE COLON expression ;

return_statement : RETURN expression ;

expression       : arithmetic | COLOUR | TEXT ;
arithmetic       : num_or_var (OPERATOR num_or_var)? ;
num_or_var       : NUMBER | VARIABLE ;
