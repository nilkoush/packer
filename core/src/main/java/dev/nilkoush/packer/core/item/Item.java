package dev.nilkoush.packer.core.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Nullable
    private String texture;
    @Nullable
    private String parentModel;
    @Nullable
    private String model;
    private boolean dyeable;
    @Nullable
    private String material;
    @Nullable
    private CustomModelData customModelData;

    public Key getKey() {
        return texture != null ? Key.key(texture) : Key.key(model);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomModelData {
        private List<String> strings;
    }
}