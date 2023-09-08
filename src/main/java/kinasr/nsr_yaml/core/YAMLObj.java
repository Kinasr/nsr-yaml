package kinasr.nsr_yaml.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * Class YAMLObj
 * The YAMLObj class extends the YAMLObject class and stores an object,
 * data, as its state.
 * It provides various methods to convert the stored data to various data types including Boolean, Byte, Short,
 * Integer, Long, Float, Double, LocalDate, LocalTime, LocalDateTime, ZonedDateTime, etc.
 * <p>
 * Constructor
 * <p>
 * protected YAMLObj(Object data)
 * This constructor takes an Object data as an argument and creates a new YAMLObj
 * instance with the specified data stored in it.
 */
public class YAMLObj extends YAMLObject {
    private final Object data;

    /**
     * Constructor for the YAMLObj class.
     *
     * @param data The data object to be stored in the YAMLObj instance.
     */
    protected YAMLObj(Object data) {
        super(data);
        this.data = data;
    }

    /**
     * Converts the data stored in the YAMLObj instance to a Boolean.
     *
     * @return The Boolean representation of the stored data.
     */
    public Boolean asBoolean() {
        return Parser.toBoolean(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a Byte.
     *
     * @return The Byte representation of the stored data.
     */
    public Byte asByte() {
        return Parser.toByte(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a Short.
     *
     * @return The Short representation of the stored data.
     */
    public Short asShort() {
        return Parser.toShort(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to an Integer.
     *
     * @return The Integer representation of the stored data.
     */
    public Integer asInteger() {
        return Parser.toInteger(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a Long.
     *
     * @return The Long representation of the stored data.
     */
    public Long asLong() {
        return Parser.toLong(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a Float.
     *
     * @return The Float representation of the stored data.
     */
    public Float asFloat() {
        return Parser.toFloat(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a Double.
     *
     * @return The Double representation of the stored data.
     */
    public Double asDouble() {
        return Parser.toDouble(data);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a LocalDate.
     *
     * @return The LocalDate representation of the stored data.
     */
    public LocalDate asLocalDate() {
        return Parser.toLocalDate(data, null);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a LocalDate, using the specified pattern.
     *
     * @param pattern The pattern to be used when parsing the data as a LocalDate.
     * @return The LocalDate representation of the stored data.
     */
    public LocalDate asLocalDate(String pattern) {
        return Parser.toLocalDate(data, pattern);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a LocalTime.
     *
     * @return The LocalTime representation of the stored data.
     */
    public LocalTime asLocalTime() {
        return Parser.toLocalTime(data, null);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a LocalTime, using the specified pattern.
     *
     * @param pattern The pattern to be used when parsing the data as a LocalTime.
     * @return The LocalTime representation of the stored data.
     */
    public LocalTime asLocalTime(String pattern) {
        return Parser.toLocalTime(data, pattern);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a LocalDateTime.
     *
     * @return The LocalDateTime representation of the stored data.
     */
    public LocalDateTime asLocalDateTime() {
        return Parser.toLocalDateTime(data, null);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a LocalDateTime, using the specified pattern.
     *
     * @param pattern The pattern to be used when parsing the data as a LocalDateTime.
     * @return The LocalDateTime representation of the stored data.
     */
    public LocalDateTime asLocalDateTime(String pattern) {
        return Parser.toLocalDateTime(data, pattern);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a ZonedDateTime.
     *
     * @return The ZonedDateTime representation of the stored data.
     */
    public ZonedDateTime asZonedDateTime() {
        return Parser.toZonedDateTime(data, null);
    }

    /**
     * Converts the data stored in the YAMLObj instance to a ZonedDateTime, using the specified pattern.
     *
     * @param pattern The pattern to be used when parsing the data as a ZonedDateTime.
     * @return The ZonedDateTime representation of the stored data.
     */
    public ZonedDateTime asZonedDateTime(String pattern) {
        return Parser.toZonedDateTime(data, pattern);
    }
}
