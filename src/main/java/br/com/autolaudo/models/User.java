package br.com.autolaudo.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

import br.com.autolaudo.enums.UserRole;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "usuarios")
public class User extends PanacheMongoEntity {
    @BsonProperty("email")
    public String email;

    @BsonProperty("passwordHash")
    public String passwordHash;

    @BsonProperty("refreshToken")
    public String refreshToken;

    @BsonProperty("role")
    public UserRole role;

    @BsonProperty("role")
    public String crq;

    @BsonProperty("crq")
    public String getCrq() {
        return crq;
    }

    public void setCrq(String crq) {
        this.crq = crq;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}