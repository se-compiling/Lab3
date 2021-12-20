package Analyse;

import Antlr.miniSysY.miniSysYBaseVisitor;
import Antlr.miniSysY.miniSysYParser;
import Global.Const;
import Global.SymbolTable;
import Types.FunctionType;
import Types.IntegerType;
import Types.PointerType;
import Types.Type;
import Values.*;
import Values.Inst.MemInst;
import Values.Inst.Tag;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.System.exit;

public class Visitor extends miniSysYBaseVisitor<Void> {

    private int nodeValue = 0;
    private Value curVal;
    private boolean isConst = false;
    private boolean isRel = false;
    private boolean isGlobal = false;
    private final Constant.ConstantInt C0 = new Constant.ConstantInt(IntegerType.getI32(),0);
    private IntegerType i32 = IntegerType.getI32();
    private IntegerType I1 = IntegerType.getI1();
    private Type.VoidType voidType = Type.VoidType.getVoidTy();
    private PointerType i32Arr = new PointerType(i32);
    private MyModule module = MyModule.getInstance();
    private Builder builder = Builder.getInstance();
    private SymbolTable ST=new SymbolTable();
    private BasicBlock BB = new BasicBlock();
    private Function func = new Function("main",new FunctionType(i32,new ArrayList<>(Collections.emptyList())),false);

    private String[] SysYFunc={"getint","getch","getarray","putint","putch","putarray"};
    private boolean[] isSysYDef={false,false,false,false,false,false};


    @Override public Void visitProgram(miniSysYParser.ProgramContext ctx) {

        module.funcList.add(func);
        func.setMainBlock(BB);
        ST.addSymbol(SysYFunc[0], builder.createFunc(SysYFunc[0], new FunctionType(i32,new ArrayList<Type>(Collections.emptyList())), true));
        ST.addSymbol(SysYFunc[1], builder.createFunc(SysYFunc[1], new FunctionType(i32, new ArrayList<Type>(Collections.emptyList())), true));
        ST.addSymbol(SysYFunc[2], builder.createFunc(SysYFunc[2], new FunctionType(i32, new ArrayList<>(Collections.singletonList(i32Arr))), true));
        ST.addSymbol(SysYFunc[3], builder.createFunc(SysYFunc[3], new FunctionType(voidType, new ArrayList<>(Collections.singletonList(i32))), true));
        ST.addSymbol(SysYFunc[4], builder.createFunc(SysYFunc[4], new FunctionType(voidType, new ArrayList<>(Collections.singletonList(i32))), true));
        ST.addSymbol(SysYFunc[5], builder.createFunc(SysYFunc[5], new FunctionType(voidType, new ArrayList<>(Arrays.asList(i32,i32Arr))), true));
        return super.visitProgram(ctx);
    }

    @Override public Void visitCompUnit(miniSysYParser.CompUnitContext ctx) {
        return super.visitCompUnit(ctx);
    }

    @Override public Void visitDecl(miniSysYParser.DeclContext ctx) { return super.visitDecl(ctx); }

    @Override public Void visitConstDecl(miniSysYParser.ConstDeclContext ctx) { return super.visitConstDecl(ctx); }

    @Override public Void visitBType(miniSysYParser.BTypeContext ctx) { return super.visitBType(ctx); }

    @Override public Void visitConstDef(miniSysYParser.ConstDefContext ctx) {
        if(ST.top().get(ctx.IDENT().getText())!=null){
            exit(-1);
        }
        if(ctx.constExp().isEmpty()){
            visit(ctx.constInitVal());
            ST.addSymbol(ctx.IDENT().getText(), curVal);
        }else {
            visit(ctx.constExp(0));
        }

        return null;
    }

    @Override public Void visitConstInitVal(miniSysYParser.ConstInitValContext ctx) {
        if(ctx.constExp()!=null) {
            visit(ctx.constExp());
        }
        return null;
    }

    @Override public Void visitVarDecl(miniSysYParser.VarDeclContext ctx) { return super.visitVarDecl(ctx); }

