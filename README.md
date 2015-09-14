Bottles Sample
==============

The bottle sample is a simple example using the modular structure of squbs and streaming service responses. It will stream lyrics from our famous bottles song to your browser. It has the following modules.

* The *bottlecube* is the module containing the logic for generating the interesting events. All cubes only know about each others via ActorSelection. No type dependencies between cubes and services. 

* The *bottlemsgs* project only contains message types (case classes) used for talking to the bottlecube. Message projects MUST NOT contain any logic.

* The *bottlesvc* starts the REST service and listens to the port. Upon arrival of a request, it sends a message to bottlecube which then gradually sends the lyrics of the song back to bottlesvc to be streamed out.

The picture below shows the interaction between the modules:
![alt text](/images/bottlesample.png "Bottle sample modules")


Getting Started
---------------

1. Build all the modules by running "sbt clean compile package" from root directory.

2. Start the server using "sbt samplesvc/run" from the root directory.

3. Go to http://localhost:8080/sample/index.html

Most important - have fun!
--------------------------
