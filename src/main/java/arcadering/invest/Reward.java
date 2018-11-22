package arcadering.invest;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public String name;
    public int cost;
    public boolean claimed;
    public Material material;
    public String info;
    public String lore;
    public boolean isMoney;
    public double moneyAmount;

    public Reward(String name, int cost, Material material, String info, String lore, boolean isMoney, double moneyAmount) {
        this.name = name;
        this.cost = cost;
        this.claimed = false;
        this.material = material;
        this.info = info;
        this.lore = lore;
        this.isMoney = isMoney;
        this.moneyAmount = moneyAmount;
    }

    public Reward(String name, int cost, Material material, String info, String lore) {
        this(name, cost, material, info, lore, false, 0.0);
    }


    public ItemStack getItemStack() {
        ItemStack i = new ItemStack(material, 1);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(info);
        List<String> Lore = new ArrayList<>();
        Lore.add(lore);
        m.setLore(Lore);
        i.setItemMeta(m);
        return i;
    }
}
