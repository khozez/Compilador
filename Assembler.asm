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

    _deuda:main_ DB ?
    _pagas:main_ DB ?
    _cuotas:main_ DB ?
    _cant:set_cuotas:main_ DB ?
    _cant:aumentar_cuotas:main_ DB ?
    _cant:pagar_cuotas:main_ DB ?
    _deuda:pagar_cuotas:main_ DB ?
    Cadena0 DB "CANTIDAD DE DEUDAS MAYOR A 3                   SE AUMENTAN 2 CUOTAS", 0
    Cadena1 DB "CANTIDAD DE DEUDAS MENOR A 3                   SE AUMENTA 1 CUOTAS", 0
    _cuotas:manejador:main_ DB ?
    _clientes:manejador:main_ DB ?
    _cant:paga_cuotas:manejador:main_ DB ?
    @aux1_ DB ?
    @aux2_ DB ?
    @aux3_ DB ?
    @aux4_ DB ?
    @aux5_ DB ?
  ; constante para cada funcion a partir de su hashcode
   __set_cuotas_main__ DD -755342446
   __aumentar_cuota_main__ DD 119046634
   __aumentar_cuotas_main__ DD -44518157
   __pagar_cuotas_main__ DD -87398665
   __paga_cuotas_manejador_main__ DD -1030714251
   __cliente_menos_paga_cuotas_manejador_main__ DD 751927103
.CODE
__set_cuotas_main:
MOV AL, _cant:set_cuotas:main
MOV _cuotas:main, AL
MOV funcion_actual, 0
RET

__aumentar_cuota_main:
MOV AL, _cuotas:main
MOV BL, 1
ADD AL, BL
MOV @aux1, AL
JO error_overflow
MOV AL, @aux1
MOV _cuotas:main, AL
MOV funcion_actual, 0
RET

__aumentar_cuotas_main:
MOV AL, _cuotas:main
MOV BL, _cant:aumentar_cuotas:main
ADD AL, BL
MOV @aux2, AL
JO error_overflow
MOV AL, @aux2
MOV _cuotas:main, AL
MOV funcion_actual, 0
RET

__pagar_cuotas_main:
MOV AL, _cuotas:main
MOV _deuda:pagar_cuotas:main, AL
MOV AL, _cuotas:main
MOV BL, 1
SUB AL, BL
MOV @aux3
MOV AL, @aux3
MOV _cuotas:main, AL
MOV AL, deuda_pagar_cuotas_main
MOV AH, 5_s
CMP AL, AH
JAE etiqueta1
invoke MessageBox, NULL, addr Cadena0, addr Cadena0, MB_OK
invoke ExitProcess, 0
MOV EAX, __aumentar_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV _cant:aumentar_cuotas:main, 2
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
MOV AL, _pagas:main
MOV BL, 1
ADD AL, BL
MOV @aux4, AL
JO error_overflow
MOV AL, @aux4
MOV _pagas:main, AL
MOV funcion_actual, 0
RET

__cliente_menos_paga_cuotas_manejador_main:
MOV AL, _clientes:manejador:main
MOV BL, 1
SUB AL, BL
MOV @aux5
MOV AL, @aux5
MOV _clientes:manejador:main, AL
MOV funcion_actual, 0
RET

__paga_cuotas_manejador_main:
MOV AL, 
MOV AH, 4_s
CMP AL, AH
JAE etiqueta3
MOV EAX, __cliente_menos_paga_cuotas_manejador_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __cliente_menos_paga_cuotas_manejador_main
JMP etiqueta4
etiqueta3: 
etiqueta4: 
MOV funcion_actual, 0
RET


START:
MOV AL, 0
MOV _cuotas:main, AL


MOV AL, 0
MOV _pagas:main, AL


MOV AL, 0
MOV _deuda:main, AL







MOV EAX, __set_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV _cant:set_cuotas:main, 20
call __set_cuotas_main


MOV EAX, __pagar_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV _cant:pagar_cuotas:main, 4
call __pagar_cuotas_main


MOV EAX, __pagar_cuotas_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
MOV _cant:pagar_cuotas:main, 2
call __pagar_cuotas_main




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
