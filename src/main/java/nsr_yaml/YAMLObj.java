package nsr_yaml;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static nsr_yaml.Parser.*;

public class YAMLObj extends YAMLObject {
    private final Object data;

    protected YAMLObj(Object data) {
        super(data);
        this.data = data;
    }

    public Boolean asBoolean() {
        return toBoolean(data);
    }

    public Byte asByte() {
        return toByte(data);
    }

    public Short asShort() {
        return toShort(data);
    }

    public Integer asInteger() {
        return toInteger(data);
    }

    public Long asLong() {
        return toLong(data);
    }

    public Float asFloat() {
        return toFloat(data);
    }

    public Double asDouble() {
        return toDouble(data);
    }

    public LocalDate asLocalDate() {
        return toLocalDate(data, null);
    }

    public LocalDate asLocalDate(String pattern) {
        return toLocalDate(data, pattern);
    }

    public LocalTime asLocalTime() {
        return toLocalTime(data, null);
    }

    public LocalTime asLocalTime(String pattern) {
        return toLocalTime(data, pattern);
    }

    public LocalDateTime asLocalDateTime() {
        return toLocalDateTime(data, null);
    }

    public LocalDateTime asLocalDateTime(String pattern) {
        return toLocalDateTime(data, pattern);
    }

    public ZonedDateTime asZonedDateTime() {
        return toZonedDateTime(data, null);
    }

    public ZonedDateTime asZonedDateTime(String pattern) {
        return toZonedDateTime(data, pattern);
    }

}
