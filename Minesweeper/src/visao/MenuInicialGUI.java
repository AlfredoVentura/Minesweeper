package visao;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MenuInicialGUI extends JFrame {
    
    // Componentes para a entrada do usuário
    private JTextField campoLinhas;
    private JTextField campoColunas;
    private JTextField campoMinas;
    private JButton botaoJogar;

    public MenuInicialGUI() {
        setTitle("Campo Minado - Configurações");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        inicializarComponentes();
        
        setVisible(true);
    }
    
    private void inicializarComponentes() {
        // Layout principal da janela
        JPanel painelPrincipal = new JPanel(new GridLayout(4, 1, 5, 5));
        
        // Linhas
        JPanel painelLinhas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelLinhas.add(new JLabel("Linhas:"));
        campoLinhas = new JTextField("6", 5); // Valor padrão
        painelLinhas.add(campoLinhas);
        painelPrincipal.add(painelLinhas);

        // Colunas
        JPanel painelColunas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelColunas.add(new JLabel("Colunas:"));
        campoColunas = new JTextField("6", 5); // Valor padrão
        painelColunas.add(campoColunas);
        painelPrincipal.add(painelColunas);

        // Minas
        JPanel painelMinas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelMinas.add(new JLabel("Minas:"));
        campoMinas = new JTextField("6", 5); // Valor padrão
        painelMinas.add(campoMinas);
        painelPrincipal.add(painelMinas);

        // Botão Jogar
        botaoJogar = new JButton("JOGAR!");
        botaoJogar.addActionListener(e -> iniciarJogo()); // Adiciona o listener
        
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.add(botaoJogar);
        painelPrincipal.add(painelBotao);

        add(painelPrincipal);
    }
    
    private void iniciarJogo() {
        try {
            int linhas = Integer.parseInt(campoLinhas.getText());
            int colunas = Integer.parseInt(campoColunas.getText());
            int minas = Integer.parseInt(campoMinas.getText());

            // Validação simples garantir que não há mais minas do que células
            if (minas >= linhas * colunas || linhas < 2 || colunas < 2 || minas < 1) {
                JOptionPane.showMessageDialog(this, "Configuração Inválida! Verifique linhas, colunas e minas.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fecha a janela de configuração
            dispose(); 
            
            // Inicia o jogo com as novas configurações
            new TabuleiroGUI(linhas, colunas, minas); 
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuInicialGUI());
    }
}