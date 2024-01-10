package pl.majkus522.mrpg.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
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
                RequestGuild requestGuild = (RequestGuild)new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild).setSessionHeaders(character).getResult(RequestGuild.class);
                player.sendMessage("Guild name: " + requestGuild.name);
                if (requestGuild.leader == character.id)
                {
                    player.sendMessage(ChatColor.GREEN + "/guild add [player]" + ChatColor.WHITE + " - add player to your guild");
                    player.sendMessage(ChatColor.GREEN + "/guild kick [player]" + ChatColor.WHITE + " - kick player from your guild");
                    player.sendMessage(ChatColor.GREEN + "/guild new_leader [player]" + ChatColor.WHITE + " - make player leader of your guild");
                }
                player.sendMessage(ChatColor.GREEN + "/guild leave" + ChatColor.WHITE + " - leave your guild");
                if (requestGuild.leader == character.id)
                    player.sendMessage(ChatColor.GREEN + "/guild delete" + ChatColor.WHITE + " - delete your guild");
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
                    HttpBuilder requestPost = new HttpBuilder(HttpMethod.POST, "guilds/").setSessionHeaders(character).setBody(new CreateGuildRequest(name, player.getName()));
                    if(requestPost.isOk())
                    {
                        player.sendMessage("Guild has been created");
                        character.guild = ((RequestPlayer)new HttpBuilder(HttpMethod.GET, "players/" + player.getName()).setSessionHeaders(character).getResult(RequestPlayer.class)).guild;
                        ScoreboardController.createScoreboard(character);
                    }
                    else
                        player.sendMessage(requestPost.getError().message);
                    break;

                case "add":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    if(args.length < 2)
                    {
                        player.sendMessage("Enter player");
                        return;
                    }
                    RequestGuild requestGuild = (RequestGuild)new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild).setSessionHeaders(character).getResult(RequestGuild.class);
                    if(character.id != requestGuild.leader)
                    {
                        player.sendMessage("You are not a leader of the guild");
                        return;
                    }
                    Player added = Bukkit.getPlayer(args[1]);
                    if(added == null || added.getLocation().distance(player.getLocation()) > 10)
                    {
                        player.sendMessage("Player is to far away");
                        return;
                    }
                    HttpBuilder requestAdd = new HttpBuilder(HttpMethod.PATCH, "guilds/" + character.guild + "/add").setSessionHeaders(character).setBody(args[1]);
                    if(requestAdd.isOk())
                    {
                        player.sendMessage("Player " + args[1] + " has been added to your guild");
                        Character addedCharacter = PlayersController.getCharacter(added);
                        addedCharacter.guild = requestGuild.slug;
                        ScoreboardController.createScoreboard(addedCharacter);
                        added.sendMessage("You have been added to " + requestGuild.name + " guild");
                    }
                    else
                        player.sendMessage(requestAdd.getError().message);
                    break;

                case "kick":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    if(args.length < 2)
                    {
                        player.sendMessage("Enter player");
                        return;
                    }
                    requestGuild = (RequestGuild)new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild).setSessionHeaders(character).getResult(RequestGuild.class);
                    if(character.id != requestGuild.leader)
                    {
                        player.sendMessage("You are not a leader of the guild");
                        return;
                    }
                    HttpBuilder requestKick = new HttpBuilder(HttpMethod.PATCH, "guilds/" + character.guild + "/kick").setSessionHeaders(character).setBody(args[1]);
                    if(requestKick.isOk())
                    {
                        player.sendMessage("Player " + args[1] + " has been kicked from your guild");
                        Player kicked = Bukkit.getPlayer(args[1]);
                        if(kicked != null)
                        {
                            Character kickedCharacter = PlayersController.getCharacter(kicked);
                            kickedCharacter.guild = null;
                            ScoreboardController.createScoreboard(kickedCharacter);
                            kicked.sendMessage("You have been kicked from " + requestGuild.name + " guild");
                        }
                    }
                    else
                        player.sendMessage(requestKick.getError().message);
                    break;

                case "leave":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    HttpBuilder requestLeave = new HttpBuilder(HttpMethod.PATCH, "players/" + player.getName() + "/leave").setSessionHeaders(character);
                    if(requestLeave.isOk())
                    {
                        player.sendMessage("You have left your guild");
                        Character leaveCharacter = PlayersController.getCharacter(player);
                        leaveCharacter.guild = null;
                        ScoreboardController.createScoreboard(leaveCharacter);
                    }
                    else
                        player.sendMessage(requestLeave.getError().message);
                    break;

                case "members":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    player.sendMessage("Guild members:");
                    HttpBuilder requestMembers = new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild + "/members").setSessionHeaders(character);
                    JsonArray array = (JsonArray) JsonParser.parseString(requestMembers.getResultString());
                    for(int index = 0; index < array.size(); index++)
                        player.sendMessage(array.get(index).getAsString());
                    break;

                case "delete":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    HttpBuilder requestDelete = new HttpBuilder(HttpMethod.DELETE, "guilds/" + character.guild).setSessionHeaders(character);
                    if (requestDelete.isOk())
                    {
                        player.sendMessage("Guild has been deleted");
                        character.guild = null;
                        ScoreboardController.createScoreboard(character);
                    }
                    else
                        player.sendMessage(requestDelete.getError().message);
                    break;

                case "new_leader":
                    if(character.guild == null)
                    {
                        player.sendMessage("You are not part of any guild");
                        return;
                    }
                    HttpBuilder requestNewLeader = new HttpBuilder(HttpMethod.PATCH, "guilds/" + character.guild).setSessionHeaders(character).setBody(new NewLeaderRequest(args[1]));
                    if (requestNewLeader.isOk())
                        player.sendMessage("Guild leader has been changed");
                    else
                        player.sendMessage(requestNewLeader.getError().message);
                    break;

                default:
                    player.sendMessage("Unknown guild command option");
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
        ArrayList<String> placeholder = new ArrayList<>();
        Character character = PlayersController.getCharacter(player);
        if(args.length == 1)
        {
            if(character.guild == null)
                placeholder.add("create");
            else
            {
                RequestGuild requestGuild = (RequestGuild)new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild).setSessionHeaders(character).getResult(RequestGuild.class);
                if (requestGuild.leader == character.id)
                {
                    placeholder.add("add");
                    placeholder.add("kick");
                    placeholder.add("new_leader");
                    placeholder.add("delete");
                }
                placeholder.add("leave");
                placeholder.add("members");
            }
        }
        else if(args.length == 2)
        {
            switch (args[0])
            {
                case "kick":
                case "new_leader":
                    HttpBuilder requestMembers = new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild + "/members").setSessionHeaders(character);
                    JsonArray array = (JsonArray) JsonParser.parseString(requestMembers.getResultString());
                    for(int index = 0; index < array.size(); index++)
                        placeholder.add(array.get(index).getAsString());
                    placeholder.remove(player.getName());
                    break;

                case "add":
                    for(Player element : ExtensionMethods.getPlayersInRange(player.getLocation(), 10))
                        placeholder.add(element.getName());
                    requestMembers = new HttpBuilder(HttpMethod.GET, "guilds/" + character.guild + "/members").setSessionHeaders(character);
                    array = (JsonArray) JsonParser.parseString(requestMembers.getResultString());
                    for(int index = 0; index < array.size(); index++)
                        placeholder.remove(array.get(index).getAsString());
                    break;
            }
        }
        return placeholder;
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

    static class NewLeaderRequest
    {
        public String leader;

        public NewLeaderRequest(String leader)
        {
            this.leader = leader;
        }
    }
}