// Copyright (c) 2013 Armando Blancas. All rights reserved.
// The use and distribution terms for this software are covered by the
// Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
// which can be found in the file epl-v10.html at the root of this distribution.
// By using this software in any fashion, you are agreeing to be bound by
// the terms of this license.
// You must not remove this notice, or any other, from this software.

grammar Smallc;

options {
    output = AST;
    ASTLabelType = CommonTree;
    backtrack = true;
}

tokens {
    VAR; // Variable definition
    DIM; // Array dimension
    FUN; // Function definition
    FWD; // Function declaration
    PAR; // Formal parameter
    BLK; // Block
    ARR; // Array indexing
    INV; // Function invocation
    FLD; // Field access
    ARG; // Invocation arguments
    CEX; // Conditional expression
    CST; // Cast expression
    SEQ; // Sequential expression
    PPP; // Postfix increment
    PMM; // Postfix decrement
}

@lexer::header {
package smallc.antlr;
}

@parser::header {
package smallc.antlr;
}

@members {
protected void mismatch(IntStream input, int ttype, BitSet follow)
throws RecognitionException {
  throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet(IntStream input, 
                                      RecognitionException _e,
                                      BitSet follow)
throws RecognitionException {
  throw _e;
}
}

@rulecatch {
catch (RecognitionException _e) {
    throw _e;
}
}

/****************************************************************
 *                                                              *
 *                            Parser                            *
 *                                                              *
 ****************************************************************/

compilation_unit
    : declaration+
    ;

declaration
    : define_decl
    | variable_list_decl
    | function_decl
    | function_def
    ;

define_decl
    : ('#' DEFINE symbol_decl          -> ^(DEFINE symbol_decl))
    | ('#' DEFINE '{' symbol_decl+ '}' -> ^(DEFINE symbol_decl)+)
    ;

symbol_decl
    : IDENTIFIER expression ';' -> IDENTIFIER expression
    ;

variable_list_decl
    : var_type variable_decl (',' variable_decl)* ';'  
      -> ^(VAR var_type variable_decl)+
    ;

variable_decl 
    : IDENTIFIER ('=' assignment_expr)? 
      -> IDENTIFIER assignment_expr?
    ;

function_decl
    : function_signature ';'  
      -> ^(FWD function_signature)
    ;

function_def
    : function_signature block
      -> ^(FUN function_signature block)
    ;

function_signature
    : function_type IDENTIFIER '(' parameter_list? ')'
      -> IDENTIFIER function_type ^(PAR parameter_list?)
    ;

parameter_list
    : type IDENTIFIER (',' type IDENTIFIER)*  
      -> ^(IDENTIFIER type)+
    ;

function_type
    : type 
    | VOID_TYPE
    ;

var_type
    : type_name array_suffix*  
      -> ^(type_name array_suffix*)
    ;

array_suffix
    : '[' expression ']' -> expression
    | '[' ']'            -> DIM
    ;

type
    : type_name array_dim*  
      -> ^(type_name array_dim*)
    ;

array_dim
    : '[' ']' -> DIM
    ;

type_name
    : primitive_type 
    | named_object
    ;

primitive_type
    : BOOLEAN_TYPE
    | BYTE_TYPE
    | CHAR_TYPE
    | SHORT_TYPE
    | INT_TYPE
    | FLOAT_TYPE
    | LONG_TYPE
    | DOUBLE_TYPE
    ;

block
    : '{' block_component* '}' 
      -> ^(BLK block_component*)
    ;

block_component
    : variable_list_decl
    | statement
    | expression
    ;

statement
    : (b=block -> $b)
    | (e=expression ';' -> $e)
    | (WHILE '(' we=expression ')' s3=statement    -> ^(WHILE $we $s3))
    | (DO s4=statement WHILE '(' de=expression ')' -> ^(DO $s4 WHILE $de))
    | (IF '(' ie=expression ')' s1=statement 
       (options {k=1; backtrack=false;}:'else' s2=statement)? -> ^(IF $ie $s1 $s2?))
    | (RETURN ret=expression? ';' -> ^(RETURN $ret?))
    | (';' -> )
    ;

expression
    : (assignment_expr -> assignment_expr)
      (',' ae=assignment_expr -> ^(SEQ $expression $ae))*
    ;

assignment_expr
    : (conditional_expr -> conditional_expr)
    | (e1=postfix_expr assign_op e2=assignment_expr -> ^(assign_op $e1 $e2))
    ;

conditional_expr
    : (or_expr -> or_expr)
      ('?' e1=expression ':' e2=conditional_expr -> ^(CEX or_expr $e1 $e2))?
    ;

or_expr
    : (and_expr -> and_expr) 
      (OR e=and_expr -> ^(OR $or_expr $e))*
    ;

and_expr
    : (bit_or_expr -> bit_or_expr) 
      (AND e=bit_or_expr -> ^(AND $and_expr $e))*
    ;

bit_or_expr
    : (xor_expr -> xor_expr)
      (BWOR e=xor_expr -> ^(BWOR $bit_or_expr $e))*
    ;

xor_expr
    : (bit_and_expr -> bit_and_expr)
      (XOR e=bit_and_expr -> ^(XOR $xor_expr $e))*
    ;

bit_and_expr
    : (equality_expr -> equality_expr)
      (BWAND e=equality_expr -> ^(BWAND $bit_and_expr $e))*
    ;

equality_expr
    : (relational_expr -> relational_expr)
      ( EQUALS e1=relational_expr -> ^(EQUALS $equality_expr $e1)
      | NOTEQ e2=relational_expr  -> ^(NOTEQ $equality_expr $e2) )*
    ;

relational_expr
    : (shift_expr -> shift_expr)
      ( LESS e1=shift_expr      -> ^(LESS $relational_expr $e1)
      | GRTR e2=shift_expr      -> ^(GRTR $relational_expr $e2)
      | LESSEQ e3=shift_expr    -> ^(LESSEQ $relational_expr $e3)
      | GRTREQ e4=shift_expr    -> ^(GRTREQ $relational_expr $e4) )*
    ;

shift_expr
    : (add_expr -> add_expr)
      ( SHIFTL e1=add_expr  -> ^(SHIFTL $shift_expr $e1)
      | SHIFTR e2=add_expr  -> ^(SHIFTR $shift_expr $e2)
      | SHIFTU e3=add_expr  -> ^(SHIFTU $shift_expr $e3) )*
    ;

add_expr
    : (mult_expr -> mult_expr)
      ( PLUS pe=mult_expr     -> ^(PLUS $add_expr $pe)
      | MINUS me=mult_expr    -> ^(MINUS $add_expr $me) )*
    ;

mult_expr
    : (cast_expr -> cast_expr)
      ( MULT mul=cast_expr    -> ^(MULT $mult_expr $mul)
      | DIV div=cast_expr     -> ^(DIV  $mult_expr $div)
      | MOD mod=cast_expr     -> ^(MOD  $mult_expr $mod) )*
    ;

cast_expr
    : ('(' type_name ')' e=cast_expr -> ^(CST type_name $e))
    | (unary_expr -> unary_expr)
    ;

unary_expr
    : (postfix_expr -> postfix_expr)
    | (unary_op e=unary_expr -> ^(unary_op $e))
    ;

postfix_expr
    : (primary_expr -> primary_expr)
      ( '(' a=argument_list? ')' -> ^(INV $postfix_expr ^(ARG $a?))
      | '[' e=expression ']'     -> ^(ARR $postfix_expr $e) 
      | '.' o=named_object       -> ^(FLD $postfix_expr $o)
      | INC                      -> ^(PPP $postfix_expr)
      | DEC                      -> ^(PMM $postfix_expr) )*
    ;

primary_expr
    : named_object
    | literal_value
    | '(' expression ')' -> expression
    ;

argument_list
    : assignment_expr (','! assignment_expr)*
    ;

assign_op
    : ASSIGN
    | PLUSEQ
    | MINEQ
    | MULTEQ
    | DIVEQ
    | MODEQ
    ;

unary_op
    : PLUS
    | MINUS
    | NOT
    | BWNOT
    | INC
    | DEC
    ;

named_object
    : IDENTIFIER 
    | QUALIFIED_NAME
    ;

literal_value
    : CHAR_LITERAL
    | DECIMAL_LITERAL
    | FLOAT_LITERAL
    | HEX_LITERAL
    | OCTAL_LITERAL
    | STRING_LITERAL
    | TRUE | FALSE | NULL
    ;

/****************************************************************
 *                                                              *
 *                             Lexer                            *
 *                                                              *
 ****************************************************************/

BOOLEAN_TYPE : 'boolean' ;
BYTE_TYPE    : 'byte'    ;
CHAR_TYPE    : 'char'    ;
SHORT_TYPE   : 'short'   ;
INT_TYPE     : 'int'     ;
FLOAT_TYPE   : 'float'   ;
LONG_TYPE    : 'long'    ;
DOUBLE_TYPE  : 'double'  ;
VOID_TYPE    : 'void'    ;

TRUE     : 'true'     ;
FALSE    : 'false'    ;
NULL     : 'null'     ;
DEFINE   : 'define'   ;
RETURN   : 'return'   ;
IF       : 'if'       ;
ELSE     : 'else'     ;
WHILE    : 'while'    ;
DO       : 'do'       ;
BREAK    : 'break'    ;
CONTINUE : 'continue' ;

OR      : '||'  ;
AND     : '&&'  ;
BWOR    : '|'   ;
BWAND   : '&'   ;
XOR     : '^'   ;
EQUALS  : '=='  ;
NOTEQ   : '!='  ;
LESS    : '<'   ;
LESSEQ  : '<='  ;
GRTR    : '>'   ;
GRTREQ  : '>='  ;
PLUS    : '+'   ;
MINUS   : '-'   ;
MULT    : '*'   ;
DIV     : '/'   ;
MOD     : '%'   ;
INC     : '++'  ;
DEC     : '--'  ;
SHIFTL  : '<<'  ;
SHIFTR  : '>>'  ;
SHIFTU  : '>>>' ;
ASSIGN  : '='   ;
PLUSEQ  : '+='  ;
MINEQ   : '-='  ;
MULTEQ  : '*='  ;
DIVEQ   : '/='  ;
MODEQ   : '%='  ;
NOT     : '!'   ;
BWNOT   : '~'   ;

IDENTIFIER
    : LETTER (LETTER | '0'..'9')*
    ;

QUALIFIED_NAME
    : IDENTIFIER ('.' IDENTIFIER)+
    ;
	
fragment
LETTER
    : '$'
    | 'A'..'Z'
    | 'a'..'z'
    | '_'
    ;

fragment
HexDigit 
    : ('0'..'9'|'a'..'f'|'A'..'F') 
    ;

fragment
UnicodeEscape
    : '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
OctalEscape
    : '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    | '\\' ('0'..'7') ('0'..'7')
    | '\\' ('0'..'7')
    ;

fragment
EscapeSequence
    : '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    | UnicodeEscape
    | OctalEscape
    ;

CHAR_LITERAL
    : '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
    ;

STRING_LITERAL
    : '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;

fragment
LongSuffix
    : ('l'|'L')
    ;

fragment
FloatSuffix
    : ('f'|'F'|'d'|'D') 
    ;

HEX_LITERAL 
    : '0' ('x'|'X') HexDigit+ LongSuffix? 
    ;

DECIMAL_LITERAL 
    : ( '0' | ('1'..'9') ('0'..'9')* ) LongSuffix? 
    ;

OCTAL_LITERAL 
    : '0' ('0'..'7')+ LongSuffix? 
    ;

fragment
Exponent 
    : ('e'|'E') ('+'|'-')? ('0'..'9')+ 
    ;

FLOAT_LITERAL
    : ('0'..'9')+ ('.' ('0'..'9')+)? Exponent? FloatSuffix?
    | '.' ('0'..'9')+ Exponent? FloatSuffix?
    ;

WS  : (' '|'\r'|'\t'|'\n') {$channel=HIDDEN;}
    ;

COMMENT
    : '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;
