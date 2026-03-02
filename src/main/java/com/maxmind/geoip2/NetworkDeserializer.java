package com.maxmind.geoip2;

import tools.jackson.core.JsonParser;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import com.maxmind.db.Network;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class provides a deserializer for the Network class.
 */
public final class NetworkDeserializer extends StdDeserializer<Network> {

    /**
     * Constructs a @{code NetworkDeserializer} object.
     */
    public NetworkDeserializer() {
        super(Network.class);
    }

    @Override
    public Network deserialize(JsonParser jsonparser, DeserializationContext context) {

        final var cidr = jsonparser.getValueAsString();
        if (cidr == null || cidr.isBlank()) {
            return null;
        }
        return parseCidr(jsonparser, cidr);
    }

    private static Network parseCidr(JsonParser p, String cidr) {
        final var parts = cidr.split("/", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CIDR format: " + cidr);
        }

        final var addrPart = parts[0];
        final var prefixPart = parts[1];

        final InetAddress address;
        try {
            address = InetAddress.getByName(addrPart);
        } catch (UnknownHostException e) {
            throw new StreamReadException(p, "Unknown host in CIDR: " + cidr, e);
        }

        final var prefixLength = parsePrefixLength(prefixPart, cidr);

        final var maxPrefix = (address.getAddress().length == 4) ? 32 : 128;
        if (prefixLength < 0 || prefixLength > maxPrefix) {
            throw new IllegalArgumentException(
                    "Prefix length out of range (0-" + maxPrefix + ") for CIDR: " + cidr);
        }

        return new Network(address, prefixLength);
    }

    private static int parsePrefixLength(String prefixPart, String cidr) {
        try {
            return Integer.parseInt(prefixPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid prefix length in CIDR: " + cidr, e);
        }
    }
}
