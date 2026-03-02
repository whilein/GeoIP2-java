package com.maxmind.geoip2;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * Serializes any Object to string.
 */
public class ToStringSerializer extends StdSerializer<Object> {
    /**
     * Constructs an instance of {@code ToStringSerializer}.
     */
    public ToStringSerializer() {
        super(Object.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializationContext context) {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.toString());
        }
    }
}
