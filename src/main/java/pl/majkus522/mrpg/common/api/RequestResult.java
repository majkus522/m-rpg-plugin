package pl.majkus522.mrpg.common.api;

import java.util.HashMap;

public class RequestResult
{
    public String content;
    public int code;
    public HashMap<String, String> headers;

    public RequestResult(int code, String content, HashMap<String, String> headers)
    {
        this.code = code;
        this.content = content;
        this.headers = headers;
    }

    public boolean isOk()
    {
        return code >= 200 && code < 300;
    }

    @Override
    public String toString() {
        return "RequestResult{" +
                "content='" + content + '\'' +
                ", code=" + code +
                ", headers=" + headers +
                '}';
    }
}