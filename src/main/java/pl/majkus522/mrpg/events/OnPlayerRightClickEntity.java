package pl.majkus522.mrpg.events;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestResult;

public class OnPlayerRightClickEntity implements Listener
{
    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked() instanceof Player && event.getHand() == EquipmentSlot.HAND)
        {
            Player player = (Player)event.getRightClicked();
            RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/players/" + player.getName(), ExtensionMethods.getSessionHeaders(player));
            RequestPlayer playerData = new Gson().fromJson(request.content, RequestPlayer.class);
            player = event.getPlayer();
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + playerData.username + ChatColor.BLUE + " =-=-=-=-=");
            player.sendMessage("Strength: " + playerData.strength);
            player.sendMessage("Agility: " + playerData.agility);
            player.sendMessage("Charisma: " + playerData.charisma);
            player.sendMessage("Intelligence: " + playerData.intelligence);
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + playerData.username + ChatColor.BLUE + " =-=-=-=-=");
        }
    }
}