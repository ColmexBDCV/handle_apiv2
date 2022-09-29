### Características

- Se utiliza SpringBoot como principal framework además de:
    - Libreria HandleAPI
    - API RestFul
    - SQLite

Con la finalidad de crear un control a la hora de generar identificadores unicos.

<!--# Proquest

<img src="https://migantoju.com/wp-content/uploads/2018/12/1_u_Jr6FozmyMCi3pe9ZsoFg-768x432.png"  width="384" height="216" />-->


**Contenido**

<!--ts-->
- [Iniciar el proyecto](#iniciar-el-proyecto)
    * [Agregar archivo admpriv](#agregar-archivo-admpriv)
	* [Ejecutar el proyecto](#ejecutar-el-proyecto)

<!--    * [Migrar base de datos](#migrar-base-de-datos)
    * [Crear role y usuario](#crear-role-y-usuario)-->
<!--te-->

** **

## Iniciar el proyecto


### Agregar archivo admpriv

Para poder ingresar al servidor de Handle institucional es necesario agregar el archivo admpriv.bin en la ruta src/main/resources/ 

y renombrarlo como 

>	`admpriv_tmp.bin`

Asi como agregar la clave de ingreso en la linea 133 del archivo HandleOperation.java


### Ejecutar el proyecto

Para ejecutar el proyecto se recomienda generar el .jar compilando el proyecto y posteriormente montarlo en un servidor JBoos propio o utilizando Docker y Docker-compose.


>	`docker, docker-compose`
