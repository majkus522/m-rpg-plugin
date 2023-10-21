package pl.majkus522.mrpg.common.classes;

import org.bukkit.entity.Player;

public class PlayerSettings
{
    public int health;
    public int mana;
    public String[] skills;
    public Position position;

    public PlayerSettings(Player player, int mana, String[] skills)
    {
        this.health = (int)player.getHealth();
        this.mana = mana;
        this.skills = skills;
        this.position = new Position(player.getLocation());
    }
}