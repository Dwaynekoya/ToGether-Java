
# ToGether - Plataforma de Gestión de Tareas
Proyecto para FP Desarrollo de Aplicaciones Multiplataforma.

ToGether es una aplicación multiplataforma para gestionar tareas desde ordenador o móvil y **compartirlas** con otros usuarios. Incluye soporte para tareas compartidas, hábitos recurrentes, grupos de usuarios y visualización de tareas completadas con anotaciones o fotos.

Emplea Java (AndroidStudio, JavaFX), PHP, JSON, MySQL.

## Características Principales
- **Conexión con BBDD**: Tanto la aplicación móvil como la versión de escritorio se comunican con la base de datos MySQL mediante los mismos ficheros PHP. Las consultas son recibidas como ficheros JSON por la aplicación e interpretadas acordemente con los objetos Java que representan.
- **Gestión de tareas**: Tareas propias, compartidas y hábitos recurrentes.
- **Subida de fotos**: Asociación de imágenes a tareas completadas o usuarios.
- **Seguimiento:** Un usuario puede seguir a otros para ver qué tareas completan. 
- **Plataformas soportadas**: Escritorio (JavaFX) y Android.
- **Seguridad**: Cifrado de contraseñas y copias de seguridad automáticas de la base de datos.

## Instalación
Es necesario que la base de datos esté en línea para el uso de la aplicación y la IP referenciada en la clase Constants del paquete dboperations sea ajustada acordemente. 


### Android
1. Descarga el archivo **`.apk`**
2. Activa la opción de instalar aplicaciones de **fuentes desconocidas** en la configuración del teléfono.
3. Abre el archivo **`.apk`** para instalar la aplicación.

### Escritorio
1. Ejecuta el instalador **`setup.exe`** 
2. Tras instalar, usa el siguiente comando para ejecutar el programa:
   ```bash java -jar --module-path "ruta/librerias/javafx" --add-modules javafx.controls,javafx.fxml ToGether.jar   ```

### Detalles técnicos
-   **Lenguajes y Frameworks**:
    -   Escritorio: Java con JavaFX.
    -   Android: Java adaptado para dispositivos móviles.
-   **Gestión de datos**: Base de datos en forma normal de Boyce-Codd para garantizar integridad y eficiencia.
-   **Arquitectura modular**: Separación en paquetes de la interfaz, clases que representan los objetos de la base de datos y clases que gestionan la comunicación con la base de datos.
-   **Compatibilidad**: Ejecutable con soporte para Windows y dispositivos Android.

[Carpeta Drive con más documentación](https://drive.google.com/drive/folders/1uTKcSYqYKk5CQFkLy20fSmBJU6j2bL40?usp=sharing) 
Incluye:
- Consultas necesarias para generar la BBDD y el código .php para comunicarse con ella.
- Manual del programador
- Manual de usuario
- Javadoc del proyecto Java
- Archivo .apk para instalación móvil
- Archivo .exe para instalación en Windows

[Repositorio de versión Android](https://github.com/Dwaynekoya/ToGetherAndroid)
