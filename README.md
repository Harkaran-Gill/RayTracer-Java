## Java Ray Tracer
A simple, CPU-based ray tracer built in Java by porting Peter Shirley's 
[Ray Tracing in One Weekend](https://raytracing.github.io/books/RayTracingInOneWeekend.html)
book series. This project is a personal exploration into the fundamentals of computer graphics
and rendering algorithms, now adapted for the Java language.

![alt text](images/final_render.png)

### About This Project
This program generates photorealistic images by simulating the path of
light rays as they interact with objects in a 3D scene. The implementation
is based on the concepts from the "Ray Tracing in One Weekend" series,
but all code is written in Java, highlighting differences in language 
features, memory management, and object-oriented design.

## Build and Run
You can run this project by either building an executable JAR file with Maven or by running the source code directly from your Integrated Development Environment (IDE).

### Build with Maven

This method compiles the project into a standalone JAR file, which is ideal for deployment or execution without an IDE.

*   **Prerequisites**: Git and Apache Maven must be installed.
*   Clone the repository and navigate into the project directory:
    ```
    git clone https://github.com/Harkaran-Gill/RayTracer-Java.git
    cd <project-directory>
    ```
*   Build the project using the Maven `package` command. This will compile the source code, run tests, and package the application into a JAR file in the `target` directory.
    ```
    mvn clean package
    ```
*   Run the application by executing the generated JAR file.
    Replace `RayTracer-1.0-SNAPSHOT.jar` with the actual name of the JAR file created in the `target` folder.
    (`RayTracer-1.0-SNAPSHOT.jar` is default for this project)
    ```
    java -jar target/RayTracer-1.0-SNAPSHOT.jar
    ```

### Run from an IDE

This method is recommended for development, as it allows for easier debugging and direct execution of the code.

*   **Prerequisites**: Git and a Java IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code.
*   Clone the repository to your local machine:
    ```
    git clone https://github.com/Harkaran-Gill/RayTracer-Java.git
    ```
*   Open or import the cloned folder as a new project in your IDE. Most modern IDEs will automatically detect the `pom.xml` file and configure the project by downloading the required dependencies.
*   Locate the `Main.java` file within the `src/main/java/com/ray/` directory and run it directly from your IDE.

#### Also look at:
[C++ RayTracer](https://github.com/Harkaran-Gill/RayTracer)
