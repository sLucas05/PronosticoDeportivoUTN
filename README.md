# Pronósticos Deportivos

### Descripción
Proyecto desarrollado en el marco del curso **Desarrollador Java Inicial** de la **Universidad Tecnológica Nacional** utilizando el lenguaje **Java**. Consiste en un sistema capaz de generar resultados aleatorios para partidos de fútbol y calcular el puntaje de los jugadores en base a sus pronósticos. Aplica una lógica de puntuación con bonificaciones dinámicas por rondas y fases acertadas, consolidando los resultados en un ranking de jugadores.

Destaca de este proyecto la integración de **Maven** como gestor y la utilización de archivos **CSV** para el procesamiento de partidos y resultados. Asimismo, se implementa persistencia en **MySQL** a través de **JDBC** para el ranking de jugadores.

### Oportunidades de Mejora
```
∘ Dividir MainApp.java en clases y funciones con el fin de optimizar y reutilizar código.

∘ Migrar de JDBC a JPA para simplificar la persistencia.

∘ Migrar la configuración de CSV a un archivo JSON.

∘ Utilizar un ArrayList en reemplazo al procesamiento de partidos en CSV para agilizar la ejecución.

∘ Renombrar las variables para mejorar la legibilidad.

∘ Normalizar la acentuación en los strings.

∘ Agregar mas pruebas unitarias para cubrir el 100% del programa.
```

### Setup
  1. Importar la base de datos MySQL [pronosticos.sql](./database/pronosticos.sql)
  2. Importar como proyecto Maven.
  3. Ejecutar `MainApp.java`
     
### Previsualización
![Pronostico](https://github.com/user-attachments/assets/7b01a133-6834-4595-b499-a804647a4402)
