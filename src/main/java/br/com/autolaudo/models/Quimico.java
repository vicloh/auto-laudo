package br.com.autolaudo.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;

@MongoEntity(collection="quimicos")
public class Quimico extends PanacheMongoEntity {

    @BsonProperty("nome")
    @NotBlank(message = "O nome não pode ser em branco")
    public String nome;

    @BsonProperty("crq")
    @NotBlank(message = "O CRQ não pode ser em branco")
    public String crq;

    @BsonProperty("regiao")
    @NotBlank(message = "A região não pode ser em branco")
    public String regiao;

    @BsonProperty("caminho_assinatura")
    @NotBlank(message = "O caminho da assinatura não pode ser em branco")
    public String caminhoAssinatura;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrq() {
        return crq;
    }

    public void setCrq(String crq) {
        this.crq = crq;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getCaminhoAssinatura() {
        return caminhoAssinatura;
    }

    public void setCaminhoAssinatura(String caminhoAssinatura) {
        this.caminhoAssinatura = caminhoAssinatura;
    }
    
}