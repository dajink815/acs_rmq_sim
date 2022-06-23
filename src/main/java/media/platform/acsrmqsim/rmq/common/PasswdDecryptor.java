/*
 * Copyright (C) 2019. Uangel Corp. All rights reserved.
 *
 */

package media.platform.acsrmqsim.rmq.common;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswdDecryptor {
    StandardPBEStringEncryptor crypto;

    public PasswdDecryptor(String key,
                           String alg) {
        crypto = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setPassword(key);
        config.setAlgorithm(alg);
        crypto.setConfig(config);
    }

    public String decrypt0(String encrypted) {
        return crypto.decrypt(encrypted);
    }

    public String encrypt(String pass) {
        return crypto.encrypt(pass);
    }

    public String decrypt(String f) {
        Pattern p = Pattern.compile("(ENC\\((.+?)\\))");
        Matcher m = p.matcher(f);
        String g=new String(f);
        while (m.find())
        {
            String enc_=m.group(1);
            String encryptedPass=m.group(2);
            String pass=decrypt0(encryptedPass);
            g=StringUtils.replace(g, enc_, pass);
        }

        return g;
    }


    public static void main(String[] args) throws Exception {
        PasswdDecryptor decryptor=new PasswdDecryptor("skt_acs", "PBEWITHMD5ANDDES");

        String tt=decryptor.encrypt("admin");
        System.out.println(tt);
        System.out.println(decryptor.decrypt0(tt));
    }
}
