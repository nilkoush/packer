package dev.nilkoush.packer.app;

import dev.nilkoush.packer.core.PackManager;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PackerApp {

    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        Path targetDir = Paths.get(currentDir, "Packer");
        if (!targetDir.toFile().exists()) {
            saveResource("items/items.yml");
            new File(targetDir.toFile(), "pack").mkdirs();
        }
        PackManager.getInstance().generate(targetDir);
        JOptionPane.showMessageDialog(
                null,
                "ResourcePack has been generated in the Packer directory.",
                "Packer",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static void saveResource(String fileName) {
        try (InputStream in = PackerApp.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                System.err.println("Soubor '" + fileName + "' nebyl nalezen v resources.");
                return;
            }

            String currentDir = System.getProperty("user.dir");
            Path targetDir = Paths.get(currentDir, "Packer");
            Path outputPath = targetDir.resolve(fileName);

            Files.createDirectories(outputPath.getParent());

            Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Soubor zkopírován do: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
