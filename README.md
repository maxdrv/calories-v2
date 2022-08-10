Before starting the application locally, you need to start the database
`cd dependency-stub && docker-compose up -d`

build fat jar with command `bash mvnw package` on mac, linux or `mvnw.cmd package` on windows

start fat jar with command `java -jar ./target/calorie-v2.jar`