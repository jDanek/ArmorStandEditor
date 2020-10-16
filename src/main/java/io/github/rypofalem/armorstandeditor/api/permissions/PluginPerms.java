package io.github.rypofalem.armorstandeditor.api.permissions;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import org.bukkit.entity.Player;

public enum PluginPerms {
    BASIC("asedit.basic"),
    COPY("asedit.copy"),
    INVISIBLE("asedit.invisible"),
    RENAME("asedit.rename");


    private final String perm;

    PluginPerms(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return this.perm;
    }

    public boolean checkPlayer(Player player) {
        return this.checkPlayer(player, true);
    }

    public boolean checkPlayer(Player player, boolean sendMessageOnInvalidation) {
        boolean state;
        if (player.hasPermission(this.getPerm())) {
            state = true;
        } else {
            if (sendMessageOnInvalidation) {
                player.sendMessage(ArmorStandEditorPlugin.instance().getLang().getMessage("noperm", "warn"));
            }
            state = false;
        }
        return state;
    }

}
