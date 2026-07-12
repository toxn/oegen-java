# oegen-java
Kotlin genealogy assistant (Œgen).

A simple and straightforward genealogy tool:
- Persons with only one firstname/lastname, one parent of each gender. 
- Exact gregorian dates of birth, baptism, marriage, death and funeral. 
- Simple places (no hierarchy) for each of these events. 
- Loading and saving to a custom xml format. 
- Swing interface with tree and detail view. 
- Basic reports printing (tree, detail...)

## Build

Requires Java 25 (Gradle 9.6.1):

```bash
./gradlew build
./gradlew run
```
