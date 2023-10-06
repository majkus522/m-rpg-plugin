package pl.majkus522.mrpg.common.classes.data;

import pl.majkus522.mrpg.common.enums.SkillRarity;

public class SkillData
{
    public String label;
    public boolean toggle = false;
    public boolean usable = false;
    public String description;
    public SkillRarity rarity;
    public String[] evolution = new String[0];
}