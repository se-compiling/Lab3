declare void @putch(i32)
define dso_local i32 @main() {
  %x0 = alloca i32
  store i32 1, i32*  %x0
  br label  %cond_0


cond_0:
  %x3 = load i32, i32*  %x0
  %x4 = icmp slt i32 %x3,12
  br i1 %x4,label  %LAnd_jump_3,label  %next_0 


loop_0:
  %x7 = alloca i32
  store i32 0, i32*  %x7
  br label  %cond_4


next_0:
  ret i32 0

LAnd_jump_3:
  br label  %loop_0


cond_4:
  %x10 = load i32, i32*  %x7
  %x11 = load i32, i32*  %x0
  %x12 = mul i32 2,%x11
  %x13 = sub i32 %x12,1
  %x14 = icmp slt i32 %x10,%x13
  br i1 %x14,label  %LAnd_jump_7,label  %next_4 


loop_4:
  %x17 = load i32, i32*  %x7
  %x18 = sdiv i32 %x17,3
  %x19 = mul i32 %x18,3
  %x20 = sub i32 %x17,%x19
  %x21 = icmp eq i32 %x20,1
  br i1 %x21,label  %LAnd_jump_11,label  %else_10 


next_4:
  call void @putch(i32 10)
  %x33 = load i32, i32*  %x0
  %x34 = add i32 %x33,1
  store i32 %x34, i32*  %x0

LAnd_jump_7:
  br label  %loop_4


if_then_8:
  %x24 = add i32 48,1
  call void @putch(i32 %x24)
  br label  %next_9


next_9:
  %x29 = load i32, i32*  %x7
  %x30 = add i32 %x29,1
  store i32 %x30, i32*  %x7

else_10:
  call void @putch(i32 48)
  br label  %next_9


LAnd_jump_11:
  br label  %if_then_8


}
