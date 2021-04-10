# PrintScript Test Compatibility Kit

This repository provides a way for any PrintScript interpreter to test its compliance with the expected functionality.
This includes a collection of PrintScript files with their expected print results, as well as the validation result (valid or invalid).

### How to validate your implementation?
- create a new branch with the following naming convention: [group number]-validation
- Modify **build.gradle** to include your implementation as a dependency in the project
- Within _src/main/java/implementation_, define an adapter of your lexer to comply with the [PrintScriptInterpreter](src/main/java/interpreter/PrintScriptInterpreter.java) interface.
- Return your adapted implementation in [CustomImplementationFactory](src/main/java/implementation/CustomImplementationFactory.java)
