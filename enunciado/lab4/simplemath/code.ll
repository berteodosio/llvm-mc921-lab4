define i32 @a__init_function() {
%dcddbababacdcede = add i32 10, 0
%cafadfdcadccafda = add i32 5, 0
%dfcaffbbbeafc = sub i32 %dcddbababacdcede,%cafadfdcadccafda
%fcfccccfbe = add i32 4, 0
%baaccadaafcee = sub i32 %dfcaffbbbeafc,%fcfccccfbe
ret i32 %baaccadaafcee
}
define i32 @f(i32 %x) {
%fedccaeffc = add i32 %x, 0
%efcbfbacbaef = add i32 2, 0
%dfefccbbfefafeb = load i32, i32* @a
%ffcafbbefaffde = mul i32 %dfefccbbfefafeb,%efcbfbacbaef
%bdebdcacafe = add i32 %ffcafbbefaffde,%fedccaeffc
ret i32 %bdebdcacafe
}
define i32 @sm_main__init_function() {
%abdbfebeecd = call i32 @f(i32 5)

ret i32 %abdbfebeecd
}


; init global var declarations
@a = global i32 0;
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%dbeaeedcdfbfa = call i32 @a__init_function()
store i32 %dbeaeedcdfbfa, i32* @a
%aedddeafdcfda = call i32 @sm_main__init_function()
store i32 %aedddeafdcfda, i32* @sm_main
ret i32 0
}
