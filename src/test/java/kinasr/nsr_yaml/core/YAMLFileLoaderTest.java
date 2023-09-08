package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.core.YAMLFileLoader;
import kinasr.nsr_yaml.exception.YAMLFileException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class YAMLFileLoaderTest {

    @Test
    void loadDataFromExistedFile() {
        assertThat(YAMLFileLoader.load("src/test/resources/test.yaml"))
                .isInstanceOf(Object.class);
    }

    @Test
    void loadAlreadyLoadedFile() {
        YAMLFileLoader.load("src/test/resources/test.yaml");
        assertThat(YAMLFileLoader.load("src/test/resources/test.yaml"))
                .isInstanceOf(Object.class);
    }

    @Test
    void loadNotExistedFile() {
        assertThatThrownBy(() -> YAMLFileLoader.load("src/test/resources/not_existed.yaml"))
                .isInstanceOf(YAMLFileException.class);
    }

    @Test
    void loadNotAYAMLFile() {
        assertThatThrownBy(() -> YAMLFileLoader.load("src/test/resources/test.txt"))
                .isInstanceOf(YAMLFileException.class);
    }
}