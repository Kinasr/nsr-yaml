package nsr_yaml;

import exception.ParsingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Parser {

    private Parser() {
    }

    protected static final ConfigHandler config = ConfigHandler.getInstance();

    protected static <T, V> AsObject<T> to(Object obj, Class<T> clazz, Class<V> vClass) {
        if (obj == null || clazz.isAssignableFrom(Object.class))
            return new AsObject<>(clazz.cast(obj), false);

        T value = objToPrimitive(obj, clazz);
        if (value != null)
            return new AsObject<>(value, false);

        // to date
        if (clazz.isAssignableFrom(LocalDate.class))
            return new AsObject<>(clazz.cast(toLocalDate(obj, null)), false);
        else if (clazz.isAssignableFrom(LocalTime.class))
            return new AsObject<>(clazz.cast(toLocalTime(obj, null)), false);
        else if (clazz.isAssignableFrom(LocalDateTime.class))
            return new AsObject<>(clazz.cast(toLocalDateTime(obj, null)), false);
        else if (clazz.isAssignableFrom(ZonedDateTime.class))
            return new AsObject<>(clazz.cast(toZonedDateTime(obj, null)), false);

        var c = vClass != null ? vClass : Object.class;
        // to list
        if (clazz.isAssignableFrom(List.class))
            return new AsObject<>(clazz.cast(toList(obj, c)), false);
        // to map
        if (clazz.isAssignableFrom(Map.class))
            return new AsObject<>(clazz.cast(toMap(obj, c)), false);

        // to custom object
        try {
            value = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // throwing exception

        return new AsObject<>(value, true);
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
                throw new ParsingException("Can't parse [" + obj + "] to be Byte -- " + e);
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
                throw new ParsingException("Can't parse [" + obj + "] to be Short -- " + e);
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
                throw new ParsingException("Can't parse [" + obj + "] to be Integer -- " + e);
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
                throw new ParsingException("Can't parse [" + obj + "] to be Long -- " + e);
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
                throw new ParsingException("Can't parse [" + obj + "] to be Float -- " + e);
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
                throw new ParsingException("Can't parse [" + obj + "] to be Double -- " + e);
            }
        else
            throw new ParsingException("Can't parse [" + obj + "] to be Double");

        return value;
    }

    /**
     * Parsing {@link Object} to {@link String}
     */
    protected static String objToString(Object obj) {
        return obj != null ? String.valueOf(obj) : null;
    }

    protected static Date toDate(Object obj) {
        if (obj instanceof Date d)
            return d;

        return null;
    }

    protected static LocalDate toLocalDate(Object obj, String pattern) {
        var date = toDate(obj);
        if (date != null)
            return date.toInstant()
                    .atZone(config.getDateConfigZoneId().isPresent() ? ZoneId.of(config.getDateConfigZoneId().get()) :
                            ZoneId.systemDefault())
                    .toLocalDate();

        var objStr = objToString(obj);
        AtomicReference<LocalDate> localDate = new AtomicReference<>();
        if (pattern != null)
            localDate.set(LocalDate.parse(objStr, DateTimeFormatter.ofPattern(pattern)));
        else {
            config.getDateConfigDatePattern().ifPresentOrElse(
                    p -> localDate.set(LocalDate.parse(objStr, DateTimeFormatter.ofPattern(p))),
                    () -> localDate.set(LocalDate.parse(objStr)));
        }

        return localDate.get();
    }

    protected static LocalTime toLocalTime(Object obj, String pattern) {
        var date = toDate(obj);
        if (date != null)
            return date.toInstant()
                    .atZone(config.getDateConfigZoneId().isPresent() ? ZoneId.of(config.getDateConfigZoneId().get()) :
                            ZoneId.systemDefault())
                    .toLocalTime();

        var objStr = objToString(obj);
        var localTime = new AtomicReference<LocalTime>();
        if (pattern != null)
            localTime.set(LocalTime.parse(objStr, DateTimeFormatter.ofPattern(pattern)));
        else {
            config.getDateConfigTimePattern().ifPresentOrElse(
                    p -> localTime.set(LocalTime.parse(objStr, DateTimeFormatter.ofPattern(p))),
                    () -> localTime.set(LocalTime.parse(objStr)));
        }

        return localTime.get();
    }

    protected static LocalDateTime toLocalDateTime(Object obj, String pattern) {
        var date = toDate(obj);
        if (date != null)
            return date.toInstant()
                    .atZone(config.getDateConfigZoneId().isPresent() ? ZoneId.of(config.getDateConfigZoneId().get()) :
                            ZoneId.systemDefault())
                    .toLocalDateTime();

        var objStr = objToString(obj);
        AtomicReference<LocalDateTime> localDateTime = new AtomicReference<>();
        if (pattern != null)
            localDateTime.set(LocalDateTime.parse(objStr, DateTimeFormatter.ofPattern(pattern)));
        else {
            config.getDateConfigDateTimePattern().ifPresentOrElse(
                    p -> localDateTime.set(LocalDateTime.parse(objStr, DateTimeFormatter.ofPattern(p))),
                    () -> localDateTime.set(LocalDateTime.parse(objStr)));
        }

        return localDateTime.get();
    }

    protected static ZonedDateTime toZonedDateTime(Object obj, String pattern) {
        var date = toDate(obj);
        if (date != null)
            return date.toInstant()
                    .atZone(config.getDateConfigZoneId().isPresent() ? ZoneId.of(config.getDateConfigZoneId().get()) :
                            ZoneId.systemDefault());

        var objStar = objToString(obj);
        var zonedDateTime = new AtomicReference<ZonedDateTime>();
        if (pattern == null) {
            config.getDateConfigZonedPattern().ifPresentOrElse(
                    p -> zonedDateTime.set(ZonedDateTime.parse(objStar, DateTimeFormatter.ofPattern(p))),
                    () -> zonedDateTime.set(ZonedDateTime.parse(objStar)));
        } else
            zonedDateTime.set(ZonedDateTime.parse(objStar, DateTimeFormatter.ofPattern(pattern)));

        return zonedDateTime.get();
    }

    protected static <T> List<T> toList(Object obj, Class<T> clazz) {
        if (obj instanceof List<?> list) {
            return list.stream()
                    .map(item -> to(item, clazz, null).obj()).toList();
        }

        throw new ParsingException("This object [" + obj + "] can't be list");
    }

    protected static <T> Map<String, T> toMap(Object obj, Class<T> clazz) {
        if (obj instanceof Map<?, ?> map) {
            return map.keySet()
                    .stream().collect(Collectors.toMap(Parser::objToString,
                            k -> to(map.get(k), clazz, null).obj()));
        }

        throw new ParsingException("This object [" + obj + "] can't be list");
    }

    protected static <T> T toCustomObj(Object data, T inst) {
        var fields = inst.getClass().getDeclaredFields();
        var map = toMap(data, Object.class);

        // todo: support custom names like tt-bb
        for (Field field : fields) {
            field.setAccessible(true);
            var name = field.getName();
            var type = field.getType();

            if (map.containsKey(name)) {
                var asObj = to(map.get(name), type, getListMapArgument(field, type));

                Object value;
                if (asObj.isCustomObj())
                    value = toCustomObj(map.get(name), asObj.obj());
                else
                    value = asObj.obj();

                try {
                    field.set(inst, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return inst;
    }

    private static <T> T objToPrimitive(Object obj, Class<T> clazz) {
        if (clazz.isAssignableFrom(String.class))
            return clazz.cast(String.valueOf(obj));

        T value = null;

        try {
            var valueOf = clazz.getMethod("valueOf", String.class);
            value = clazz.cast(valueOf.invoke(clazz, obj.toString()));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
        }

        return value;
    }

    private static Class<?> getListMapArgument(Field field, Class<?> type) {
        var isList = type.isAssignableFrom(List.class);

        if (!isList && !type.isAssignableFrom(Map.class))
            return null;

        return ((Class<?>) ((ParameterizedType) field.getGenericType())
                .getActualTypeArguments()[isList ? 0 : 1]);
    }
}
