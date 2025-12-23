# Metodologías de Desarrollo Seguro – Práctica 2  
## CTF web, automatización con Selenium, PRNG, Git y tests de seguridad

Práctica de la asignatura **Metodologías de Desarrollo Seguro (URJC)** centrada en retos tipo CTF
y automatización de pruebas:

- Análisis de proyectos **Java** con malas prácticas (Optimizer, La Lotería).
- Recuperación de secretos en repositorios **Git** (Git Gud).
- *Web crawling* con **DFS** sobre un blog.
- Bots con **Selenium** (Python y Java) para superar retos tipo “10 Fast Fingers” y “Whack-a-Mole”.
- Explotación de un **PRNG predecible** en Java (CWE-337) en el reto “La Lotería”.
- Refactor seguro y **tests automatizados** (JUnit 5 + Mockito) sobre `SecureCalculator`.
- Retos en **C** y **Python 2.7** (Agenda, Crash me…if you can, Agenda v2) enfocados a gestión
  insegura de memoria y ejecución de código.

---

## Estructura del repositorio

```text
.
├─ src/
│  ├─ DFS_Blog.py                      # Crawler DFS contra blog vulnerable (requests + BeautifulSoup)
│  ├─ FastFingers.py                   # Bot Selenium para reto estilo 10 Fast Fingers
│  ├─ LoteriaCTF/                      # Proyecto Java (IntelliJ) – exploit PRNG "La Lotería"
│  │  └─ LoteriaExploitFinal.java
│  ├─ selenium-WhackaMole/             # Proyecto Java (Maven) – bot Whack-a-Mole con Selenium
│  │  ├─ pom.xml
│  │  ├─ index.html
│  │  ├─ main.css
│  │  ├─ js/
│  │  │  └─ app.js
│  │  └─ WhackAMoleBot.java
│  └─ unit-tests-aulavirtual/          # Proyecto Maven – SecureCalculator + tests
│     ├─ pom.xml
│     ├─ SecureCalculator.java
│     └─ SecureCalculatorTests.java
└─ docs/
   └─ Practica2_MDS.pdf                # Memoria completa de la práctica (Java, Git, C, Python, Testing)
```

> Nota: algunos retos (Optimizer, Git Gud, Agenda en C y Agenda v2 en Python) se documentan
> solo en `docs/Practica2_MDS.pdf`, ya que el código original se ejecutaba dentro de
> entornos de laboratorio/containers específicos.

---

## 1. DFS_Blog.py – Crawler DFS sobre blog

Script en Python que realiza un **recorrido en profundidad (DFS)** sobre un blog
vulnerable (`r2-ctf-vulnerable.numa.host`):

- Usa `requests` + `BeautifulSoup` para descargar y parsear HTML.
- Restringe la navegación al dominio permitido (`allowed_domain`) y lleva un
  `visited_urls` para no ciclar.
- Cuenta apariciones exactas de **"URJC"** tanto en toda la página como dentro de
  `<div class="article-post">`.

---

## 2. FastFingers.py – Bot Selenium para reto “10 Fast Fingers”

Bot en Python que automatiza un reto tipo **10 Fast Fingers**:

- Abre un navegador (Chrome) mediante `selenium.webdriver`.
- Carga una página local del reto (por ejemplo `http://localhost:63343/10fastfingers/...`).
- Localiza el texto dinámico (`.text-display`), lo divide en palabras y las envía
  al `<textarea id="textInput">` con `send_keys`, superando el reto en pocos segundos.
- Muestra cómo un test de “velocidad de escritura” es trivialmente automatizable
  si no se aplican medidas anti-bot.

---

## 3. LoteriaCTF – Explotación de PRNG predecible (CWE-337)

Proyecto Java (`LoteriaExploitFinal.java`) que explota una lotería vulnerable:

