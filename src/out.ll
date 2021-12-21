declare void @putch(i32)
define dso_local i32 @main() {
  %x0 = alloca i32
  store i32 1, i32*  %x0
  br label  %cond_0


cond_0:
  %x3 = load i32, i32*  %x0
  %x4 = icmp slt i32 %x3,12
  br i1 %x4,label  %LAnd_jump_3,label  %next_2 


loop_1:
  %x7 = alloca i32
  store i32 0, i32*  %x7
  br label  %cond_4


next_2:
  ret i32 0

LAnd_jump_3:
  br label  %loop_1


cond_4:
  %x10 = icmp eq i32 1,1
  br i1 %x10,label  %LAnd_jump_7,label  %next_6 


loop_5:
  %x13 = load i32, i32*  %x7
  %x14 = sdiv i32 %x13,3
  %x15 = mul i32 %x14,3
  %x16 = sub i32 %x13,%x15
  %x17 = icmp eq i32 %x16,1
  br i1 %x17,label  %LAnd_jump_11,label  %else_10 


next_6:
  call void @putch(i32 10)
  %x39 = load i32, i32*  %x0
  %x40 = add i32 %x39,1
  store i32 %x40, i32*  %x0
  br label  %continue_16


LAnd_jump_7:
  br label  %loop_5


if_then_8:
  %x20 = add i32 48,1
  call void @putch(i32 %x20)
  br label  %next_9


next_9:
  %x25 = load i32, i32*  %x7
  %x26 = add i32 %x25,1
  store i32 %x26, i32*  %x7
  %x28 = load i32, i32*  %x7
  %x29 = load i32, i32*  %x0
  %x30 = mul i32 2,%x29
  %x31 = sub i32 %x30,1
  %x32 = icmp sge i32 %x28,%x31
  br i1 %x32,label  %LAnd_jump_14,label  %next_13 


else_10:
  call void @putch(i32 48)
  br label  %next_9


LAnd_jump_11:
  br label  %if_then_8


if_then_12:
  br label  %break_15


next_13:
  br label  %cond_4


LAnd_jump_14:
  br label  %if_then_12


break_15:
  br label  %next_6


continue_16:
  br label  %cond_4


}
