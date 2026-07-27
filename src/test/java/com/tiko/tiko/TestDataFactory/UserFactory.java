package com.tiko.tiko.TestDataFactory;

import com.tiko.tiko.Users.Entity.User;

import java.util.UUID;

public final class UserFactory {

    private UserFactory() {
    }

    public static User create() {
        return new User(
                "Test User",
                "user-" + UUID.randomUUID() + "@test.com",
                "password123"
        );
    }

    public static User create(String name) {
        return new User(
                name,
                "user-" + UUID.randomUUID() + "@test.com",
                "password123"
        );
    }

    public static User create(String name, String email) {
        return new User(
                name,
                email,
                "password123"
        );
    }

    public static User create(String name, String email, String passwordHash) {
        return new User(
                name,
                email,
                passwordHash
        );
    }
}