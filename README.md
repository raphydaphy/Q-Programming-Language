# What is Q

The Q Programming Language is a powerful and easy to read programming language with features from Assembly and C. Although it is designed to be used specifically for programming the Q Operating System, it can be used for general purpose lowish-level programming projects that would origionally have been done in Assembly/C/C++.

Q is an Object Oriented Programming Language with one key feature that no other languages have - Assembly Level CPU Access with ActionScript like syntax. What this means, in simpler terms, is you are able to change individual registers such as <code>eax</code> or <code>al</code> like you would in assembly, but in a language that also supports <code>if statments</code>, <code>while loops</code>, <code>functions</code> and many other features edvident in high-level languages such as Java and C.

## Writing in Q

Currently there is no compiler for Q. This is because Q is designed to be written and compiled on [Q OS](www.github.com/raphydaphy/Q-OS). Although it may seem unnessary to have to be using Q OS to write Q code, it eliminates the need for many complex compiler logic needed when compiling code for different systems other than that you are compiling on known as [Cross Compiling](https://en.wikipedia.org/wiki/Cross_compiler).

When writing code in Q, it is best to use the `vim` text editor. This is because [plankp](https://github.com/plankp) created a syntax highlighting file for Q that you can install on your computer simply by downloading and running a setup script that I wrote. In this repository, navigate to [/dev/setup.sh](https://github.com/raphydaphy/Q-Programming-Language/blob/master/dev/setup.sh) or just click that link. Download the file and make sure it has permission to `execute this file as a program`. You can make sure it does in Ubuntu by right clicking the file in the file browser when you have downloaded it, choosing `Properties` then click the second tab in the menu that opens, `Permissions`. Simply tick the bow saying `Allow executing file as program` and then you are able to open a Terminal window, navigate to the folder you put the `setup.sh` file in and run one command to install the `.vim` scripts.

    ./setup.sh
    
This will setup everything you need to be able to write code in the `vim` text editor on Ubuntu using the Q Language with the `.code` file extension.

## What is this repository for?

If you are wondering why this repository exists with hardly anything in the code area, you are not alone! Almost everything for Q Programming Language currently can be found in the Wiki tab of this repository. When we start working on a compiler for Q Programming Language, there will obviously be a lot more information in the Code tab of the repository. 

Thank you for looking at this repository and I hope you like Q Programming Language... not that there is anything much in the code area to see yet :)
