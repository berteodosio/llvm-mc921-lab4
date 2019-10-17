define i32 @sm_main__init_function() {
%beabbbcfabcdfde = add i32 5, 0
ret i32 %beabbbcfabcdfde
}


; init global var declarations
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%bbcfeaefeadf = call i32 @sm_main__init_function()
store i32 %bbcfeaefeadf, i32* @sm_main
ret i32 0
}
