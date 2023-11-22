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
    _numero:main_ DB ?
.CODE

START:
MOV AL, 4_s
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
