package br.com.autolaudo.dto;

public class QuimicoResponseDTO {
    private String nome;
    private String crq;
    private String regiao;
    private String caminhoAssinatura;

    public QuimicoResponseDTO(String nome, String crq, String regiao, String caminhoAssinatura) {
        this.nome = nome;
        this.crq = crq;
        this.regiao = regiao;
        this.caminhoAssinatura = caminhoAssinatura;
    }

    public String getNome() {
        return nome;
    }
    public String getCrq() {
        return crq;
    }
    public String getRegiao() {
        return regiao;
    }
    public String getCaminhoAssinatura() {
        return caminhoAssinatura;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCrq(String crq) {
        this.crq = crq;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public void setCaminhoAssinatura(String caminhoAssinatura) {
        this.caminhoAssinatura = caminhoAssinatura;
    }
}