package com.exemplo;

import com.exemplo.util.ConverterEnvToProperty;
import com.mycorp.mynamespace.sampleRecord;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.logging.Logger;

public class AvroProducerRemote {
    public static void main(String[] args) {
        var logger = Logger.getAnonymousLogger();
        var properties = ConverterEnvToProperty.fromConverterEnv(AvroProducerRemote.class.getClassLoader().getResourceAsStream("application.properties"));
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());

        var producer = new KafkaProducer<String, sampleRecord>(properties);
        var topic = "orders";

        sampleRecord sr = sampleRecord
                .newBuilder()
                    .setOrderAddress("Rua")
                    .setOrderId(1)
                    .setOrderTime(123)
                .build();

        var producerRecord = new ProducerRecord<String, sampleRecord>(topic, sr);

        producer.send(producerRecord, (recordMetadata, e) -> {
            if (e == null){
                logger.info("Sucess!");
                logger.info(recordMetadata.toString());
            } else {
                e.printStackTrace();
            }
        });

        producer.flush();
        producer.close();
    }
}
