package ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Serialization {
    private static final Logger logger = LogManager.getLogger(Serialization.class);
    private static final int MAX_SERIALIZATION_SIZE = 10 * 1024 * 1024; // 10MB

    public static byte[] serialize(Object obj) throws IOException {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot serialize null object");
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(obj);
            byte[] result = bos.toByteArray();

            if (result.length > MAX_SERIALIZATION_SIZE) {
                throw new IOException("Serialized object size exceeds limit");
            }

            return result;
        } catch (NotSerializableException e) {
            logger.error("Serialization failed for type: {}", obj.getClass().getName(), e);
            throw e;
        }
    }

    public static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid data for deserialization");
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            return (T) ois.readObject();
        } catch (InvalidClassException | ClassCastException e) {
            logger.error("Deserialization error", e);
            throw e;
        }
    }
}
