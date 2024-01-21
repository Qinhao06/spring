package qh.springframework.beans.core.io;


import cn.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UrlResource implements Resource{

    private final URL Url;

    public UrlResource(URL url) {
        Assert.notNull(url, "Url can not be null");
        Url = url;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = Url.openConnection();

        try{
            return con.getInputStream();
        }
        catch (IOException e){
            if(con instanceof HttpURLConnection){
                ((HttpURLConnection)con).disconnect();
            }
            throw e;
        }

    }
}
