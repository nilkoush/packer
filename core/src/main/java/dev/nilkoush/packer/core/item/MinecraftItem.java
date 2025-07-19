package dev.nilkoush.packer.core.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinecraftItem {

    private SelectModel model;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectModel {
        private String type;
        private String property;
        private List<Case> cases;
        private PackerModel fallback;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Case {
        private String when;
        private PackerModel model;
    }
}
