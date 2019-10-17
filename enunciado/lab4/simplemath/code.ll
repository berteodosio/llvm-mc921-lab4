define i32 @sm_main__init_function() {
%eddbcedbdeb = i32 5
ret i32 %eddbcedbdeb
}


; init global var declarations
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%bdcdbaaeafbd = call i32 @sm_main__init_function()
store i32 %bdcdbaaeafbd, i32* @sm_main
ret i32 0
}
