package com.gdev.core.kafka;

import com.gdev.core.cache.ConsumersOffsetsCache;
import com.gdev.core.cache.GroupMetadataCache;
import com.gdev.core.cache.model.DataPoint;
import com.gdev.core.cache.TopicOffsetsCache;
import com.gdev.core.cache.model.GroupMemberMetadata;
import com.gdev.core.cache.model.GroupMetadata;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.ArrayOf;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.protocol.types.Schema;
import org.apache.kafka.common.protocol.types.Struct;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;

public class KafkaNewConsumerOffsetThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNewConsumerOffsetThread.class);

    public static final String GROUP = "group";
    public static final String TOPIC = "topic";
    public static final String PARTITION = "partition";
    public static final String OFFSET = "offset";
    public static final String METADATA = "metadata";
    public static final String TIMESTAMP = "timestamp";

    public static final String CONSUMER = "consumer";
    public static final String PRODUCER = "producer";
    public static final String MEASUREMENT_NAME = "lag";
    public static final String DB_NAME = "kafka";

    public static final short CURRENT_OFFSET_KEY_SCHEMA_VERSION = (short)1;
    public static final short CURRENT_GROUP_KEY_SCHEMA_VERSION = (short)2;

    private static Schema OFFSET_COMMIT_KEY_SCHEMA = new Schema(
            new Field(GROUP, Schema.STRING),
            new Field(TOPIC, Schema.STRING),
            new Field(PARTITION, Schema.INT32));

    private static Field OFFSET_KEY_GROUP_FIELD = OFFSET_COMMIT_KEY_SCHEMA.get(GROUP);
    private static Field OFFSET_KEY_TOPIC_FIELD = OFFSET_COMMIT_KEY_SCHEMA.get(TOPIC);
    private static Field OFFSET_KEY_PARTITION_FIELD = OFFSET_COMMIT_KEY_SCHEMA.get(PARTITION);

    private static Schema OFFSET_COMMIT_VALUE_SCHEMA_V0 = new Schema(
            new Field(OFFSET, Schema.INT64),
            new Field(METADATA, Schema.STRING, "Associated metadata.", ""),
            new Field(TIMESTAMP, Schema.INT64));

    private static Field OFFSET_VALUE_OFFSET_FIELD_V0 = OFFSET_COMMIT_VALUE_SCHEMA_V0.get(OFFSET);
    private static Field OFFSET_VALUE_METADATA_FIELD_V0 = OFFSET_COMMIT_VALUE_SCHEMA_V0.get(METADATA);
    private static Field OFFSET_VALUE_TIMESTAMP_FIELD_V0 = OFFSET_COMMIT_VALUE_SCHEMA_V0.get(TIMESTAMP);

    private static Schema OFFSET_COMMIT_VALUE_SCHEMA_V1 = new Schema(
            new Field("offset", Schema.INT64),
            new Field(METADATA, Schema.STRING, "Associated metadata.", ""),
            new Field("commit_timestamp", Schema.INT64),
            new Field("expire_timestamp", Schema.INT64));

    private static Field OFFSET_VALUE_OFFSET_FIELD_V1 = OFFSET_COMMIT_VALUE_SCHEMA_V1.get(OFFSET);
    private static Field OFFSET_VALUE_METADATA_FIELD_V1 = OFFSET_COMMIT_VALUE_SCHEMA_V1.get(METADATA);
    private static Field OFFSET_VALUE_COMMIT_TIMESTAMP_FIELD_V1 = OFFSET_COMMIT_VALUE_SCHEMA_V1.get("commit_timestamp");
    private static Field OFFSET_VALUE_EXPIRE_TIMESTAMP_FIELD_V1 = OFFSET_COMMIT_VALUE_SCHEMA_V1.get("expire_timestamp");


    private static Schema GROUP_METADATA_KEY_SCHEMA = new Schema(new Field(GROUP, Schema.STRING));
    private static Field GROUP_KEY_GROUP_FIELD = GROUP_METADATA_KEY_SCHEMA.get(GROUP);

    //=========================GROUP INFO ===============

    private static final String MEMBER_ID_KEY = "member_id";
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_HOST_KEY = "client_host";
    private static final String REBALANCE_TIMEOUT_KEY = "rebalance_timeout";
    private static final String SESSION_TIMEOUT_KEY = "session_timeout";
    private static final String SUBSCRIPTION_KEY = "subscription";
    private static final String ASSIGNMENT_KEY = "assignment";

    private static Schema MEMBER_METADATA_V0 = new Schema(
            new Field(MEMBER_ID_KEY, Schema.STRING),
            new Field(CLIENT_ID_KEY, Schema.STRING),
            new Field(CLIENT_HOST_KEY, Schema.STRING),
            new Field(SESSION_TIMEOUT_KEY, Schema.INT32),
            new Field(SUBSCRIPTION_KEY, Schema.BYTES),
            new Field(ASSIGNMENT_KEY, Schema.BYTES));

    private static Schema MEMBER_METADATA_V1 = new Schema(
            new Field(MEMBER_ID_KEY, Schema.STRING),
            new Field(CLIENT_ID_KEY, Schema.STRING),
            new Field(CLIENT_HOST_KEY, Schema.STRING),
            new Field(REBALANCE_TIMEOUT_KEY, Schema.INT32),
            new Field(SESSION_TIMEOUT_KEY, Schema.INT32),
            new Field(SUBSCRIPTION_KEY, Schema.BYTES),
            new Field(ASSIGNMENT_KEY, Schema.BYTES));

    private static final String PROTOCOL_TYPE_KEY = "protocol_type";
    private static final String GENERATION_KEY = "generation";
    private static final String PROTOCOL_KEY = "protocol";
    private static final String LEADER_KEY = "leader";
    private static final String MEMBERS_KEY = "members";

    private static Schema GROUP_METADATA_VALUE_SCHEMA_V0 = new Schema(
            new Field(PROTOCOL_TYPE_KEY, Schema.STRING),
            new Field(GENERATION_KEY, Schema.INT32),
            new Field(PROTOCOL_KEY, Schema.NULLABLE_STRING),
            new Field(LEADER_KEY, Schema.NULLABLE_STRING),
            new Field(MEMBERS_KEY, new ArrayOf(MEMBER_METADATA_V0)));

    private static Schema GROUP_METADATA_VALUE_SCHEMA_V1 = new Schema(
            new Field(PROTOCOL_TYPE_KEY, Schema.STRING),
            new Field(GENERATION_KEY, Schema.INT32),
            new Field(PROTOCOL_KEY, Schema.NULLABLE_STRING),
            new Field(LEADER_KEY, Schema.NULLABLE_STRING),
            new Field(MEMBERS_KEY, new ArrayOf(MEMBER_METADATA_V1) ));


    private KafkaConsumer<byte[], byte[]> consumer ;
    private Cache consumerOffsetCache;
    private Cache groupMetadataCache;

    public  KafkaNewConsumerOffsetThread(Properties properties){
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("__consumer_offsets"));
        LOGGER.info(Thread.currentThread().getName()+" start Kafka consumer  offset topic");
        consumerOffsetCache = ConsumersOffsetsCache.getInstance();
        this.groupMetadataCache = GroupMetadataCache.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            getOffsets(consumer, consumerOffsetCache, groupMetadataCache);
        }
    }

    private  void getOffsets(KafkaConsumer<byte[], byte[]> consumer, Cache cache, Cache groupCache){
        ConsumerRecords<byte[], byte[]> records = consumer.poll(10000);
        for (ConsumerRecord<byte[], byte[]> consumerRecord : records) {
            if (consumerRecord.value() != null && consumerRecord.key() != null) {
                ByteBuffer key = ByteBuffer.wrap(consumerRecord.key());
                short version = key.getShort();
                if (version <= CURRENT_OFFSET_KEY_SCHEMA_VERSION) {
                    Struct struct = (Struct) OFFSET_COMMIT_KEY_SCHEMA.read(key);
                    // version 0 and 1 refer to offset
                    String group = struct.getString(OFFSET_KEY_GROUP_FIELD);
                    String topic = struct.getString(OFFSET_KEY_TOPIC_FIELD);
                    int partition = struct.getInt(OFFSET_KEY_PARTITION_FIELD);

                    cache.put(group+"#"+topic+"="+partition, readOffsetMessageValue(ByteBuffer.wrap(consumerRecord.value())) );
                }else if (version == CURRENT_GROUP_KEY_SCHEMA_VERSION) {
                    // version 2 refers to offset
                    Struct struct = (Struct) GROUP_METADATA_KEY_SCHEMA.read(key);
                    String group = struct.getString("group");
                    LOGGER.debug("["+group+"] Group Info");
                    groupCache.put(group,readGroupMessageValue(ByteBuffer.wrap(consumerRecord.value()), group));
                } else{
                    throw new IllegalStateException("Unknown version " + version + " for group metadata message");
                }
            }
        }
    }
    private DataPoint readOffsetMessageValue(ByteBuffer buffer){
        if (buffer == null) { // tombstone
            return  null;
        } else {
            short version = buffer.getShort();
            Schema valueSchema = schemaForOffset(version);
            Struct value = valueSchema.read(buffer);
            if (version == 0) {
                Long offset = value.getLong(OFFSET_VALUE_OFFSET_FIELD_V0);
                String metadata = value.getString(OFFSET_VALUE_METADATA_FIELD_V0);
                Long timestamp = value.getLong(OFFSET_VALUE_TIMESTAMP_FIELD_V0);
                return new DataPoint(offset,timestamp);
            } else if (version == 1) {
                Long offset = value.getLong(OFFSET_VALUE_OFFSET_FIELD_V1);
                String metadata = value.getString(OFFSET_VALUE_METADATA_FIELD_V1);
                Long commitTimestamp = value.getLong(OFFSET_VALUE_COMMIT_TIMESTAMP_FIELD_V1);
                //Long expireTimestamp = value.getLong(OFFSET_VALUE_EXPIRE_TIMESTAMP_FIELD_V1);
                return new DataPoint(offset,commitTimestamp);
            } else {
                throw new IllegalStateException("Unknown offset message version");
            }
        }
    }
    private Schema schemaForOffset(short version)  {
        if (version == (short) 0)
            return OFFSET_COMMIT_VALUE_SCHEMA_V0;
        if (version == (short) 1)
            return OFFSET_COMMIT_VALUE_SCHEMA_V1;
        throw new IllegalStateException("Unknown offset schema version " + version);
    }



    private GroupMetadata readGroupMessageValue(ByteBuffer buffer, String group){
        if (buffer == null) { // tombstone
            return  null;
        } else {
            short version = buffer.getShort();
            Schema valueSchema = schemaForGroup(version);
            Struct value = valueSchema.read(buffer);
            if (version == 0 || version == 1 ) {
                String protocolType = value.getString(PROTOCOL_TYPE_KEY);
                Integer generationId = value.getInt(GENERATION_KEY);
                String leaderId = value.getString(LEADER_KEY);
                String protocol = value.getString(PROTOCOL_KEY);

                GroupMetadata groupMetadata = new GroupMetadata(protocolType,generationId,leaderId,protocol);

                Object[] memberMetadataArray = value.getArray(MEMBERS_KEY);
                for(int i=0; i < memberMetadataArray.length;i++){

                    Struct memberMetadata = (Struct)memberMetadataArray[i];
                    String memberId = memberMetadata.getString(MEMBER_ID_KEY);
                    String clientId = memberMetadata.getString(CLIENT_ID_KEY);
                    String clientHost = memberMetadata.getString(CLIENT_HOST_KEY);
                    Integer sessionTimeout = memberMetadata.getInt(SESSION_TIMEOUT_KEY);
                    Integer rebalanceTimeout = 0;
                    if (version == 0) rebalanceTimeout = sessionTimeout ;
                    else memberMetadata.getInt(REBALANCE_TIMEOUT_KEY);
                    groupMetadata.addMemberMetadata(new GroupMemberMetadata(memberId,clientId,clientHost,sessionTimeout,rebalanceTimeout));
                    // subscription = Utils.toArray(memberMetadata.get(SUBSCRIPTION_KEY).asInstanceOf[ByteBuffer])
                    //member.assignment = Utils.toArray(memberMetadata.get(ASSIGNMENT_KEY).asInstanceOf[ByteBuffer])

                }

                return groupMetadata;
            } else {
                throw new IllegalStateException("Unknown offset message version");
            }
        }
    }
    private Schema schemaForGroup(short version) {
        if (version == (short) 0)
            return GROUP_METADATA_VALUE_SCHEMA_V0;
        if (version == (short) 1)
            return GROUP_METADATA_VALUE_SCHEMA_V1;
        throw new IllegalStateException("Unknown offset schema version " + version);
    }

}
