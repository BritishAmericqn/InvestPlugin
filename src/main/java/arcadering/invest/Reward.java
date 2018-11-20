package arcadering.invest;

import org.bukkit.Material;

public class Reward {
    public String name;
    public int cost;
    public boolean claimed;
    public Material material;
    public String info;


    public Reward(String name, int cost, Material material, String info) {
        this.name = name;
        this.cost = cost;
        this.claimed = false;
        this.material = material;
        this.info = info;

    }
}