    @Override public Void visitVarDef(miniSysYParser.VarDefContext ctx) {
        if(ST.top().get(ctx.IDENT().getText())!=null){
            exit(-1);
        }
        String name = ctx.IDENT().getText();
        if(ctx.constExp().isEmpty()){
            if(!ST.isGlobal()){
                String val=ctx.IDENT().getText();
                MemInst allca = builder.createAlloca(BB,i32);
                ST.addSymbol(val,allca);
                if(ctx.initVal()!=null) {
                    visit(ctx.initVal());
                    builder.createStore(BB, curVal, allca);
                }
            }else{
                if (ctx.initVal() != null) {
                    isGlobal = true;
                    visit(ctx.initVal());
                    isGlobal = false;
                    Constant constant = (Constant) curVal;
                    var v = builder.getGlobalVar(name, i32, constant);
                    ST.addSymbol(name, v);
                } else {
                    Constant constant = C0;
                    var v = builder.getGlobalVar(name, i32,constant );
                    ST.addSymbol(name, v);
                }
            }
        }
        return null;
    }

    @Override
    public Void visitInitVal(miniSysYParser.InitValContext ctx) {
        if(ctx.exp()!=null){
            if(!isGlobal){
                visit(ctx.exp());
            }else {
                isConst= true;
                visit(ctx.exp());
                isConst = false;
                curVal = new Constant.ConstantInt(i32,nodeValue);
            }
        }
        return null;
    }

    @Override public Void visitFuncDef(miniSysYParser.FuncDefContext ctx) {
        return super.visitFuncDef(ctx);
    }

    @Override public Void visitFuncType(miniSysYParser.FuncTypeContext ctx) { return super.visitFuncType(ctx); }

    @Override public Void visitFuncFParams(miniSysYParser.FuncFParamsContext ctx) { return super.visitFuncFParams(ctx); }

    @Override public Void visitFuncFParam(miniSysYParser.FuncFParamContext ctx) { return super.visitFuncFParam(ctx); }

    @Override public Void visitBlock(miniSysYParser.BlockContext ctx) {
        ST.addBlock();
        visitChildren(ctx);
        ST.popBlock();
        return null;
    }

    @Override public Void visitBlockItem(miniSysYParser.BlockItemContext ctx) { return super.visitBlockItem(ctx); }

    @Override public Void visitStmt(miniSysYParser.StmtContext ctx) { return super.visitStmt(ctx); }

    @Override public Void visitAssign_stmt(miniSysYParser.Assign_stmtContext ctx) {
        visit(ctx.lVal());
        Value lval = curVal;
        visit(ctx.exp());
        Value rval = curVal;
        //System.out.println("赋值检查点");
        //System.out.println(rval.getName());
        //System.out.println(rval.getType());
        if(rval.getType().isIntegerType()){
            builder.createStore(BB,rval,lval);
        }
        else
            builder.createStore(BB,lval,rval);
        return null;
    }

    @Override public Void visitExp_stmt(miniSysYParser.Exp_stmtContext ctx) { return super.visitExp_stmt(ctx); }

    @Override public Void visitIf_stmt(miniSysYParser.If_stmtContext ctx) {
        BasicBlock ifBlock = new BasicBlock("if_then_"+func.getBlockNum());
        func.insertBlock(ifBlock);
        BasicBlock nextBlock = new BasicBlock("next_"+func.getBlockNum());
        func.insertBlock(nextBlock);
        BasicBlock elseBlock;
        if(ctx.ELSE()!=null) {
            elseBlock = new BasicBlock("else_"+func.getBlockNum());
            func.insertBlock(elseBlock);
        }else {
            elseBlock = nextBlock;
        }
        ctx.cond().elseBlock=elseBlock;
        ctx.cond().ifBlock = ifBlock;
        visitCond(ctx.cond());
        BB=ifBlock;
        visitStmt(ctx.stmt(0));
        builder.createBr(nextBlock,BB);
        if(ctx.ELSE()!=null) {
            BB=elseBlock;
            visitStmt(ctx.stmt(1));
            builder.createBr(nextBlock,BB);
        }
        BB=nextBlock;
        return null;
    }

