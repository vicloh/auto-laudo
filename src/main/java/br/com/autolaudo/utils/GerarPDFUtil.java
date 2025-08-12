package br.com.autolaudo.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;

import br.com.autolaudo.dto.DadosEmpresaDTO;
import br.com.autolaudo.enums.TipoLaudoEnum;
import br.com.autolaudo.models.Quimico;
import br.com.autolaudo.services.QuimicoService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class GerarPDFUtil {

    @Inject
    MongoClient mongoClient;

    @Inject
    QuimicoService quimicoService;

    public byte[] gerarLaudo(DadosEmpresaDTO dadosEmpresaDTO, String templatePath, String crq) throws Exception {
        String caminhoAssinatura = "/assinatura/assinatura_victor_lohan.png";
        String cnpjFormatado = formatarCnpj(dadosEmpresaDTO.getCnpj());
        String cepFormatado = formatarCep(dadosEmpresaDTO.getCep());
        String numeroFormatado;

        if (dadosEmpresaDTO.getNumero() != null && !dadosEmpresaDTO.getNumero().trim().isEmpty()) {
            numeroFormatado = "Nº " + dadosEmpresaDTO.getNumero();
        } else {
            numeroFormatado = "S/N";
        }

        String enderecoFormatado = String.format("RUA %s, %s", 
            dadosEmpresaDTO.getLogradouro(), 
            numeroFormatado);
        
        String municipioFormatado = String.format("%s - %s, CEP %s", 
            dadosEmpresaDTO.getMunicipio(),
            dadosEmpresaDTO.getUf(),
            cepFormatado);
        
        try (InputStream templateStream = getClass().getResourceAsStream(templatePath)) {
            
            if (templateStream == null) {
                throw new Exception("Arquivo de template não encontrado no caminho: " + templatePath + ". Verifique se ele está na pasta 'src/main/resources'.");
            }

            byte[] templateBytes = templateStream.readAllBytes();

            try (PDDocument pdfDocument = Loader.loadPDF(templateBytes)) {

                PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
                if (acroForm == null) {
                    throw new Exception("O PDF template não contém um formulário AcroForm...");
                }
                System.out.println("Dados Empresa: " + dadosEmpresaDTO.getRazaoSocial());
                acroForm.getField("razao_social").setValue(dadosEmpresaDTO.getRazaoSocial());
                acroForm.getField("cnpj").setValue(cnpjFormatado);                
                acroForm.getField("endereco").setValue(enderecoFormatado);
                acroForm.getField("municipio").setValue(municipioFormatado);
                acroForm.getField("bairro").setValue(dadosEmpresaDTO.getBairro());
                acroForm.getField("data_servico").setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                acroForm.getField("data_validade").setValue(
                    templatePath.contains(TipoLaudoEnum.LAUDO_DEDETIZACAO.getNomeTemplate()) ? 
                    getDataValidadeDedetizacao() : 
                    getDataValidadeLimpezaCaixaDagua()
                );
                System.out.println("CRQ: " + crq);
                PDField signatureField = acroForm.getField("assinatura");
                byte[] assBytes = getAssinatura(crq);
                        PDImageXObject signatureImage = PDImageXObject.createFromByteArray(pdfDocument, assBytes, "assinatura");
                        PDRectangle position = signatureField.getWidgets().get(0).getRectangle();
                        PDPage page = signatureField.getWidgets().get(0).getPage();
                        acroForm.getFields().remove(signatureField);
                        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page, AppendMode.APPEND, true, true)) {
                            contentStream.drawImage(signatureImage, position.getLowerLeftX(), position.getLowerLeftY(), position.getWidth(), position.getHeight());
                        }

                acroForm.flatten();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                pdfDocument.save(baos);
                return baos.toByteArray();
            }
        }
    }

    private String getDataValidadeDedetizacao() {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataValidade = dataAtual.plusMonths(3);
        return dataValidade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String getDataValidadeLimpezaCaixaDagua() {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataValidade = dataAtual.plusMonths(6);
        return dataValidade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    private String formatarCnpj(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            return cnpj;
        }
        return cnpj.substring(0, 2) + "." +
               cnpj.substring(2, 5) + "." +
               cnpj.substring(5, 8) + "/" +
               cnpj.substring(8, 12) + "-" +
               cnpj.substring(12, 14);
    }

    private String formatarCep(String cep) {
    if (cep == null || !cep.matches("\\d{8}")) {
        return cep; 
        }
    return cep.substring(0, 5) + "-" + cep.substring(5, 8);
    }

    
    private byte[] getAssinatura(String crq) {
        Quimico quimico = quimicoService.buscarPorCrq(crq);
        try {
            ObjectId id = new ObjectId(quimico.getCaminhoAssinatura());
            MongoDatabase db = mongoClient.getDatabase("autolaudo_db");
            GridFSBucket bucket = GridFSBuckets.create(db, "imagens");

            GridFSFile gridFSFile = bucket.find(new Document("_id", id)).first();
            if (gridFSFile == null) {
                throw new IllegalArgumentException("ID inválido ou arquivo não encontrado.");
            }

            String contentType = "application/octet-stream";
            Document metadata = gridFSFile.getMetadata();
            if (metadata != null && metadata.containsKey("contentType")) {
                contentType = metadata.getString("contentType");
            } else {
                // tenta inferir pelo nome
                String filename = gridFSFile.getFilename();
                if (filename != null) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) contentType = "image/jpeg";
                    else if (filename.endsWith(".png")) contentType = "image/png";
                    else if (filename.endsWith(".gif")) contentType = "image/gif";
                }
            }

            GridFSDownloadStream downloadStream = bucket.openDownloadStream(id);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            InputStream in = downloadStream;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            in.close();
            downloadStream.close();

            return baos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }    
    }
}