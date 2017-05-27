## Description

This project is an application meant for modelling the worldwide famous Rubik's cube with its variations differing in how many layers they consist of.

## Requirement

Requires Oracle Java 1.8 and Maven 3.0 or above.

## Build and Run

In the main directory use the following commands:

```
mvn package
java -jar target/RubiksCube-1.0-jar-with-dependencies.jar
```

## Generating the site

In the main directory use the following command:

```
mvn site
```

## Usage

Usage of this application is very simple. 
 * To inspect the cube from different positions, just drag with the mouse near the cube. 
 * To rotate the layers of the cube, drag with the mouse on the cube.
 * Don't forget to save the game if you want to continue solving later.
