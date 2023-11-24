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
    _y:cb:main_ DD ?
    _h:f2:cb:main_ DD ?
    _x:ca:main_ DB ?
    _y:ca:main_ DD ?
    _h:f2:cb:ca:main_ DD ?
.CODE
PUBLIC
__f2_cb_main PROC
FLD _h:f2:cb:main
FSTP y_cb_main
MOV funcion_actual, 0
RET
__f2_cb_main ENDP

PUBLIC
__f1_ca_main PROC
MOV AL, 1_s
MOV x_ca_main, AL
MOV funcion_actual, 0
RET
__f1_ca_main ENDP


START:

MOV EAX, __f2_cb_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
FLD 4.0
FSTP _h:f2:cb:main
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
