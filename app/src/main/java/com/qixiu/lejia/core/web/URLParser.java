package com.qixiu.lejia.core.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Long on 2018/5/10
 */
public class URLParser {

    private String queryString;
    private String charset = "utf-8";

    private boolean compiled = false;
    private Map<String, String> parsedParams;

    public static URLParser fromURL(String url) {
        URLParser parser = new URLParser();
        String[] split = url.split("\\?", 2);
        parser.queryString = (split.length > 1 ? split[1] : "");
        return parser;
    }

    public static URLParser fromQueryString(String queryString) {
        URLParser parser = new URLParser();
        parser.queryString = queryString;
        return parser;
    }

    public URLParser useCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public URLParser compile() throws UnsupportedEncodingException {
        if (this.compiled) {
            return this;
        }
        String paramString = this.queryString.split("#")[0];
        String[] params = paramString.split("&");

        this.parsedParams = new HashMap<>(params.length);
        for (String p : params) {
            String[] kv = p.split("=");
            if (kv.length == 2) {
                this.parsedParams.put(kv[0], URLDecoder.decode(kv[1], this.charset));
            }
        }
        this.compiled = true;

        return this;
    }

    public String getParameter(String name) {
        if (this.compiled) {
            return this.parsedParams.get(name);
        }
        String paramString = this.queryString.split("#")[0];
        Matcher match = Pattern.compile("(^|&)" + name + "=([^&]*)").matcher(paramString);
        match.lookingAt();
        return match.group(2);
    }

    public URLParser setParameter(String name, String value) throws UnsupportedEncodingException {
        if (!this.compiled) {
            compile();
        }
        this.parsedParams.put(name, value);

        return this;
    }

    public Map<String, String> toMap() {
        return parsedParams;
    }

}
