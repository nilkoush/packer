package dev.nilkoush.packer.plugin.commands;

import dev.nilkoush.packer.plugin.menus.ItemsMenu;
import dev.nilkoush.packer.core.PackManager;
import dev.nilkoush.packer.plugin.PackerPlugin;
import dev.nilkoush.thelibrary.paper.format.FormatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackerCommand extends Command {

    public PackerCommand() {
        super("packer");
        setPermission("packer.command.admin");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("generate")) {
                Path basePath = PackerPlugin.getInstance().getDataFolder().toPath();
                PackManager.getInstance().generate(basePath);
                sender.sendMessage(FormatUtil.format("<prefix> <gray>ResourcePack <secondary>packer.zip <gray>has been generated."));
            } else {
                sendHelp(sender);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("items")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(FormatUtil.format("<prefix> <gray>This is only player command."));
                    return false;
                }
                String category = args[1];
                try {
                    new ItemsMenu(player, category).open();
                } catch (IllegalStateException ex) {
                    player.sendMessage(FormatUtil.format("<prefix> <gray>Items in path <secondary>" + category + " <gray>does not exist."));
                }
            } else {
                sendHelp(sender);
            }
        } else {
            sendHelp(sender);
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of("generate", "items")
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(FormatUtil.format("<prefix> <secondary>/packer generate <light>- <gray>Generates packer.zip from the configuration files."));
        sender.sendMessage(FormatUtil.format("<prefix> <secondary>/packer items <category> <light>- <gray>Opens menu with the specified category"));
    }
}
