package dev.nilkoush.packer.core.item;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomItem {

    private PackerModel model;

    @SerializedName("oversized_in_gui")
    private boolean oversizedInGui;
}
