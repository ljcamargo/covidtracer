# COVIDTracker

[[Read in english]](https://github.com/ljcamargo/covidtracker/blob/main/README.md)

Esta es una aplicación android de código abierto (open-source) para registrar asistencia a la entrada de establecimientos mediante códigos QR y número de celular para seguimiento de contactos y permitir a las autoridades contactar a visitantes que pudieran haber sido expuestos al COVID durante su visita.

El objetivo de esta aplicación de etiqueta blanca (white-label) y su código es que sea derivada o bifurcada (fork) para que sea tematizada y adaptada a las necesidades de cada gobierno o localidad para eficientar el proceso de registro de entrada a los establecimiento y evitar largas fukas de espera y disminuir la propagación del COVID.

El mecanismo de esta aplicación está inspirado y construido sobre el “Registro de Apertura” del Gobierno de la Ciudad de México y fue desarrollado de manera independiente y extraoficial.

## Precaución
Esta app ha sido desarrollada independientemente y sin relación con algún gobierno o institución.

Con fines demostrativos, esta app implementa una conexión no oficial con el sitio web de registro del Gobierno de la CIudad de Mexico simulando el proceso manual de registro y podría cambiar de un momento a otro y dejar de funcionar en cualquier momento. No está pensada para su uso oficial o para sustituir mecanismos oficiales. Utilícela para fines de prueba y bajo su responsabilidad.

## ¿Cómo funciona?
* Abra la apliciación
* Escanee el código QR en el punto de entrada del establecimiento
* Reciba la confirmación y muéstrela al responsable.

## Características
* Escaneo de QR asistido (zxing)
* Almacena el teléfono celular la primera vez para agilizar el proceso
* Introduzca el código del establecimiento en caso de que no funcione el QR
* Verifica la integridad del número telefónico, código manual o QR.
* Lista de últimas visitas
* Soporte multilenguaje (español e inglés)

## Pasos para implementar su propia versión
1. Bifurcar(fork) el proyecto
2. Cambiar el valor hexadecimal de los colores
3. Cambiar los textos y traducciones
4. Cambiar íconos y artes
5. Cambiar los patrones regex para validar teléfono y códigos
6. Implementar repositorio y fuente de datos
7. Cambiar plantillas de términos, privacidad y acerca de. También puede apuntar a sitio externo.
8. Cambiar el nombre del paquete
9. Compilar y Distribuir

## Especificaciones Tecnológicas
* Kotlin 1.4+
* Corrutinas
* Ktor
* Koin (Inyección de Dependencias)
* Material Design
* MVVC

## Errores y Retroalimentación
Para reportar errores, retroalimentar o generar una discusión visita  [Github Issues](https://github.com/ljcamargo/covidtracker/issues).

## Licencia
Licencia bajo términos de la Apache License, Version 2.0 (la "Licencia");
usted no puede utilizar este código excepto bajo los términos de la Licencia
Puede obtener una copia de los términnos de la Licencia en:

http://www.apache.org/licenses/LICENSE-2.0

Excepto por lo dispuesto por la ley local aplicable o aceptado por escrito por la autora o el autor, el software distribuido bajo términos de la Licencia se suministra “TAL CUAL” sin ninguna garantía de ninguna clase, explícita o implícita

Consulte la Licencia para conocer a detalle los permisos y limitaciones bajo términos de la Licencia

## Icons
Íconos realizados por  <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a>, <a href="https://www.flaticon.com/authors/kiranshastry" title="Kiranshastry">Kiranshastry</a>, <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a>, <a href="https://www.flaticon.com/authors/xnimrodx" title="xnimrodx">xnimrodx</a>, <a href="https://www.flaticon.com/authors/dinosoftlabs" title="DinosoftLabs">DinosoftLabs</a> desde <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>

