.386
.MODEL flat, stdcall
option casemap :none
include C:\masm32\include\windows.inc
include C:\masm32\include\kernel32.inc
include C:\masm32\include\masm32.inc
include C:\masm32\include\user32.inc
includelib C:\masm32\lib\kernel32.lib
includelib C:\masm32\lib\masm32.lib
includelib C:\masm32\lib\user32.lib

.STACK 200h
.DATA
   aux_mem_2bytes DW ?
    __funcion_actual__ DD 0
    __aux_conv DW ?
   _ErrorOverflowSum DB "Error por Overflow en una suma", 0
   _ErrorDiv0 DB "Error Division por Cero en ejecucion", 0
   _ErrorRec DB "Error recursion en funcion", 0
   _ErrorOverflowProd DB "Error por Overflow en un producto", 0

    _w_main_ DB ?
    _z_main_ DB ?
    _y_main_ DB ?
    _x_main_ DB ?
    _j_main_ DD ?
    _i_main_ DD ?
    __23 DD 2.3
    @aux1 DB ?
.CODE

START:
MOV AL, 5
MOV _x_main_, AL


MOV _y_main_, 5

FLD __23
FSTP _i_main_


FLD _i_main_
FSTP _j_main_


MOV AL, 7
MOV _x_main_, AL


MOV _z_main_, 7

MOV AL, 7
MOV BL, 5
ADD AL, BL
MOV @aux1, AL
JO ErrorOverflowSum

MOV AL, @aux1
MOV _x_main_, AL


MOV AL, _x_main_
MOV _w_main_, AL




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