    @Override public Void visitWhile_stmt(miniSysYParser.While_stmtContext ctx) {
        BasicBlock loopBlock = new BasicBlock("loop_"+func.getBlockNum());
        BasicBlock nextBlock = new BasicBlock("next_"+func.getBlockNum());
        BasicBlock condBlock = new BasicBlock("cond_"+func.getBlockNum());
        func.insertBlock(condBlock);
        func.insertBlock(loopBlock);
        func.insertBlock(nextBlock);
        builder.createBr(condBlock,BB);
        ctx.cond().ifBlock=loopBlock;
        ctx.cond().elseBlock=nextBlock;
        BB=condBlock;
        visit(ctx.cond());
        BB=loopBlock;
        visit(ctx.stmt());
        BB=nextBlock;
        return null;
    }

    @Override public Void visitBreak_stmt(miniSysYParser.Break_stmtContext ctx) { return super.visitBreak_stmt(ctx); }

    @Override public Void visitContinue_stmt(miniSysYParser.Continue_stmtContext ctx) { return super.visitContinue_stmt(ctx); }

    @Override public Void visitReturn_stmt(miniSysYParser.Return_stmtContext ctx) {
        if(ctx.exp()!=null) {
            visit(ctx.exp());
            builder.createRet(curVal,BB);
        }else {
            builder.createRet(BB);
        }
        return null;
    }
    @Override public Void visitExp(miniSysYParser.ExpContext ctx) {
        return super.visitExp(ctx);
    }

    @Override public Void visitCond(miniSysYParser.CondContext ctx) {
        ctx.lOrExp().elseBlock = ctx.elseBlock;
        ctx.lOrExp().ifBlock = ctx.ifBlock;
        visit(ctx.lOrExp());
        return null;
    }

    @Override public Void visitLVal(miniSysYParser.LValContext ctx) {
        String name = ctx.IDENT().getText();
        Value val = ST.find(name);
        //System.out.println("LVal检查点");
        //System.out.println(val.getName());
        if(val == null){
            System.out.println("未定义的变量名");
            exit(255);
        }else if(val.getType().isIntegerType()){
            curVal = val;
        }else if(val.getType().isPointerType()){
            if(((PointerType)val.getType()).getCommand().isIntegerType()){
                if(ctx.exp().isEmpty()){
                    curVal = val;
                    return null;
                }
            }
        }

        return null;
    }

    @Override public Void visitPrimaryExp(miniSysYParser.PrimaryExpContext ctx) {
        //System.out.println(isConst);
        if(isConst){
            if(ctx.number()!=null){
                visit(ctx.number());
            }else if(ctx.lVal()!=null){
                visit(ctx.lVal());
                nodeValue = ((Constant.ConstantInt)curVal).getValue();
            }else if(ctx.exp()!=null){
                visit(ctx.exp());
            }else {
                System.out.println("PrimaryExp中isConst失败");
                exit(255);
            }
        }else {
            if(ctx.number()!=null){
                visit(ctx.number());
            }
            if(ctx.lVal()!=null){
                visit(ctx.lVal());
                //System.out.println("load检查点");
                //System.out.println(curVal.getName());
                //System.out.println(curVal.getType());
                if(curVal.getType().isIntegerType()){
                    return null;
                }
                curVal = builder.createLoad(((PointerType)curVal.getType()).getCommand(),curVal,BB);
            }
            if(ctx.exp()!=null){
                visit(ctx.exp());
            }
//            else {
//                System.out.println("PrimaryExp中!isConst失败");
//                exit(255);
//            }
        }
         return null;
    }

