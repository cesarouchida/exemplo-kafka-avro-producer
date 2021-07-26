package com.exemplo;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class AvroProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");
        properties.setProperty("acks","1");
        properties.setProperty("retries","10");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url","http://localhost:8081");

        KafkaProducer<String, TaxPayer> producer = new KafkaProducer<String, TaxPayer>(properties);
        String topic = "meu-topico";

        TaxPayer taxPlayer = TaxPayer
                .newBuilder()
                    .setName("nome")
                    .setDocument("documento")
                    .setSituation(true)
                .build();

        ProducerRecord<String, TaxPayer> producerRecord = new ProducerRecord<String, TaxPayer>(topic, taxPlayer);
        producer.send(producerRecord, new Callback(){
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null){
                    System.out.println("Sucess!");
                    System.out.println(recordMetadata.toString());
                } else {
                    e.printStackTrace();
                }
            }
        });

        producer.flush();
        producer.close();
    }
}
