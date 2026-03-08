package visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import modelo.Campo;
import modelo.CampoEvento;
import modelo.CampoObservador;



// HERANÇA: Estende JButton para ter a funcionalidade de um botão Swing
// POLIMORFISMO/INTERFACE: Implementa CampoObservador para reagir ao Modelo
public class CampoBotao extends JButton implements CampoObservador, MouseListener {

    private final Campo campo;
    
    // Constantes de cor (ajustes para a aparência)
    private final Color BG_PADRAO = new Color(0, 0, 0);
    private final Color BG_MARCADO = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);
    private final Color TEXTO_AZUL = new Color(0, 0, 200);  // Cor para 2 minas
    private final Color TEXTO_VERMELHO = new Color(200, 0, 0); // Cor para 3+ minas

    // COMPOSIÇÃO: O botão está associado a um Campo do Modelo
    public CampoBotao(Campo campo) {
        this.campo = campo;
        setBackground(BG_PADRAO);
        setOpaque(true);

        // Demarcação: Adiciona uma borda simples em linha
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Liga o botão (Visão) ao Campo (Modelo)
        campo.registrarObservador(this);
        addMouseListener(this); // Liga os eventos de clique do mouse
    }

    // Método polimórfico da Interface CampoObservador
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
        }
    }
    
    private void aplicarEstiloAbrir() {
        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            setText("X"); // Exibir 'X' na explosão
            return;
        }

        setBackground(Color.LIGHT_GRAY);
        // Demarcação: Remove a borda para dar a sensação de que o campo 'afundou'
        setBorder(BorderFactory.createEmptyBorder()); 

        int minasVizinhas = campo.minasNaVizinhanca(); // Pega o valor do Modelo
        
        // Lógica principal e única para exibição do número e cor
        if (minasVizinhas > 0) {
            
            // 1. Define a cor do texto baseada na contagem
            if (minasVizinhas == 1) {
                setForeground(TEXTO_VERDE);
            } else if (minasVizinhas == 2) {
                setForeground(TEXTO_AZUL);
            } else { // Para 3 minas ou mais
                setForeground(TEXTO_VERMELHO);
            }
            
            // 2. Define o texto do botão
            setText(minasVizinhas + ""); 
            
        } else {
            // Se for 0, o campo fica vazio
            setText(""); 
        }
        
        // **NOTA:** O código duplicado que estava aqui foi removido!
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setText("M");
    }
    
    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setText("X"); // Indicador de mina explodida
    }
    
    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
    }
    
    // Implementação dos métodos da Interface MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { // Botão Esquerdo
            if (campo.isAberto()) {
                campo.abrirVizinhosSeguros();
            } 
            else {
                campo.getTabuleiro().realizarJogada(campo);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Apenas Botão Direito
            campo.alternarMarcacao();
        }
        // Qualquer outro botão será ignorado, evitando o bloqueio.
    }
    
    // Outros métodos da interface MouseListener (deixados vazios)
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
}
