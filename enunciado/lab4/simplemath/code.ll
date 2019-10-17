define i32 @a(i32 %x) {
%bfbbeecfcdde = add i32 %x, 0
ret i32 %bfbbeecfcdde
}
define i32 @sm_main__init_function() {
%fcacadefffcfdade = call i32 @a(i32 10)

ret i32 %fcacadefffcfdade
}


; init global var declarations
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%beeadaacefeb = call i32 @sm_main__init_function()
store i32 %beeadaacefeb, i32* @sm_main
ret i32 0
}
