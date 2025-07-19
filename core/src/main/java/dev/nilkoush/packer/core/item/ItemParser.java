package dev.nilkoush.packer.core.item;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemParser {

    public static Map<String, Item> parseYmlFile(Path path) {
        Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
        Map<String, Item> result = new HashMap<>();

        try (InputStream in = Files.newInputStream(path)) {
            Object data = yaml.load(in);
            if (!(data instanceof Map<?, ?> map)) return result;

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String name = entry.getKey().toString();
                Object value = entry.getValue();

                if (value instanceof Map<?, ?> rawSection) {
                    Item item = new Item();

                    if (rawSection.get("texture") instanceof String texture) item.setTexture(texture);
                    if (rawSection.get("model") instanceof String model) item.setModel(model);
                    if (rawSection.get("parent_model") instanceof String parentModel) item.setParentModel(parentModel);
                    if (rawSection.get("dyeable") instanceof Boolean dyeable) item.setDyeable(dyeable);
                    if (rawSection.get("material") instanceof String material) item.setMaterial(material);

                    if (rawSection.get("custom_model_data") instanceof Map<?, ?> customDataComponent) {
                        Item.CustomModelData customModelData = new Item.CustomModelData();
                        if (customDataComponent.get("strings") instanceof List<?> strings) customModelData.setStrings((List<String>) strings);
                        item.setCustomModelData(customModelData);
                    }

                    result.put(name, item);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse YAML file: " + path, e);
        }

        return result;
    }

    public static Map<String, Item> loadAllFromDirectory(Path rootDir) throws IOException {
        Map<String, Item> allItems = new HashMap<>();
        try (var paths = Files.walk(rootDir)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".yml"))
                    .forEach(path -> allItems.putAll(parseYmlFile(path)));
        }
        return allItems;
    }
}