package com.example.together.dboperations;

import java.io.IOException;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * USED FOR GSON. Makes Gson interpret 0,1 (mysql) as false, true
 */
class BooleanTypeAdapter extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.value(false);  // Default value for null
        } else {
            out.value(value);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.NULL) {
            in.nextNull();
            return false;  // Default value for null
        }
        switch (token) {
            case NUMBER:
                int intValue = in.nextInt();
                return intValue == 1;
            case BOOLEAN:
                return in.nextBoolean();
            case STRING:
                String stringValue = in.nextString();
                if ("1".equals(stringValue)) {
                return true;
                } else if ("0".equals(stringValue)) {
                return false;
                }
                throw new JsonSyntaxException("Expected 1 or 0 but was " + stringValue);
            default:
                throw new JsonSyntaxException("Expected boolean or number but was " + token);
        }
    }
}
