# Formations-batch
This application has been implemented during a Spring Batch online course followed through [Udemy platform](https://www.udemy.com/course/spring-batch-par-la-pratique/).

## Batch goal
This batch loads instructors, trainings and sessions. All these data are contained in files (*.xml*, *.csv* or *.txt* format) and they are loaded into a relational database. It then computes information to send an email to each instructor concerning their planning.

## Batch Structure
This batch is structured in three phases:
1. Parallel flow with two steps (one loading the instructors and the other the trainings).
2. A conditional step with decider to load sessions (depending on whether they are on a *.csv* or on a *.txt* file)
3. A last step compute these information and send an email to each instructor concerning their planning

## Job launch process
### Job parameters
1. formateursFile (.csv file)
2. formationsFile (.xml file)
3. seancesFile (.csv or .txt file)

### Job launch profile
In order to activate database connection (postgresql) *prod* profile must be activated

### Command line
The following command line can be used to launch job for project version *0.0.1-SNAPSHOT*:

java -jar -Dspring.profiles.active=prod ./target/formations-batch-0.0.1-SNAPSHOT.jar- formateursFile=file:C:\\Users\\etien\\Downloads\\inputs\\formateursFile.csv- formationsFile=file:C:\\Users\\etien\\Downloads\\inputs\\formationsFile.xml- seancesFile=file:C:\\Users\\etien\\Downloads\\inputs\\seancesFile.csv