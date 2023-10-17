package pl.majkus522.mrpg.controllers;

import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.classes.api.RequestFakeStatus;
import pl.majkus522.mrpg.common.classes.data.StatData;
import pl.majkus522.mrpg.common.classes.effects.StatusEffect;
import pl.majkus522.mrpg.common.enums.HttpMethod;

public class StatusController
{
    public static void sendPlayerStatus(Player player)
    {
        if (!PlayersController.isPlayerLogged(player))
            return;
        Character character = PlayersController.getCharacter(player);
        player.sendMessage(ChatColor.BLUE + "=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=");
        for (StatData element : Config.characterStats)
            createLine(element.display + ": " + character.getStat(element.label), element.description, player);
        if (character.statusEffects.size() > 0)
        {
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-=-=-=-=-=");
            player.sendMessage("Status effects:");
            for (StatusEffect effect : character.statusEffects)
            {
                String line = "    " + effect.getType().toColor() + effect.getTitle();
                if (!effect.isInfinite())
                    line += ChatColor.WHITE + " " + effect.getTime() + "s";
                player.sendMessage(line);
            }
        }
        player.sendMessage(ChatColor.BLUE + "=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=");
    }

    public static void sendOtherPlayerStatus(Player sender, Player whose)
    {
        if (!(PlayersController.isPlayerLogged(sender) || PlayersController.isPlayerLogged(whose)))
            return;

        if(SkillsController.playerHasSkillEnabled(whose, "statusHide") && !SkillsController.playerHasSkill(sender, "statusVision"))
        {
            sender.sendMessage("You can't see this player status");
            return;
        }

        boolean statusVision = SkillsController.playerHasSkill(sender, "statusVision");
        int senderLevel = -1;
        if(!statusVision)
            senderLevel = PlayersController.getCharacter(sender).getLevel();
        boolean statusFake = statusVision ? false : SkillsController.playerHasSkill(whose, "statusFake");
        PlayerStatus status;
        if(statusFake)
        {
            HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/fake-status/" + whose.getName()).setSessionHeaders(whose);
            if(!request.isOk())
            {
                sender.sendMessage("Server error");
                throw new RuntimeException(new Exception(request.getError().message));
            }
            status = (RequestFakeStatus)request.getResult(RequestFakeStatus.class);
            status.initStats(new JsonParser().parse(request.content).getAsJsonObject());
        }
        else
            status = PlayersController.getCharacter(whose);
        int round = -1;
        if(status.getLevel() - senderLevel > 5)
            round = -2;
        if (statusVision)
            round = 0;
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
        createLine("Level: " + round(status.getLevel(), round), "", sender);
        createLine("Money: " + (round == 0 ? status.getMoney() : round(status.getMoney(), round)), "", sender);
        for (StatData element : Config.characterStats)
            createLine(element.display + ": " + status.getStat(element.label), element.description, sender);
        Character character = PlayersController.getCharacter(whose);
        if (character.statusEffects.size() > 0)
        {
            sender.sendMessage(ChatColor.BLUE + "=-=-=-=-=-=-=-=-=");
            sender.sendMessage("Status effects:");
            for (StatusEffect effect : character.statusEffects)
                sender.sendMessage("    " + effect.getType().toColor() + effect.getTitle());
        }
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
    }

    static String round(double value, int decimalPlace)
    {
        return (decimalPlace < 0 ? "~" : "") + ExtensionMethods.roundInt(value, decimalPlace);
    }

    static void createLine(String text, String hover, Player player)
    {
        TextComponent component = new TextComponent(text);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        player.spigot().sendMessage(component);
    }
}