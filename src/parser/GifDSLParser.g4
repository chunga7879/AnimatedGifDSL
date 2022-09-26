parser grammar GifDSLParser;
options { tokenVocab=GifDSLLexer; }

program          : (statement (NL statement)*)? EOF ;

statement        : INDENT? (function | with_statement | return_statement | control | COMMENT) ;

control          : control_type COLON ;
control_type     : define_statement | loop_statement | if_statement ;

define_statement : DEFINE VARIABLE define_params ;
define_params    : BRACKET_START VARIABLE (BRACKET_SEP VARIABLE)* BRACKET_END ;

if_statement     : IF BRACKET_START comparison BRACKET_END ;
comparison       : num_or_var COMPARE num_or_var ;
loop_statement   : LOOP VARIABLE IN (range | VARIABLE) ;
range            : BRACKET_START num_or_var BRACKET_SEP num_or_var BRACKET_END ;

function         : FUNCTION_NAME value? (ON value)? (AS value)? ;
with_statement   : WITH VARIABLE COLON value ;

return_statement : RETURN value ;

value            : num_or_var | TEXT ;
num_or_var       : (NUMBER | VARIABLE) (OPERATOR (NUMBER | VARIABLE))? ;