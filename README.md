# TP Implementación Criptografía y Seguridad - Grupo 4
## Integrantes
- Nicolás Birsa
- Salvador Castagnino
- Tomás Martínez
- Agustín Morantes

## Dependencias
- Maven
- JDK 21

## Instrucciones de compilación
Compilar el JAR con el siguiente comando desde la raíz del proyecto:
```sh
mvn package
```
El resultado se puede encontrar en el path `target/steganography-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Instrucciones de ejecución
1. Hacer ejecutable el script `stegobmp` que se encuentra en la raíz con el siguiente comando:
   ```sh
   chmod +x stegobmp
   ```
2. Correr el script con los argumentos deseados de la siguiente forma:
   ```sh
   ./stegobmp [argumentos]
   ```

## Argumentos

### `-extract`
Indica que se va a exctraer información.

### `-embed`
Indica que se va a ocultar información.

### `-p [bmp file path]`
Path del archivo BMP portador.

### `-in [filepath]`
Path del archivo a ocultar.

### `-out [filepath]`
Path del archivo de salida.

### `-steg <LSB1 | LSB4 | LSBI>`
Algoritmo de esteganografiado a utilizar.

### `-m <ecb | cfb | ofb | cbc>`
Modo de encadenamiento de bloques para encripción a utilizar.

### `-pass [password]`
Clave de encripción a utilizar.

