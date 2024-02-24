# Creeps.

## Brief

This document is a quick overview of the creeps given files.

## What's in the archive

In the provided archive you will find the following files:

  * `README.md`: this file.
  * `pom.xml`: the sample maven project file that you must use for your project.
  * `creeps-server.jar`: the server, for you to train on.
  * `given.jar`: a compiled java library to help you in your endeavor.
  * `given-javadoc.jar`: given library documentation, can be extracted with `jar xf`.

## Installing the given jar to your local repository

  * Make sure the folder `~/.m2/repository/` exists,  if not create it
    (assuming you have built maven projects before, the folder should already
    exist).
  * Run the command `mvn install:install-file -Dfile=given.jar -DgroupId=com.epita -DartifactId=given -Dversion=3.0-SNAPSHOT -Dpackaging=jar`.
  * You should be good to use the provided pom.xml file.

In case the assistants publish a new version of the file, simply repeat the

### First run:

`java -jar creeps-server.jar --printAchievements=true` will print
the list of all achievements you can get.

### Tutorial:

`java -jar creeps-server.jar --trackAchievements=true --enableEnemies=false --enableGC=false --citizenFeedingRate=100000`
starts the server without enemies, without Hector and without starvation.
Ideal for early development. You can even add `--enableEasyMode=true` earlier
on to help grasping the mechanics of the game.

### The game, standard configuration:

`java -jar creeps-server.jar --trackAchievements=true`
starts the server with the setup that will be used on the live server.

## Web client

The web client is enabled by default when running the local server.
To use it, you have to connect to `http://localhost:port` where port is
either 1337 by default or the value of the -httpPort option given to the
server.

## Documentation

The documentation is generated using Javadoc.
To use it, you can extract `given-javadoc.jar` with `jar xf`.
You can then open the `index.html` file using any web browser.


