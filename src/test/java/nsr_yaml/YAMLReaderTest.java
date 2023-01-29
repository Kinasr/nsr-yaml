package nsr_yaml;

import helper.Gender;
import helper.Person;
import helper.Pet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class YAMLReaderTest {
    @Mock
    ConfigHandler configHandler;

    @Nested
    class All {
        @Test
        void getAllAsObject() {
            var data = Map.of("object", "object");

            assertThat(new YAMLReader(data, new ObjMapper(true)).get().asObject())
                    .isInstanceOf(Object.class)
                    .isEqualTo(data);
        }

        @Test
        void getAllAsString() {
            var data = Map.of("object", "object");

            assertThat(new YAMLReader(data, new ObjMapper(true)).get().asString())
                    .isInstanceOf(String.class)
                    .isEqualTo("{object=object}");
        }

        @Test
        void getAllAsMap() {
            var data = Map.of("object", "object");

            assertThat(new YAMLReader(data, new ObjMapper(true)).get().asMap())
                    .isInstanceOf(Map.class)
                    .isEqualTo(data);
        }

        @Test
        void getAllAsMapOfIntegers() {
            var data = Map.of("a", 0, "b", 1);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get().asMap(Integer.class))
                    .isInstanceOf(Map.class)
                    .isEqualTo(data);
        }

        @Test
        void getAllAsList() {
            var data = List.of("object", "object");

            assertThat(new YAMLReader(data, new ObjMapper(true)).get().asList())
                    .isInstanceOf(List.class)
                    .isEqualTo(data);
        }

        @Test
        void getAllAsListOfIntegers() {
            var data = List.of(0, 1, 2);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get().asList(Integer.class))
                    .isInstanceOf(List.class)
                    .isEqualTo(data);
        }

        @Nested
        class AsMethod {
            @Test
            void getAllAsObject() {
                var data = Map.of("object", "object");

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(Object.class))
                        .isInstanceOf(Object.class)
                        .isEqualTo(data);
            }

            @Test
            void getAllAsString() {
                var data = Map.of("object", "object");

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(String.class))
                        .isInstanceOf(String.class)
                        .isEqualTo("{object=object}");
            }

            @Test
            @SuppressWarnings("unchecked")
            void getAllAsMap() {
                var data = Map.of("object", "object");

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(Map.class))
                        .isInstanceOf(Map.class)
                        .isEqualTo(data);
            }

            @Test
            @SuppressWarnings("unchecked")
            void getAllAsMapOfIntegers() {
                var data = Map.of("a", 0, "b", 1);

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(Map.class))
                        .isInstanceOf(Map.class)
                        .isEqualTo(data);
            }

            @Test
            @SuppressWarnings("unchecked")
            void getAllAsList() {
                var data = List.of("object", "object");

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(List.class))
                        .isInstanceOf(List.class)
                        .isEqualTo(data);
            }

            @Test
            @SuppressWarnings("unchecked")
            void getAllAsSet() {
                var data = List.of("object", "object");

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(HashSet.class))
                        .isInstanceOf(HashSet.class)
                        .isEqualTo(data);
            }

            @Test
            @SuppressWarnings("unchecked")
            void getAllAsListOfIntegers() {
                var data = List.of(0, 1, 2);

                assertThat(new YAMLReader(data, new ObjMapper(true)).get().as(List.class))
                        .isInstanceOf(List.class)
                        .isEqualTo(data);
            }
        }
    }

    @Nested
    class Key {
        // region Object
        @Test
        void getObjectFromMap() {
            var data = Map.of(
                    "text", "test"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("text").asObject())
                    .isInstanceOf(Object.class)
                    .isEqualTo("test");
        }

        @Test
        void getObjectFromList() {
            var data = List.of("obj1", "obj2");

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("[0]").asObject())
                    .isInstanceOf(Object.class)
                    .isEqualTo("obj1");
        }

        @Test
        void getObjectFromMapAndList() {
            var data = Map.of(
                    "a", List.of(1, 2, 3)
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("a.[0]").asObject())
                    .isInstanceOf(Object.class)
                    .isEqualTo(1);
        }

        @Test
        void getObjectFromListAndMap() {
            var data = List.of(
                    Map.of("a", "A")
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("[0].a").asObject())
                    .isInstanceOf(Object.class)
                    .isEqualTo("A");
        }

        @Test
        void getListObject() {
            var d = List.of("obj1", "obj2");
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList())
                    .isInstanceOf(Object.class)
                    .isEqualTo(d);
        }

        @Test
        void getMapObject() {
            var d = Map.of("o1", "obj1", "o2", "obj2");
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap())
                    .isInstanceOf(Object.class)
                    .isEqualTo(d);
        }

        @Test
        void getNullObject() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asObject() == null)
                    .isTrue();
        }
        // endregion

        // region String
        @Test
        void getString() {
            var data = Map.of(
                    "text", "test"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("text").asString())
                    .isInstanceOf(String.class)
                    .isEqualTo("test");
        }

        @Test
        void getLongString() {
            var data = Map.of(
                    "text", """
                            this is not a normal string it
                            spans more than
                            one line
                            see?
                            """
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("text").asString())
                    .isInstanceOf(String.class)
                    .isEqualTo("""
                            this is not a normal string it
                            spans more than
                            one line
                            see?
                            """);
        }

        @Test
        void getNumberAsString() {
            var data = Map.of(
                    "number", 10
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asString())
                    .isInstanceOf(String.class)
                    .isEqualTo("10");
        }

        @Test
        void getListString() {
            var d = List.of("obj1", "obj2");
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(String.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapString() {
            var d = Map.of("o1", "obj1", "o2", "obj2");
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(String.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullString() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asString() == null)
                    .isTrue();
        }
        // endregion

        //  region Boolean
        @Test
        void getBoolean01() {
            var data = Map.of(
                    "boolean", true
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("boolean").asBoolean())
                    .isInstanceOf(Boolean.class)
                    .isTrue();
        }

        @Test
        void getBoolean02() {
            var data = Map.of(
                    "boolean", "off"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("boolean").asBoolean())
                    .isInstanceOf(Boolean.class)
                    .isFalse();
        }

        @Test
        void getListBoolean() {
            var d = List.of(true, false);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Boolean.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapBoolean() {
            var d = Map.of("o1", true, "o2", false);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Boolean.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullBoolean() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asBoolean() == null)
                    .isTrue();
        }
        // endregion

        // region Byte
        @Test
        void getByte() {
            var data = Map.of(
                    "number", 100
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asByte())
                    .isInstanceOf(Byte.class)
                    .isEqualTo((byte) 100);
        }

        @Test
        void getByteHex() {
            var data = Map.of(
                    "number", 0xF
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asByte())
                    .isInstanceOf(Byte.class)
                    .isEqualTo((byte) 15);
        }

        @Test
        void getByteOctal() {
            var data = Map.of(
                    "number", 017
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asByte())
                    .isInstanceOf(Byte.class)
                    .isEqualTo((byte) 15);
        }

        @Test
        void getListByte() {
            var d = List.of((byte) 0, (byte) 10);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Byte.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapByte() {
            var d = Map.of("o1", (byte) 0, "o2", (byte) 10);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Byte.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullByte() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asByte() == null)
                    .isTrue();
        }
        // endregion

        // region Short
        @Test
        void getShort() {
            var data = Map.of(
                    "number", 100
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asShort())
                    .isInstanceOf(Short.class)
                    .isEqualTo((short) 100);
        }

        @Test
        void getShortHex() {
            var data = Map.of(
                    "number", 0x12d4
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asShort())
                    .isInstanceOf(Short.class)
                    .isEqualTo((short) 4820);
        }

        @Test
        void getShortOctal() {
            var data = Map.of(
                    "number", 023332
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asShort())
                    .isInstanceOf(Short.class)
                    .isEqualTo((short) 9946);
        }

        @Test
        void getListShort() {
            var d = List.of((short) 10, (short) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Short.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapShort() {
            var d = Map.of("o1", (short) 10, "o2", (short) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Short.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullShort() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asShort() == null)
                    .isTrue();
        }
        // endregion

        // region Integer
        @Test
        void getInteger() {
            var data = Map.of(
                    "number", 100
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asInteger())
                    .isInstanceOf(Integer.class)
                    .isEqualTo(100);
        }

        @Test
        void getIntegerHex() {
            var data = Map.of(
                    "number", 0x12d4
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asInteger())
                    .isInstanceOf(Integer.class)
                    .isEqualTo(4820);
        }

        @Test
        void getIntegerOctal() {
            var data = Map.of(
                    "number", 023332
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asInteger())
                    .isInstanceOf(Integer.class)
                    .isEqualTo(9946);
        }

        @Test
        void getListInteger() {
            var d = List.of(10, 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Integer.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapInteger() {
            var d = Map.of("o1", 10, "o2", 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Integer.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullInteger() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asInteger() == null)
                    .isTrue();
        }
        // endregion

        // region Long
        @Test
        void getLong() {
            var data = Map.of(
                    "number", 100
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asLong())
                    .isInstanceOf(Long.class)
                    .isEqualTo(100);
        }

        @Test
        void getLongHex() {
            var data = Map.of(
                    "number", 0x12d4
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asLong())
                    .isInstanceOf(Long.class)
                    .isEqualTo(4820);
        }

        @Test
        void getLongOctal() {
            var data = Map.of(
                    "number", 023332
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asLong())
                    .isInstanceOf(Long.class)
                    .isEqualTo(9946);
        }

        @Test
        void getListLong() {
            var d = List.of((long) 10, (long) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Long.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapLong() {
            var d = Map.of("o1", (long) 10, "o2", (long) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Long.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullLong() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asLong() == null)
                    .isTrue();
        }
        // endregion

        // region Float
        @Test
        void getFloat() {
            var data = Map.of(
                    "number", 5.3
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asFloat())
                    .isInstanceOf(Float.class)
                    .isEqualTo(5.3f);
        }

        @Test
        void getFloatEx() {
            var data = Map.of(
                    "number", 12.3015e+05
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asFloat())
                    .isInstanceOf(Float.class)
                    .isEqualTo(1230150.0f);
        }

        @Test
        void getFloatInfinity() {
            var data = Map.of(
                    "number", "Infinity"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asFloat())
                    .isInstanceOf(Float.class)
                    .isEqualTo(Float.POSITIVE_INFINITY);
        }

        @Test
        void getFloatNegativeInfinity() {
            var data = Map.of(
                    "number", "-Infinity"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asFloat())
                    .isInstanceOf(Float.class)
                    .isEqualTo(Float.NEGATIVE_INFINITY);
        }

        @Test
        void getFloatNotANumber() {
            var data = Map.of(
                    "number", "NaN"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asFloat())
                    .isInstanceOf(Float.class)
                    .isEqualByComparingTo(Float.NaN);
        }

        @Test
        void getListFloat() {
            var d = List.of((float) 10.5, (float) 20.9);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Float.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapFloat() {
            var d = Map.of("o1", (float) 10, "o2", (float) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Float.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullFloat() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asFloat() == null)
                    .isTrue();
        }
        // endregion

        // region Double
        @Test
        void getDouble() {
            var data = Map.of(
                    "number", 5.3
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asDouble())
                    .isInstanceOf(Double.class)
                    .isEqualTo(5.3);
        }

        @Test
        void getDoubleEx() {
            var data = Map.of(
                    "number", 12.3015e+05
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asDouble())
                    .isInstanceOf(Double.class)
                    .isEqualTo(1230150.0);
        }

        @Test
        void getDoubleInfinity() {
            var data = Map.of(
                    "number", "Infinity"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asDouble())
                    .isInstanceOf(Double.class)
                    .isEqualTo(Double.POSITIVE_INFINITY);
        }

        @Test
        void getDoubleNegativeInfinity() {
            var data = Map.of(
                    "number", "-Infinity"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asDouble())
                    .isInstanceOf(Double.class)
                    .isEqualTo(Double.NEGATIVE_INFINITY);
        }

        @Test
        void getDoubleNotANumber() {
            var data = Map.of(
                    "number", "NaN"
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("number").asDouble())
                    .isInstanceOf(Double.class)
                    .isEqualByComparingTo(Double.NaN);
        }

        @Test
        void getListDouble() {
            var d = List.of((double) 10, (double) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asList(Double.class))
                    .isEqualTo(d);
        }

        @Test
        void getMapDouble() {
            var d = Map.of("o1", (double) 10, "o2", (double) 20);
            var data = Map.of(
                    "data", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").asMap(Double.class))
                    .isEqualTo(d);
        }

        @Test
        void getNullDouble() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asDouble() == null)
                    .isTrue();
        }
        // endregion

        // region Local Date
        @Test
        void getLocalDateWithConfiguredPattern() {
            var d = "2020/15/02";
            var p = "yyyy/dd/MM";

            var data = Map.of(
                    "date", d
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigDatePattern();

            var date = new YAMLReader(data, new ObjMapper(true)).get("date").asLocalDate();
            config.close();

            assertThat(date)
                    .isInstanceOf(LocalDate.class)
                    .isEqualTo(LocalDate.parse(d, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getLocalDateWithCustomPatter() {
            var d = "2020/15/02";
            var p = "yyyy/dd/MM";

            var data = Map.of(
                    "date", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("date").asLocalDate(p))
                    .isInstanceOf(LocalDate.class)
                    .isEqualTo(LocalDate.parse(d, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getListOfLocalDate() {
            var p = "yyyy/dd/MM";
            var d0 = "2020/15/02";
            var d1 = "2022/30/01";
            var d2 = "1990/01/05";

            var data = Map.of(
                    "dates", List.of(d0, d1, d2)
            );

            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigDatePattern();

            var date = new YAMLReader(data, new ObjMapper(true))
                    .get("dates").asList(LocalDate.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(date)
                    .isEqualTo(List.of(
                            LocalDate.parse(d0, formatter),
                            LocalDate.parse(d1, formatter),
                            LocalDate.parse(d2, formatter)
                    ));
        }

        @Test
        void getMapOfLocalDate() {
            var p = "yyyy/dd/MM";
            var d0 = "2020/15/02";
            var d1 = "2022/30/01";
            var d2 = "1990/01/05";

            var data = Map.of(
                    "dates", Map.of(
                            "d0", d0,
                            "d1", d1,
                            "d2", d2
                    )
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigDatePattern();

            var date = new YAMLReader(data, new ObjMapper(true))
                    .get("dates").asMap(LocalDate.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(date)
                    .isEqualTo(Map.of(
                            "d0", LocalDate.parse(d0, formatter),
                            "d1", LocalDate.parse(d1, formatter),
                            "d2", LocalDate.parse(d2, formatter)
                    ));
        }

        @Test
        void getNullLocalDate() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asLocalDate() == null)
                    .isTrue();
        }
        // endregion

        // region Local Time
        @Test
        void getLocalTimeWithConfiguredPattern() {
            var t = "20 55 59";
            var p = "HH mm ss";

            var data = Map.of(
                    "time", t
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigTimePattern();

            var time = new YAMLReader(data, new ObjMapper(true)).get("time").asLocalTime();
            config.close();

            assertThat(time)
                    .isInstanceOf(LocalTime.class)
                    .isEqualTo(LocalTime.parse(t, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getLocalTimeWithCustomPatter() {
            var t = "20 55 59";
            var p = "HH mm ss";

            var data = Map.of(
                    "time", t
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("time").asLocalTime(p))
                    .isInstanceOf(LocalTime.class)
                    .isEqualTo(LocalTime.parse(t, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getListOfLocalTime() {
            var p = "HH mm ss";
            var t0 = "00 00 00";
            var t1 = "23 59 59";
            var t2 = "10 10 10";

            var data = Map.of(
                    "times", List.of(t0, t1, t2)
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigTimePattern();

            var time = new YAMLReader(data, new ObjMapper(true))
                    .get("times").asList(LocalTime.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(time)
                    .isEqualTo(List.of(
                            LocalTime.parse(t0, formatter),
                            LocalTime.parse(t1, formatter),
                            LocalTime.parse(t2, formatter)
                    ));
        }

        @Test
        void getMapOfLocalTime() {
            var p = "HH mm ss";
            var t0 = "00 00 00";
            var t1 = "23 59 59";
            var t2 = "10 10 10";

            var data = Map.of(
                    "times", Map.of(
                            "t0", t0,
                            "t1", t1,
                            "t2", t2
                    )
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigTimePattern();

            var time = new YAMLReader(data, new ObjMapper(true))
                    .get("times").asMap(LocalTime.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(time)
                    .isEqualTo(Map.of(
                            "t0", LocalTime.parse(t0, formatter),
                            "t1", LocalTime.parse(t1, formatter),
                            "t2", LocalTime.parse(t2, formatter)
                    ));
        }

        @Test
        void getNullLocalTime() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asLocalTime() == null)
                    .isTrue();
        }
        // endregion

        // region Local DateTime
        @Test
        void getLocalDateTimeWithConfiguredPattern() {
            var p = "yyyy/dd/MM HH:mm:ss";
            var d = "2020/05/10 10:20:01";

            var data = Map.of(
                    "dateTime", d
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigDateTimePattern();

            var dateTime = new YAMLReader(data, new ObjMapper(true)).get("dateTime")
                    .asLocalDateTime();
            config.close();

            assertThat(dateTime)
                    .isInstanceOf(LocalDateTime.class)
                    .isEqualTo(LocalDateTime.parse(d, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getLocalDateTimeWithCustomPatter() {
            var p = "yyyy/dd/MM HH:mm:ss";
            var d = "2020/05/10 10:20:01";

            var data = Map.of(
                    "dateTime", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("dateTime")
                    .asLocalDateTime(p))
                    .isInstanceOf(LocalDateTime.class)
                    .isEqualTo(LocalDateTime.parse(d, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getListOfLocalDateTime() {
            var p = "yyyy/dd/MM HH:mm:ss";
            var d0 = "2020/05/10 10:20:01";
            var d1 = "2050/20/10 10:20:01";
            var d2 = "1999/06/10 10:20:01";

            var data = Map.of(
                    "dateTimes", List.of(d0, d1, d2)
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigDateTimePattern();

            var dateTime = new YAMLReader(data, new ObjMapper(true))
                    .get("dateTimes").asList(LocalDateTime.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(dateTime)
                    .isEqualTo(List.of(
                            LocalDateTime.parse(d0, formatter),
                            LocalDateTime.parse(d1, formatter),
                            LocalDateTime.parse(d2, formatter)
                    ));
        }

        @Test
        void getMapOfLocalDateTime() {
            var p = "yyyy/dd/MM HH:mm:ss";
            var d0 = "2020/05/10 10:20:01";
            var d1 = "2050/20/10 10:20:01";
            var d2 = "1999/06/10 10:20:01";

            var data = Map.of(
                    "dateTimes", Map.of(
                            "d0", d0,
                            "d1", d1,
                            "d2", d2
                    )
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigDateTimePattern();

            var dateTime = new YAMLReader(data, new ObjMapper(true))
                    .get("dateTimes").asMap(LocalDateTime.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(dateTime)
                    .isEqualTo(Map.of(
                            "d0", LocalDateTime.parse(d0, formatter),
                            "d1", LocalDateTime.parse(d1, formatter),
                            "d2", LocalDateTime.parse(d2, formatter)
                    ));
        }

        @Test
        void getNullLocalDateTime() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asLocalDateTime() == null)
                    .isTrue();
        }
        // endregion

        // region Zoned Date Time
        @Test
        void getZonedDateTimeWithConfiguredPattern() {
            var p = "yyyy/dd/MM HH:mm:ss Z";
            var d = "2020/15/02 20:10:00 +0200";

            var data = Map.of(
                    "date", d
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigZonedPattern();

            var date = new YAMLReader(data, new ObjMapper(true)).get("date")
                    .asZonedDateTime();
            config.close();

            assertThat(date)
                    .isInstanceOf(ZonedDateTime.class)
                    .isEqualTo(ZonedDateTime.parse(d, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getZonedDateTimeWithCustomPatter() {
            var p = "yyyy/dd/MM HH:mm:ss Z";
            var d = "2020/15/02 20:10:00 +0200";

            var data = Map.of(
                    "date", d
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("date")
                    .asZonedDateTime(p))
                    .isInstanceOf(ZonedDateTime.class)
                    .isEqualTo(ZonedDateTime.parse(d, DateTimeFormatter.ofPattern(p)));
        }

        @Test
        void getListOfZonedDateTime() {
            var p = "yyyy/dd/MM HH:mm:ss Z";
            var d0 = "2020/15/02 20:10:00 +0200";
            var d1 = "2050/15/02 20:10:00 +0200";
            var d2 = "1990/15/02 20:10:00 +0200";

            var data = Map.of(
                    "dates", List.of(d0, d1, d2)
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigZonedPattern();

            var date = new YAMLReader(data, new ObjMapper(true))
                    .get("dates").asList(ZonedDateTime.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(date)
                    .isEqualTo(List.of(
                            ZonedDateTime.parse(d0, formatter),
                            ZonedDateTime.parse(d1, formatter),
                            ZonedDateTime.parse(d2, formatter)
                    ));
        }

        @Test
        void getMapOfZonedDateTime() {
            var p = "yyyy/dd/MM HH:mm:ss Z";
            var d0 = "2020/15/02 20:10:00 +0200";
            var d1 = "2050/15/02 20:10:00 +0200";
            var d2 = "1990/15/02 20:10:00 +0200";

            var data = Map.of(
                    "dates", Map.of(
                            "d0", d0,
                            "d1", d1,
                            "d2", d2
                    )
            );


            MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
            config.when(ConfigHandler::getInstance)
                    .thenReturn(configHandler);
            doReturn(Optional.of(p))
                    .when(configHandler)
                    .getDateConfigZonedPattern();

            var date = new YAMLReader(data, new ObjMapper(true))
                    .get("dates").asMap(ZonedDateTime.class);
            config.close();

            var formatter = DateTimeFormatter.ofPattern(p);
            assertThat(date)
                    .isEqualTo(Map.of(
                            "d0", ZonedDateTime.parse(d0, formatter),
                            "d1", ZonedDateTime.parse(d1, formatter),
                            "d2", ZonedDateTime.parse(d2, formatter)
                    ));
        }

        @Test
        void getNullZonedDateTime() {
            var data = new HashMap<String, Object>();
            data.put("null", null);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("null").asZonedDateTime() == null)
                    .isTrue();
        }
        // endregion
    }

    @Nested
    class CustomObject {
        @Test
        void getSimpleObject() {
            var person = new Person()
                    .setName("Ahmed")
                    .setAge(50);
            var data = Map.of(
                    "data", Map.of(
                            "name", "Ahmed",
                            "age", 50
                    )
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data")
                    .as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(person);
        }

        @Test
        void getSimpleObjectWithLocalDate() {
            var person = new Person()
                    .setName("Ahmed")
                    .setAge(50)
                    .setDateOfBirth(LocalDate.parse("1990-10-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            var data = Map.of(
                    "data", Map.of(
                            "name", "Ahmed",
                            "age", 50,
                            "dateOfBirth", "1990-10-10"
                    )
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(person);
        }

        @Test
        void getSimpleObjectWithEnum() {
            var person = new Person()
                    .setName("Ahmed")
                    .setAge(50)
                    .setGender(Gender.MALE);
            var data = Map.of(
                    "data", Map.of(
                            "name", "Ahmed",
                            "age", 50,
                            "gender", "MALE"
                    )
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data").as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(person);
        }

        @Test
        void getSimpleObjectHasListOfCustomObjects() {
            var person = new Person()
                    .setName("Ahmed")
                    .setAge(50)
                    .setChildren(List.of(
                            new Person().setName("Ali"),
                            new Person().setName("Sara")
                    ));
            var data = Map.of(
                    "data", Map.of(
                            "name", "Ahmed",
                            "age", 50,
                            "children", List.of(
                                    Map.of("name", "Ali"),
                                    Map.of("name", "Sara")
                            )
                    )
            );

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data")
                    .as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(person);
        }

        @Test
        void getSimpleObjectHasMapOfCustomObjects() {
            var person = new Person()
                    .setName("Ahmed")
                    .setAge(50)
                    .setPets(Map.of(
                            "A", new Pet().setKind("Cat")
                    ));
            var data = Map.of("data", person);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data")
                    .as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(data);
        }

        @Test
        void getListCustomObject() {
            var persons = List.of(
                    new Person().setName("Ahmed").setAge(50),
                    new Person().setName("Mohamed").setAge(30)
            );
            var data = Map.of("data", persons);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data")
                    .as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(data);
        }

        @Test
        void getMapCustomObject() {
            var persons = Map.of(
                    "A", new Person().setName("Ahmed").setAge(50),
                    "B", new Person().setName("Mohamed").setAge(30)
            );
            var data = Map.of("data", persons);

            assertThat(new YAMLReader(data, new ObjMapper(true)).get("data")
                    .as(Person.class))
                    .isInstanceOf(Person.class)
                    .isEqualTo(data);
        }
    }

    @Nested
    class Environments {
    }
}