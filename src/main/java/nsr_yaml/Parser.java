package nsr_yaml;

import annotation.Alias;
import exception.ParsingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Parser {
    private static final Map<Class<?>, Function<Object, ?>> parsingMap = new HashMap<>();

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

    private Parser() {
    }

    @SuppressWarnings("unchecked")
    protected static <T, V> T to(Object obj, Class<T> clazz, Class<V> clazz2) {
        if (obj == null)
            return null;

        Object value;
        var listMapComponentType = clazz2 != null ? clazz2 : Object.class;

        if (parsingMap.containsKey(clazz))
            value  = parsingMap.get(clazz).apply(obj);
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
                throw new ParsingException("Can't create an instance of [" + clazz.getName() + "], please make sure that " +
                        "this class has no-arguments constructor", e);
            }
        }

        return clazz.cast(value);
    }

    /**
     * Parsing {@link Object} to {@link Boolean}
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
            throw new ParsingException("Can't parse [" + obj + "] to be Boolean");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link Byte}
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
                throw new ParsingException("Can't parse [" + obj + "] to be Byte", e);
            }
        else
            throw new ParsingException("Can't parse [" + obj + "] to be Byte");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link Short}
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
                throw new ParsingException("Can't parse [" + obj + "] to be Short", e);
            }
        else
            throw new ParsingException("Can't parse [" + obj + "] to be Short");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link Integer}
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
                throw new ParsingException("Can't parse [" + obj + "] to be Integer", e);
            }
        else
            throw new ParsingException("Can't parse [" + obj + "] to be Integer");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link Long}
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
                throw new ParsingException("Can't parse [" + obj + "] to be Long", e);
            }
        } else
            throw new ParsingException("Can't parse [" + obj + "] to be Long");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link Float}
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
                throw new ParsingException("Can't parse [" + obj + "] to be Float",  e);
            }
        else
            throw new ParsingException("Can't parse [" + obj + "] to be Float");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link Double}
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
                throw new ParsingException("Can't parse [" + obj + "] to be Double", e);
            }
        else
            throw new ParsingException("Can't parse [" + obj + "] to be Double");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link String}
     */
    protected static String toString(Object obj) {
        return obj != null ? String.valueOf(obj) : null;
    }

    protected static LocalDate toLocalDate(Object obj, String pattern) {
        if (obj == null)
            return null;

        var objStr = toString(obj);
        try {
            return LocalDate.parse(objStr,
                            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").withLocale(Locale.US))
                    .plusDays(OffsetDateTime.now().getOffset().getTotalSeconds() < 0 ? 1 : 0);
        } catch (DateTimeParseException ignore) {
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

    protected static LocalDateTime toLocalDateTime(Object obj, String pattern) {
        if (obj == null)
            return null;

        var objStr = toString(obj);
        try {
            return LocalDateTime.parse(objStr,
                            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").withLocale(Locale.US))
                    .minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
        } catch (DateTimeParseException ignore) {
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

    protected static ZonedDateTime toZonedDateTime(Object obj, String pattern) {
        if (obj == null)
            return null;

        var objStr = toString(obj);
        try {
            return ZonedDateTime.parse(objStr,
                    DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").withLocale(Locale.US));
        } catch (DateTimeParseException ignore) {
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

    private static <T> T[] toArray(Object obj, Class<T[]> clazz) {
        var arr = toList(obj, clazz.getComponentType()).toArray();

        return Arrays.copyOf(arr, arr.length, clazz);
    }

    private static <T> T toCustomObj(Object data, T inst) {
        var fields = inst.getClass().getDeclaredFields();
        Map<String, Object> map;
        try {
            map = toMap(data, Object.class);
        } catch (ParsingException ignore) {
            throw new ParsingException("Can't parse [" + data + "] to be " + inst.getClass());
        }

        for (Field field : fields) {
            field.setAccessible(true);
            var name = field.getName();
            var type = field.getType();
            var alisa = field.isAnnotationPresent(Alias.class) ?
                    field.getAnnotation(Alias.class).value() : null;


            var nameInYAML = (alisa != null && map.containsKey(alisa)) ?
                    alisa : map.containsKey(name) ?
                    name : null;

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

    private static <T> T toEnum(Object obj, Class<T> clazz) {
        try {
            return clazz.cast(clazz.getMethod("valueOf", String.class)
                    .invoke(null, toString(obj)));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ParsingException("Ensure that this Enum [" + clazz.getName() +
                    "] contains this value [" + obj + "]", e);
        }
    }

    private static Class<?> getListMapArgument(Field field, Class<?> type) {
        var isList = type.isAssignableFrom(List.class);

        if (!isList && !type.isAssignableFrom(Map.class))
            return null;

        return ((Class<?>) ((ParameterizedType) field.getGenericType())
                .getActualTypeArguments()[isList ? 0 : 1]);
    }
}
