package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    private String authToken;
    public AuthToken() {
        authToken = generateRandom();
    }

    public String generateRandom() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // create random string builder
        StringBuilder sb = new StringBuilder();
        // create an object of Random class
        Random random = new Random();
        // specify length of random string
        int length = 10;
        for (int i = 0; i < length; i++) {
            // generate random index number
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            // append the character to string builder
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public String getAuthToken(){
        return this.authToken;
    }
    //store a random string
}
