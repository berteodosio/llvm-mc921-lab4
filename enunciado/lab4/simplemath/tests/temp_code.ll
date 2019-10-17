define i32 @sm_main__init_function() {
    %ebadbbaceefae = i32 5
    ret i32 %ebadbbaceefae
}


; init global var declarations
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%bfbbffe = call i32 @sm_main__init_function()
store i32 %bfbbffe, i32* @sm_main
ret i32 0
}