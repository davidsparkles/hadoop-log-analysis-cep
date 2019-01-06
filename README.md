# Hadoop Log Analysis CEP

This repository includes a system for analysing the log files of vaious services of a Hadoop cluster. 
It uses a log file agent per log file and a complex event processing system to evaluate queries on the log stream. 
As the main integration pipeline Apache Kafka is used.


## Build

`mvn package`


## Prerequisite for execution on a cluster

1. Java Runtime Environment
2. If you want to use Kafka as the integration and messaging framework, than you need to have Kafka and a Zookeeper running and configured, incl. a topic for the log events.


## Installation

1. Move the compiled `target/***-jar-with-dependencies.jar` to the server
2. `mkdir config` in the same folder as the jar

Hint: Configuration allows to set input and outputs as well as specify environment specific parameters like the log-type-specific parsing.


## Run

### Agent

Requires a `agent.conf` in the config folder. (See `src/main/resources` and `src/test/resources` for examples.)

`java -cp config:***-jar-with-dependencies.jar de.davidsparkles AgentMain` 

### PatternDetection

Requires a `patternDetection.conf` in the config folder. (See `src/main/resources` and `src/test/resources` for examples.)

`java -cp config:***-jar-with-dependencies.jar de.davidsparkles PatternDetectionMain`
