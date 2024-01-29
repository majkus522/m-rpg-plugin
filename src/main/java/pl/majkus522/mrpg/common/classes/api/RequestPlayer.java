package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;

public class RequestPlayer extends PlayerStatus implements IRequestResult
{
    public String username;
    public String email;
    public String helmet;
    public String chestplate;
    public String leggings;
    public String boots;
}