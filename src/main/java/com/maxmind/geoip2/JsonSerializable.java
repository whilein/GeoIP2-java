package com.maxmind.geoip2;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Interface for classes that can be serialized to JSON.
 * Provides default implementation for toJson() method.
 */
public interface JsonSerializable {

    /**
     * @return JSON representation of this object. The structure is the same as
     * the JSON provided by the GeoIP2 web service.
     */
    default String toJson() {
        JsonMapper mapper = JsonMapper.builder()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .addModule(new InetAddressModule())
            .changeDefaultPropertyInclusion(inclusion -> inclusion
                    .withValueInclusion(JsonInclude.Include.NON_NULL)
                    .withValueInclusion(JsonInclude.Include.NON_EMPTY))
            .build();

        return mapper.writeValueAsString(this);
    }

}
