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
   _ErrorRec DB "Error recursion en funcion", 0
   _ErrorOverflowProd DB "Error por Overflow en un producto", 0

    _x_main_ DB ?
    _y_func_main_ DB ?
    Cadena0 DB "HOLA", 0
    Cadena1 DB "CHAU", 0
  ; constante para cada funcion a partir de su hashcode
   __func_main__ DD -159861164
.CODE
__func_main:
MOV AL, _y_func_main_
MOV AH, 4
CMP AL, AH
JAE etiqueta1
invoke MessageBox, NULL, addr Cadena0, addr Cadena0, MB_OK
invoke ExitProcess, 0
JMP etiqueta2
etiqueta1: 
invoke MessageBox, NULL, addr Cadena1, addr Cadena1, MB_OK
invoke ExitProcess, 0
etiqueta2: 
MOV __funcion_actual__, 0
RET


START:
MOV AL, 4
MOV _x_main_, AL



MOV EAX, __func_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV _y_func_main_, 4
call __func_main




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
