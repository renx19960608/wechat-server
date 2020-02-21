package com.renx.commom.multirequestbody;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;


public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {  
  
    private final byte[] body;

    private byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }


    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = toByteArray(request.getInputStream());
    }  
  
    @Override  
    public BufferedReader getReader() throws IOException {  
        return new BufferedReader(new InputStreamReader(getInputStream()));  
    }  
  
    @Override  
    public ServletInputStream getInputStream() throws IOException {  
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);  
        return new ServletInputStream() {
            @Override
            public void setReadListener(ReadListener var1){

            }

            @Override
            public  boolean isFinished(){
                return true;
            }
            @Override
            public boolean isReady(){
                return true;
            }
  
            @Override  
            public int read() throws IOException {  
                return bais.read();  
            }  
        };  
    }  
  
}