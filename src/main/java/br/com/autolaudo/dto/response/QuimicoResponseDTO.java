package br.com.autolaudo.dto.response;

import br.com.autolaudo.models.Quimico;

public class QuimicoResponseDTO {
    private String crq;
    private String nome;
    private String regiao;
    private String caminhoAssinatura;

    public static QuimicoResponseDTO fromEntity(Quimico quimico) {
        QuimicoResponseDTO dto = new QuimicoResponseDTO();
        dto.setCrq(quimico.getCrq());
        dto.setNome(quimico.getNome());
        dto.setRegiao(quimico.getRegiao());
        dto.setCaminhoAssinatura(quimico.getCaminhoAssinatura());
        return dto;
    }

    // Getters e setters
    public String getCrq() {
        return crq;
    }

    public void setCrq(String crq) {
        this.crq = crq;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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