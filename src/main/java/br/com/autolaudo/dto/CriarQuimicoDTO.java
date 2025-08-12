package br.com.autolaudo.dto;

import org.bson.codecs.pojo.annotations.BsonProperty;

import jakarta.validation.constraints.NotBlank;

public class CriarQuimicoDTO {
    
    public String nome;

    public String crq;
   
    public String regiao;
    
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
