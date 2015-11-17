# What is Q

The Q Programming Language is a powerful and easy to read programming language with features from Assembly and C. Although it is designed to be used specifically for programming the Q Operating System, it can be used for general purpose lowish-level programming projects that would origionally have been done in Assembly/C/C++.

Q is an Object Oriented Programming Language with one key feature that no other languages have - Assembly Level CPU Access with ActionScript like syntax. What this means, in simpler terms, is you are able to change individual registers such as <code>eax</code> or <code>al</code> like you would in assembly, but in a language that also supports <code>if statments</code>, <code>while loops</code>, <code>functions</code> and many other features edvident in high-level languages such as Java and C.

## Writing in Q

Currently there is no compiler for Q. This is because Q is designed to be written and compiled on [Q OS](www.github.com/raphydaphy/Q-OS). Although it may seem unnessary to have to be using Q OS to write Q code, it eliminates the need for many complex compiler logic needed when compiling code for different systems other than that you are compiling on known as [Cross Compiling](https://en.wikipedia.org/wiki/Cross_compiler).

When writing Q code during the very early stages of development for the Q Programming Language, editing Q Code is best done in a pre-existing text editor with syntax highlighting set to <code>sh</code> or <code>shell</code> because the syntax of Q is somewhat similar to that of Shell Scripts - at least on the outside from a readers point of view.
