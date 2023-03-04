package com.savelii.lab4;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Entity
@Table(name = "users")
public class Auth
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String api_key;

    public Auth(String login, String password)
    {
        this.login = login;
        setPassword(password);
        this.api_key = generateKey();
    }

    public Auth() {

    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String generateKey()
    {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass)
    {
        this.password = encrypt(pass);
    }

    public static String encrypt(String pass)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] messageDigest = md.digest(pass.getBytes());

            return new BigInteger(1, messageDigest).toString();
        }
        catch (NoSuchAlgorithmException ex){};
        return BigInteger.valueOf(0).toString();
    }

    public Long getId() {
        return id;
    }
}
