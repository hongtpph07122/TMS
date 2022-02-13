package com.oauthcentralization.app.tmsoauth2.config.oauthExceptionConfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthExceptionConfig> {
    public CustomOauthExceptionSerializer() {
        super(CustomOauthExceptionConfig.class);
    }

    @Override
    public void serialize(CustomOauthExceptionConfig value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("code", value.getHttpErrorCode());
        jsonGenerator.writeBooleanField("status", false);
        jsonGenerator.writeStringField("message", buildMessage(value.getMessage()));
        jsonGenerator.writeObjectField("errors", Arrays.asList(value.getOAuth2ErrorCode(), value.getMessage()));
        if (CollectionsUtility.isNotEmptyMap(value.getAdditionalInformation())) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jsonGenerator.writeStringField(key, add);
            }
        }
        jsonGenerator.writeEndObject();
    }


    private String buildMessage(String value) {
        value = StringUtils.trimWhitespace(value).toLowerCase();
        if (value.equalsIgnoreCase("Bad credentials") || value.equalsIgnoreCase("User is disabled")) {
            return "Invalid credentials";
        }

        if (value.equalsIgnoreCase("User account is locked")) {
            return "Account unavailable";
        }

        if (value.equalsIgnoreCase("User account has expired")) {
            // return "Your password has been changed automatically according to the system regulations. Please check your email or contact the admin";
            return "Your password has been expired";
        }

        return value;
    }
}
