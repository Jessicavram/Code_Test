
package com.neo4j;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Jessica
 */

public class Interfaz extends JFrame implements MouseListener
{
    ComponentesBD componentes;
    Resultado resultado;
    public String regex;
    JTextArea area;   
    JLabel error;
    
    public Interfaz()
    {
        super("Insertar Consulta");
        componentes = new ComponentesBD();
    }

    public void crear_patron()
    {
        String bloque = "([a-zA-Z_0-9\\.\\-]+)";
        String tabla = "([a-zA-Z_0-9\\-]+)";
        String espacio = "\\s+";
        String valor = "('[^']+')";
        String operacion = "((=)|(>=)|(<=)|(!=)|(>)|(<))\\s*";
        String operacionLogica = "((AND)|(OR))";
        String operando = "(("+bloque+")|("+valor+"))\\s*";
        String condicion= "("+operando+operacion+operando+")";
        String condiciones = condicion+"("+espacio+operacionLogica+espacio+condicion+")*";
        String columnas = "(("+bloque+"\\s*,\\s*)*"+bloque+")";
        String tablas = "("+tabla+"("+espacio+"JOIN"+espacio+tabla+"("+espacio+"ON"+espacio+condiciones+")?)*)";
        String parteSelect = "SELECT"+espacio+columnas;
        String parteFrom = "FROM"+espacio+tablas;
        String parteWhere = "WHERE"+espacio+condiciones;
        regex = parteSelect+espacio+parteFrom+"("+espacio+parteWhere+")?\\s*;\\s*";
    }
    
  
    public boolean match_exp(String text)
    {
        return text.matches(regex);
    }
 
    public void crear_interfaz()
    {
        this.setBounds(100,200,600,400);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.DARK_GRAY);
        
        area = new JTextArea();
        area.setBounds(50,60,500,100);
        
        JButton boton = new JButton("Crear grafo");
        boton.setBounds(200,200,180,30);
        boton.setName("crear");
        boton.addMouseListener(this);
        
        error = new JLabel();
        error.setFont(new Font("Arial",Font.BOLD,14));
        error.setForeground(Color.WHITE);
        error.setBounds(50,250,300,30);
        
        JLabel titulo;
        
        titulo = new JLabel("Consulta SQL:");
        titulo.setFont(new Font("Arial",Font.BOLD,14));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(50,20,300,30);
        
        JScrollPane scroll;
        scroll = new JScrollPane(area);
        scroll.setBounds(50,60,500,100);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        this.add(scroll);
        this.add(error);
        this.add(boton);
        this.add(titulo);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); 
    }
    
    public static void main(String[] args) throws IOException
    {
        Interfaz i = new Interfaz();
        i.crear_patron();
        i.crear_interfaz();      
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        if(e.getComponent().getName().compareTo("crear")==0)
        {
            String text = area.getText();
            boolean res = match_exp(text);
            if(res)
            {
                error.setText("");
                componentes.construir_grafo(text.replaceAll(";",""));
                resultado = new Resultado(componentes.consultar());
                resultado.mostrar();
            }
            else
            {
                error.setText("Ingrese una consulta SQL valida");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
