define i32 @a__init_function() {
%efadbbbcafcabdfc = add i32 1, 0
ret i32 %efadbbbcafcabdfc
}
define i32 @b__init_function() {
%bbaceffcdcfb = add i32 1, 0
ret i32 %bbaceffcdcfb
}
define i32 @c__init_function() {
%feeafaccdbdfcfa = load i32, i32* @b
%eadafceea = load i32, i32* @a
%abbbffaaabbf = add i32 %eadafceea,%feeafaccdbdfcfa
ret i32 %abbbffaaabbf
}
define i32 @d__init_function() {
%eaeefbbfa = load i32, i32* @c
%cdbdd = load i32, i32* @b
%efcffefaadaa = add i32 %cdbdd,%eaeefbbfa
ret i32 %efcffefaadaa
}
define i32 @e__init_function() {
%cdeeeadcdbbcbf = load i32, i32* @d
%faaacfcfaebe = load i32, i32* @c
%beeedfffffecebc = add i32 %faaacfcfaebe,%cdeeeadcdbbcbf
ret i32 %beeedfffffecebc
}
define i32 @f__init_function() {
%eadadfcbe = load i32, i32* @a
%ddcbfebbbecbcda = load i32, i32* @b
%adfbaadded = add i32 %ddcbfebbbecbcda,%eadadfcbe
%fafecffafbebcabef = load i32, i32* @c
%ccabebabaacb = add i32 %fafecffafbebcabef,%adfbaadded
%afabbcfb = load i32, i32* @d
%bbdbdbae = add i32 %afabbcfb,%ccabebabaacb
%bddbeaadcdcb = load i32, i32* @e
%bddbbcddeee = add i32 %bddbeaadcdcb,%bbdbdbae
ret i32 %bddbbcddeee
}
define i32 @g(i32 %a, i32 %b, i32 %c, i32 %d) {
%fdaebecfddac = add i32 %a, 0
%abcdefdebaadc = add i32 %b, 0
%eeffaafceef = add i32 %abcdefdebaadc,%fdaebecfddac
%ffdffadaceffa = add i32 %c, 0
%daaeadffdb = add i32 %ffdffadaceffa,%eeffaafceef
%badcbbfbeabcd = add i32 %d, 0
%adebcafeffd = add i32 %badcbbfbeabcd,%daaeadffdb
ret i32 %adebcafeffd
}
define i32 @sm_main__init_function() {
%cbecfaceffbf = call i32 @g(i32 2,i32 3,i32 4,i32 5)

%faeeaed = load i32, i32* @f
%bcafbafcaedbc = add i32 2, 0
%acefebdaeaa = mul i32 %bcafbafcaedbc,%faeeaed
%ebecedefdea = sub i32 %cbecfaceffbf,%acefebdaeaa
ret i32 %ebecedefdea
}


; init global var declarations
@a = global i32 0;
@b = global i32 0;
@c = global i32 0;
@d = global i32 0;
@sm_main = global i32 0;
@e = global i32 0;
@f = global i32 0;
; end global var declarations

define i32 @batata() {
%aaefbeadfdabb = call i32 @a__init_function()
store i32 %aaefbeadfdabb, i32* @a
%aeaddbdbebbdbf = call i32 @b__init_function()
store i32 %aeaddbdbebbdbf, i32* @b
%beebfcebd = call i32 @c__init_function()
store i32 %beebfcebd, i32* @c
%cfecdfcddcaee = call i32 @d__init_function()
store i32 %cfecdfcddcaee, i32* @d
%fbcfdb = call i32 @sm_main__init_function()
store i32 %fbcfdb, i32* @sm_main
%dcbfcefdfeb = call i32 @e__init_function()
store i32 %dcbfcefdfeb, i32* @e
%ddaeebaabeb = call i32 @f__init_function()
store i32 %ddaeebaabeb, i32* @f
ret i32 0
}
