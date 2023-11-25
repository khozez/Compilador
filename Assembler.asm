.386
.MODEL flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\user32.lib

.STACK 200h
.DATA
   aux_mem_2bytes DW ?
    __funcion_actual__ DD 0
    __aux_conv DW ?
   _ErrorOverflowSum DB "Error por Overflow en una suma", 0
   _ErrorDiv0 DB "Error Division por Cero en ejecucion", 0
   _ErrorRec DB "Error recursion en funcion", 0
   _ErrorOverflowProd DB "Error por Overflow en un producto", 0

    _z_main_ DB ?
    _x_main_ DB ?
    _q_main_ DD ?
    _p_main_ DD ?
    @aux1 DD ?
.CODE

START:
MOV AL, 120
MOV _x_main_, AL


MOV AL, 0
MOV _z_main_, AL


MOV EAX, 4294967295
MOV _p_main_, EAX


MOV EAX, 1
MOV _q_main_, EAX


MOV EAX, _p_main_
MOV EBX, 4294967295
ADD EAX, EBX
MOV @aux1, EAX
JC ErrorOverflowSum

MOV EAX, @aux1
MOV _q_main_, EAX




JMP final
ErrorOverflowSum:
invoke MessageBox, NULL, addr _ErrorOverflowSum, addr _ErrorOverflowSum, MB_OK
invoke ExitProcess, 0
JMP final
ErrorDiv0:
invoke MessageBox, NULL, addr _ErrorDiv0, addr _ErrorDiv0, MB_OK
invoke ExitProcess, 0
JMP final
ErrorOverflowProd:
invoke MessageBox, NULL, addr _ErrorOverflowProd, addr _ErrorOverflowProd, MB_OK
invoke ExitProcess, 0
error_recursion:
invoke MessageBox, NULL, addr _ErrorRec, addr _ErrorRec, MB_OK
final:
END START
