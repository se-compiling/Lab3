package Values;


import Types.FunctionType;
import Types.Type;

import java.util.ArrayList;

public class Function extends Value{
    private boolean isSysY;
    private boolean isDef= false;
    private BasicBlock mainBlock;

    public boolean isDef() {
        return isDef;
    }

    public void setDef(boolean def) {
        isDef = def;
    }

    private FunctionType funcTy;
    private int blockNum=0;
    private ArrayList<BasicBlock> blocks = new ArrayList<>();

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public Function(String name, Type type, boolean isSysY) {
        super(name, type);
        setFuncTy();
        this.isSysY=isSysY;
    }

    public boolean isSysY() {
        return isSysY;
    }

    public void setSysY(boolean sysY) {
        isSysY = sysY;
    }

    public BasicBlock getMainBlock() {
        return mainBlock;
    }

    public void setMainBlock(BasicBlock mainBlock) {
        this.mainBlock = mainBlock;
    }

    public FunctionType getFuncTy() {
        return funcTy;
    }

    public void setFuncTy() {
        this.funcTy = (FunctionType) this.type;
    }
    public void insertBlock(BasicBlock bb){
        this.blocks.add(bb);
        this.blockNum++;
    }
    @Override
    public String toString() {
        String str ="define dso_local "+
                this.funcTy.typeToString()+
                " @"+this.name+"("+
                this.funcTy.paramsToString()+
                ")"+" {\n"+mainBlock.toString()+"\n";
        if(!this.blocks.isEmpty()){
            for(var block:blocks){
                str+=block.getName()+":\n"+block.toString()+"\n";
            }
        }
        return str+"}\n";
    }
}
