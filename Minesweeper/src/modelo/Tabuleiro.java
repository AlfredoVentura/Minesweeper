package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Tabuleiro {
    
    // Encapsulamento Lógica interna do tabuleiro
    private final int linhas;
    private final int colunas;
    private final int minas;

    private boolean jogoIniciado = false;
    
    private final List<Campo> campos = new ArrayList<>();

    private final List<TabuleiroObservador> observadores = new ArrayList<>();
    
    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        
        gerarCampos();
        associarVizinhos();
    }

    public void realizarJogada(Campo campoClicado) {
        if (!campoClicado.isAberto() && !campoClicado.isMarcado()) {
            
            if (!jogoIniciado) {
                // Coloca as minas depois do clique
                jogoIniciado = true;
                sortearMinas(campoClicado);
            }
            
            // Tenta abrir o campo inicia a cascata se for zero
            campoClicado.abrir();
            verificarObjetivo();

            if (objetivoAlcancado()) {
                observadores.forEach(obs -> obs.jogoEncerrado(true)); // true = vitória
            }
        }
    }

    // Métodos privados para configurar o tabuleiro
    private void gerarCampos() {
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                campos.add(new Campo(l, c, this));
            }
        }
    }

    private void associarVizinhos() {
        // Itera sobre todos os campos para ligar cada campo ao seu vizinho
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                if (c1 != c2 && ehVizinho(c1, c2)) {
                c1.adicionarVizinho(c2);
                }
            }
        }
    }

    // Sorteio de minas que garante a segurança do primeiro clique
    private void sortearMinas(Campo campoSeguro) {
        long minasSorteadas = 0;
        
        Predicate<Campo> podeMinar = c -> c != campoSeguro 
                                        && !campoSeguro.getVizinhos().contains(c) 
                                        && !c.isMinado();

        while (minasSorteadas < minas) {
            int aleatorio = new Random().nextInt(campos.size());
            Campo campoSelecionado = campos.get(aleatorio);
            
            if (podeMinar.test(campoSelecionado)) {
                campoSelecionado.minar();
                minasSorteadas++;
            }
        }
    }

    // Verifica se uma casa é vizinha de outra
    private boolean ehVizinho(Campo c1, Campo c2) {
        // Diferença absoluta de linha e coluna
        int deltaLinha = Math.abs(c1.getLinha() - c2.getLinha());
        int deltaColuna = Math.abs(c1.getColuna() - c2.getColuna());
        
        // Vizinho se a diferença máxima for 1 linha E 1 coluna
        boolean deltaMaximo = deltaLinha <= 1 && deltaColuna <= 1;
        
        // O campo não é vizinho de si mesmo (deltaGeral > 0)
        int deltaGeral = deltaLinha + deltaColuna;

        return deltaGeral >= 1 && deltaMaximo;
    }
    
    // Métodos públicos de Interface para a Visão
    public void abrir(int linha, int coluna) {
        // Encontra o campo e chama o método abrir()
        campos.stream()
            .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
            .findFirst()
            .ifPresent(c -> c.abrir());
    }

    public void marcar(int linha, int coluna) {
        // Encontra o campo e chama o método alternarMarcacao()
        campos.stream()
            .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
            .findFirst()
            .ifPresent(c -> c.alternarMarcacao());
    }

    public void registrarObservador(TabuleiroObservador observador) {
        observadores.add(observador);
    }

    public void notificarDerrota() {
        observadores.forEach(obs -> obs.jogoEncerrado(false)); // false = derrota
    }
    
    public boolean objetivoAlcancado() {
        // Lógica para verificar se o jogador ganhou (todos abertos ou marcados corretamente)
        return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }

    public void verificarObjetivo() {
        // Esta é a lógica que estava no final do realizarJogada()
        if (objetivoAlcancado()) {
            // Notifica o TabuleiroGUI sobre a vitória
            observadores.forEach(obs -> obs.jogoEncerrado(true)); 
        }
    }
    
    // Método para o GUI acessar a lista de campos e registrar os observadores
    public List<Campo> getCampos() {
        return campos;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }
}