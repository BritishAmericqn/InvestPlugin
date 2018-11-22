package arcadering.invest;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


public final class Invest extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private IconMenu menu;
    private ArrayList<Reward> rewards;
    private static Economy econ = null;
    private PersistentConfig pc;

    private void setupRewards() {
        rewards = new ArrayList<>();
        rewards.add(new Reward("§aReward 1", 50, Material.TRIPWIRE_HOOK, "§eVote §7Key §fx1", "§cRight Click §7the §eVote §7Crate"));
        rewards.add(new Reward("§aReward 2", 100, Material.TRIPWIRE_HOOK, "§3Rare §7Key §fx1","§cRight Click §7the §3Rare §7Crate"));
        rewards.add(new Reward("§aReward 3", 150, Material.EMERALD, "§a10T","§cRight Click §7the §3Rare §7Crate", true, 1e9));
        int lasti = Math.min(pc.getLastClaimed(), rewards.size());

        for (int i = 0; i < lasti; i++) {
            Reward reward = rewards.get(i);
            reward.claimed = true;
        }
    }

    private int getCost(String rewardName) {
        for (Reward reward : rewards) {
            if (reward.name.equals(rewardName)) {
                return reward.cost;
            }
        }
        return 0;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        initializeCommunityPot();

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
    }

    private void initializeCommunityPot() {
        try {
            pc = new PersistentConfig();
            pc.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pc.close();
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
                investingPlayer.sendMessage("§6The Total money invested is §a§l$" + pc.getGlobalBalance());
            }
            else {
                int a = Integer.parseInt(value.trim());
                if (!econ.has(investingPlayer, (double) a)) {
                    investingPlayer.sendMessage("§7You don't have enough funds to donate §a§l" + a);
                    return true;
                }
                econ.withdrawPlayer(investingPlayer, (double) a);

                investingPlayer.sendMessage("§7You have donated §a§l" + a + " §7to Invest");

                long globalBalance = pc.adjustGlobalBalance(a);

                investingPlayer.sendMessage("§6The Total money invested is now §a§l$" + globalBalance);

                int lastClaimed = pc.getLastClaimed();
                for (int i1 = 0; i1 < rewards.size(); i1++) {
                    Reward reward = rewards.get(i1);
                    if (globalBalance >= reward.cost && !reward.claimed) {
                        reward.claimed = true;
                        pc.setLastClaimed(i1);

                        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "§8Players have been given a " + reward.info);

                        if (reward.isMoney) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                econ.depositPlayer(player, reward.moneyAmount);
                                player.sendMessage("§8You have been given a " + reward.info);
                            }
                        }
                        else {
                            ItemStack i = reward.getItemStack();
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.getInventory().addItem(i);
                                player.sendMessage("§8You have been given a " + reward.info);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