- El servidor genera tokens con `new Random(System.currentTimeMillis()).nextInt(MAX_NUMBER)`.
- El exploit:
  1. Reinicia la sesión vía `/reset` y obtiene la cookie.
  2. Fuerza la generación de un token fallido y recupera el token anterior desde el HTML.
  3. Recorre una ventana de posibles semillas en torno al `System.currentTimeMillis()` registrado.
  4. Encuentra la semilla que reproduce el token observado.
  5. Calcula el siguiente número de la secuencia y lo envía como token válido.

Con esto se explota la debilidad **CWE-337 (Predictable Seed in PRNG)** y se obtiene
la flag descrita en la memoria.

## 4. selenium-WhackaMole – Bot Java para juego Whack-a-Mole

Proyecto Maven que contiene un bot (`WhackAMoleBot.java`) capaz de jugar
automáticamente a un juego tipo **Whack-a-Mole**:

- Usa **FirefoxDriver** (Selenium) + `geckodriver`.
- Abre `index.html` del juego (servido en localhost).
- En un bucle, detecta qué topo está activo (clases/atributos del DOM) y hace
  clic automáticamente hasta obtener la puntuación necesaria.
- La memoria muestra capturas del juego y del código del bot.

---

## 5. unit-tests-aulavirtual – SecureCalculator + JUnit 5/Mockito

Proyecto Maven donde se refactoriza `SecureCalculator` y se añaden tests de seguridad:

### SecureCalculator.java

- Reemplaza `Math.random()`/`Random` por **`SecureRandom`**.
- Corrige:
  - `multiply(int a, int b)` con detección explícita de overflow.
  - `divide(double a, double b)` con control de división por cero.
  - `mod(int a, int b)` manejando correctamente valores negativos y `Integer.MIN_VALUE`.
  - `isOdd(int n)` / `isEven(int n)` mediante operaciones bit a bit.
- Introduce un sistema de logging configurable.

### SecureCalculatorTests.java

- Tests con **JUnit 5** y **Mockito** que:
  - Validan rangos, manejo de excepciones y comportamiento esperado.
  - Comprueban unicidad y rango de los números pseudoaleatorios.
  - Verifican la interacción con el logger y distintos niveles de logging.

---

## 6. Otros retos documentados solo en `docs/Practica2_MDS.pdf`

Además del código incluido en `src/`, la memoria recoge otros retos que se
resuelven principalmente con análisis manual y comandos en consola:

### 6.1 Java – Optimizer

- Análisis de un proyecto Java de “optimización”.
- Se descubre una **clave secreta en Base64** hardcodeada en el código fuente.
- Lección: no almacenar API keys, flags ni secretos en el código.

### 6.2 Misc – Git Gud

- Reto basado en un repositorio **Git** donde se han intentado borrar datos sensibles.
- Usando `git log -p --all` y `git fsck --lost-found` se localizan **commits huérfanos**
  con una clave SSH y una cadena Base64 que contiene la flag.
- Lección: borrar archivos no basta, hay que limpiar el **historial**.

### 6.3 C – Agenda

- Agenda de contactos en C basada en un array de tamaño fijo.
- No se validan índices negativos, lo que permite leer memoria fuera del array.
- Solución: validar que el índice esté en `[0, SIZE-1]` antes de acceder.

### 6.4 C – Crash me…if you can

- Binario que compara una secuencia de 8 caracteres con valores esperados usando XOR.
- Si se introduce la secuencia correcta, muestra la flag y provoca un crash (SIGSEGV) intencionado.

### 6.5 Python – Agenda v2 (Python 2.7)

- Agenda de contactos en **Python 2.7** que usa `input()` sin validar.
- En Python 2 `input()` evalúa lo que se escribe, lo que permite ejecutar código arbitrario
  (por ejemplo `__import__('os').system('env')`) y leer variables de entorno sensibles.
- Solución: migrar a **Python 3** y usar `input()` + validación estricta antes de convertir tipos.

---
