package br.com.autolaudo.dto.request;

import java.io.InputStream;

import org.jboss.resteasy.reactive.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class CriarUserQuimicoRequestDTO {
    @FormParam("email")
    @PartType(MediaType.TEXT_PLAIN)
    private String email;

    @FormParam("password")
    @PartType(MediaType.TEXT_PLAIN)
    private String password;

    @FormParam("nomeQuimico")
    @PartType(MediaType.TEXT_PLAIN)
    private String nomeQuimico;

    @FormParam("crqQuimico")
    @PartType(MediaType.TEXT_PLAIN)
    private String crq;

    @FormParam("regiaoQuimico")
    @PartType(MediaType.TEXT_PLAIN)
    private String regiao;

    @FormParam("arquivo")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream arquivo;

    // Getters e setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomeQuimico() {
        return nomeQuimico;
    }
    public void setNomeQuimico(String nomeQuimico) {
        this.nomeQuimico = nomeQuimico;
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

    public InputStream getArquivo() {
        return arquivo;
    }
    public void setArquivo(InputStream arquivo) {
        this.arquivo = arquivo;
    }
}