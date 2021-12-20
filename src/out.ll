declare void @putint(i32)
define dso_local i32 @main() {
  %x0 = alloca i32
  %x1 = alloca i32
  store i32 56, i32*  %x0
  store i32 4, i32*  %x1
  %x4 = load i32, i32*  %x0
  %x5 = sub i32 0,4
  %x6 = sub i32 %x4,%x5
  %x7 = load i32, i32*  %x1
  %x8 = add i32 %x6,%x7
  store i32 %x8, i32*  %x0
  %x10 = load i32, i32*  %x0
  %x11 = icmp eq i32 %x10,0
  %x12 = zext i1 %x11 to i32
  %x13 = icmp eq i1 %x11,0
  %x14 = zext i1 %x13 to i32
  %x15 = icmp eq i1 %x13,0
  %x16 = zext i1 %x15 to i32
  %x17 = zext i1 %x15 to i32
  %x18 = sub i32 0,%x17
  %x19 = icmp ne  i32 %x18,0
  br i1 %x19,label  %LAnd_jump_3,label  %else_2 


if_then_0:
  %x22 = sub i32 0,1
  %x23 = sub i32 0,%x22
  %x24 = sub i32 0,%x23
  store i32 %x24, i32*  %x0
  br label  %next_1


next_1:
  %x31 = load i32, i32*  %x0
  call void @putint(i32 %x31)
  ret i32 0

else_2:
  %x27 = load i32, i32*  %x1
  %x28 = add i32 0,%x27
  store i32 %x28, i32*  %x0
  br label  %next_1


LAnd_jump_3:
  br label  %if_then_0


}
