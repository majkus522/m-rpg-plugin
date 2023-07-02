package pl.majkus522.mrpg.common.api;

public class RequestResult
{
    public String content;
    public int code;

    public RequestResult(int code, String content)
    {
        this.code = code;
        this.content = content;
    }

    public boolean isOk()
    {
        return code >= 200 && code < 300;
    }

    @Override
    public String toString()
    {
        return "RequestResult{" +
                "content='" + content + '\'' +
                ", code=" + code +
                '}';
    }
}