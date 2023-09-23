package pl.majkus522.mrpg.common.classes;

public class PlayerStatus
{
    public int id;
    protected int level;
    protected int exp;
    protected int str;
    protected int agl;
    protected int chr;
    protected int intl;
    protected int def;
    protected int vtl;
    protected int dex;
    protected float money;

    public int getLevel()
    {
        return level;
    }

    public int getExp()
    {
        return exp;
    }

    public int getStr()
    {
        return str;
    }

    public int getAgl()
    {
        return agl;
    }

    public int getChr()
    {
        return chr;
    }

    public int getIntl()
    {
        return intl;
    }

    public int getDef()
    {
        return def;
    }

    public int getVtl()
    {
        return vtl;
    }

    public int getDex()
    {
        return dex;
    }

    public float getMoney()
    {
        return money;
    }
}