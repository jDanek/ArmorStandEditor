package io.github.rypofalem.armorstandeditor.command;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import io.github.rypofalem.armorstandeditor.api.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class SlotCommand extends SubCommand {

    public SlotCommand(ArmorStandEditorPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "slot";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getSyntax() {
        return "/" + this.plugin.getCommandManager().getMainCommandName()
                + " " + this.getName()
                + " <1-9>";
    }

    @Override
    public void execute(Player player, String[] args) {

        if (args.length <= 1) {
            player.sendMessage(plugin.getLang().getMessage("noslotnumcom", "warn"));
            player.sendMessage(this.getSyntax());
        }

        if (args.length > 1) {
            try {
                byte slot = (byte) (Byte.parseByte(args[1]) - 0b1);
                if (slot >= 0 && slot < 9) {
                    plugin.editorManager.getPlayerEditor(player.getUniqueId()).setCopySlot(slot);
                } else {
                    player.sendMessage(this.getSyntax());
                }

            } catch (NumberFormatException nfe) {
                player.sendMessage(this.getSyntax());
            }
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }


}
