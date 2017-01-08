
Running the example Locally:
cd <Path to your workspace>

java -Dlog4j.configuration="file:<Path to your workspace>\KafkaExample2\src\main\resources\log4j.xml" -cp target\uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Consumer test-topic 192.168.241.142  "<Path to your workspace>\KafkaExample2\\output\\"

java -Dlog4j.configuration="file:<Path to your workspace>\KafkaExample2\src\main\resources\log4j_Producer.xml" -cp target\uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Producer 1000 test-topic 192.168.241.142:9092


----------------

##On Google Compute Engine: Copy your project to your home directory on the GCE Virtual Machine:
cd /home/<yourloginid>/KafkaExample2

java -Dlog4j.configuration="file:/home/<yourloginid>/KafkaExample2/src/main/resources/log4j.xml" -cp target/uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Consumer test-topic <List of broker ips on GCE>  "/home/<yourloginid>/KafkaExample2/output/"

java -Dlog4j.configuration="file:/home/<yourloginid>/KafkaExample2/src/main/resources/log4j_Producer.xml" -cp target/uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Producer 1000 test-topic <broker_ip:port>


