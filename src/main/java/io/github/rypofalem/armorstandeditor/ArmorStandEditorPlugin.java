/*
 * ArmorStandEditor: Bukkit plugin to allow editing armor stand attributes
 * Copyright (C) 2016  RypoFalem
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.command.*;
import io.github.rypofalem.armorstandeditor.language.Language;
import io.github.rypofalem.armorstandeditor.manager.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArmorStandEditorPlugin extends JavaPlugin {

    private static ArmorStandEditorPlugin instance;

    private NamespacedKey iconKey;
    public final PersistentDataType<Byte, Byte> editToolKeyType = PersistentDataType.BYTE;
    public final NamespacedKey editToolKey = new NamespacedKey(this, "ASE");

    // managers
    private CommandManager commandManager;
    public PlayerEditorManager editorManager;

    // configs
    private Language lang;
    public Material editTool = Material.FLINT;
    public boolean requireToolData = false;
    public boolean sendToActionBar = true;
    public int editToolData = Integer.MIN_VALUE;
    public boolean requireToolLore = false;
    public String editToolLore = null;
    public boolean requireToolKey = false; // Not configurable through config, since it's up to plugin to hook into
    boolean debug = false; //weather or not to broadcast messages via print(String message)
    double coarseRot;
    double fineRot;

    public ArmorStandEditorPlugin() {
        instance = this;
    }

    public static ArmorStandEditorPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // initialization
        initConfig();
        initLanguage();
        initCommands();

        // instance
        lang = new Language(getConfig().getString("lang"), this);
        editorManager = new PlayerEditorManager(this);

        // events
        getServer().getPluginManager().registerEvents(editorManager, this);
    }



    @Override
    public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder() == editorManager.getMenuHolder())
                player.closeInventory();
        }
    }

    private void initConfig() {
        updateConfig("", "config.yml");

        coarseRot = getConfig().getDouble("coarse");
        fineRot = getConfig().getDouble("fine");
        String toolType = getConfig().getString("tool", "FLINT");
        editTool = Material.getMaterial(toolType);
        requireToolData = getConfig().getBoolean("requireToolData", false);
        if (requireToolData) editToolData = getConfig().getInt("toolData", Integer.MIN_VALUE);
        requireToolLore = getConfig().getBoolean("requireToolLore", false);
        if (requireToolLore) editToolLore = getConfig().getString("toolLore", null);
        debug = getConfig().getBoolean("debug", false);
        sendToActionBar = getConfig().getBoolean("sendMessagesToActionBar", true);
    }

    private void initLanguage() {
        String[] availableLanguages = new String[]{
                "test_NA.yml", "nl_NL.yml", "uk_UA.yml",
                "zh.yml", "fr_FR.yml", "ro_RO.yml",
                "ja_JP.yml", "de_DE.yml"
        };
        for (String s : availableLanguages) {
            updateConfig("lang/", s);
        }
        //English is the default language and needs to be unaltered to so that there is always a backup message string
        saveResource("lang/en_US.yml", true);
    }

    private void initCommands() {
        commandManager = new CommandManager("ase");

        // register subcommands
        commandManager.registerSubCommand(new AdjustmentCommand(this));
        commandManager.registerSubCommand(new ToggleCommand(this));
        commandManager.registerSubCommand(new AxisCommand(this));
        commandManager.registerSubCommand(new HelpCommand(this));
        commandManager.registerSubCommand(new ModeCommand(this));
        commandManager.registerSubCommand(new SlotCommand(this));

        this.getCommand(this.commandManager.getMainCommandName()).setExecutor(this.commandManager);
    }

    public void log(String message) {
        this.getServer().getLogger().info("ArmorStandEditor: " + message);
    }

    public void print(String message) {
        if (debug) {
            this.getServer().broadcastMessage(message);
            log(message);
        }
    }

    public Language getLang() {
        return lang;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public boolean isEditTool(ItemStack item) {
        if (item == null) return false;
        if (editTool != item.getType()) return false;
        if (requireToolData && item.getDurability() != (short) editToolData) return false;
        if (requireToolLore && editToolLore != null) {
            if (!item.hasItemMeta()) return false;
            if (!item.getItemMeta().hasLore()) return false;
            if (item.getItemMeta().getLore().isEmpty()) return false;
            if (!item.getItemMeta().getLore().get(0).equals(editToolLore)) return false;
        }
        if (requireToolKey) {
            if (!item.hasItemMeta()) return false;
            if (item.getItemMeta().getPersistentDataContainer().get(editToolKey, editToolKeyType) == null) return false;
        }
        return true;
    }

    public NamespacedKey getIconKey() {
        if (iconKey == null) iconKey = new NamespacedKey(this, "command_icon");
        return iconKey;
    }

    private void updateConfig(String folder, String config) {
        if (!new File(getDataFolder() + File.separator + folder + config).exists()) {
            saveResource(folder + config, false);
        }
    }
}
//todo: 
//Access to "DisabledSlots" data (probably simplified just a toggle enable/disable)
//Access to the "Marker" switch (so you can make the hitbox super small)
