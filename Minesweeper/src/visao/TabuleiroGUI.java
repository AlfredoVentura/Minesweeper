package visao;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.Tabuleiro;
import modelo.TabuleiroObservador;


public class TabuleiroGUI extends JFrame implements TabuleiroObservador {

    private final int linhasOriginal;
    private final int colunasOriginal;
    private final int minasOriginal;
    
    private final Tabuleiro tabuleiro;

    public TabuleiroGUI(int linhas, int colunas, int minas) {
        this.linhasOriginal = linhas;
        this.colunasOriginal = colunas;
        this.minasOriginal = minas;

        tabuleiro = new Tabuleiro(linhas, colunas, minas);

        tabuleiro.registrarObservador(this);
        
        // Painel para conter os botões
        JPanel painelTabuleiro = new JPanel();
        
        // Layout para os botões
        painelTabuleiro.setLayout(new GridLayout(
                tabuleiro.getLinhas(), tabuleiro.getColunas(), 2, 2));
        
        // Adicionando os botões ao Painel e registrando observadores
        tabuleiro.getCampos().forEach(c -> {
            CampoBotao botao = new CampoBotao(c); // Instanciação (Objeto)
            c.registrarObservador(botao); // <-- Registro
            painelTabuleiro.add(botao);
        });
        
        // Adiciona o painel à janela principal
        add(painelTabuleiro);
        
        setTitle("Campo Minado POO");
        setSize(600, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fecha o processo ao fechar
        setVisible(true);
    }

    // Mensagem ao finalizar o jogo
    @Override
    public void jogoEncerrado(boolean resultado) {
        String mensagem = resultado ? "🎉 Parabéns! Você VENCEU!" : "💣 Fim de Jogo! Você PERDEU.";

        Object[] options = {"Reiniciar", "Voltar ao Menu", "Sair"}; 
        
        int escolha = JOptionPane.showOptionDialog(
            this,
            mensagem,
            "Fim de Jogo",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (escolha == 0) { // Índice 0: "Reiniciar"
            reiniciarJogo();
        } else if (escolha == 1) { // Índice 1: "Voltar ao Menu"
            voltarAoMenu(); 
        } else { // Índice 2: "Sair" (Ou se a janela for fechada)
            System.exit(0);
        }
    }

    private void reiniciarJogo() {
        // 1. Fecha a janela atual (JFrame)
        dispose(); 
        
        // 2. Abre uma nova instância do jogo
        new TabuleiroGUI(linhasOriginal, colunasOriginal, minasOriginal); 
    }

    private void voltarAoMenu() {
        // 1. Fecha a janela atual do jogo
        dispose(); 
        
        // 2. Cria uma nova instância da tela de configuração, voltando ao início
        new MenuInicialGUI(); 
    }
}