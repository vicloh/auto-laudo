package br.com.autolaudo.enums;

public enum TipoLaudoEnum {
    LAUDO_DEDETIZACAO("/LaudoDedetizacao.pdf"),
    LAUDO_LIMPEZA_CAIXA_DAGUA("/LaudoLimpezaCaixaDagua.pdf"),
    LAUDO_DEDETIZACAO_E_DESRATIZACAO("/LaudoDedetizacaoEDesratizacao.pdf");

    private final String nomeTemplate;

    TipoLaudoEnum(String nomeTemplate) {
        this.nomeTemplate= nomeTemplate;

    }

    public String getNomeTemplate() {
        return nomeTemplate;
    }
    
}
