.386
.MODEL flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib

.STACK 200h
.DATA
   aux_mem_2bytes DW ?
    __funcion_actual__ DD 0
   ErrorOverflowSum DB "Error por Overflow en una suma", 0
   ErrorDiv0 DB "Error Division por Cero en ejecucion", 0
   ErrorOverflowProd DB "Error por Overflow en un producto", 0

    _x_a:ca:main_ DB ?
    _x_a:cb:main_ DB ?
    _y:cb:main_ DB ?
    @aux1_ DB ?
  ; constante para cada funcion a partir de su hashcode
   __aumentar_ca_main__ DD -679688194
   __aumentar_cb_main__ DD -651059043
.CODE
__aumentar_ca_main:
MOV AL, _x_a:ca:main
MOV BL, 1
ADD AL, BL
MOV @aux1, AL
JO error_overflow
MOV AL, @aux1
MOV _x_a:ca:main, AL
MOV funcion_actual, 0
RET

__aumentar_cb_main:
MOV AL, 1
MOV _y:cb:main, AL
MOV funcion_actual, 0
RET


START:

MOV EAX, __aumentar_cb_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __aumentar_cb_main




JMP final
error_overflow:
invoke MessageBox, NULL, addr ErrorOverflowSum, addr ErrorOverflowSum, MB_OK
invoke ExitProcess, 0
JMP final
division_por_cero:
invoke MessageBox, NULL, addr ErrorDiv, addr ErrorDiv, MB_OK
invoke ExitProcess, 0
JMP final
error_overflow:
invoke MessageBox, NULL, addr ErrorOverflowProd, addr ErrorOverflowProd, MB_OK
invoke ExitProcess, 0
final:
END START
