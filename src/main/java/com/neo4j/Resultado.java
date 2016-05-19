
package com.neo4j;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 *
 * @author Jessica
 */
public class Resultado extends JFrame
{
    Node nodoPpal;
    Panel vista;
    int desplazamiento=10;
    
    
    public Resultado()
    {
        super();
    }
    
    public Resultado(Node nodo)
    {
        super("Mostrar Consulta");
        nodoPpal=nodo;
    }
    
    public void mostrar()
    {
        this.setBounds(50,50,905,737);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);
        
        vista = new Panel();
        vista.setLayout(null);
        vista.setBounds(0,100,900,610);
        
        this.pintar_nodo(0, nodoPpal);
        
        JLabel leyenda1[] = new JLabel[6];
        String texto1[] = {"Consulta.png","Select.png","Tabla.png","Operacion.png","Atributo.png","Valor.png"}; 
        
        for (int i = 0; i < 6; i++)
        {
            leyenda1[i] = new JLabel();
            leyenda1[i].setBounds(20+110*i,20,100,25);
            leyenda1[i].setIcon(new ImageIcon("imagenes/"+texto1[i]));  
            this.add(leyenda1[i]);
        }
        
        JLabel leyenda2[] = new JLabel[7];
        String texto2[] = {"(1):Datos","(2):Tablas","(3):Condicion","(4):Incluir","(5):Join","(6):On","(7):Operador"};

        for (int i = 0; i < 7; i++)
        {
            leyenda2[i] = new JLabel();
            leyenda2[i].setBounds(20+110*i,60,100,25);
            leyenda2[i].setText(texto2[i]); 
            leyenda2[i].setFont(new Font("Arial",Font.BOLD,14));
            this.add(leyenda2[i]);
        }
        
        this.add(vista);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);   
    }
   
    public String getLabel(Node nodo)
    {
        return nodo.getLabels().iterator().next().name();
    }
    
    public String getNombre(Node nodo)
    {
        return nodo.getProperty("nombre").toString();
    }

    public int pintar_nodo(int fila,Node nodo)
    {
        int desp_inicial = desplazamiento;
        
        ArrayList<Integer> end;
        end = new ArrayList<>();
        
        ArrayList<String> r;
        r = new ArrayList<>();
        
        for(Relationship rel : nodo.getRelationships())
        {
            if(!rel.getEndNode().equals(nodo))
            {
                int x = pintar_nodo(fila+1,rel.getNodes()[1]);
                end.add(x+25);
                r.add(rel.getType().name());
            }
        }
        int desp_final = desplazamiento;
            
        if(desp_final==desp_inicial)
        {
            desplazamiento+=60;
            desp_final+=60;
        }
        else
        {
            int x1 = (desp_inicial+desp_final)/2-5;
            int y1 = fila*100+60;
            
            int y2 = fila*100+110;
            for(int i=0;i<end.size();i++)
            {
                vista.addCoordenada(x1, end.get(i), y1, y2, r.get(i));
            }
        }
        
        int x = (desp_inicial+desp_final-60)/2;       
        vista.addImagen(x,fila*100+10,getLabel(nodo),getNombre(nodo));
        return x;
    }   
}

