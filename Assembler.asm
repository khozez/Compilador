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

    _cuotas_manejador_main_ DB ?
    _clientes_manejador_main_ DB ?
    _cant_paga_cuotas_manejador_main_ DB ?
    _cant_objeto_main_ DB ?
    _id_objeto_main_ DB ?
    Cadena0 DB "OBJETO CREADO", 0
    _x_set_cant_objeto_main_ DB ?
    _cant_mueble_main_ DB ?
    _id_mueble_main_ DB ?
    _x_mueble_main_ DB ?
    _porciento_uso_mueble_main_ DD ?
    _u_set_uso_mueble_main_ DD ?
    _cant_silla_main_ DB ?
    _id_silla_main_ DB ?
    _x_silla_main_ DB ?
    _porciento_uso_silla_main_ DD ?
    _u_silla_main_ DD ?
    Cadena1 DB "OBJETO CREADO: SILLA", 0
    _s3_mesa_main_ DD ?
    _s2_mesa_main_ DD ?
    _s1_mesa_main_ DD ?
    _cont_mesa_main_ DB ?
    _s_agregar_silla_mesa_main_ DD ?
    Cadena2 DB "CANT SILLAS SUPERADAS", 0
    __950 DD 95.0
    @aux1 DB ?
    @aux2 DB ?
  ; constante para cada funcion a partir de su hashcode
   __paga_cuotas_manejador_main__ DD -1030714251
   __cliente_menos_paga_cuotas_manejador_main__ DD 751927103
   __print_tipo_objeto_main__ DD 785596854
   __set_cant_objeto_main__ DD -1529759727
   __set_uso_mueble_main__ DD 1373855055
   __print_tipo_silla_main__ DD 1684463878
   __reset_cont_mesa_main__ DD -1249745771
   __agregar_silla_mesa_main__ DD -1808903906
.CODE
__cliente_menos_paga_cuotas_manejador_main:
MOV AL, _clientes_manejador_main_
MOV BL, 1
SUB AL, BL
MOV @aux1, AL
MOV AL, @aux1
MOV _clientes_manejador_main_, AL
MOV __funcion_actual__, 0
RET

__paga_cuotas_manejador_main:
MOV AL, _cant_paga_cuotas_manejador_main_
MOV AH, 4
CMP AL, AH
JBE etiqueta1
MOV EAX, __cliente_menos_paga_cuotas_manejador_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __cliente_menos_paga_cuotas_manejador_main
JMP etiqueta2
etiqueta1: 
etiqueta2: 
MOV __funcion_actual__, 0
RET

__print_tipo_objeto_main:
invoke MessageBox, NULL, addr Cadena0, addr Cadena0, MB_OK
invoke ExitProcess, 0
MOV __funcion_actual__, 0
RET

__set_cant_objeto_main:
MOV AL, _x_set_cant_objeto_main_
MOV _cant_objeto_main_, AL
MOV __funcion_actual__, 0
RET

__set_uso_mueble_main:
FLD _u_set_uso_mueble_main_
FSTP _porciento_uso_mueble_main_
MOV __funcion_actual__, 0
RET

__print_tipo_silla_main:
invoke MessageBox, NULL, addr Cadena1, addr Cadena1, MB_OK
invoke ExitProcess, 0
MOV __funcion_actual__, 0
RET

__reset_cont_mesa_main:
MOV AL, 0
MOV _cont_mesa_main_, AL
MOV __funcion_actual__, 0
RET

__agregar_silla_mesa_main:
MOV AL, _cont_mesa_main_
MOV AH, 2
CMP AL, AH
JAE etiqueta3
MOV AL, _cont_mesa_main_
MOV AH, 1
CMP AL, AH
JNE etiqueta4
FLD _s_agregar_silla_mesa_main_
FSTP _s1_mesa_main_
JMP etiqueta5
etiqueta4: 
MOV AL, _cont_mesa_main_
MOV AH, 2
CMP AL, AH
JNE etiqueta6
FLD _s_agregar_silla_mesa_main_
FSTP _s2_mesa_main_
JMP etiqueta7
etiqueta6: 
FLD _s_agregar_silla_mesa_main_
FSTP _s3_mesa_main_
etiqueta7: 
etiqueta5: 
MOV AL, _cont_mesa_main_
MOV BL, 1
ADD AL, BL
MOV @aux2, AL
JO ErrorOverflowSum
MOV AL, @aux2
MOV _cont_mesa_main_, AL
JMP etiqueta8
etiqueta3: 
invoke MessageBox, NULL, addr Cadena2, addr Cadena2, MB_OK
invoke ExitProcess, 0
etiqueta8: 
MOV __funcion_actual__, 0
RET


START:




MOV AL, 1
MOV _id_objeto_main_, AL


MOV AL, 2
MOV _id_mueble_main_, AL


MOV AL, 3
MOV _id_silla_main_, AL


MOV EAX, __set_uso_mueble_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
FLD __950
FSTP __950
call __set_uso_mueble_main


MOV EAX, __agregar_silla_mesa_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __agregar_silla_mesa_main


MOV EAX, __agregar_silla_mesa_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __agregar_silla_mesa_main


MOV EAX, __agregar_silla_mesa_main__
CMP EAX, __funcion_actual__
JE error_recursion
MOV __funcion_actual__, EAX
call __agregar_silla_mesa_main




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