    @Override public Void visitNumber(miniSysYParser.NumberContext ctx) {
        if (ctx.DECIMAL_CONST() != null) {
            nodeValue = (new BigInteger(ctx.DECIMAL_CONST().getText(), 10).intValue());
        }
        else if (ctx.HEXADECIMAL_CONST() != null) {
            nodeValue = (new BigInteger(ctx.HEXADECIMAL_CONST().getText().substring(2), 16).intValue());
        }
        else if (ctx.OCTAL_CONST() != null) {
            nodeValue = (new BigInteger(ctx.OCTAL_CONST().getText(), 8)).intValue();
        }
        else {
            System.out.println("在Number_const中不是三种number之一");
            exit(255);
        }
        if (!isConst) {
            curVal = builder.getConstantInt(nodeValue);
        }
        return null;
    }

    @Override public Void visitUnaryExp(miniSysYParser.UnaryExpContext ctx) {
        if(!isConst){
            if(ctx.primaryExp()!=null){
                visit(ctx.primaryExp());
            }else if(ctx.callee()!=null){
                visit(ctx.callee());
            }else if(ctx.unaryExp()!=null){
                visit(ctx.unaryExp());
                Value tmp=curVal;
                if (tmp.getType().isI1()) {
                    builder.createZext(tmp,BB);
                }

                if (ctx.unaryOp().MINUS() != null) {
                    if (tmp.getType().isI1()) {
                        tmp = builder.createZext(tmp,BB);
                    }
                    curVal = builder.createBinary(BB,Tag.Sub, C0, tmp);
                }
                if(ctx.unaryOp().NOT()!=null){
                    curVal = builder.createBinary(BB,Tag.Eq,tmp,C0);
                }

            }
        }else {
            if(ctx.primaryExp()!=null){
                visit(ctx.primaryExp());
            }else if(ctx.callee()!=null){
                System.out.println("UnaryExp中Const时调用函数");
                exit(255);
            }else if(ctx.unaryExp()!=null){
                visit(ctx.unaryExp());

                if (ctx.unaryOp().MINUS() != null) {
                    nodeValue = -nodeValue;
                }
                if (ctx.unaryOp().NOT() != null) {
                    nodeValue = nodeValue == 1 ? 0 : 1;
                }
            }
        }
        return null;
    }

    @Override public Void visitCallee(miniSysYParser.CalleeContext ctx) {
        Value called = ST.find(ctx.IDENT().getText());
        Function calledFunc = (Function) called;
        var params = new ArrayList<Value>();
//        System.out.println("callee检查点");
//        System.out.println(calledFunc.name);
//        System.out.println(calledFunc.isSysY());
        if(calledFunc.isSysY()){
            //System.out.println("isSysY检查点");
            switch (calledFunc.name){
                case "getint" ->{
                    if(!isSysYDef[0]){
                        System.out.println(isSysYDef[0]);
                        Const.IR.append("declare i32 @getint()\n");
                        isSysYDef[0]=true;
                    }
                    //System.out.println("getint检查点");
                    curVal = builder.createCall(BB,calledFunc,params);
                }
                case "getch" ->{
                    if(!isSysYDef[1]){
                        Const.IR.append("declare i32 @getch()\n");
                        isSysYDef[1]=true;
                    }
                    curVal = builder.createCall(BB,calledFunc,params);
                }case "getarray" ->{
                    //Const.IR.append("declare i32 @getarray()\n")
                }case "putint" ->{
                    if(!isSysYDef[3]){
                        Const.IR.append("declare void @putint(i32)\n");
                        isSysYDef[3]=true;
                    }
                    //System.out.println("putint检查点");
                    visit(ctx.funcRParams());
                    params.add(curVal);
                    curVal = builder.createCall(BB,calledFunc,params);
                }case "putch" ->{
                    if(!isSysYDef[4]){
                        Const.IR.append("declare void @putch(i32)\n");
                        isSysYDef[4]=true;
                    }
                    visit(ctx.funcRParams());
                    params.add(curVal);
                    curVal = builder.createCall(BB,calledFunc,params);
                }case "putarray" ->{

                    //Const.IR.append("declare void @putarray()\n");
                }
                default -> throw new IllegalStateException("Unexpected value: " + calledFunc.name);
            }
        }
        return null;
    }


