package kinasr.nsr_yaml.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
}
