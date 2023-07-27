package dev.mindvr.tgplayground;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;

import static java.util.Objects.requireNonNull;

public class ResourceLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public <T> T load(Class<T> target, Class<?> clazz, String name) {
        String path = clazz.getPackage().getName().replace(".", "/") + "/" + name;
        File file = new File(requireNonNull(clazz.getClassLoader().getResource(path)).toURI());
        return objectMapper.readValue(file, target);
    }
}
