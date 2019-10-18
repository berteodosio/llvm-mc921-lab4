define i32 @sm_main__init_function() {
%aafffaecd = add i32 30, 0
%debedeaddcedaba = add i32 12, 0
%bffefaefeacdddc = add i32 %debedeaddcedaba,%aafffaecd
%fbdcbbff = add i32 15, 0
%ceecdeaafdbbbc = add i32 2, 0
%fddeecac = sub i32 %fbdcbbff,%ceecdeaafdbbbc
%dceafcbbfdbabfedabe = add i32 3, 0
%baceaaefbaaeeed = sdiv i32 %fddeecac,%dceafcbbfdbabfedabe
%bdabfdabeeafed = add i32 7, 0
%eeaeaef = add i32 %bdabfdabeeafed,%baceaaefbaaeeed
%ccfbcbfd = add i32 2, 0
%fcbafdacebec = sub i32 %eeaeaef,%ccfbcbfd
%cefdbebfabecacd = mul i32 %fcbafdacebec,%bffefaefeacdddc
%dbeaeccbceaeec = add i32 77, 0
%fdfbaaeebeaaf = add i32 0, 0
%cdcdfcccabdcbcba = add i32 1, 0
%fcdafaebebafee = sub i32 %fdfbaaeebeaaf,%cdcdfcccabdcbcba
%dbfeefccceab = mul i32 %fcdafaebebafee,%dbeaeccbceaeec
%ccbcaffe = sub i32 %cefdbebfabecacd,%dbfeefccceab
%aebeacfabcefa = add i32 42, 0
%adbfcfeaeecdeedf = sub i32 %ccbcaffe,%aebeacfabcefa
ret i32 %adbfcfeaeecdeedf
}


; init global var declarations
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%cefdecfecbdaf = call i32 @sm_main__init_function()
store i32 %cefdecfecbdaf, i32* @sm_main
ret i32 0
}
