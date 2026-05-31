package net.bilal.appeldoffresbackend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTool {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        System.out.println(
                encoder.encode("1234")
        );
    }
}