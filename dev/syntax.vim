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

" Floating point number with decimal no E or e (+,-)
syn match celFloat '\d\+\.\d*'
syn match celFloat '[-+]\d\+\.\d*'

" Identifier [a-zA-Z_][a-zA-Z0-9_]*
syn match identifier '[a-zA-Z_][a-zA-Z0-9_]*' contains=builtin

syn region block start="{" end="}" fold transparent contains=ALLBUT,QPLSec
syn region block start="(" end=")" fold transparent contains=ALLBUT,QPLSec,QPLCmd,QPLCond,QPLLoop

" Function is a type!?
syn keyword builtin contained unsigned signed integer char string long bool short byte float double function

syn keyword QPLLink inc nextgroup=QPLString skipwhite

syn keyword QPLCmd set nextgroup=identifier skipwhite
syn keyword QPLCmd make nextgroup=identifier skipwhite
syn keyword QPLCond if else
syn keyword QPLLoop while
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
