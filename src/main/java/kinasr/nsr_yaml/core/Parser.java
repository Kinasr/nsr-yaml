package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.annotation.Alias;
import kinasr.nsr_yaml.exception.ParsingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Parser Class
 * <p>
 * The Parser class is a utility class for converting an Object to a specified Class of type T.
 * <p>
 * Fields
 * parsingMap - a static final Map of Class objects to Functions for parsing Objects
 * to the specified Class.
 * <p>
 * Constructor
 * Parser - a private constructor for the Parser class.
 */
public class Parser {
    private static final Map<Class<?>, Function<Object, ?>> parsingMap = new HashMap<>();
    private static final String FROM_DATE_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    static {
        parsingMap.put(Object.class, obj -> obj);
        parsingMap.put(Boolean.class, Parser::toBoolean);
        parsingMap.put(Byte.class, Parser::toByte);
        parsingMap.put(Short.class, Parser::toShort);
        parsingMap.put(Integer.class, Parser::toInteger);
        parsingMap.put(Long.class, Parser::toLong);
        parsingMap.put(Float.class, Parser::toFloat);
        parsingMap.put(Double.class, Parser::toDouble);
        parsingMap.put(String.class, Parser::toString);
        parsingMap.put(LocalDate.class, obj -> toLocalDate(obj, null));
        parsingMap.put(LocalTime.class, obj -> toLocalTime(obj, null));
        parsingMap.put(LocalDateTime.class, obj -> toLocalDateTime(obj, null));
        parsingMap.put(ZonedDateTime.class, obj -> toZonedDateTime(obj, null));
    }

    /**
     * Constructor for the Parser class.
     */
    private Parser() {
    }

