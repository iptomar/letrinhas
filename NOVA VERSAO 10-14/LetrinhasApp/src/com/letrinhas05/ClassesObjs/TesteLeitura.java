package com.letrinhas05.ClassesObjs;

/**
 * @author Alexandre
 */
public class TesteLeitura  extends Teste{

    protected String conteudoTexto;
    protected String professorAudioUrl;

    public TesteLeitura(){

    }

    public TesteLeitura(int idTeste,int areaId ,int professorId ,String titulo, String texto, long dataInsercaoTeste, int grauEscolar,int tipo , String conteudoTexto, String professorAudioUrl) {
        super(idTeste, areaId,professorId, titulo, texto, dataInsercaoTeste, grauEscolar, tipo);
        this.conteudoTexto = conteudoTexto;
        this.setProfessorAudioUrl(professorAudioUrl);
    }

    public TesteLeitura(String conteudoTexto, String professorAudioUrl) {
        this.conteudoTexto = conteudoTexto;
        this.setProfessorAudioUrl(professorAudioUrl);
    }

    public String getConteudoTexto() {
        return conteudoTexto;
    }

    public void setConteudoTexto(String conteudoTexto) {
        this.conteudoTexto = conteudoTexto;
    }


    public String getProfessorAudioUrl() {
        return professorAudioUrl;
    }

    public void setProfessorAudioUrl(String professorAudioUrl) {
        this.professorAudioUrl = professorAudioUrl;
    }
}
