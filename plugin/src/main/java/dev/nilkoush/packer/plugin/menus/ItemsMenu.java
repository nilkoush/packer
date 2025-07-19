package dev.nilkoush.packer.plugin.menus;

import dev.nilkoush.packer.core.item.Item;
import dev.nilkoush.packer.core.item.ItemParser;
import dev.nilkoush.packer.plugin.PackerPlugin;
import dev.nilkoush.thelibrary.paper.format.FormatUtil;
import dev.nilkoush.thelibrary.paper.item.ItemBuilder;
import dev.nilkoush.thelibrary.paper.menu.Menu;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;

public class ItemsMenu extends Menu {

    public ItemsMenu(Player player, String category) {
        super(player, 6, FormatUtil.format("Items - " + category));

        Path itemsPath = PackerPlugin.getInstance().getDataFolder().toPath().resolve("items/" + category + ".yml");
        Map<String, Item> items = ItemParser.parseYmlFile(itemsPath);
        items.forEach((name, item) -> {
            ItemBuilder itemBuilder;
            if (item.getCustomModelData() != null && item.getMaterial() != null) {
                Material material = Material.matchMaterial(item.getMaterial());
                if (material == null) {
                    throw new IllegalArgumentException("Invalid material: " + item.getMaterial());
                }
                itemBuilder = new ItemBuilder(material);
                if (item.getCustomModelData().getStrings() != null) {
                    itemBuilder.setStrings(item.getCustomModelData().getStrings());
                }
            } else {
                Material material = item.isDyeable() ? Material.LEATHER_HORSE_ARMOR : Material.ECHO_SHARD;
                itemBuilder = new ItemBuilder(material);
                itemBuilder.setItemModel(NamespacedKey.fromString(item.getKey().asString()));
            }
            itemBuilder.setDisplayName(FormatUtil.format(name));
            addButton(itemBuilder.build(), event -> {
                player.getInventory().addItem(itemBuilder.build());
            });
        });
    }
}
