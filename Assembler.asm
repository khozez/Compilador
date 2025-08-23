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

    _z_main_ DB ?
    _y_main_ DB ?
    _x_main_ DB ?
    @aux1 DB ?
    @aux2 DB ?
    @aux3 DB ?
.CODE

START:
MOV AL, 10
MOV _x_main_, AL


MOV AL, 5
MOV _y_main_, AL


MOV AL, 10
MOV BL, 5
ADD AL, BL
MOV @aux1, AL
JO ErrorOverflowSum

MOV AL, @aux1
MOV _z_main_, AL


MOV AL, _z_main_
MOV BL, 2
ADD AL, BL
MOV @aux2, AL
JO ErrorOverflowSum

MOV AL, @aux2
MOV _z_main_, AL


MOV AL, 6
MOV _y_main_, AL


MOV AL, _z_main_
MOV BL, 6
SUB AL, BL
MOV @aux3, AL

MOV AL, @aux3
MOV _x_main_, AL




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
