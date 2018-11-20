package arcadering.invest;

import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.TripwireHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public final class Invest extends JavaPlugin {

    private IconMenu menu;
    private ArrayList<Reward> rewards;
    private int globalBalance;

    private void setupRewards() {
        rewards = new ArrayList<>();
        rewards.add(new Reward("§aReward 1", 50, Material.TRIPWIRE_HOOK, "§eVote §7Key §fx1"));
        rewards.add(new Reward("§aReward 2", 100, Material.TRIPWIRE_HOOK, "§3Rare §7Key §fx1"));
        rewards.add(new Reward("§aReward 3", 150, Material.EMERALD, "§a10T"));
        //add rewards here
    }
    Boolean Reward1claimed = false;
    Boolean Reward2claimed = false;
    private int getCost(String rewardName) {
        for (Reward reward : rewards) {
            if (reward.name.equals(rewardName)) {
                return reward.cost;
            }
        }
        return 0;
    }

    @Override
    public void onEnable() {
        setupRewards();

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Debugger!");
        this.getCommand("invest").setExecutor(this);

        menu = new IconMenu("Invest | §cclick on item to see how much money it takes to earn!", 18, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                event.getPlayer().sendMessage("§aReward §7will be given at " + Integer.toString(getCost(event.getName())));
                event.setWillClose(true);
            }
        }, this);

        for (int i = 0; i < rewards.size(); i++) {
            Reward reward = rewards.get(i);
            menu.setOption(i+2, new ItemStack(reward.material, 1), reward.name, "§e" + reward.info);
        }
/*
                .setOption(3, new ItemStack(Material.TRIPWIRE_HOOK, 1), "§aReward 1", "§eVote §7Key §fx1")
                .setOption(4, new ItemStack(Material.TRIPWIRE_HOOK, 1), "§aReward 2", "§3Rare §7Key §fx1")
                .setOption(5, new ItemStack(Material.EMERALD, 1), "§aReward 3", "§210T");
                */

        // Plugin startup logic
        //this.getCommand("invest" + int).setExecutor(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player investingPlayer = null;
        if (sender instanceof Player) {
            investingPlayer = (Player) sender;
        }
        else {
            return true;
        }

        if (args.length == 0) {
            menu.open(investingPlayer);
        }
        else {
            String value = args[0];

            if ("total".equals(value)) {
                investingPlayer.sendMessage("§6The Total money invested is §a§l$" + globalBalance);
            }
            else {
                int a = Integer.parseInt(value.trim());
                investingPlayer.sendMessage("§7You have donated §a§l" + a + " §7to Invest");
                globalBalance += a;
                investingPlayer.sendMessage("§6The Total money invested is now §a§l$" + globalBalance);
                if (globalBalance >= 50 && Reward1claimed == false) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ItemStack i = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                        ItemMeta m = i.getItemMeta();
                        List<String> Lore = new ArrayList<>();
                        m.setDisplayName("§eVote §7Key");
                        Lore.add("§cRight Click §7the §eVote §7Crate");
                        m.setLore(Lore);
                        i.setItemMeta(m);
                        player.getInventory().addItem(i);
                        Reward1claimed = true;

                        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "§8You have been given a §eVote §7Key§8!");
                        player.sendMessage("§8You have been given a §eVote §7Key§8!");
                    }
                }
                        if (globalBalance >= 100 && Reward2claimed == false && Reward1claimed == true) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                ItemStack i = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                                ItemMeta m = i.getItemMeta();
                                List<String> Lore = new ArrayList<>();
                                m.setDisplayName("§3Rare §7Key");
                                Lore.add("§cRight Click §7the §3Rare §7Crate");
                                m.setLore(Lore);
                                i.setItemMeta(m);
                                player.getInventory().addItem(i);
                                Reward2claimed = true;

                                getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "§8You have been given a §3Rare §7Key§8!");
                                player.sendMessage("§8You have been given a §3Rare §7Key§8!");
                                }
                        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + Integer.toString(globalBalance));
                        }
            }
        }
        //getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Command works!");

        //sender.sendMessage("Invest Command working!");
        //return super.onCommand(sender, command, label, args);
        return true;
    }



    private void bar(User user) {
        user.getMoney();

        /*
        if (money <= player.balance);
        {
            player.balance - money = player.balance;
            money + totalmoney = totalmoney;
            if (totalmoney = rewardOne.price++ 1);
            {
                p.getInventory.add(new ItemStack(rewardOne.reward));
                rewardOne.claimed = true;
            }
            elseif(totalmoney = rewardTwo.price++1); {
            p.getInventory.add(new ItemStack(rewardTwo.reward));
            rewardTwo.claimed = true;
        }
else{
            alert("Not Enough Money!");
        }*/
        }

}
//            (showGui);

