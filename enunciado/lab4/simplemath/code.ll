define i32 @sm_main__init_function() {
%cadfaeeebadc = add i32 5, 0
ret i32 %cadfaeeebadc
}


; init global var declarations
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%eacbffbbaaddfa = call i32 @sm_main__init_function()
store i32 %eacbffbbaaddfa, i32* @sm_main
ret i32 0
}