    /**
     * Converts the given `Object` to a specified `Class` of type `T`.
     *
     * @param obj    The `Object` to be converted.
     * @param clazz  The target `Class` of type `T`.
     * @param clazz2 The component `Class` of type `V` for `List` or `Map` types.
     * @param <T>    The type of the target class.
     * @param <V>    The type of the component class for `List` or `Map` types.
     * @return The converted `Object` of type `T`.
     * @throws ParsingException If the conversion fails due to missing constructor or unsupported type.
     */
    @SuppressWarnings("unchecked")
    protected static <T, V> T to(Object obj, Class<T> clazz, Class<V> clazz2) {
        if (obj == null)
            return null;

        Object value;
        var listMapComponentType = clazz2 != null ? clazz2 : Object.class;

        if (parsingMap.containsKey(clazz))
            value = parsingMap.get(clazz).apply(obj);
        else if (clazz.isAssignableFrom(List.class))
            value = toList(obj, listMapComponentType);
        else if (clazz.isAssignableFrom(Map.class))
            value = toMap(obj, listMapComponentType);
        else if (clazz.isArray())
            value = toArray(obj, (Class<T[]>) clazz);
        else if (clazz.isEnum())
            value = toEnum(obj, clazz);
        else {
            try {
                value = toCustomObj(obj, clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new ParsingException("Can't create an instance of [" + clazz.getName() + "]," +
                        " please make sure that this class has no-arguments constructor", e);
            }
        }

        return clazz.cast(value);
    }

    /**
     * Converts the given `Object` to a `Boolean`.
     *
     * @param obj The `Object` to be converted.
     * @return The converted `Boolean`.
     * @throws ParsingException If the conversion fails.
     */
    protected static Boolean toBoolean(Object obj) {
        Boolean value;

        if (obj == null)
            value = null;
        else if (obj instanceof Boolean bool)
            value = bool;
        else if (obj instanceof String str)
            value = Boolean.valueOf(str);
        else
            throw new ParsingException(parsingErrorMsg(obj, "Boolean"));

        return value;
    }

    /**
     * Converts an object to a Byte.
     *
     * @param obj The object to be converted to a Byte.
     * @return The resulting Byte, or null if the input object is null.
     * @throws ParsingException if the input object can't be parsed to a Byte.
     */
    protected static Byte toByte(Object obj) {
        Byte value;

        if (obj == null)
            value = null;
        else if (obj instanceof Number num)
            value = num.byteValue();
        else if (obj instanceof String str)
            try {
                value = Byte.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParsingException(parsingErrorMsg(obj, "Byte"), e);
            }
        else
            throw new ParsingException(parsingErrorMsg(obj, "Byte"));

        return value;
    }

    /**
     * Converts an object to a Short.
     *
     * @param obj The object to be converted to a Short.
     * @return The resulting Short, or null if the input object is null.
     * @throws ParsingException if the input object can't be parsed to a Short.
     */
    protected static Short toShort(Object obj) {
        Short value;

        if (obj == null)
            value = null;
        else if (obj instanceof Number num)
            value = num.shortValue();
        else if (obj instanceof String str)
            try {
                value = Short.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParsingException(parsingErrorMsg(obj, "Short"), e);
            }
        else
            throw new ParsingException(parsingErrorMsg(obj, "Short"));

        return value;
    }

    /**
     * Converts an object to an Integer.
     *
     * @param obj The object to be converted to an Integer.
     * @return The resulting Integer, or null if the input object is null.
     * @throws ParsingException if the input object can't be parsed to an Integer.
     */
    protected static Integer toInteger(Object obj) {
        Integer value;

        if (obj == null)
            value = null;
        else if (obj instanceof Number num)
            value = num.intValue();
        else if (obj instanceof String str)
            try {
                value = Integer.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParsingException(parsingErrorMsg(obj, "Integer"), e);
            }
        else
            throw new ParsingException(parsingErrorMsg(obj, "Integer"));

        return value;
    }

    /**
     * Converts an object to a Long.
     *
     * @param obj The object to be converted to a Long.
     * @return The resulting Long, or null if the input object is null.
     * @throws ParsingException if the input object can't be parsed to a Long.
     */
    protected static Long toLong(Object obj) {
        Long value;

        if (obj == null)
            value = null;
        else if (obj instanceof Number num)
            value = num.longValue();
        else if (obj instanceof String str) {
            try {
                value = Long.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParsingException(parsingErrorMsg(obj, "Long"), e);
            }
        } else
            throw new ParsingException(parsingErrorMsg(obj, "Long"));

        return value;
    }

    /**
     * Converts an object to a Float.
     *
     * @param obj The object to be converted to a Float.
     * @return The resulting Float, or null if the input object is null.
     * @throws ParsingException if the input object can't be parsed to a Float.
     */
    protected static Float toFloat(Object obj) {
        Float value;

        if (obj == null)
            value = null;
        else if (obj instanceof Number num)
            value = num.floatValue();
        else if (obj instanceof String str)
            try {
                value = Float.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParsingException(parsingErrorMsg(obj, "Float"), e);
            }
        else
            throw new ParsingException(parsingErrorMsg(obj, "Float"));

        return value;
    }

    /**
     * Converts an object to a Double.
     *
     * @param obj The object to be converted to a Double.
     * @return The resulting Double, or null if the input object is null.
     * @throws ParsingException if the input object can't be parsed to a Double.
     */
    protected static Double toDouble(Object obj) {
        Double value;

        if (obj == null)
            value = null;
        else if (obj instanceof Number num)
            value = num.doubleValue();
        else if (obj instanceof String str)
            try {
                value = Double.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParsingException(parsingErrorMsg(obj, "Double"), e);
            }
        else
            throw new ParsingException(parsingErrorMsg(obj, "Double"));

        return value;
    }

    /**
     * Converts an object to a String.
     *
     * @param obj The object to be converted to a String.
     * @return The resulting String, or null if the input object is null.
     */
    protected static String toString(Object obj) {
        return obj != null ? String.valueOf(obj) : null;
    }

    /**
     * Converts the given `obj` to a `LocalDate` object based on the specified `pattern`.
     *
     * @param obj     the object to be converted to `LocalDate`
     * @param pattern the pattern to be used for parsing the `obj` to `LocalDate`
     * @return the converted `LocalDate` object, or `null` if the `obj` is `null`
     * @throws DateTimeParseException if the string representation of `obj` cannot be parsed to `LocalDate`
     */
    protected static LocalDate toLocalDate(Object obj, String pattern) {
        if (obj == null)
            return null;

        var objStr = toString(obj);
        try {
            return LocalDate.parse(objStr,
                            DateTimeFormatter.ofPattern(FROM_DATE_PATTERN).withLocale(Locale.US))
                    .plusDays(OffsetDateTime.now().getOffset().getTotalSeconds() < 0 ? 1 : 0);
        } catch (DateTimeParseException ignore) {
            // Ignore the exception, this to handle the dates that read as Date object by default
        }

        AtomicReference<LocalDate> localDate = new AtomicReference<>();
        if (pattern != null)
            localDate.set(LocalDate.parse(objStr, DateTimeFormatter.ofPattern(pattern)));
        else {
            ConfigHandler.getInstance().getDateConfigDatePattern().ifPresentOrElse(
                    p -> localDate.set(LocalDate.parse(objStr, DateTimeFormatter.ofPattern(p))),
                    () -> localDate.set(LocalDate.parse(objStr)));
        }

        return localDate.get();
    }

    /**
     * Convert the given object to a LocalTime object.
     *
     * @param obj     The object to be converted to a LocalTime object.
     * @param pattern The date pattern to be used for parsing the object.
     * @return The converted LocalTime object. Returns null if the input object is null.
     */
    protected static LocalTime toLocalTime(Object obj, String pattern) {
        if (obj == null)
            return null;

        var localTime = new AtomicReference<LocalTime>();

        if (obj instanceof Integer objInt) {
            localTime.set(LocalTime.ofSecondOfDay(objInt));
        } else {
            var objStr = toString(obj);
            if (pattern != null)
                localTime.set(LocalTime.parse(objStr, DateTimeFormatter.ofPattern(pattern)));
            else {
                ConfigHandler.getInstance().getDateConfigTimePattern().ifPresentOrElse(
                        p -> localTime.set(LocalTime.parse(objStr, DateTimeFormatter.ofPattern(p))),
                        () -> localTime.set(LocalTime.parse(objStr)));
            }
        }

        return localTime.get();
    }

    /**
     * Convert the given object to a LocalDateTime object.
     *
     * @param obj     The object to be converted to a LocalDateTime object.
     * @param pattern The date pattern to be used for parsing the object.
     * @return The converted LocalDateTime object. Returns null if the input object is null.
     */
    protected static LocalDateTime toLocalDateTime(Object obj, String pattern) {
        if (obj == null)
            return null;

        var objStr = toString(obj);
        try {
            return LocalDateTime.parse(objStr,
                            DateTimeFormatter.ofPattern(FROM_DATE_PATTERN).withLocale(Locale.US))
                    .minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
        } catch (DateTimeParseException ignore) {
            // Ignore the exception, this to handle the dates that read as Date object by default
        }

        AtomicReference<LocalDateTime> localDateTime = new AtomicReference<>();
        if (pattern != null)
            localDateTime.set(LocalDateTime.parse(objStr, DateTimeFormatter.ofPattern(pattern)));
        else {
            ConfigHandler.getInstance().getDateConfigDateTimePattern().ifPresentOrElse(
                    p -> localDateTime.set(LocalDateTime.parse(objStr, DateTimeFormatter.ofPattern(p))),
                    () -> localDateTime.set(LocalDateTime.parse(objStr)));
        }

        return localDateTime.get();
    }

    /**
     * Convert the given object to a ZonedDateTime object.
     *
     * @param obj     The object to be converted to a ZonedDateTime object.
     * @param pattern The date pattern to be used for parsing the object.
     * @return The converted ZonedDateTime object. Returns null if the input object is null.
     */
    protected static ZonedDateTime toZonedDateTime(Object obj, String pattern) {
        if (obj == null)
            return null;

        var objStr = toString(obj);
        try {
            return ZonedDateTime.parse(objStr,
                    DateTimeFormatter.ofPattern(FROM_DATE_PATTERN).withLocale(Locale.US));
        } catch (DateTimeParseException ignore) {
            // Ignore the exception, this to handle the dates that read as Date object by default
        }

        var zonedDateTime = new AtomicReference<ZonedDateTime>();
        if (pattern == null) {
            ConfigHandler.getInstance().getDateConfigZonedPattern().ifPresentOrElse(
                    p -> zonedDateTime.set(ZonedDateTime.parse(objStr, DateTimeFormatter.ofPattern(p))),
                    () -> zonedDateTime.set(ZonedDateTime.parse(objStr)));
        } else
            zonedDateTime.set(ZonedDateTime.parse(objStr, DateTimeFormatter.ofPattern(pattern)));

        return zonedDateTime.get();
    }

    /**
     * Convert the given object to a list of objects of the specified class.
     *
     * @param obj   The object to be converted to a list.
     * @param clazz The class of the objects in the list.
     * @param <T>   The type of the objects in the list.
     * @return The converted list of objects.
     * @throws ParsingException if the input object cannot be converted to a list.
     */
    protected static <T> List<T> toList(Object obj, Class<T> clazz) {
        if (obj instanceof List<?> list) {
            var nList = new ArrayList<T>();
            list.forEach(
                    item -> nList.add(to(item, clazz, null))
            );
            return nList;
        }

        throw new ParsingException("This object [" + obj + "] can't be list");
    }

    /**
     * Convert the given object to a map of keys and values of the specified class.
     *
     * @param obj   The object to be converted to a map.
     * @param clazz The class of the values in the map.
     * @param <T>   The type of the values in the map.
     * @return The converted map of keys and values.
     * @throws ParsingException if the input object cannot be converted to a map.
     */
    protected static <T> Map<String, T> toMap(Object obj, Class<T> clazz) {
        if (obj instanceof Map<?, ?> map) {
            var nMap = new HashMap<String, T>();
            map.forEach(
                    (k, v) -> nMap.put(k.toString(), to(v, clazz, null))
            );
            return nMap;
        }

        throw new ParsingException("This object [" + obj + "] can't be Map");
    }

    /**
     * Converts the given `obj` to an array of the specified type `clazz`.
     *
     * @param obj   the object to be converted
     * @param clazz the class representing the type of the array to be returned
     * @param <T>   the type of the array elements
     * @return an array of the specified type `clazz`
     * @throws ParsingException if the given `obj` can't be converted to a list
     */
    private static <T> T[] toArray(Object obj, Class<T[]> clazz) {
        var arr = toList(obj, clazz.getComponentType()).toArray();

        return Arrays.copyOf(arr, arr.length, clazz);
    }

    /**
     * Converts an object to a custom object of the given instance.
     *
     * @param obj  Object to be converted.
     * @param inst Instance of the custom object to convert the object to.
     * @param <T>  The type of the custom object.
     * @return A custom object of the given instance.
     * @throws ParsingException If the conversion fails, or if the instance cannot be created.
     */
    private static <T> T toCustomObj(Object obj, T inst) {
        var fields = inst.getClass().getDeclaredFields();
        Map<String, Object> map;
        try {
            map = toMap(obj, Object.class);
        } catch (ParsingException ignore) {
            throw new ParsingException(parsingErrorMsg(obj, inst.getClass().toString()));
        }

        map = Helper.changeEnv(map);

        for (Field field : fields) {
            field.setAccessible(true);
            var name = field.getName();
            var type = field.getType();
            var alisa = field.isAnnotationPresent(Alias.class) ?
                    field.getAnnotation(Alias.class).value() : null;

            String nameInYAML = null;
            if ((alisa != null && map.containsKey(alisa)))
                nameInYAML = alisa;
            else if (map.containsKey(name))
                nameInYAML = name;

            if (nameInYAML != null) {
                if (type.isPrimitive())
                    throw new ParsingException("Primitive types are not supported please use wrapper classes instead. " +
                            "at [" + name + " " + type.getName() + "]");

                try {
                    var value = to(map.get(nameInYAML), type, getListMapArgument(field, type));
                    field.set(inst, value);
                } catch (ParsingException e) {
                    throw new ParsingException("Can't set this value [" + map.get(nameInYAML) + "] for this field [" +
                            name + " " + type.getName() + "]", e);
                } catch (IllegalAccessException e) {
                    throw new ParsingException("Can't access this field [" + name + type.getName() + "]", e);
                }
            }
        }

        return inst;
    }

    /**
     * Convert the given `obj` to the specified enum class `clazz`.
     *
     * @param obj   The object to be converted.
     * @param clazz The target enum class.
     * @param <T>   The type of the target enum class.
     * @return The converted enum object.
     * @throws ParsingException If the specified enum class does not contain the value of the given `obj`, or if there is a problem invoking the conversion.
     */
    private static <T> T toEnum(Object obj, Class<T> clazz) {
        try {
            return clazz.cast(clazz.getMethod("valueOf", String.class)
                    .invoke(null, toString(obj)));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ParsingException("Ensure that this Enum [" + clazz.getName() +
                    "] contains this value [" + obj + "]", e);
        }
    }

    /**
     * This method retrieves the argument class type of List or Map generic type.
     *
     * @param field - The field that we want to retrieve its argument class type.
     * @param type  - The type of the field, either a List or Map.
     * @return the argument class type of the generic List or Map type.
     * @throws ParsingException if the field is not a List or Map generic type.
     */
    private static Class<?> getListMapArgument(Field field, Class<?> type) {
        var isList = type.isAssignableFrom(List.class);

        if (!isList && !type.isAssignableFrom(Map.class))
            return null;

        return ((Class<?>) ((ParameterizedType) field.getGenericType())
                .getActualTypeArguments()[isList ? 0 : 1]);
    }

    private static String parsingErrorMsg(Object obj, String type) {
        return "Can't parse [" + obj + "] to be " + type;
    }
}
