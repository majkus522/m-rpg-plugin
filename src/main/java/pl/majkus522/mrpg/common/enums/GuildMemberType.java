package pl.majkus522.mrpg.common.enums;

public enum GuildMemberType
{
    leader, vice_leader, member;

    public String toPrettyString()
    {
        String[] part = this.toString().replace("_", " ").split(" ");
        String result = "";
        boolean first = true;
        for(String element : part)
        {
            if(!first)
                result += " ";
            result += element.substring(0, 1).toUpperCase() + element.substring(1);
            first = false;
        }
        return result;
    }
}