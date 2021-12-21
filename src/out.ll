declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
define dso_local i32 @main() {
  %x0 = alloca i32
  %x3 = alloca i32
  %x5 = alloca i32
  %x1 = call i32 @getint()
  store i32 %x1, i32*  %x0
  store i32 0, i32*  %x3
  store i32 0, i32*  %x5
  br label  %cond_0


cond_0:
  %x8 = load i32, i32*  %x3
  %x9 = load i32, i32*  %x0
  %x10 = icmp slt i32 %x8,%x9
  br i1 %x10,label  %LAnd_jump_3,label  %next_2 


loop_1:
  %x13 = load i32, i32*  %x3
  %x14 = srem i32 %x13,2
  %x15 = icmp eq i32 %x14,0
  br i1 %x15,label  %LAnd_jump_6,label  %next_5 


next_2:
  ret i32 0

LAnd_jump_3:
  br label  %loop_1


if_then_4:
  %x18 = load i32, i32*  %x3
  %x19 = add i32 %x18,1
  store i32 %x19, i32*  %x3
  br label  %continue_7

  br label  %next_5


next_5:
  %x23 = load i32, i32*  %x3
  %x24 = add i32 %x23,1
  store i32 %x24, i32*  %x3
  %x26 = load i32, i32*  %x5
  %x27 = load i32, i32*  %x3
  %x28 = add i32 %x26,%x27
  store i32 %x28, i32*  %x5
  %x30 = load i32, i32*  %x5
  call void @putint(i32 %x30)
  call void @putch(i32 10)
  br label  %cond_0


LAnd_jump_6:
  br label  %if_then_4


continue_7:

}
