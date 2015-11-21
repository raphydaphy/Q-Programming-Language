" Vim syntax file
" Language: QPL
" Maintainer: Paul Plankp
" Latest Revision: 21 November 2015

if exists("b:current_syntax")
  finish
endif

" Integer with - + or nothing in front
syn match celNumber '\d\+'
syn match celNumber '[-+]\d\+'

syn match celHexNum '0x\x\{2,4}'

" Floating point number with decimal no E or e (+,-)
syn match celFloat '\d\+\.\d*'
syn match celFloat '[-+]\d\+\.\d*'

" Identifier [a-zA-Z_][a-zA-Z0-9_]*
syn match identifier '\h\w*' contains=builtin

syn region block start="{" end="}" fold transparent contains=ALLBUT,QPLSec
syn region block start="(" end=")" fold transparent contains=ALLBUT,QPLSec,QPLCmd,QPLCond,QPLLoop

" Function is a type!?
syn keyword builtin contained unsigned signed integer char string long bool short byte float double function template

syn keyword QPLLink inc nextgroup=QPLString skipwhite

syn keyword QPLCmd set make nextgroup=identifier skipwhite
syn keyword QPLCmd move into
syn keyword QPLCond if else
syn keyword QPLLoop while for
syntax keyword QPLBool True False Nil
syntax keyword QPLBinop + - ** * // / % < <= > >= ^ \|\| \| ~ ! && & nextgroup=QPLString,QPLChar,QPLNumber,QPLFloat,identifier skipwhite

syn keyword QPLSec section nextgroup=identifier skipwhite

syntax region QPLString start=/\v"/ skip=/\v\\./ end=/\v"/
syntax match QPLChar '\'.\''

syn keyword noteTodos contained TODO FIXME XXX NOTE
syntax match potionComment "##.*$" contains=noteTodos
hi def link potionComment 	Comment
hi def link noteTodos		Todo
hi def link celNumber		Number
hi def link celHexNum		Number
hi def link celFloat		Float
hi def link QPLChar		Character
hi def link QPLBool		Boolean
hi def link builtin		Type
hi def link QPLCmd		Statement
hi def link QPLSec		PreProc
hi def link QPLLink		PreProc
hi def link QPLString		String
hi def link QPLCond		Conditional
hi def link QPLLoop		Repeat
hi def link QPLBinop		Operator