    @Override public Void visitFuncRParams(miniSysYParser.FuncRParamsContext ctx) { return super.visitFuncRParams(ctx); }

    @Override public Void visitParam(miniSysYParser.ParamContext ctx) {
        return super.visitParam(ctx);
    }

    @Override public Void visitMulExp(miniSysYParser.MulExpContext ctx) {
        if(isConst){
            visit(ctx.unaryExp(0));
            int tmp = nodeValue;
            for (int i = 1; i < ctx.unaryExp().size(); i++) {
                visit(ctx.unaryExp(i));
                if (ctx.mulOp(i - 1).MUL() != null) {
                    tmp *= nodeValue;
                }
                else if (ctx.mulOp(i - 1).DIV() != null) {
                    tmp /= nodeValue;
                }
                else if (ctx.mulOp(i - 1).MOD() != null) {
                    tmp %= nodeValue;
                }
            }
            nodeValue = tmp;
            return null;
        } else {
            visit(ctx.unaryExp(0));
            Value lVal = curVal;
            for (int i = 1; i < ctx.unaryExp().size(); i++) {
                visit(ctx.unaryExp(i));
                Value rVal = curVal;
                if (lVal.getType().isI1()) {
                    lVal = builder.createZext(lVal,BB);
                }
                if (rVal.getType().isI1()) {
                    rVal = builder.createZext(rVal, BB);
                }
                if (ctx.mulOp(i - 1).MUL() != null) {
                    lVal = builder.createBinary(BB,Tag.Mul, lVal, rVal);
                }
                if (ctx.mulOp(i - 1).DIV() != null) {
                    lVal = builder.createBinary(BB,Tag.Div, lVal, rVal);
                }
                if (ctx.mulOp(i - 1).MOD() != null) {
                    if (rVal instanceof Constant.ConstantInt) {
                        int num = ((Constant.ConstantInt) rVal).getValue();
                        if (Math.abs(num) == 1) {
                            lVal = builder.createBinary(BB,Tag.Mod, lVal, rVal);
                        } else if ((Math.abs(num) & (Math.abs(num) - 1)) == 0) {
                            lVal = builder.createBinary(BB,Tag.Mod, lVal, rVal);
                        } else if (num < 0) {
                            var a = builder.createBinary(BB,Tag.Div, lVal, rVal);
                            var b = builder.createBinary(BB,Tag.Mul, a,
                                    new Constant.ConstantInt(i32, Math.abs(((Constant.ConstantInt) rVal).getValue())));
                            lVal = builder.createBinary(BB,Tag.Sub, lVal, b);
                        } else if (num > 0) {
                            var a = builder.createBinary(BB,Tag.Div, lVal, rVal);
                            var b = builder.createBinary(BB,Tag.Mul, a, rVal);
                            lVal = builder.createBinary(BB,Tag.Sub, lVal, b);
                        }
                    } else {
                        var a = builder.createBinary(BB,Tag.Div, lVal, rVal);
                        var b = builder.createBinary(BB,Tag.Mul, a, rVal);
                        lVal = builder.createBinary(BB,Tag.Sub, lVal, b);
                    }
                    //lVal = f.buildBinary(Tag.Mod, lVal, rVal, BB);
                }
                curVal = lVal;
            }
        }
        return null; 
    }


    @Override public Void visitAddExp(miniSysYParser.AddExpContext ctx) {
        if(isConst){
            visit(ctx.mulExp(0));
            int tmp = nodeValue;
            for(int i=1;i<ctx.mulExp().size();i++){
                visit(ctx.mulExp(i));
                if(ctx.addOp(i-1).PLUS()!=null){
                    tmp+=nodeValue;
                }else if(ctx.addOp(i-1).MINUS()!=null){
                    tmp-=nodeValue;
                }
            }
            nodeValue = tmp;
        }else {
            visit(ctx.mulExp(0));
            Value lVal = curVal;
            for(int i=1;i<ctx.mulExp().size();i++){
                visit(ctx.mulExp(i));
                Value rVal = curVal;
                if (lVal.getType().isI1()) {
                    lVal = builder.createZext(lVal,BB);
                }else if (rVal.getType().isI1()) {
                    rVal = builder.createZext(rVal, BB);
                }else if (ctx.addOp(i - 1).PLUS() != null) {
                    lVal = builder.createBinary(BB,Tag.Add, lVal, rVal);
                }else if (ctx.addOp(i - 1).MINUS() != null) {
                    lVal = builder.createBinary(BB,Tag.Sub, lVal, rVal);
                }
            }
            curVal = lVal;
        }
        return null; 
    }
    
