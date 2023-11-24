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
   _ErrorOverflowSum DB "Error por Overflow en una suma", 0
   _ErrorDiv0 DB "Error Division por Cero en ejecucion", 0
   _ErrorOverflowProd DB "Error por Overflow en un producto", 0

    _y_main_ DB ?
    _z_main_ DB ?
    _x_main_ DB ?
    @aux1 DB ?
.CODE

START:
MOV AL, 4
MOV _x_main_, AL


MOV AL, 0
MOV _y_main_, AL


MOV AH, 0
MOV AL, _x_main_
MOV BL, _y_main_
CMP BL, 0
JE ErrorDiv0
DIV BL
MOV @aux1, AL

MOV AL, @aux1
MOV _z_main_, AL




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
final:
END START
