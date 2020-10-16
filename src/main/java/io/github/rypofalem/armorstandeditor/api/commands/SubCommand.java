package io.github.rypofalem.armorstandeditor.api.commands;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    public ArmorStandEditorPlugin plugin;

    public SubCommand(ArmorStandEditorPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void execute(Player player, String args[]);

    public abstract List<String> getSubcommandArguments(Player player, String args[]);

}