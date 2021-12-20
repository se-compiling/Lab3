package Values.Inst;

import Types.IntegerType;
import Types.Type;
import Values.Value;

public class BinaryInst extends Inst{
    public BinaryInst(Tag tag, Type type, Value l,Value r) {
        super(tag, type, 2);
        if(this.isLogicalBinary()){
            this.setType(IntegerType.getI1());
        }
        if(this.isArithmeticBinary()){
            this.setType(IntegerType.getI32());
        }
        this.setOperand(l,0);
        this.setOperand(r,1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  "+this.getName() + " = ");
        sb.append(
                switch (this.tag) {
                    case Add -> "add i32 ";
                    case Sub -> "sub i32 ";
                    case Mul -> "mul i32 ";
                    case Div -> "sdiv i32 ";
                    case Mod -> "srem i32 ";
                    case Lt -> "icmp slt " + this.getOperand(0).getType() + " ";
                    case Le -> "icmp sle " + this.getOperand(0).getType() + " ";
                    case Ge -> "icmp sge " + this.getOperand(0).getType() + " ";
                    case Gt -> "icmp sgt " + this.getOperand(0).getType() + " ";
                    case Eq -> "icmp eq " + this.getOperand(0).getType() + " ";
                    case Ne -> "icmp ne  " + this.getOperand(0).getType() + " ";
                    default -> "";
                }
        );
        sb.append(this.getOperand(0).getName() + "," + this.getOperand(1).getName());
        return sb.toString();
    }
}
