package nsr_yaml;

import exception.ParsingException;
import exception.YAMLFileException;
import helper.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

class YAMLTest {
    private final YAMLReader reader = YAML.read("src/test/resources/test.yaml");
    private final String utcZoneId = "UTC";
    private final String zoneIdBeforeUTC = "America/Rio_Branco";
    private final String zoneIdAfterUTC = "Asia/Oral";

    @Test
    void getAllAsObject() {
        assertThat(reader.get().asObject())
                .isInstanceOf(Object.class);
    }

    @Test
    void getAllAsString() {
        assertThat(reader.get().asString())
                .isInstanceOf(String.class);
    }

    @Test
    void getAllAsList() {
        assertThat(YAML.read("src/test/resources/list.yaml").get().asList())
                .isEqualTo(List.of("a", "b", "c"));
    }

    @Test
    void getAllAsMap() {
        assertThat(YAML.read("src/test/resources/map.yaml").get().asMap())
                .isEqualTo(Map.of("a", "A", "b", "B", "c", "C"));
    }

    @Test
    void getAllAsStringArrayUsingAs() {
        assertThat(YAML.read("src/test/resources/list.yaml").get().as(String[].class))
                .isEqualTo(new String[]{"a", "b", "c"});
    }

    @Test
    void getArrayOfCustomObject() {
        assertThat(reader.get("person.children").as(Person[].class))
                .isEqualTo(new Person[]{new Person().setName("Ali").setAge(10),
                        new Person().setName("Sara").setAge(7)});
    }

