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

package io.github.rypofalem.armorstandeditor.manager;

import io.github.rypofalem.armorstandeditor.api.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final String mainCommandName;
    private final HashMap<String, SubCommand> subCommandHashMap = new HashMap<>();

    public CommandManager(String mainCommandName) {
        this.mainCommandName = mainCommandName;
    }

    public void registerSubCommand(SubCommand subCommand) {
        this.subCommandHashMap.put(
                subCommand.getName().toLowerCase(),
                subCommand
        );
    }

    public String getMainCommandName() {
        return this.mainCommandName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length > 0) {
                String typedSubCmd = args[0].toLowerCase();
                if (subCommandHashMap.containsKey(typedSubCmd)) {
                    subCommandHashMap.get(typedSubCmd).execute(p, args);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> subcommandsArguments = new ArrayList<>();
        if (args.length == 1) {
            for (String key : subCommandHashMap.keySet()) {
                if (key.startsWith(args[0].toLowerCase())) {
                    subcommandsArguments.add(key);
                }
            }
            return subcommandsArguments;
        } else if (args.length >= 2) {
            String typedSubCmd = args[0].toLowerCase();
            if (subCommandHashMap.containsKey(typedSubCmd)) {
                return subCommandHashMap.get(typedSubCmd).getSubcommandArguments((Player) sender, args);
            }
        }

        return null;
    }
}