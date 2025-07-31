package dev.nilkoush.packer.core;

import com.google.gson.Gson;
import dev.nilkoush.packer.core.item.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.atlas.AtlasSource;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@NoArgsConstructor
public class PackManager {

    @Getter
    private static final PackManager instance = new PackManager();
    private static final Gson GSON = new Gson();

    public void generate(Path basePath) {
        ResourcePack resourcePack = ResourcePack.resourcePack();
        resourcePack.packMeta(46, "Packer");

        Path packPath = basePath.resolve("pack");
        try (Stream<Path> paths = Files.walk(packPath)) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        File file = path.toFile();

                        Path relativePath = packPath.relativize(path);
                        String relativePathStr = relativePath.toString().replace("\\", "/");

                        resourcePack.unknownFile(relativePathStr, Writable.file(file));
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Atlas.Builder atlas = Atlas.atlas().key(Atlas.BLOCKS);
        Path assetsPath = packPath.resolve("assets");
        try (Stream<Path> paths = Files.walk(assetsPath, 3)) {
            paths.filter(Files::isDirectory)
                    .filter(path -> path.getNameCount() >= 3)
                    .filter(path -> path.getName(path.getNameCount() - 1).toString().equals("textures"))
                    .forEach(texturesPath -> {
                        String namespace = texturesPath.getName(texturesPath.getNameCount() - 2).toString(); // assets/<namespace>/textures
                        try (Stream<Path> textureFiles = Files.walk(texturesPath)) {
                            textureFiles
                                    .filter(Files::isRegularFile)
                                    .filter(path -> path.toString().endsWith(".png"))
                                    .forEach(filePath -> {
                                        Path relativeToTextures = texturesPath.relativize(filePath);
                                        String texture = relativeToTextures.toString()
                                                .replace("\\", "/")
                                                .replaceAll("\\.[^.]+$", "");
                                        Key key = Key.key(namespace, texture);
                                        System.out.println(key);
                                        AtlasSource atlasSource = AtlasSource.single(key);
                                        atlas.addSource(atlasSource).build();
                                    });
                        } catch (IOException e) {
                            throw new RuntimeException("Error processing textures in: " + texturesPath, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resourcePack.atlas(atlas.build());

        Path itemsPath = basePath.resolve("items");
        try {
            Map<String, Item> items = ItemParser.loadAllFromDirectory(itemsPath);
            items.forEach((name, item) -> {
                if (item.getTexture() != null) {
                    Key key = Key.key(item.getTexture());
                    ModelTextures textures = ModelTextures.builder()
                            .layers(ModelTexture.ofKey(key))
                            .build();
                    Key parentKey = item.getParentModel() == null ? Key.key(Key.MINECRAFT_NAMESPACE, "item/generated") : Key.key(item.getParentModel());
                    Model model = Model.model()
                            .key(key)
                            .parent(parentKey)
                            .textures(textures)
                            .build();
                    resourcePack.model(model);
                }

                if (item.getCustomModelData() != null && item.getMaterial() != null) {
                    MinecraftItem root = new MinecraftItem();
                    MinecraftItem.SelectModel selectModel = new MinecraftItem.SelectModel();
                    selectModel.setType("minecraft:select");
                    selectModel.setProperty("minecraft:custom_model_data");
                    List<MinecraftItem.Case> cases = new ArrayList<>();
                    if (item.getCustomModelData().getStrings() != null) {
                        for (String string : item.getCustomModelData().getStrings()) {
                            MinecraftItem.Case itemCase = new MinecraftItem.Case();
                            itemCase.setWhen(string);
                            PackerModel model = new PackerModel();
                            model.setType("model");
                            model.setModel(item.getKey().asString());
                            model.setTints(item.isDyeable() ? List.of(new PackerModel.Tint("minecraft:dye", -6265536)) : List.of());
                            itemCase.setModel(model);
                            cases.add(itemCase);
                        }
                    }
                    selectModel.setCases(cases);
                    PackerModel fallbackModel = new PackerModel();
                    fallbackModel.setType("model");
                    fallbackModel.setModel("item/" + item.getMaterial().toLowerCase());
                    fallbackModel.setTints(item.isDyeable() ? List.of(new PackerModel.Tint("minecraft:dye", -6265536)) : List.of());
                    selectModel.setFallback(fallbackModel);
                    root.setModel(selectModel);
                    String json = GSON.toJson(root);
                    resourcePack.unknownFile("assets/minecraft/items/" + item.getMaterial().toLowerCase() + ".json", Writable.stringUtf8(json));
                } else {
                    CustomItem root = new CustomItem();
                    PackerModel model = new PackerModel();
                    model.setType("minecraft:model");
                    model.setTints(item.isDyeable() ? List.of(new PackerModel.Tint("minecraft:dye", -6265536)) : List.of());
                    model.setModel(item.getKey().asString());
                    root.setModel(model);
                    root.setOversizedInGui(true);
                    String json = GSON.toJson(root);
                    resourcePack.unknownFile("assets/" + item.getKey().namespace() + "/items/" + item.getKey().value() + ".json", Writable.stringUtf8(json));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File outputFolder = new File(basePath.toFile(), "packer.zip");
        MinecraftResourcePackWriter.minecraft().writeToZipFile(outputFolder, resourcePack);
    }
}
