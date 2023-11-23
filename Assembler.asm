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
    __funcion_actual__ DD 0
   ErrorOverflowSum DB "Error por Overflow en una suma", 10, 0
   ErrorDiv0 DB "Error Division por Cero en ejecucion", 10, 0
   ErrorOverflowProd DB "Error por Overflow en un producto", 10, 0

; constante para cada funcion a partir de su hashcode
f2_cb_main DD -119407386
f1_ca_main DD 1659417926
    _y:cb:main_ DB ?
    _h:f2:cb:main_ DB ?
    _x:ca:main_ DB ?
    _y:ca:main_ DB ?
    _h:f2:cb:ca:main_ DB ?
.CODE
__f2_cb_main:
MOV AL, 
MOV y_cb_main, AL
MOV funcion_actual, 0
RET
__f1_ca_main:
MOV AL, 1_s
MOV x_ca_main, AL
MOV funcion_actual, 0
RET

START:

MOV EAX, __f2_cb_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV AL, 4_s
call __f2_cb_main




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
