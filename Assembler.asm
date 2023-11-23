; .386
; .MODEL flat, stdcall
; option casemap :none
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C :VARARG

.STACK 200h
.DATA
    aux_mem_2bytes DW ?
    funcion_actual DD 0
   ErrorOverflowSum DB "Error por Overflow en una suma", 10, 0
   ErrorDiv0 DB "Error Division por Cero en ejecucion", 10, 0
   ErrorOverflowProd DB "Error por Overflow en un producto", 10, 0

; constante para cada funcion a partir de su hashcode
f1_main DD 1824968941
    _y:main_ DB ?
    _x:main_ DB ?
.CODE
_f1_main:
MOV AL, 1_s
MOV x_main, AL
MOV AL, 1_s
MOV y_main, AL

 END

START:
MOV AL, 4_s
MOV x_main, AL


MOV AL, 8_s
MOV y_main, AL





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
