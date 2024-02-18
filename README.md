![GitHub release (latest by date)](https://img.shields.io/github/v/release/kinasr/nsr-yaml)

NSR-YAML
---
A light-wight library provides easy and customizable ways to read data from YAML files.

## Quick Start Guide

### 1. Installation

- Learn how to install a Maven package from GitHub by
  visiting [this link](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package).
- Add this library as a dependency in your project's pom.xml file:
    ``` xml
    <dependency>
        <groupId>io.github.kinasr</groupId>
        <artifactId>nsr-yaml</artifactId>
        <version>0.0.3-beta</version>
    </dependency>
  ```
- Run `mvn install` to ensure the library is installed.

### 2. Hello World

- Start by creating a YAML file within your project, for example, `path/hello.yaml`. Add the following content:
  ```yaml
  helloWorld: Hello World
  ```
- Use the following code to read the "hello world" message from your YAML file:
  ```java
  public class HelloWorld {
    public static void main(String[] args) {
        var helloWorld = YAML.read("path/hello.yaml").get("helloWorld").asString();
    }
  }
  ```

---

## Configuration

Optionally, you can include a `nsr_config.yaml` configuration file in your project's `src/main/resources/` directory.

  ```yaml
  date-config:
    date-pattern: "yyyy-MM-dd"
    time-pattern: "HH:mm:ss"
    date-time-pattern: "yyyy-MM-dd HH:mm:ss"
    zoned-date-time-pattern: "yyyy-MM-dd HH:mm:ss Z"

  environments:
    - local
    - production 
   ```

In the configuration file, you can customize default date and time patterns. Additionally, the "environments" feature
allows you to modify YAML key-value pairs based on the environment.  
For example, the following code would print "Hello from local" if the environment is local and "Hello from production"
if the environment is production:

```yaml
hello@local: Hello from local
hello@production: Hello from production
```

```java
  public class HelloWorld {
    public static void main(String[] args) {
        var helloWorld = YAML.read("path/hello.yaml").get("hello").asString();
        
        System.out.println(helloWorld);
    }
  }
  ```

## Conclusion

NSR-YAML is a lightweight and easy-to-use library for reading data from YAML files in Java.
It provides a number of features to make it easy to customize the way that YAML data is read, including the ability to
change the default date and time patterns and to use the environments feature to change the YAML key value depending on
the environment.