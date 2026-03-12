# Proyecto-Integrado-GPM-IAM

- Descripcion de lo que hay que hacer 

Desarrollaréis en parejas una aplicación Java completa sobre un dominio de vuestra elección. La aplicación debe tener interfaz de consola e interfaz gráfica con Swing, persistir datos en ficheros CSV y exportar a JSON, y estar construida sobre una jerarquía de clases con herencia e interfaces.

El proyecto se entrega en tres hitos, cada uno como un tag en el repositorio GitHub. La nota de cada RA se evalúa de forma independiente según la rúbrica correspondiente. El proyecto finaliza con una presentación oral de 10 minutos al resto de la clase.

- Normas generales

✗   NO se permite
-> Librerías externas en el pom.xml (solo Java SE puro).
-> Editor visual de GUI (NetBeans Matisse, IntelliJ GUI Designer). La GUI se construye a mano.
-> Bases de datos. La persistencia es exclusivamente mediante ficheros.
-> Serialización con ObjectOutputStream. El formato es CSV y JSON textual.
-> Entregas en .zip. Solo se acepta la URL del repositorio GitHub.
-> Código copiado de otro grupo. Se comprobará similitud entre entregas.

✓   SÍ se permite y recomienda
-> Usar Maven únicamente como herramienta de build, sin añadir dependencias.
-> Consultar la documentación oficial de Java y los fragmentos de este enunciado.
-> Hacer commits frecuentes que muestren la evolución real del trabajo.
-> Proponer un dominio propio (con validación previa del profesor, ver sección 3.2).
-> Añadir funcionalidades extra una vez cubiertos todos los requisitos.

- Estructura del repositorio GitHub

nombre-proyecto/               ← nombre del repositorio
├── pom.xml
├── README.md                  ← descripción, instrucciones, nombres del equipo
├── uml/
│   └── diagrama.png           ← diagrama UML (entregado en Hito 1)
├── datos/
│   └── catalogo.csv           ← fichero de datos
└── src/main/java/com/equipo/proyecto/
    ├── modelo/                ← clases del dominio
    ├── persistencia/          ← GestorFicheros
    ├── colecciones/           ← clase que gestiona las colecciones
    ├── consola/               ← MenuConsola
    └── gui/                   ← VentanaPrincipal, DialogoFormulario

- Proyecto escogido
🎮   GameVault
Gestión de colección de videojuegos
Jerarquía: Videojuego → JuegoFisico / JuegoDigital
Interfaces: Valorable, Exportable
Colecciones: ArrayList catálogo + HashMap por género
Perfil: Técnico / gamer

Requisitos técnicos mínimos
Independientemente del dominio elegido, todos los proyectos deben cumplir los siguientes requisitos. Son los que se verificarán en la evaluación.

RA7 — Modelo de clases

-> Una superclase abstracta con al menos 2 métodos abstractos y validaciones en el constructor.
-> Al menos 2 subclases concretas que usen super() y sobrescriban los métodos abstractos.
-> Al menos 2 interfaces con propósito diferenciado, implementadas por la jerarquía.
-> Un método genérico con bounded type (<T extends SuperClase>).
-> Javadoc en todas las clases y métodos públicos (@param, @return, @throws donde aplique).
-> Un diagrama UML en la carpeta uml/ del repositorio.

RA6 — Tipos avanzados

-> Un array como atributo en alguna clase del modelo (p.ej. categorías, etiquetas).
-> ArrayList como colección principal de objetos del dominio.
-> HashMap como índice secundario para búsquedas por clave.
-> Un Iterator explícito en el método de eliminación.
-> Al menos 4 operaciones con Stream: filter, sorted, groupingBy y una operación de reducción.
-> Expresiones regulares en al menos 2 sitios: una búsqueda y una validación.
-> Exportación a JSON construida manualmente con StringBuilder. El JSON debe ser válido.

RA5 — E/S y GUI

-> Menú de consola con formato tabular usando printf.
-> 3 mecanismos de E/S de ficheros: BufferedReader (lectura), FileWriter+BufferedWriter (escritura), RandomAccessFile (acceso por ID).
-> Todos los accesos a fichero con try-with-resources.
-> GUI Swing construida a mano: JTable + JScrollPane, JDialog modal, JTextField buscador.
-> Al menos un ActionListener con lambda y otro con clase anónima.
-> WindowListener que intercepta el cierre y pregunta si guardar.
