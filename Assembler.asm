    _numero:main_ DB ?
.CODE

START:
MOV AL, 
MOV numero_main, AL




JMP final
error_overflow:
invoke StdOut, addr ErrorOverflowSum
JMP final
division_por_cero:
invoke StdOut, addr ErrorDiv0
JMP final
error_overflow:
invoke StdOut, addr ErrorOverflowProd
final:
END START
