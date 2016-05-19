
package com.neo4j;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Jessica
 */
public class Panel extends JPanel
{
    ArrayList<Coordenadas> relaciones;
    ArrayList<Imagen> nodos;
    
    public Panel()
    {
        super();
        relaciones = new ArrayList<>();
        nodos = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g)
    {
        
        g.setColor(Color.WHITE);
        g.fillRect(0,0,900,610);
        g.setColor(Color.BLACK); 
        g.drawLine(0,0,900,0);
        g.drawLine(0,1,900,1);
        g.setFont(new Font("Arial",Font.BOLD,9));
        
        for(Coordenadas coord : relaciones)
        {
           g.setColor(Color.DARK_GRAY);
           g.drawLine(coord.x1, coord.y1, coord.x2, coord.y2);
           g.drawLine(coord.x1+1, coord.y1, coord.x2+1, coord.y2);
           g.drawLine(coord.x1-1, coord.y1, coord.x2-1, coord.y2);
           
           int xm=(coord.x1+coord.x2)/2-5;
           int ym=(coord.y1+coord.y2)/2-5;
           
           
           g.setColor(Color.WHITE);
           g.fillRect(xm,ym-10,13,13);
           
           g.setColor(Color.RED);
           g.drawString("("+getRelacion(coord.label)+")",xm,ym);
        }
        
        g.setColor(Color.WHITE);
        for(Imagen ima: nodos)
        {
            g.drawImage(getImagen(ima.label).getImage(),ima.x,ima.y,null);    
            if(ima.nombre.length()<=8)
            g.drawString(ima.nombre,ima.x+3,ima.y+28);
            else
            g.drawString(ima.nombre.substring(0,5)+"...",ima.x+3,ima.y+28);
        }
    }
    
    public ImageIcon getImagen(String label)
    {
        ImageIcon icon=null;
        switch(label)
        {
            case "Consulta": icon = new ImageIcon("imagenes/Azul.png");  break;
            case "Select": icon = new ImageIcon("imagenes/Verde.png"); break;
            case "Tabla": icon = new ImageIcon("imagenes/Mostaza.png"); break;
            case "Atributo": icon = new ImageIcon("imagenes/Rojo.png"); break;
            case "Valor": icon = new ImageIcon("imagenes/Rosado.png"); break;
            case "Operacion": icon = new ImageIcon("imagenes/Morado.png"); break;
        }
        return icon;
    }
    
    public int getRelacion(String label)
    {
        int x=0;
        switch(label)
        {
            case "Datos": x=1;  break;
            case "Tablas": x=2; break;
            case "Condicion": x=3; break;
            case "Incluir": x=4; break;
            case "Join": x=5; break;
            case "On": x=6; break;
            case "Operador": x=7; break;
        }
        return x;
    }
    
    public void addCoordenada(int x1, int x2, int y1, int y2,String label)
    {
        Coordenadas coord = new Coordenadas(x1, x2, y1, y2,label);
        relaciones.add(coord);
    }
    
    public void addImagen(int x, int y, String label, String nombre)
    {
        Imagen ima = new Imagen(x, y, label, nombre);
        nodos.add(ima);
    }
    
    public class Coordenadas
    {
        int x1,x2,y1,y2;
        String label;

        public Coordenadas() 
        {
            
        }

        public Coordenadas(int x1, int x2, int y1, int y2,String label)
        {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.label=label;
        }  
    }
    
    public class Imagen
    {
        int x,y;
        String label;
        String nombre;
        
        public Imagen()
        {
            
        }

        public Imagen(int x, int y, String label, String nombre) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.nombre = nombre;
        }
    }
    
}
