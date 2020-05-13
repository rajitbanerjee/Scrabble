# Scrabble by DarkMode.
Java implementation of the popular word game, [Scrabble](https://en.wikipedia.org/wiki/Scrabble).   
Developed for [COMP20050: Software Engineering Project 2](https://sisweb.ucd.ie/usis/!W_HU_MENU.P_PUBLISH?p_tag=MODULE&MODULE=COMP20050) 
at [UCD](https://www.ucd.ie/cs/).

## Getting Started
Follow these instructions to get a copy of the project running on your local machine.

### Prerequisites
Java 8 is required for the project, since JavaFX is bundled with it.   
Install JDK 8 from Oracle [here](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html), 
in case you don't have it.

### Build and run
[Download](https://github.com/rajitbanerjee/Scrabble/releases/download/v5.0/Scrabble-5.0.jar) 
and run (double click) the JAR file from the [latest release](https://github.com/rajitbanerjee/Scrabble/releases).  
An alternative (using Gradle) is to clone the repository, then
* Build project and run JUnit tests
```
./gradlew build
```
* Run the game
```
./gradlew run
``` 

Dark Theme:  
  <img src="images/DarkTheme.jpg" height="400" width="800">   

Light Theme:   
  <img src="images/LightTheme.jpg" height="400" width="800">   


## Documentation
Use the command to generate API documentation under **build > docs > javadoc**:
```
./gradlew javadoc
```  

## Authors
Team 15: DarkMode.
* Katarina Cvetkovic ([@katarinac](https://github.com/katarinac))
* Jason Tee ([@AmplifiedHuman](https://github.com/AmplifiedHuman))
* Rajit Banerjee ([@rajitbanerjee](https://github.com/rajitbanerjee))

## Acknowledgements 
* Dr. Chris Bleakley (lecturer)
* Duncan Wallace (TA), and the team of lab demonstrators.
