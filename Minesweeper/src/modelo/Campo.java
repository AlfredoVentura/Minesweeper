package modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    //ENCAPSULAMENTO: Atributos privados
    private final int linha;
    private final int coluna;

    private final Tabuleiro tabuleiro;
    
    private boolean minado = false;
    private boolean aberto = false;
    private boolean marcado = false;

    private final List<Campo> vizinhos = new ArrayList<>();
    
    //Lista de observadores que reagem a mudanças no campo
    private final  List<CampoObservador> observadores = new ArrayList<>();

    // Oculta o estado real, expondo apenas o necessário
    public Campo(int linha, int coluna, Tabuleiro tabuleiro) {
        this.linha = linha;
        this.coluna = coluna;
        this.tabuleiro = tabuleiro;
        this.aberto = false;
        this.minado = false;
        this.marcado = false;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    // Método para registrar os componentes gráficos que observarão este campo
    public void registrarObservador(CampoObservador observador) {
        observadores.add(observador);
    }
    
    // Notifica todos os observadores sobre uma mudança fazendo o uso de Polimorfismo
    private void notificarObservadores(CampoEvento evento) {
        for (CampoObservador obs : observadores) {
            obs.eventoOcorreu(this, evento);
        }
    }
    
    // Métodos essenciais de lógica do jogo
    
    public boolean abrir() {
        if (!aberto && !marcado) {
            aberto = true;
            
            if (minado) {
                // Notificação de explosão
                notificarObservadores(CampoEvento.EXPLODIR);
                tabuleiro.notificarDerrota();
                return true;
            }
            
            // Notificação de abertura
            notificarObservadores(CampoEvento.ABRIR);
            
            // Dispara a cascata se não tiver minas vizinhas
            if (vizinhancaSegura()) {
                abrirVizinhos();
            }
            return true;
        } else {
            return false;
        }
    }

    //Abre as casas vizinhas sem minas
    void abrirVizinhos() { 
        for (Campo vizinho : vizinhos) {
            vizinho.abrir(); 
        }
    }

    public boolean abrirVizinhosSeguros() {
        // A Jogada Rápida só funciona se a célula:
        // 1. Estiver aberta
        // 2. Não for uma mina
        // 3. O número de marcados for igual ao número de minas vizinhas
        if (isAberto() && !isMinado()) {
            if (minasNaVizinhanca() == marcadosNaVizinhanca()) {
                
                // Tenta abrir todos os vizinhos não marcados
                for (Campo vizinho : vizinhos) {
                    if (!vizinho.isMarcado()) {
                        // Tenta abrir o vizinho. Isso irá recursivamente
                        // disparar a cascata se o vizinho for zero.
                        if (vizinho.abrir()) {
                            // Se abrir uma mina aqui, o jogo acaba!
                            if (vizinho.isMinado()) {
                                return true; // Retorna true para sinalizar que o jogo acabou
                            }
                        }
                    }
                }
                tabuleiro.verificarObjetivo();
                return true;
            }
        }
        return false;
    }

    public List<Campo> getVizinhos() {
        return vizinhos;
    }

    // Verifica vitória 
    public boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    
    // Marca uma casa
    public void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;
            // Notificação de marcação
            notificarObservadores(marcado ? CampoEvento.MARCAR : CampoEvento.DESMARCAR);
        }
    }

    public void adicionarVizinho(Campo vizinho) {
        vizinhos.add(vizinho);
    }

    // Verifica minas vizinhas
    public int minasNaVizinhanca() { 
        return (int) vizinhos.stream().filter(v -> v.isMinado()).count(); 
    }

    // Zera as variaveis para reinicar o jogo
    public void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
        
        // Notifica a GUI que o campo deve voltar ao estado inicial (preto/fechado)
        notificarObservadores(CampoEvento.REINICIAR); 
    }

    public int marcadosNaVizinhanca() {
        // Conta quantos vizinhos estão marcados
        return (int) vizinhos.stream().filter(v -> v.isMarcado()).count(); 
    }

    // Demais getters e setters necessários para o Tabuleiro e a GUI
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public boolean isMinado() { return minado; }
    public boolean isAberto() { return aberto; }
    public boolean isMarcado() { return marcado; }
    public boolean vizinhancaSegura() { return minasNaVizinhanca() == 0; }
    public void minar() { minado = true; } // Usado apenas pelo Tabuleiro
}