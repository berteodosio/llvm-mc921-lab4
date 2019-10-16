	.section	__TEXT,__text,regular,pure_instructions
	.macosx_version_min 10, 14
	.globl	_sm_main__init_function ## -- Begin function sm_main__init_function
	.p2align	4, 0x90
_sm_main__init_function:                ## @sm_main__init_function
	.cfi_startproc
## %bb.0:
	movl	$5, %eax
	retq
	.cfi_endproc
                                        ## -- End function
	.globl	_batata                 ## -- Begin function batata
	.p2align	4, 0x90
_batata:                                ## @batata
	.cfi_startproc
## %bb.0:
	pushq	%rax
	.cfi_def_cfa_offset 16
	callq	_sm_main__init_function
	movl	%eax, _sm_main(%rip)
	xorl	%eax, %eax
	popq	%rcx
	retq
	.cfi_endproc
                                        ## -- End function
	.globl	_sm_main                ## @sm_main
.zerofill __DATA,__common,_sm_main,4,2

.subsections_via_symbols
