package nsr_yaml;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class YAMLTest {
    private final YAMLReader reader = YAML.read("src/test/resources/test.yaml");


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
                .isEqualTo(new String[] {"a", "b", "c"});
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
}