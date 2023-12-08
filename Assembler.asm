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

    _x_a_main_ DB ?
    _y_set_a_main_ DB ?
    Cadena0 DB "SE AUMENTA EN 1", 0
    _x_b_main_ DB ?
    _y_b_main_ DB ?
    _c2_b_main_ DD ?
    _y_set_b_main_ DB ?
    Cadena1 DB "SE AUMENTA EN 3", 0
    _y_chequear_b_main_ DB ?
    Cadena2 DB "SUMA 1", 0
    Cadena3 DB "SUMA 2", 0
    Cadena4 DB "SUMA 3", 0
    Cadena5 DB "SUMA 4", 0
    Cadena6 DB "SUMA 5", 0
    _b1_c_main_ DD ?
    _i_c_main_ DB ?
    Cadena7 DB "FINALIZA LLAMADO A C", 0
    _z_main_ DB ?
    _y_main_ DB ?
    _x_main_ DB ?
    _f_main_ DD ?
    Cadena8 DB "SUMA CERO", 0
    Cadena9 DB "NO FUNCIONA SUMA CON SIGNO", 0
    __750 DD 75.0
    Cadena10 DB "F IGUAL A 75.0", 0
    __20 DD 2.0
    __375 DD 37.5
    Cadena11 DB "F IGUAL A 37.5", 0
    @aux1 DB ?
    @aux2 DB ?
    @aux3 DB ?
    @aux4 DB ?
    @aux5 DB ?
    @aux6 DB ?
    @aux7 DD ?
    @aux9 DD ?
  ; constante para cada funcion a partir de su hashcode
   __set_a_main__ DD -1704345260
   __set_b_main__ DD -1675716109
   __chequear_b_main__ DD 752588355
   __suma_c_main__ DD 1435485438
   __aumenta_suma_c_main__ DD -1610346546
.CODE
__set_a_main:
MOV AL, _y_set_a_main_
MOV BL, 1
ADD AL, BL
MOV @aux1, AL
JO ErrorOverflowSum
MOV AL, @aux1
MOV _x_a_main_, AL
invoke MessageBox, NULL, addr Cadena0, addr Cadena0, MB_OK
MOV __funcion_actual__, 0
RET

__set_b_main:
MOV AL, _y_set_b_main_
MOV BL, 3
ADD AL, BL
MOV @aux2, AL
JO ErrorOverflowSum
MOV AL, @aux2
MOV _x_b_main_, AL
invoke MessageBox, NULL, addr Cadena1, addr Cadena1, MB_OK
MOV __funcion_actual__, 0
RET

__chequear_b_main:
MOV AL, _y_chequear_b_main_
MOV AH, 1
CMP AL, AH
JNE etiqueta1
invoke MessageBox, NULL, addr Cadena2, addr Cadena2, MB_OK
JMP etiqueta2
etiqueta1: 
MOV AL, _y_chequear_b_main_
MOV AH, 2
CMP AL, AH
JNE etiqueta3
invoke MessageBox, NULL, addr Cadena3, addr Cadena3, MB_OK
JMP etiqueta4
etiqueta3: 
MOV AL, _y_chequear_b_main_
MOV AH, 3
CMP AL, AH
JNE etiqueta5
invoke MessageBox, NULL, addr Cadena4, addr Cadena4, MB_OK
JMP etiqueta6
etiqueta5: 
MOV AL, _y_chequear_b_main_
MOV AH, 4
CMP AL, AH
JNE etiqueta7
invoke MessageBox, NULL, addr Cadena5, addr Cadena5, MB_OK
JMP etiqueta8
etiqueta7: 
invoke MessageBox, NULL, addr Cadena6, addr Cadena6, MB_OK
etiqueta8: 
etiqueta6: 
etiqueta4: 
etiqueta2: 
MOV __funcion_actual__, 0
RET

__aumenta_suma_c_main:
MOV AL, _i_c_main_
MOV BL, 1
ADD AL, BL
MOV @aux3, AL
JO ErrorOverflowSum
MOV AL, @aux3
MOV _i_c_main_, AL
MOV EAX, __chequear_b_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV AL, _i_c_main_
MOV _y_chequear_b_main_, AL
call __chequear_b_main
MOV __funcion_actual__, 0
RET

__suma_c_main:
etiqueta9:MOV AL, _i_c_main_
MOV AH, 5
CMP AL, AH
JE etiqueta10
MOV EAX, __set_b_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV AL, _i_c_main_
MOV _y_set_b_main_, AL
call __set_b_main
MOV EAX, __aumenta_suma_c_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __aumenta_suma_c_main
JMP etiqueta9
etiqueta10:MOV __funcion_actual__, 0
RET


START:


MOV EAX, __suma_c_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __suma_c_main


invoke MessageBox, NULL, addr Cadena7, addr Cadena7, MB_OK


MOV AL, 15
MOV _x_main_, AL


MOV AL, -15
MOV _y_main_, AL


MOV AL, _x_main_
MOV BL, _y_main_
ADD AL, BL
MOV @aux4, AL
JO ErrorOverflowSum

MOV AL, @aux4
MOV _z_main_, AL


MOV AL, _z_main_
MOV AH, 0
CMP AL, AH
JNE etiqueta11

invoke MessageBox, NULL, addr Cadena8, addr Cadena8, MB_OK

JMP etiqueta12
etiqueta11: 

invoke MessageBox, NULL, addr Cadena9, addr Cadena9, MB_OK

etiqueta12: 




MOV AL, _z_main_
MOV BL, 5
ADD AL, BL
MOV @aux5, AL
JO ErrorOverflowSum

MOV AL, @aux5
MOV _z_main_, AL


MOV AL, _z_main_
MOV BL, _x_main_
IMUL BL
MOV @aux6, AL

MOV AL, @aux6
CBW
MOV __aux_conv, AX
FILD __aux_conv
FSTP @aux7

FLD @aux7
FSTP _f_main_


FLD _f_main_
FCOM __750
FSTSW aux_mem_2bytes
MOV AX, aux_mem_2bytes
 SAHF
JNE etiqueta13

invoke MessageBox, NULL, addr Cadena10, addr Cadena10, MB_OK

JMP etiqueta14
etiqueta13: 

etiqueta14: 




FLD _f_main_
FLD __20
FTST
FSTSW aux_mem_2bytes
MOV AX, aux_mem_2bytes
SAHF
JE ErrorDiv0
FDIV
FSTP @aux9

FLD @aux9
FSTP _f_main_


FLD _f_main_
FCOM __375
FSTSW aux_mem_2bytes
MOV AX, aux_mem_2bytes
 SAHF
JNE etiqueta15

invoke MessageBox, NULL, addr Cadena11, addr Cadena11, MB_OK

JMP etiqueta16
etiqueta15: 

etiqueta16: 






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
