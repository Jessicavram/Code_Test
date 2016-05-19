
package com.neo4j;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author Jessica
 */

public class ComponentesBD
{

   GraphDatabaseFactory factory;
   GraphDatabaseService service; 
    
   public enum Nodo implements Label
   {
        Consulta, Select, Operacion, Atributo, Valor, Tabla
   }
   
   public enum Relacion implements RelationshipType
   {
        Datos, Incluir, Tablas, Join, On, Condicion, Operador
   }
   
   public ComponentesBD()
   {
       factory = new GraphDatabaseFactory();
       service = factory.newEmbeddedDatabase(new File("db"));
   }
   
   public void construir_grafo(String consulta)
   {
        String data[] = consulta.split("(SELECT)|(FROM)|(WHERE)");
            
        String columnas = data[1].trim();
        String tablas = "";
        String condiciones = "";
        
        int cont=2;
            
        if(consulta.contains("FROM"))
        {
            tablas = data[cont].trim();
            cont++;
        }
        if(consulta.contains("WHERE"))
        {
            condiciones = data[cont].trim();
            cont++;
        }
        
        service.execute("match(n) detach delete(n)");
        
        try(Transaction tx = service.beginTx())
        {
            Node nodoPpal = service.createNode(Nodo.Consulta);
            nodoPpal.setProperty("nombre","Consulta");
            nodoPpal.setProperty("consulta",consulta);
            
            Node nodoSelect = contruir_select(columnas, service);
            nodoPpal.createRelationshipTo(nodoSelect,Relacion.Datos);
            
            if(tablas!="")
            {
                Node nodoFrom = construir_from(tablas, service);
                nodoPpal.createRelationshipTo(nodoFrom,Relacion.Tablas);
            }
            
            if(condiciones!="")
            {
                Node nodoWhere = construir_condicion(condiciones, service);
                nodoPpal.createRelationshipTo(nodoWhere,Relacion.Condicion);
            }
            
            tx.success();
            
            System.out.println("Grafo creado exitosamete");
            
        }
   }
   
   public Node contruir_select(String columnas, GraphDatabaseService service)
   {
       String data[] = columnas.split(",");
       
       Node nodoSelect = service.createNode(Nodo.Select);
       nodoSelect.setProperty("nombre","Select");
       
       for(String dato : data)
       {
           Node nodoColumna = service.createNode(Nodo.Atributo);
           nodoColumna.setProperty("nombre",dato.trim());
           nodoColumna.setProperty("alias",dato.trim());
           
           nodoSelect.createRelationshipTo(nodoColumna,Relacion.Incluir);
       }       
       return nodoSelect;
   }
   
   public Node construir_from(String tablas, GraphDatabaseService service)
   {
       String Tdata[] = tablas.split("JOIN");
       
       Node nodoFrom = service.createNode(Nodo.Tabla);
       nodoFrom.setProperty("nombre",Tdata[0].trim());
       nodoFrom.setProperty("Alias",Tdata[0].trim());
       
       Node nodoAux=nodoFrom;
       
       for(int i=1;i<Tdata.length;i++)
       {
           String Cdata[] = Tdata[i].trim().split("ON");
           
           Node nodoTabla = service.createNode(Nodo.Tabla);
           nodoTabla.setProperty("nombre",Cdata[0].trim());
           nodoTabla.setProperty("Alias",Cdata[0].trim());
           
           nodoAux.createRelationshipTo(nodoTabla,Relacion.Join);
           
           if(Cdata.length>1)
           {
               Node nodoCondicion = construir_condicion(Cdata[1],service);
               
               nodoAux.createRelationshipTo(nodoCondicion,Relacion.On);
           }
           
           nodoAux=nodoTabla;
       }      
       return nodoFrom;
   }
 
   public Node construir_condicion(String condicion, GraphDatabaseService service)
   {
       Node nodo=null;
       
       String Data[]=null;
       
       if(condicion.contains("OR"))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split("OR");
          nodo.setProperty("nombre","OR");
       }
       else if(condicion.contains("AND"))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split("AND");
          nodo.setProperty("nombre","AND");
       }
       else if(condicion.contains("!="))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split("!=");
          nodo.setProperty("nombre","!=");
       }
       else if(condicion.contains("<="))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split("<=");
          nodo.setProperty("nombre","<=");
       }
       else if(condicion.contains(">="))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split(">=");
          nodo.setProperty("nombre",">="); 
       }
       else if(condicion.contains("="))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split("=");
          nodo.setProperty("nombre","="); 
       }
       else if(condicion.contains("<"))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split("<");
          nodo.setProperty("nombre","<"); 
       }
       else if(condicion.contains(">"))
       {
          nodo = service.createNode(Nodo.Operacion);
          Data = condicion.split(">");
          nodo.setProperty("nombre",">"); 
       }
       
       if(nodo!=null)
       {
           Node operador1 = construir_condicion(Data[0].trim(), service);
           Node operador2 = construir_condicion(Data[1].trim(), service);
           
           nodo.createRelationshipTo(operador1,Relacion.Operador);
           nodo.createRelationshipTo(operador2,Relacion.Operador);
       }
       else
       {
           if(condicion.contains("'"))
           {
               String valor = condicion.split("'")[1];
               nodo = service.createNode(Nodo.Valor);
               nodo.setProperty("nombre",valor);
           }
           else
           {
               nodo = service.createNode(Nodo.Atributo);
               nodo.setProperty("nombre",condicion);
               nodo.setProperty("alias",condicion);
           }
       }
       
       return nodo;
   }
   
   public Node consultar()
   {
       Result result = service.execute( "match (n:Consulta) return (n)" );
       Map<String,Object> row = result.next();
       Entry<String,Object> column = row.entrySet().iterator().next();
       return (Node)column.getValue();
   }
}
