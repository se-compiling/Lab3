package Values.Inst;

import Types.PointerType;
import Types.Type;
import Values.Value;

public class MemInst extends Inst{

    public MemInst(Tag tag, Type type, int OP_num) {
        super(tag, type, OP_num);
    }

    public static class AllocaInst extends MemInst{
        private Type allocaType;

        public AllocaInst( Type type) {
            super(Tag.Alloca, new PointerType(type), 0);
            this.allocaType=type;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("  "+this.getName());
            sb.append(" = alloca ");
            sb.append(allocaType);
            return sb.toString();
        }
    }

    public static class LoadInst extends MemInst{

        public LoadInst( Type type,Value value) {
            super(Tag.Load, type, 1);
            this.setOperand(value,0);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("  "+this.getName());
            sb.append(" = load ");
            sb.append(this.getType());
            sb.append(", ");
            sb.append(this.getOperand(0).getType());
            sb.append(" ");
            sb.append(this.getOperand(0).getName());
            return sb.toString();
        }
    }

    public static class StoreInst extends MemInst{

        public StoreInst(Value value, Value pointer) {
            super(Tag.Store, Type.VoidType.getVoidTy(), 2);
            setOperand(value,0);
            setOperand(pointer,1);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Value lVal = this.getOperand(0);
            Value rVal = this.getOperand(1);
            sb.append("  store " + lVal.getType().toString() + " " + lVal.getName() + ", ");
            sb.append(rVal.getType().toString() + " " + rVal.getName());
            return sb.toString();
        }
    }

    public static class ZextInst extends MemInst{
        private Type dest;
        public ZextInst(Type type,Value val ) {
            super(Tag.Zext, type, 1);
            dest = type;
            this.setOperand( val,0);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("  "+this.getName());
            sb.append(" = zext i1 ");
            sb.append(this.getOperand(0).getName());
            sb.append(" to i32");;
            return sb.toString();
        }
    }
}
