# Scrabble by DarkMode.
Java implementation of the popular word game, [Scrabble](https://en.wikipedia.org/wiki/Scrabble).   
Developed for [COMP20050: Software Engineering Project 2](https://sisweb.ucd.ie/usis/!W_HU_MENU.P_PUBLISH?p_tag=MODULE&MODULE=COMP20050) 
at [UCD](https://www.ucd.ie/cs/).

*Note*: Please check the ['bot' branch](https://github.com/UCD-COMP20050/DarkMode/tree/bot) for Sprint 5 (bot design) using the lecturer's base classes for compatibilty.

## Getting Started
Follow these instructions to get a copy of the project running on your local machine.

### Prerequisites
* Java 8 is recommended for the project, since JavaFX is bundled with it.
* Install JDK 8 from Oracle [here](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html), 
in case you don't have it.
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) is our preferred IDE for the project.

### Playing the game
* [Download](https://github.com/UCD-COMP20050/DarkMode/releases/download/4.0/DarkMode.jar) 
and run (double click) the JAR file from the [latest release](https://github.com/UCD-COMP20050/DarkMode/releases).  
* Alternatively, clone this repository and build the project in your IDE. 
* Then, run the application's Main class (in package: game_engine under /src).

Dark Theme:  
  <img src="images/DarkTheme.jpg" height="400" width="800">   

Light Theme:   
  <img src="images/LightTheme.jpg" height="400" width="800">   

## Running the tests 
### IntelliJ IDEA
Clone the repository into your IntelliJ project, then:
* Mark the /test directory as Test Sources Root.
* Set JDK 8 as your Project SDK under Project Structure.
* Add JUnit5.4 (org.junit.jupiter:junit-jupiter:5.4.2) under Project Structure > Libraries > + > From Maven.
* Right click on the /test directory and ‘Run All Tests’ to generate the JUnit test report.

### Eclipse
Clone the repository into your Eclipse project, then:
* Go to project Properties > Java Build Path > Source > set 'Contains test sources: Yes' for /test directory.
* Go to project Properties > Java Build Path > Source > set different 'Output folder' for /src and /test directories.
* If you don't have JUnit 5 set up, Eclipse will show errors for the /test directory.   
Open any test file, hover over an @Test annotation, and select 'Add JUnit 5 library to the build path'.
* Right click on the /test directory and 'Run As > JUnit Test' to generate the JUnit test report.

## Documentation
To view the API documentation, clone the repository and open 'index.html' under /docs. 

## Authors
Team 15: DarkMode.
* Katarina Cvetkovic ([@katarinac](https://github.com/katarinac))
* Jason Tee ([@AmplifiedHuman](https://github.com/AmplifiedHuman))
* Rajit Banerjee ([@rajitbanerjee](https://github.com/rajitbanerjee))

## Acknowledgements 
* Dr. Chris Bleakley (lecturer)
* Duncan Wallace (TA), and the team of lab demonstrators.
