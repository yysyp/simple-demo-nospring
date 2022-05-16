
For all the classes in src/main/java/ps/demo/util please refer to
../simple-demo/src/main/java/ps/demo/util

AND the "../simple-demo/src/main/java/ps/demo/util" should be the latest.

#Build normal fat jar:
1, update the <mainClass> value in pom.xml
2, run mvn clean package -D spring-boot.repackage.skip=true -D fat.jar=true
3, get the fat jar in: target/simple-demo-1.0.0-jar-with-dependencies.jar
