
Running the example:
cd C:\_CodeSamplesEtc\KafkaExample2

java -Dlog4j.configuration="file:C:\JavaDev\scalaWorkspace\KafkaExample2\src\main\resources\log4j.xml" -cp target\uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Consumer test-topic 192.168.241.142  "C:\\JavaDev\\scalaWorkspace\\KafkaExample2\\output\\"

java -Dlog4j.configuration="file:C:\JavaDev\scalaWorkspace\KafkaExample2\src\main\resources\log4j_Producer.xml" -cp target\uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Producer 1000 test-topic 192.168.241.142:9092


----------------

On GCE:
cd /home/vennkumar/KafkaExample2

java -Dlog4j.configuration="file:/home/vennkumar/KafkaExample2/src/main/resources/log4j.xml" -cp target/uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Consumer test-topic 10.142.0.3  "/home/vennkumar/KafkaExample2/output/"

java -Dlog4j.configuration="file:/home/vennkumar/KafkaExample2/src/main/resources/log4j_Producer.xml" -cp target/uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Producer 1000 test-topic 10.142.0.3:9092


