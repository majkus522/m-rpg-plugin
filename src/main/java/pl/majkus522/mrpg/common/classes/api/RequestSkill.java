package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.interfaces.IRequestResult;

import java.util.Objects;

public class RequestSkill implements IRequestResult
{
    public String skill;
    protected int toggle;

    public boolean getToggle()
    {
        return toggle == 1;
    }

    @Override
    public String toString()
    {
        return "RequestSkill{" +
                "skill='" + skill + '\'' +
                ", toggle=" + toggle +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RequestSkill that = (RequestSkill) o;
        return toggle == that.toggle && Objects.equals(skill, that.skill);
    }
}