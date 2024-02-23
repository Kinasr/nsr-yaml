package kinasr.nsr_yaml.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static kinasr.nsr_yaml.core.Helper.changeEnv;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ChangeEnvTest {

    @Mock
    ConfigHandler configHandler;

    @Test
    void changeEnvWithTheFirstEnv() {
        var environments = List.of("A", "B", "C");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(environments))
                .when(configHandler)
                .getEnvironments();

        var date = new YAMLReader(Map.of(
                "value@A", "A value",
                "value@B", "B value",
                "value@C", "C value"
        ), new ObjMapper(true)).get("value").asString();
        config.close();

        assertThat(date).isEqualTo("A value");
    }

    @Test
    void changeEnvWithTheSecondEnv() {
        var environments = List.of("B", "C", "A");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(environments))
                .when(configHandler)
                .getEnvironments();

        var date = new YAMLReader(Map.of(
                "value@A", "A value",
                "value@B", "B value",
                "value@C", "C value"
        ), new ObjMapper(true)).get("value").asString();
        config.close();

        assertThat(date).isEqualTo("B value");
    }

    @Test
    void changeEnvWithTheSecondEnvIfFirstIsMissed() {
        var environments = List.of("A", "B", "C");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(environments))
                .when(configHandler)
                .getEnvironments();

        var date = new YAMLReader(Map.of(
                "value@B", "B value",
                "value@C", "C value"
        ), new ObjMapper(true)).get("value").asString();
        config.close();

        assertThat(date).isEqualTo("B value");
    }

    @Test
    void changeEnvShouldReturnTheFirstMatchedEnvEvenIfDefaultValueExist() {
        var environments = List.of("A", "B", "C");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(environments))
                .when(configHandler)
                .getEnvironments();

        var date = new YAMLReader(Map.of(
                "value@A", "A value",
                "value@B", "B value",
                "value", "Default value"
        ), new ObjMapper(true)).get("value").asString();
        config.close();

        assertThat(date).isEqualTo("A value");
    }

    @Test
    void changeEnvShouldReturnTheDefaultValueIfAllEnvironmentsAreMissed() {
        var environments = List.of("A", "B", "C");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(environments))
                .when(configHandler)
                .getEnvironments();

        var date = new YAMLReader(Map.of(
                "value@D", "D value",
                "value@E", "E value",
                "value", "Default value"
        ), new ObjMapper(true)).get("value").asString();
        config.close();

        assertThat(date).isEqualTo("Default value");
    }

    @Test
    void changeEnvReturnTheDefaultValueIfNoEnvironmentIsSpecified() {
        var environments = List.of("A", "B", "C");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(environments))
                .when(configHandler)
                .getEnvironments();

        var date = new YAMLReader(Map.of(
                "value", "Default value"
        ), new ObjMapper(true)).get("value").asString();
        config.close();

        assertThat(date).isEqualTo("Default value");
    }

    @Test
    void testChangeEnv_withNullMap_returnsOriginalMap() {
        // Arrange
        Map<String, Object> map = null;

        // Act
        Map<String, Object> result = changeEnv(map);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void testChangeEnv_withEmptyEnvironments_returnsOriginalMap() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.empty())
                .when(configHandler)
                .getEnvironments();

        // Act
        Map<String, Object> result = changeEnv(map);
        config.close();

        // Assert
        assertThat(result).isEqualTo(map);
    }

    @Test
    void testChangeEnv_withMultipleEnvironmentsAndKeys_changesKeys() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("key1@dev", "value1");
        map.put("key2@prod", "value2");
        map.put("key3@dev", "value3");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(List.of("dev", "prod")))
                .when(configHandler)
                .getEnvironments();

        // Act
        Map<String, Object> result = changeEnv(map);
        config.close();

        // Assert
        assertThat(result)
                .isEqualTo(Map.of("key1", "value1", "key2", "value2", "key3", "value3"));
    }

    @Test
    void testChangeEnv_withNoMatchingKeys_returnsOriginalMap() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        MockedStatic<ConfigHandler> config = Mockito.mockStatic(ConfigHandler.class);
        config.when(ConfigHandler::getInstance)
                .thenReturn(configHandler);
        doReturn(Optional.of(List.of("dev", "prod")))
                .when(configHandler)
                .getEnvironments();

        // Act
        Map<String, Object> result = changeEnv(map);
        config.close();

        // Assert
        assertThat(result).isEqualTo(map);
    }
}
