package br.com.autolaudo.dto;

import java.io.InputStream;

import javax.print.attribute.standard.Media;

import org.jboss.resteasy.reactive.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class ImagemFormDTO {
    @FormParam("nome")
    @PartType(MediaType.TEXT_PLAIN)
    public String nome;

    @FormParam("arquivo")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream arquivo;

    @FormParam("nomeQuimico")
    @PartType(MediaType.TEXT_PLAIN)
    public String nomeQuimico;

    @FormParam("crqQuimico")
    @PartType(MediaType.TEXT_PLAIN)
    public String crq;
    
    @FormParam("regiaoQuimico")
    @PartType(MediaType.TEXT_PLAIN)
    public String regiao;
    
    @FormParam("caminhoAssinatura")
    @PartType(MediaType.TEXT_PLAIN)
    public String caminhoAssinatura;

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

    public String getCaminhoAssinatura() {
        return caminhoAssinatura;
    }

    public void setCaminhoAssinatura(String caminhoAssinatura) {
        this.caminhoAssinatura = caminhoAssinatura;
    }
    
}