    @Test
    void getAllAsListUsingAs() {
        assertThat(YAML.read("src/test/resources/list.yaml").get().as(List.class))
                .isEqualTo(List.of("a", "b", "c"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllAsMapUsingAs() {
        assertThat(YAML.read("src/test/resources/map.yaml").get().as(Map.class))
                .isInstanceOf(Map.class)
                .isEqualTo(Map.of("a", "A", "b", "B", "c", "C"));
    }

    @Test
    void getString() {
        assertThat(reader.get("person.children[0].name").asString())
                .isEqualTo("Ali");
    }

    @Test
    void getBoolean() {
        assertThat(reader.get("boolean").asBoolean())
                .isInstanceOf(Boolean.class)
                .isTrue();
    }

    @Test
    void getByte() {
        assertThat(reader.get("number").asByte())
                .isInstanceOf(Byte.class)
                .isEqualTo((byte) 10);
    }

    @Test
    void getInteger() {
        assertThat(reader.get("number").asInteger())
                .isInstanceOf(Integer.class)
                .isEqualTo(10);
    }

    @Test
    void getLong() {
        assertThat(reader.get("number").asLong())
                .isInstanceOf(Long.class)
                .isEqualTo(10);
    }

    @Test
    void getFloat() {
        assertThat(reader.get("numberD").asFloat())
                .isInstanceOf(Float.class)
                .isEqualTo((float) 5.9);
    }

    @Test
    void getDouble() {
        assertThat(reader.get("numberD").asDouble())
                .isInstanceOf(Double.class)
                .isEqualTo(5.9);
    }

    // region Local Date
    @Test
    void getLocalDateUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(utcZoneId)));
        assertThat(reader.get("date").asLocalDate())
                .isInstanceOf(LocalDate.class)
                .isEqualTo(LocalDate.parse("2020-05-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateBeforeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdBeforeUTC)));
        assertThat(reader.get("date").asLocalDate())
                .isInstanceOf(LocalDate.class)
                .isEqualTo(LocalDate.parse("2020-05-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateAfterUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdAfterUTC)));
        assertThat(reader.get("date").asLocalDate())
                .isInstanceOf(LocalDate.class)
                .isEqualTo(LocalDate.parse("2020-05-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateWithoutQuotesUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(utcZoneId)));
        assertThat(reader.get("date2").asLocalDate())
                .isInstanceOf(LocalDate.class)
                .isEqualTo(LocalDate.parse("2020-05-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateWithoutQuotesBeforeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdBeforeUTC)));
        assertThat(reader.get("date2").asLocalDate())
                .isInstanceOf(LocalDate.class)
                .isEqualTo(LocalDate.parse("2020-05-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateWithoutQuotesAfterUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdAfterUTC)));
        assertThat(reader.get("date2").asLocalDate())
                .isInstanceOf(LocalDate.class)
                .isEqualTo(LocalDate.parse("2020-05-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }
    // endregion

    // region Local Time
    @Test
    void getLocalTimeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(utcZoneId)));
        assertThat(reader.get("time").asLocalTime())
                .isInstanceOf(LocalTime.class)
                .isEqualTo(LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalTimeBeforeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdBeforeUTC)));
        assertThat(reader.get("time").asLocalTime())
                .isInstanceOf(LocalTime.class)
                .isEqualTo(LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalTimeAfterUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdAfterUTC)));
        assertThat(reader.get("time").asLocalTime())
                .isInstanceOf(LocalTime.class)
                .isEqualTo(LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalTimeWithoutQuotesUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(utcZoneId)));
        assertThat(reader.get("time2").asLocalTime())
                .isInstanceOf(LocalTime.class)
                .isEqualTo(LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalTimeWithoutQuotesBeforeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdBeforeUTC)));
        assertThat(reader.get("time2").asLocalTime())
                .isInstanceOf(LocalTime.class)
                .isEqualTo(LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalTimeWithoutQuotesAfterUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdAfterUTC)));
        assertThat(reader.get("time2").asLocalTime())
                .isInstanceOf(LocalTime.class)
                .isEqualTo(LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }
    // endregion

    // region Local Date Time
    @Test
    void getLocalDateTimeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(utcZoneId)));
        assertThat(reader.get("date-time").asLocalDateTime())
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(LocalDateTime.parse("2020-10-01 20:22:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateTimeBeforeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdBeforeUTC)));
        assertThat(reader.get("date-time").asLocalDateTime())
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(LocalDateTime.parse("2020-10-01 20:22:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateTimeAfterUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdAfterUTC)));
        assertThat(reader.get("date-time").asLocalDateTime())
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(LocalDateTime.parse("2020-10-01 20:22:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateTimeWithoutQuotesUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(utcZoneId)));
        assertThat(reader.get("date-time2").asLocalDateTime())
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(LocalDateTime.parse("2020-10-01 20:22:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateTimeWithoutQuotesBeforeUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdBeforeUTC)));
        assertThat(reader.get("date-time2").asLocalDateTime())
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(LocalDateTime.parse("2020-10-01 20:22:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }

    @Test
    void getLocalDateTimeWithoutQuotesAfterUTC() {
        SimpleTimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(zoneIdAfterUTC)));
        assertThat(reader.get("date-time2").asLocalDateTime())
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(LocalDateTime.parse("2020-10-01 20:22:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SimpleTimeZone.setDefault(TimeZone.getDefault());
    }
    // endregion

    @Test
    void getZonedDateTime() {
        assertThat(reader.get("zoned-date").asZonedDateTime())
                .isInstanceOf(ZonedDateTime.class)
                .isEqualTo(ZonedDateTime.parse("2020-05-01 10:05:00 +0200",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")));
    }

    @Test
    void getZonedDateTimeWithoutQuotes() {
        assertThat(reader.get("zoned-date2").asZonedDateTime())
                .isInstanceOf(ZonedDateTime.class)
                .isEqualTo(ZonedDateTime.parse("2020-05-01 10:05:00 +0200",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")));
    }

    @Test
    void getStringWithEnv() {
        assertThat(reader.get("text-env").asString())
                .isInstanceOf(String.class)
                .isEqualTo("test local");
    }

    @Test
    void getCustomObject() {
        var person = new Person()
                .setName("Ahmed")
                .setAge(50)
                .setChildren(List.of(
                        new Person().setName("Ali").setAge(10),
                        new Person().setName("Sara").setAge(7)
                ));

        assertThat(reader.get("person").as(Person.class))
                .isInstanceOf(Person.class)
                .isEqualTo(person);
    }

    @Test
    void getNull() {
        assertThat(reader.get("n").asObject() == null)
                .isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void invalidFilePath(String filePath) {
        assertThatThrownBy(() -> YAML.read(filePath))
                .isInstanceOf(YAMLFileException.class);
    }

    @Test
    void getInterface() {
        var thrown = catchThrowableOfType(() -> reader.get().as(TestInterface.class),
                ParsingException.class);

        assertThat(thrown.getMessage()).isEqualTo("Interfaces can not be initialized");
    }

    @Test
    void getRecord() {
        var thrown = catchThrowableOfType(() -> reader.get().as(TestRecord.class),
                ParsingException.class);

        assertThat(thrown.getMessage()).isEqualTo("Records are not supported");
    }

    @Test
    void loadNotExistedFile() {
        assertThatThrownBy(() -> YAML.read("not-exist.yaml"))
                .isInstanceOf(YAMLFileException.class);
    }

    @Test
    void customObjectWithEnvInIt() {
        assertThat(reader.get("person2").as(Person.class))
                .isEqualTo(new Person().setName("local name"));
    }

    @Test
    void readEnum() {
        assertThat(reader.get("gender").as(Gender.class))
                .isEqualTo(Gender.MALE);
    }

    @Test
    void readEnumByItsValue() {
        assertThat(reader.get("pet-type").as(PetType.class))
                .isEqualTo(PetType.CAT);

    }
}