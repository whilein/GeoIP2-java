package com.maxmind.geoip2;

import tools.jackson.core.JsonParser;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Deserializes a string to an InetAddress.
 */
public class InetAddressDeserializer extends StdDeserializer<InetAddress> {
    /**
     * Constructs an instance of {@code InetAddressDeserializer}.
     */
    public InetAddressDeserializer() {
        super(InetAddress.class);
    }

    @Override
    public InetAddress deserialize(JsonParser p, DeserializationContext ctxt) {
        var value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return InetAddress.getByName(value);
        } catch (UnknownHostException e) {
            throw new StreamReadException(p, "Invalid IP address: " + value, e);
        }
    }
}
