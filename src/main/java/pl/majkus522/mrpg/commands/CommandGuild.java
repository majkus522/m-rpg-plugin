package pl.majkus522.mrpg.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.api.RequestGuild;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.util.ArrayList;
import java.util.List;

public class CommandGuild extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        Character character = PlayersController.getCharacter(player);
        if (args.length == 0)
        {
            if(character.guild == null)
            {
                player.sendMessage("You are not part of any guild");
                player.sendMessage(ChatColor.GREEN + "/guild create [name]" + ChatColor.WHITE + " - create own guild");
            }
            else
            {
                RequestGuild requestGuild = (RequestGuild)new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild).setSessionHeaders(player).getResult(RequestGuild.class);
                player.sendMessage("Guild name: " + requestGuild.name);
                if (requestGuild.leader == character.id)
                    player.sendMessage(ChatColor.GREEN + "/guild delete" + ChatColor.WHITE + " - delete guild");
            }
        }
        else
        {
            switch (args[0].toLowerCase())
            {
                case "create":
                    if(character.guild != null)
                    {
                        player.sendMessage("You are already part of a guild");
                        return;
                    }
                    if(args.length == 1)
                    {
                        player.sendMessage("Enter guild name");
                        return;
                    }
                    String name = "";
                    for(int index = 1; index < args.length; index++)
                        name += args[index] + " ";
                    HttpBuilder requestPost = new HttpBuilder(HttpMethod.POST, "guilds/").setSessionHeaders(player).setBody(new CreateGuildRequest(name, player.getName()));
                    if(requestPost.isOk())
                    {
                        player.sendMessage("Guild has been created");
                        character.guild = ((RequestPlayer)new HttpBuilder(HttpMethod.GET, "players/" + player.getName()).setSessionHeaders(player).getResult(RequestPlayer.class)).guild;
                        ScoreboardController.createScoreboard(character);
                    }
                    else
                        player.sendMessage(requestPost.getError().message);
                    break;

                case "delete":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    HttpBuilder requestDelete = new HttpBuilder(HttpMethod.DELETE, "guilds/" + character.guild).setSessionHeaders(player);
                    if (requestDelete.isOk())
                    {
                        player.sendMessage("Guild has been deleted");
                        character.guild = null;
                        ScoreboardController.createScoreboard(character);
                    }
                    else
                        player.sendMessage(requestDelete.getError().message);
                    break;
            }
        }
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        console.sendMessage("Command can only be used by player");
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        return new ArrayList<>();
    }

    @Override
    public String getCommand()
    {
        return "guild";
    }

    static class CreateGuildRequest
    {
        public String name;
        public String leader;

        public CreateGuildRequest(String name, String leader)
        {
            this.name = name;
            this.leader = leader;
        }
    }
}