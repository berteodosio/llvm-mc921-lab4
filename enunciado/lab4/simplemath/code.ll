define i32 @a__init_function() {
%edabdcdbbdced = add i32 1, 0
ret i32 %edabdcdbbdced
}
define i32 @b__init_function() {
%ecdbaecbdefaba = add i32 1, 0
ret i32 %ecdbaecbdefaba
}
define i32 @c__init_function() {
%aefcbaabfaecccacd = load i32, i32* @b
%cfbfaccaabbfccbbb = load i32, i32* @a
%baadee = add i32 %cfbfaccaabbfccbbb,%aefcbaabfaecccacd
ret i32 %baadee
}
define i32 @d__init_function() {
%adccdaabebdd = load i32, i32* @c
%babaafdcecc = load i32, i32* @b
%beceffeefdaef = add i32 %babaafdcecc,%adccdaabebdd
ret i32 %beceffeefdaef
}
define i32 @e__init_function() {
%fcbcccdccbdada = load i32, i32* @d
%edebbfbbccee = load i32, i32* @c
%fdacffabcaacafae = add i32 %edebbfbbccee,%fcbcccdccbdada
ret i32 %fdacffabcaacafae
}
define i32 @f__init_function() {
%cbabfadcacc = load i32, i32* @a
%eafeddeffeadab = load i32, i32* @b
%baacbdbfcefcdd = add i32 %eafeddeffeadab,%cbabfadcacc
%dbefd = load i32, i32* @c
%fbbafeafecefc = add i32 %dbefd,%baacbdbfcefcdd
%eeffafeded = load i32, i32* @d
%dbaeceddae = add i32 %eeffafeded,%fbbafeafecefc
%ddcadadccefd = load i32, i32* @e
%aefdeafcbddd = add i32 %ddcadadccefd,%dbaeceddae
ret i32 %aefdeafcbddd
}
define i32 @g(i32 %a, i32 %b, i32 %c, i32 %d) {
%cbcbaafacbea = add i32 %a, 0
%facebdbcb = add i32 %b, 0
%eecbadcddabee = add i32 %facebdbcb,%cbcbaafacbea
%eedcffbfc = add i32 %c, 0
%bcceabfbbdd = add i32 %eedcffbfc,%eecbadcddabee
%cbeaaceacdefdbf = add i32 %d, 0
%cddaebabaaab = add i32 %cbeaaceacdefdbf,%bcceabfbbdd
ret i32 %cddaebabaaab
}
define i32 @sm_main__init_function() {
%dcbebafbdd = call i32 @g(i32 2,i32 3,i32 4,i32 5)

%dfccdecdbbcfbcbed = load i32, i32* @f
%efdbdacccdfbde = add i32 2, 0
%efaebccbbccace = mul i32 %efdbdacccdfbde,%dfccdecdbbcfbcbed
%adfbefebdfcead = sub i32 %dcbebafbdd,%efaebccbbccace
ret i32 %adfbefebdfcead
}


; init global var declarations
@a = global i32 0;
@b = global i32 0;
@c = global i32 0;
@d = global i32 0;
@e = global i32 0;
@f = global i32 0;
@sm_main = global i32 0;
; end global var declarations

define i32 @batata() {
%bafbbecdaceb = call i32 @a__init_function()
store i32 %bafbbecdaceb, i32* @a
%cebffccbfec = call i32 @b__init_function()
store i32 %cebffccbfec, i32* @b
%ffbaddebda = call i32 @c__init_function()
store i32 %ffbaddebda, i32* @c
%adefaeeeaffcaaadc = call i32 @d__init_function()
store i32 %adefaeeeaffcaaadc, i32* @d
%babadbaeace = call i32 @e__init_function()
store i32 %babadbaeace, i32* @e
%dfdfeadbcb = call i32 @f__init_function()
store i32 %dfdfeadbcb, i32* @f
%addbeefdbcfefe = call i32 @sm_main__init_function()
store i32 %addbeefdbcfefe, i32* @sm_main
ret i32 0
}
