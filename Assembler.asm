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

    _deuda_main_ DB ?
    _pagas_main_ DB ?
    _cuotas_main_ DB ?
    _y_main_ DD ?
    __10 DD 1.0
    _valor_set_y_main_ DD ?
    _cuotas_set_y_main_ DB ?
    _cant_set_cuotas_main_ DB ?
    _cant_aumentar_cuotas_main_ DB ?
    _cant_pagar_cuotas_main_ DB ?
    _deuda_pagar_cuotas_main_ DB ?
    Cadena0 DB "CANTIDAD DE DEUDAS MAYOR A 3                   SE AUMENTAN 2 CUOTAS", 0
    Cadena1 DB "CANTIDAD DE DEUDAS MENOR A 3                   SE AUMENTA 1 CUOTAS", 0
    _cuotas_manejador_main_ DB ?
    _clientes_manejador_main_ DB ?
    _cant_paga_cuotas_manejador_main_ DB ?
    @aux1 DB ?
    @aux2 DB ?
    @aux3 DB ?
    @aux4 DB ?
    @aux5 DB ?
    @aux6 DD ?
  ; constante para cada funcion a partir de su hashcode
   __set_y_main__ DD -1017245636
   __set_cuotas_main__ DD -755342446
   __aumentar_cuota_main__ DD 119046634
   __aumentar_cuotas_main__ DD -44518157
   __pagar_cuotas_main__ DD -87398665
   __paga_cuotas_manejador_main__ DD -1030714251
   __cliente_menos_paga_cuotas_manejador_main__ DD 751927103
.CODE
__set_y_main:
FLD _valor_set_y_main_
FSTP _y_main_
MOV AL, 4
MOV _cuotas_set_y_main_, AL
MOV __funcion_actual__, 0
RET

__set_cuotas_main:
MOV AL, _cant_set_cuotas_main_
MOV _cuotas_main_, AL
MOV __funcion_actual__, 0
RET

__aumentar_cuota_main:
MOV AL, _cuotas_main_
MOV BL, 1
ADD AL, BL
MOV @aux1, AL
JO ErrorOverflowSum
MOV AL, @aux1
MOV _cuotas_main_, AL
MOV __funcion_actual__, 0
RET

__aumentar_cuotas_main:
MOV AL, _cuotas_main_
MOV BL, _cant_aumentar_cuotas_main_
ADD AL, BL
MOV @aux2, AL
JO ErrorOverflowSum
MOV AL, @aux2
MOV _cuotas_main_, AL
MOV __funcion_actual__, 0
RET

__pagar_cuotas_main:
MOV AL, _cuotas_main_
MOV _deuda_pagar_cuotas_main_, AL
MOV AL, _cuotas_main_
MOV BL, 1
SUB AL, BL
MOV @aux3, AL
MOV AL, @aux3
MOV _cuotas_main_, AL
MOV AL, _cant_pagar_cuotas_main_
MOV AH, 3
CMP AL, AH
JBE etiqueta1
invoke MessageBox, NULL, addr Cadena0, addr Cadena0, MB_OK
invoke ExitProcess, 0
MOV EAX, __aumentar_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV AL, 2
MOV _cant_aumentar_cuotas_main_, AL
call __aumentar_cuotas_main
JMP etiqueta2
etiqueta1: 
invoke MessageBox, NULL, addr Cadena1, addr Cadena1, MB_OK
invoke ExitProcess, 0
MOV EAX, __aumentar_cuota_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __aumentar_cuota_main
etiqueta2: 
MOV AL, _pagas_main_
MOV BL, 1
ADD AL, BL
MOV @aux4, AL
JO ErrorOverflowSum
MOV AL, @aux4
MOV _pagas_main_, AL
MOV __funcion_actual__, 0
RET

__cliente_menos_paga_cuotas_manejador_main:
MOV AL, _clientes_manejador_main_
MOV BL, 1
SUB AL, BL
MOV @aux5, AL
MOV AL, @aux5
MOV _clientes_manejador_main_, AL
MOV __funcion_actual__, 0
RET

__paga_cuotas_manejador_main:
MOV AL, _cant_paga_cuotas_manejador_main_
MOV AH, 4
CMP AL, AH
JBE etiqueta3
MOV EAX, __cliente_menos_paga_cuotas_manejador_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __cliente_menos_paga_cuotas_manejador_main
JMP etiqueta4
etiqueta3: 
etiqueta4: 
MOV __funcion_actual__, 0
RET


START:
FLD __10
FSTP _y_main_


MOV AL, 0
MOV _cuotas_main_, AL


MOV AL, 0
MOV _pagas_main_, AL


MOV AL, 0
MOV _deuda_main_, AL








MOV AL, _cuotas_main_
CBW
MOV __aux_conv, AX
FILD __aux_conv
FSTP @aux6

MOV EAX, __set_y_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
FLD @aux6
FSTP @aux6
call __set_y_main


MOV EAX, __set_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV AL, 20
MOV _cant_set_cuotas_main_, AL
call __set_cuotas_main


MOV EAX, __pagar_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV AL, 2
MOV _cant_pagar_cuotas_main_, AL
call __pagar_cuotas_main




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