    @Override public Void visitRelExp(miniSysYParser.RelExpContext ctx) {
        visit(ctx.addExp(0));
        var lVal = curVal;
        for (int i = 1; i < ctx.addExp().size(); i++) {
            isRel = false;
            visit(ctx.addExp(i));
            var rVal = curVal;
            if (ctx.relOp(i - 1).LE() != null) {
                lVal = builder.createBinary(BB,Tag.Le, lVal, rVal);
            }
            if (ctx.relOp(i - 1).GE() != null) {
                lVal = builder.createBinary(BB,Tag.Ge, lVal, rVal);
            }
            if (ctx.relOp(i - 1).GT() != null) {
                lVal = builder.createBinary(BB,Tag.Gt, lVal, rVal);
            }
            if (ctx.relOp(i - 1).LT() != null) {
                lVal = builder.createBinary(BB,Tag.Lt, lVal, rVal);
            }
        }
        curVal = lVal;
        return null; 
    }
    
    @Override public Void visitEqExp(miniSysYParser.EqExpContext ctx) {
        visit(ctx.relExp(0));
        var lVal = curVal;
        for (int i = 1; i < ctx.relExp().size(); i++) {
            isRel = false;
            visit(ctx.relExp(i));
            if (ctx.eqOp(i - 1).EQ() != null) {
                lVal = builder.createBinary(BB,Tag.Eq, lVal, curVal);
            }
            if (ctx.eqOp(i - 1).NEQ() != null) {
                lVal = builder.createBinary(BB,Tag.Ne, lVal, curVal);
            }

        }
        curVal = lVal;
        return null;
    }
    
    @Override public Void visitLAndExp(miniSysYParser.LAndExpContext ctx) {
        ctx.eqExp().forEach(exp -> {
            BasicBlock newbb = new BasicBlock("LAnd_jump_"+func.getBlockNum());
            func.insertBlock(newbb);
            isRel = true;
            visit(exp);
            if (isRel) {

                curVal = builder.createBinary(BB,Tag.Ne, curVal, C0);
                isRel = false;
            }
            builder.createBr(curVal, newbb, ctx.elseBlock, BB);
            BB=newbb;
        });
        builder.createBr(ctx.ifBlock, BB);
        return null;
    }

    @Override public Void visitLOrExp(miniSysYParser.LOrExpContext ctx) {
        ctx.lAndExp(0).isFirst = true;
        for (int i = 0; i < ctx.lAndExp().size() - 1; i++) {
            BasicBlock newbb = new BasicBlock("LOr_jump_"+func.getBlockNum());
            func.insertBlock(newbb);
            ctx.lAndExp(i).ifBlock = ctx.ifBlock;
            ctx.lAndExp(i).elseBlock = newbb;
            visit(ctx.lAndExp(i));
            BB = newbb;
        }
        ctx.lAndExp(ctx.lAndExp().size() - 1).ifBlock = ctx.ifBlock;
        ctx.lAndExp(ctx.lAndExp().size() - 1).elseBlock = ctx.elseBlock;
        visit(ctx.lAndExp(ctx.lAndExp().size() - 1));
        return null; 
    }

    @Override public Void visitConstExp(miniSysYParser.ConstExpContext ctx) {
        isConst = true;
        visit(ctx.addExp());
        curVal = builder.getConstantInt(nodeValue);
        isConst = false;
        return null;
    }
}

