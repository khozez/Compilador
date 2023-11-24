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

    _x:main_ DB ?
    _y:main_ DD ?
    _z:main_ DD ?
    @aux1_ DD ?
    @aux3_ DD ?
    @aux4_ DD ?
    @aux6_ DD ?
.CODE

START:
MOV AL, 4
MOV _x:main, AL


FLD 4
FSTP _y:main

FILD _x:main
FSTP @aux1
FLD aux1
FLD 4.0
FADD
FSTP @aux3

FLD @aux3
FSTP _z:main

FILD _y:main
FSTP @aux4
FLD z:main
FLD aux4
FSUB
FSTP @aux6

FLD @aux6
FSTP _z:main



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
