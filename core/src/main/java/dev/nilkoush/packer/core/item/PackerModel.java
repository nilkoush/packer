package dev.nilkoush.packer.core.item;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackerModel {

    private String type;
    private String model;
    private List<Tint> tints;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tint {
        public String type;

        @SerializedName("default")
        public int defaultColor;
    }
}
