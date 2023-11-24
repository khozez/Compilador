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

    _y:main_ DD ?
    @aux1_ DD ?
.CODE

START:
FILD 4_s
FSTP @aux1
FLD 
FSTP y_main



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
