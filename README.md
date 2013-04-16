Proyecto "RTCapturaSDK"
================

Proyecto para medir el rendimiento en la adquisición de datos mediante la cámara de un teléfono móvil con sitema Android.
En este proyecto forma parte de una serie de pruebas en dispositivos Android.

## Dependencias Generales ##
Los programas descritos a continuación deben estar instalados y configurados:
* JDK de Oracle (V. 1.7.0_x) - [Ver info](http://www.oracle.com/technetwork/es/java/javase/downloads/index.html)
* ANDROID SDK (R. 21.x) - [Ver info](http://developer.android.com/sdk/index.html)
* ANDROID NDK (Release r8d) - [Ver info](http://developer.android.com/tools/sdk/ndk/index.html)
* Apache ANT (V. 1.8.x) - [Ver info](http://ant.apache.org/bindownload.cgi)
* Cmake (V. 2.8.x) - [Ver info](http://www.cmake.org/cmake/resources/software.html)
* Git (V. 1.7.x.x) - [Ver info](http://rogerdudler.github.com/git-guide/index.es.html)
* Adicionalmente se debe crear un Dispositivo Virtual Android, esto se logra usando el SDK y el AVD Maganer. - [Ver info](http://developer.android.com/tools/devices/managing-avds-cmdline.html)

##Aspectos Técnicos##
Los siguientas aspectos deben tomarse en consideración:

###Software###
Para el desarrollo se uso lo siguiente:
* Sistemas operativos GNU/Linux [openSUSE (12.2)](http://www.opensuse.org/es/), [LinuxMint](http://www.linuxmint.com/).
* Editor de código (Kate, Nano, Vim, etc).
* Terminal linux.

###IDE (Opcional)###
Se podria usar tambien Eclipse IDE para Desarrolladores Java, con las configuraciones para desarrollo en Android - [Ver info](http://developer.android.com/sdk/installing/installing-adt.html)

## Instrucciones ##
Para generar los ejecutables del proyecto siga las siguientes instrucciones:

1. Obtenemos el proyecto que esta en el repositorio git __`https://github.com/fzzio/realtime_captura`__
```bash
  # Nos movemos al directorio donde descargaremos el repositorio
  $ cd ruta/donde/almacena/proyectos

  # Clonamos el repositorio que esta en Githuby en el directorio RealtimeCaptura
  $ git clone git@github.com:fzzio/realtime_captura.git RealtimeCaptura
```

2. Realizamos lo siguiente para compilar y generar los archivos para Android.
```bash
  # Nos ubicamos en el proyecto
  $ cd ruta/donde/almacena/proyectos/RealtimeCaptura

  # Actualizamos las configuraciones del proyecto
  $ android update project -p .

  # Compilamos los archivos de ndk que estan en el directorio jni/
  $ ndk-build

  # Usamos ant para generar los archivos ejecutables, sea en debug o release
  $ ant debug

  # Instalamos el .apk generado en el emulador
  $ ant install

  # Si tenemos conectado el dispositivo en modo de desarrollador,
  # podremos instalar la aplicación en el teléfono directamente
  $ adb -d install -r ruta/a/tu/app/nombre_app_debug.apk
```
3. Revisamos en el teléfono o emulador la aplicación funcionando.


##Restricciones##
1. Se usó para las pruebas teléfonos Android con las siguientes caracteristicas:

  ###Galaxy S II AT&T ###
    * Marca: `Samsung Electronics`
    * Modelo: `SAMSUNG-SGH-I777`
    * Versión de Android: `4.03: Ice Cream Sandwich`
    * Versión de Banda Base: `I777UCLE5`
    * Versión de Kernel: `3.0.15-I777UCLE5-CL652575 se.infra@SEP-74 #3`

  ###Galaxy S III###
    * Marca: `Samsung Electronics`
    * Modelo: `GT-I9300`
    * Versión de Android: `4.1.1: Jelly Bean`
    * Versión de Banda Base: `I9300UBDLI2`
    * Versión de Kernel: `3.0.31-306699 se.infra@SEP-94 #1`


2. Los resultados obtenidos se justificaran en base al __Tiempo de Adquisición y Captura__, mas no en pintado de la imagen en pantalla.

##Autores##
* Andrea Obando (andyoban@gmail.com)
* Fabricio Orrala (fabricio.orrala@gmail.com)