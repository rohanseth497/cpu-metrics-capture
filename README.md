## Basics

- The producer is continuously emiting CPU usage metrics into a Kafka topic (cpu-metrics)	
- The consumer is a Kafka application which uses Kafka Streams API to calculate the moving average of the CPU metrics of each machine
- Processing is happening via clients which can be scaled
- Using local state store from Kafka to keep track of metrics of a particular consumer.
- Consumer also expose a REST endpoint which would help in sending this real time metrics to the requestor
