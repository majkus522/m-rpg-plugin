package pl.majkus522.mrpg.events;

import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.entity.CustomEntity;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnPlayerKillsEntity implements Listener
{
    @EventHandler
    public void onPlayerKills(EntityDeathEvent event)
    {
        if (event.getEntity().getKiller() != null)
        {
            Character character = PlayersController.getCharacter(event.getEntity().getKiller());
            if (event.getEntity() instanceof Player)
            {
                Character dead = PlayersController.getCharacter((Player)event.getEntity());
                character.addExp((int)(dead.getExp() * 0.25));
                character.addMoney((float)(dead.getMoney() * 0.01 * 0.75));
            }
            else if (((CraftEntity)event.getEntity()).getHandle() instanceof CustomEntity)
            {
                CustomEntity entity = (CustomEntity)((CraftEntity)event.getEntity()).getHandle();
                character.addExp(entity.getExp(character.player));
            }
        }
    }
}