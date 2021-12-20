declare i32 @getint()
declare i32 @getch()
declare void @putint(i32)
declare void @putch(i32)
define dso_local i32 @main() {
  %x0 = alloca i32
  %x3 = alloca i32
  %x6 = alloca i32
  %x9 = alloca i32
  %x11 = alloca i32
  %x1 = call i32 @getint()
  store i32 %x1, i32*  %x0
  %x4 = call i32 @getint()
  store i32 %x4, i32*  %x3
  %x7 = call i32 @getint()
  store i32 %x7, i32*  %x6
  store i32 4, i32*  %x9
  %x12 = call i32 @getch()
  store i32 %x12, i32*  %x11
  %x14 = load i32, i32*  %x11
  call void @putint(i32 %x14)
  call void @putch(i32 10)
  %x17 = load i32, i32*  %x0
  %x18 = load i32, i32*  %x3
  %x19 = add i32 %x17,%x18
  %x20 = load i32, i32*  %x6
  %x21 = add i32 %x19,%x20
  %x22 = load i32, i32*  %x9
  %x23 = sub i32 %x21,%x22
  call void @putint(i32 %x23)
  ret i32 0

}
