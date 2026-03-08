package modelo;

// Interface Usada para o Polimorfismo
public interface CampoObservador {
    
    // Método polimórfico: O que o observador deve fazer quando um evento ocorre
    void eventoOcorreu(Campo campo, CampoEvento evento);
}