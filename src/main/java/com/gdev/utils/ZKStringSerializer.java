package com.gdev.utils;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
/**
 * Created by mgarmes on 17/08/2016.
 */
public class ZKStringSerializer implements ZkSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKStringSerializer.class);

    public byte[] serialize(Object o) throws ZkMarshallingError {
        String data = (String) o;
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
            throw new ZkMarshallingError(e.getMessage(),e);
        }


    }

    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        if (bytes == null)
            return  null;
        else {
            try {
                return new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage(), e);
                throw new ZkMarshallingError(e.getMessage(),e);
            }
        }
    }
}
