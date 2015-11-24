/*
 * The MIT License
 *
 * Copyright 2015 plank.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.qosdev.QPL;

import com.ymcmp.dictionary.Dictionary;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author plank
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new RuntimeException("Please supply file as argument");
        }
        ANTLRInputStream ais = new ANTLRFileStream(args[0]);
        QPLLexer lex = new QPLLexer(ais);
        TokenStream toks = new CommonTokenStream(lex);
        QPLParser parse = new QPLParser(toks);
        ParseTree tree = parse.prog();
        System.out.println(new ImpVisitor(Paths.get(args[0]).getParent().normalize().toString()).visit(tree));
    }

}

class ImpVisitor extends QPLBaseVisitor<String> {

    final String __FILE_PATH__;

    public ImpVisitor(String path) {
        super();
        __FILE_PATH__ = path;
    }

    @Override
    public String visitDataTypes(QPLParser.DataTypesContext ctx) {
        return super.visitDataTypes(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStatement(QPLParser.StatementContext ctx) {
        return visit(ctx.h);
    }

    @Override
    public String visitStatements(QPLParser.StatementsContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(visit(ctx.left)).append(";\n");
        if (ctx.right != null) {
            sb.append(visit(ctx.right)).append(";\n");
        }
        return sb.toString();
    }

    @Override
    public String visitSection(QPLParser.SectionContext ctx) {
        return visit(ctx.st);
    }

    @Override
    public String visitProg(QPLParser.ProgContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(visit(ctx.left));
        if (ctx.right != null) {
            sb.append(visit(ctx.right));
        }
        String[] split = sb.toString().split("\n+");
        sb.setLength(0);
        for (String st : split) {
            if (!";".equals(st.trim())) {
                sb.append(st).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String visitTypeScope(QPLParser.TypeScopeContext ctx) {
        return addPtrType(ctx.getText().trim().substring(1));
    }

    @Override
    public String visitFtypeScope(QPLParser.FtypeScopeContext ctx) {
        return addPtrType(ctx.getText().trim().substring(1));
    }

    private String addPtrType(final String praw) {
        String raw = praw.trim();
        if (raw.endsWith(PTR_TYPE_ID)) {
            return addPtrType(raw.substring(0, raw.length() - PTR_TYPE_ID.length())) + "*";
        } else {
            return raw;
        }
    }
    private static final String PTR_TYPE_ID = "_ptr";

    @Override
    public String visitScopedId(QPLParser.ScopedIdContext ctx) {
        return visit(ctx.ts) + " " + ctx.id.getText();
    }

    @Override
    public String visitFscopedId(QPLParser.FscopedIdContext ctx) {
        String rt = "void"; // By default return void (not int)
        if (ctx.ts != null) {
            rt = visit(ctx.ts);
        }
        return rt + " " + ctx.id.getText();
    }

    @Override
    public String visitDefParams_end(QPLParser.DefParams_endContext ctx) {
        return visit(ctx.si);
    }

    @Override
    public String visitDefParams_req(QPLParser.DefParams_reqContext ctx) {
        return visit(ctx.si) + ", " + visit(ctx.r);
    }

    @Override
    public String visitFuncId(QPLParser.FuncIdContext ctx) {
        StringBuilder txt = new StringBuilder();
        String fname = visit(ctx.fi);
        txt.append(fname);
        txt.append("(");
        if (ctx.p != null) {
            txt.append(visit(ctx.p));
        }
        txt.append(")");
        String[] split = fname.split("\\s");
        FUNC_LIST.put(split[split.length - 1], txt.toString());
        checkRedef(split[split.length - 1], idTypes.COMMON);
        IDENTIFIER_LIST.add(split[split.length - 1], idTypes.FUNCTION);
        return txt.toString();
    }

    @Override
    public String visitMakeFunc(QPLParser.MakeFuncContext ctx) {
        return visit(ctx.fi);
    }

    @Override
    public String visitMakeCmn(QPLParser.MakeCmnContext ctx) {
        String name = ctx.id.getText();
        if (name.endsWith(PTR_TYPE_ID)) {
            throw new RuntimeException("Variable names cannot end with " + PTR_TYPE_ID + ":" + name);
        }
        checkRedef(name, idTypes.FUNCTION); // Can only redefine from funtions
        IDENTIFIER_LIST.add(name, idTypes.COMMON);
        return addPtrType(ctx.t.getText()) + " " + name;
    }

    /**
     *
     * @param name
     * @param tail The type where redefinition is allowed
     */
    private void checkRedef(final String name, final idTypes... tail) {
        final List<idTypes> nameCorresp = IDENTIFIER_LIST.getValues(name);
        if (nameCorresp == null) {
            // null means undefined
            return;
        }
        for (idTypes t : tail) {
            if (nameCorresp.stream().anyMatch(t::equals)) {
                return;
            }
        }
        throw new RuntimeException("Identifier " + name + " redefined from type " + nameCorresp);
    }

    enum idTypes {
        META, TEMPLATE, FUNCTION, COMMON
    }

    static final Dictionary<String, idTypes> IDENTIFIER_LIST = new Dictionary<>();
    static final Map<String, String> FUNC_LIST = new HashMap<>();

    @Override
    public String visitMakeMeta(QPLParser.MakeMetaContext ctx) {
        String name = ctx.id.getText();
        if (name.endsWith(PTR_TYPE_ID)) {
            throw new RuntimeException("Variable names cannot end with " + PTR_TYPE_ID + ":" + name);
        }
        checkRedef(name);
        IDENTIFIER_LIST.add(name, idTypes.META);
        return ""; // `const` comes after
    }

    @Override
    public String visitMakeTemp(QPLParser.MakeTempContext ctx) {
        String name = ctx.id.getText();
        if (name.endsWith(PTR_TYPE_ID)) {
            throw new RuntimeException("Variable names cannot end with " + PTR_TYPE_ID + ":" + name);
        }
        checkRedef(name);
        IDENTIFIER_LIST.add(name, idTypes.TEMPLATE);
        return ""; // `typedef` come after
    }

    @Override
    public String visitFuncType(QPLParser.FuncTypeContext ctx) {
        StringBuilder sb = new StringBuilder("{");
        if (ctx.st != null) {
            sb.append("\n").append(visit(ctx.st));
        }
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String visitSetVal(QPLParser.SetValContext ctx) {
        String name = ctx.id.getText();
        List<idTypes> pImpl = IDENTIFIER_LIST.getValues(name);
        if (pImpl == null) {
            throw new RuntimeException("Variable name " + name + " not `made` yet!");
        }
        switch (pImpl.get(0)) {
        case FUNCTION:
            return FUNC_LIST.get(name) + " " + visit(ctx.val);
        case COMMON: // Normal assign
            String val = ctx.val.getText();
            if (val.startsWith("function")) {
                throw new RuntimeException("Bad definition");
            } else if ("Null".equals(val)) {
                val = "0";
            }
            return name + " = " + val;
        case META: // `const`
            // Type inferencing here! Skills!
            String rawVal = ctx.val.getText();
            String inferredType;
            if (rawVal.startsWith("function")) {
                throw new RuntimeException("Function cannot be assigned as META");
            } else if (rawVal.startsWith("\"")) {
                inferredType = "string";
            } else if (rawVal.equals("True") || rawVal.equals("False")) {
                inferredType = "bool";
            } else if (rawVal.startsWith("'")) {
                inferredType = "char";
            } else if (rawVal.matches("[+-]\\d+")) {
                try {
                    Integer.parseInt(rawVal);
                    inferredType = "int";
                } catch (NumberFormatException nfe) {
                    inferredType = "uint64";
                }
            } else if (rawVal.matches("[+-]\\d*\\.\\d+")) {
                try {
                    Float.parseFloat(rawVal);
                    inferredType = "float";
                } catch (NumberFormatException nfe) {
                    inferredType = "long double";
                }
            } else {
                inferredType = "(void*)"; // Why bother...
            }
            return "const " + inferredType + " " + name + " = " + rawVal;
        case TEMPLATE: // `typedef`
            return "typedef (" + addPtrType(ctx.val.getText()) + ") (" + name + ")";
        default:
            throw new RuntimeException("Compiler fell into invalid state!");
        } // Ignore case for function -> RuntimeException
    }

    @Override
    public String visitDType(QPLParser.DTypeContext ctx) {
        if ("Null".equals(ctx.getText())) {
            return "0";
        }
        return ctx.getText();
    }

    @Override
    public String visitExprBracket(QPLParser.ExprBracketContext ctx) {
        return "(" + visit(ctx.cent) + ")";
    }

    @Override
    public String visitExprUnary(QPLParser.ExprUnaryContext ctx) {
        return ctx.op.getText() + visit(ctx.right);
    }

    @Override
    public String visitExprSimple(QPLParser.ExprSimpleContext ctx) {
        return visit(ctx.left) + ctx.op.getText() + visit(ctx.right);
    }

    @Override
    public String visitExprRoot(QPLParser.ExprRootContext ctx) {
        throw new RuntimeException("Operator // is currently not supported");
    }

    @Override
    public String visitExprPow(QPLParser.ExprPowContext ctx) {
        throw new RuntimeException("Operator ** is currently not supported");
    }

    @Override
    public String visitReturnVal(QPLParser.ReturnValContext ctx) {
        return "return " + visit(ctx.xp);
    }

    @Override
    public String visitWhileLoop(QPLParser.WhileLoopContext ctx) {
        StringBuilder sb = new StringBuilder("while (");
        sb.append(visit(ctx.xp)).append(")");
        if (ctx.st != null) {
            sb.append(" {\n").append(visit(ctx.st)).append("}");
        } // Otherwise loop has empty body ';'
        return sb.toString();
    }

    @Override
    public String visitElseCondition(QPLParser.ElseConditionContext ctx) {
        StringBuilder sb = new StringBuilder(" else");
        if (ctx.mst != null) {
            sb.append(" {\n").append(visit(ctx.mst)).append("}");
        } // Otherwise cond has empty body ';'
        return sb.toString();
    }

    @Override
    public String visitIfCondition(QPLParser.IfConditionContext ctx) {
        StringBuilder sb = new StringBuilder("if (");
        sb.append(visit(ctx.xp)).append(")");
        if (ctx.st != null) {
            sb.append(" {\n").append(visit(ctx.st)).append("}");
        } // Otherwise cond has empty body ';'
        if (ctx.ef != null) {
            sb.append(visit(ctx.ef));
        } // Otherwise no else if's
        if (ctx.el != null) {
            sb.append(visit(ctx.el));
        } // Otherwise no else body
        return sb.toString();
    }

    @Override
    public String visitElsifCondition(QPLParser.ElsifConditionContext ctx) {
        StringBuilder sb = new StringBuilder(" else if (");
        sb.append(visit(ctx.xp)).append(")");
        if (ctx.mst != null) {
            sb.append(" {\n").append(visit(ctx.mst)).append("}");
        } // Otherwise cond has empty body ';'
        if (ctx.req != null) {
            sb.append(visit(ctx.req));
        }
        return sb.toString();
    }

    @Override
    public String visitIncludeFile(QPLParser.IncludeFileContext ctx) {
        String filePath = ctx.path.getText().trim();
        filePath = filePath.substring(1, filePath.length() - 1);
        if (filePath.charAt(0) != '/') {
            filePath = Paths.get(__FILE_PATH__, filePath).toString();
        }
        try {
            ANTLRInputStream ais = new ANTLRFileStream(filePath);
            QPLLexer lex = new QPLLexer(ais);
            TokenStream toks = new CommonTokenStream(lex);
            QPLParser parse = new QPLParser(toks);
            ParseTree tree = parse.prog();
            return new ImpVisitor(filePath).visit(tree);
        } catch (IOException ex) {
            System.err.println(filePath + " cannot be found! Ignoring");
            return "";
        }
    }

}
