grammar QPL;

prog:
    left=section right=prog?
;

section:
    Ksection Text Lparen
        st=statements?
    Rparen
;

statements:
    left=statement right=statements
    | left=statement
;

statement:
    h=hstatement SEMI
;

hstatement:
    makeCmn
    | makeFunc
    | makeTemp
    | makeMeta
    | setVal
    | returnVal
    | whileLoop
    | ifCondition
;

ifCondition:
    Kif xp=expr Lparen
        st=statements?
    Rparen
    ef=elsifCondition?
    el=elseCondition?
;

elsifCondition:
    Kelse Kif xp=expr Lparen mst=statements? Rparen req=elsifCondition?
;

elseCondition:
    Kelse Lparen mst=statements? Rparen
;

Kelse:
    'else'
;

Kif:
    'if'
;

whileLoop:
    Kwhile xp=expr Lparen
        st=statements?
    Rparen
;

Kwhile:
    'while'
;

returnVal:
    Kresult xp=expr
;

Kresult:
    'result'
;

setVal:
    Kset id=Identifier '=' val=setDataTypes
;

funcType:
    Kfunction Lparen st=statements? Rparen
;

Kset:
    'set'
;

makeCmn:
    Kmake t=declarable id=Identifier
;

makeFunc:
    Kmake Kfunction fi=funcId
;

makeTemp:
    Kmake Ktemplate id=Identifier
;

makeMeta:
    Kmake Kmeta id=Identifier
;

funcId:
    fi=fscopedId Lbrac p=defParams? Rbrac
;

defParams:
    si=scopedId COMMA r=defParams? #defParams_req
    | si=scopedId #defParams_end
;

COMMA:
    ','
;

fscopedId:
    id=Identifier ts=ftypeScope?
;

scopedId:
    id=Identifier ts=typeScope
;

// Identifier in types end with `_ptr` means something pointer
ftypeScope:
    Scope (Void_t | declarable)
;

typeScope:
    Scope declarable
;

Scope:
    ':'
;

Void_t:
    'void'
;

declarable:
    Primitives | Identifier
;

Primitives:
    'bool'
    | 'byte'
    | 'char'
    | 'short'
    | Int_t
    | 'long'
    | 'float'
    | 'double'
    | 'string'
    | 'u'? Int_t '4'
    | 'u'? Int_t '8'
    | 'u'? Int_t '16'
    | 'u'? Int_t '32'
    | 'u'? Int_t '64'
;

fragment
Int_t:
    'int'
;

SEMI:
    ';'
;

Kfunction:
    'function'
;

Ktemplate:
    'template'
;

Kmeta:
    'meta'
;

Kmake:
    'make'
;

Ksection:
    'section'
;

Lparen:
    '{'
;

Rparen:
    '}'
;

Lbrac:
    '('
;

Rbrac:
    ')'
;

setDataTypes:
    funcType
    | expr
;

SimpleOp:
    [+-] | '*' | '/' | '^' | '|' | '%' | '&' | '<' | '>' | '<=' | '>=' | '&&' | '||' | '=='
;

UnaryOp:
    '~' | '!'
;

expr:
    left=expr op=SimpleOp right=expr #exprSimple
    | left=expr '**' right=expr #exprPow
    | left=expr '//' right=expr #exprRoot
    | op=UnaryOp right=expr #exprUnary
    | Lbrac cent=expr Rbrac #exprBracket
    | dataTypes #dType
;

dataTypes:
    Identifier
    | Text
    | Number
    | Bool
    | Null
;

Identifier:
    [a-zA-Z_][0-9a-zA-Z_]*
;

Text:
    '"' (~["\r\n\\] | TextEsc)* '"'
;

fragment
TextEsc:
    '\\' ['"aAbBfFnNrRtTvV\\]
;

Number:
    [+-]? (Float | Int | Hex)
;

fragment
Float:
    Digit* '.' Digit+
;

fragment
Int:
    '0'
    | [1-9] Digit*
;

fragment
Hex:
    '0x' HexDigit HexDigit
;

fragment
HexDigit:
    [a-fA-F] | Digit
;

fragment
Digit:
    [0-9]
;

Null:
    'Null'
;

Bool:
    'True' | 'False'
;

LineComment:
    '##' ~[\r\n]* -> skip
;

Whitespace:
    [ \t\r\n\f]+ -> skip
;
