package br.com.autolaudo.services;

import br.com.autolaudo.dto.DadosEmpresaDTO;
import br.com.autolaudo.enums.TipoLaudoEnum;
import br.com.autolaudo.utils.GerarPDFUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PdfService {

    @Inject
    GerarPDFUtil gerarPDFUtil;

    public byte[] gerarLaudoDedetizacao(DadosEmpresaDTO dadosEmpresaDTO, String crq, String dataServicoString) throws Exception {
        return gerarPDFUtil.gerarLaudo(dadosEmpresaDTO, TipoLaudoEnum.LAUDO_DEDETIZACAO.getNomeTemplate(), crq, dataServicoString);
    }

    public byte[] gerarLaudoLimpezaCaixaDagua(DadosEmpresaDTO dadosEmpresaDTO, String crq, String dataServicoString) throws Exception {
        return gerarPDFUtil.gerarLaudo(dadosEmpresaDTO, TipoLaudoEnum.LAUDO_LIMPEZA_CAIXA_DAGUA.getNomeTemplate(), crq, dataServicoString);
    }

    public byte[] gerarLaudoDedetizacaoEDesratizacaoPdf(DadosEmpresaDTO dadosEmpresaDTO, String crq, String dataServicoString) throws Exception {
        return gerarPDFUtil.gerarLaudo(dadosEmpresaDTO, TipoLaudoEnum.LAUDO_DEDETIZACAO_E_DESRATIZACAO.getNomeTemplate(), crq, dataServicoString);
    }
    
}