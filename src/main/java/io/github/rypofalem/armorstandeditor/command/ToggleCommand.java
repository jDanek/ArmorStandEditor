package io.github.rypofalem.armorstandeditor.command;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import io.github.rypofalem.armorstandeditor.api.commands.SubCommand;
import io.github.rypofalem.armorstandeditor.api.modes.EditMode;
import io.github.rypofalem.armorstandeditor.api.permissions.PluginPerms;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToggleCommand extends SubCommand {

    public ToggleCommand(ArmorStandEditorPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getSyntax() {
        return "/" + this.plugin.getCommandManager().getMainCommandName()
                + " " + this.getName()
                + " <option>";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length <= 1) {
            player.sendMessage(plugin.getLang().getMessage("nomodecom", "warn"));
            player.sendMessage(this.getSyntax());
        }

        if (args.length > 1) {
            for (EditMode mode : EditMode.values()) {
                if (mode.toString().toLowerCase().contentEquals(args[1].toLowerCase())) {
                    if (args[1].equals("invisible") && !PluginPerms.INVISIBLE.checkPlayer(player, true)) {
                        return;
                    }
                    plugin.editorManager.getPlayerEditor(player.getUniqueId()).setMode(mode);
                    return;
                }
            }
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = Arrays.asList(
                EditMode.BASEPLATE.toString(),
                EditMode.GRAVITY.toString(),
                EditMode.INVISIBLE.toString(),
                EditMode.SHOWARMS.toString(),
                EditMode.SIZE.toString()
        );

        List<String> result = new ArrayList<>();
        if (args.length == 2) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                    result.add(a.toLowerCase());
                }
            }
            return result;
        }
        return null;

    }

}
