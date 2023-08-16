package pl.majkus522.mrpg.common.enums;

public enum HttpMethod
{
    GET, POST, PATCH, HEAD, PUT, DELETE;

    @Override
    public String toString()
    {
        if (this == PATCH)
            return "POST";
        return super.toString();
    }
}
