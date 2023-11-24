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

    _y_main_ DD ?
    _z_main_ DD ?
    _x_main_ DD ?
    __34E38 DD 3.4E+38
    @aux1 DD ?
.CODE

START:
FLD __34E38
FSTP _x_main_


FLD __34E38
FSTP _y_main_


FLD _x_main_
FLD _y_main_
FMUL
FSTP @aux1
FSTSW aux_mem_2bytes
SAHF
JC ErrorOverflowProd

FLD @aux1
FSTP _z_main_




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
