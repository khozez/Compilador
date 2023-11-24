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

    _z:main_ DB ?
    _x:main_ DB ?
    Cadena0 DB "X ES 3", 0
    @aux1_ DB ?
    @aux2_ DB ?
.CODE

START:
MOV AL, 4
MOV _x:main, AL


MOV AL, _x:main
MOV BL, 1
ADD AL, BL
MOV @aux1, AL
JO error_overflow

MOV AL, @aux1
MOV _z:main, AL

MOV AL, _x:main
MOV BL, 1
SUB AL, BL
MOV @aux2

MOV AL, @aux2
MOV _x:main, AL



MOV AL, x_main
MOV AH, 3_s
CMP AL, AH
JNE etiqueta1

invoke MessageBox, NULL, addr Cadena0, addr Cadena0, MB_OK
invoke ExitProcess, 0

JMP etiqueta2
etiqueta1: 

etiqueta2: 






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
