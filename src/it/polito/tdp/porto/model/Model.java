package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {

	private Map<Integer, Author> autori;
	private Graph<Author, DefaultEdge> grafo;
	private PortoDAO pDao;
	
	
	public Model() {
		this.pDao = new PortoDAO();
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.autori = new HashMap<>();
	}
	
	public void creaGrafo() {
		pDao.getAutori(autori);
		Graphs.addAllVertices(this.grafo, autori.values());
		pDao.creaArchi(grafo, autori);
	}

	public Map<Integer, Author> getAutori() {
		return autori;
	}

	public void setAutori(Map<Integer, Author> autori) {
		this.autori = autori;
	}

	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Author, DefaultEdge> grafo) {
		this.grafo = grafo;
	}
	
	public Set<Author> getVicini(Author autore){
		
		return Graphs.neighborSetOf(this.grafo, autore);
	}
	
	public String printNeighbors(Author autore) {
		
		String s ="";
		for(Author a : this.getVicini(autore)) {
			s+=a.toString()+"\n";
		}
		return s;
	}
	
	public List<Author> getPath(Author a1, Author a2){
		
		List<Author> percorso = new ArrayList<>();
		Map<Author, Author> visita = new HashMap<>();
		visita.put(a1, null);
		
		BreadthFirstIterator<Author, DefaultEdge> it = new BreadthFirstIterator<>(grafo,a1);
		it.addTraversalListener(new TraversalListener<Author, DefaultEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Author> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Author> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> et) {
				Author sorgente = grafo.getEdgeSource(et.getEdge());
				Author destinazione = grafo.getEdgeTarget(et.getEdge());
				
				if(visita.containsKey(sorgente) && !visita.containsKey(destinazione))
					visita.put(destinazione, sorgente);
				else if(!visita.containsKey(sorgente) && visita.containsKey(destinazione))
					visita.put(sorgente, destinazione);

				
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		while (it.hasNext()) {
			it.next();
		}
		if(!visita.containsKey(a1) || !visita.containsKey(a2))
			return null;
		
		Author step = a2;
		percorso.add(a2);
		while(!step.equals(a1)) {
			percorso.add(visita.get(step));
			step = visita.get(step);
		}
		return percorso;
	}
	
	public List<String> getPapers(Author a1, Author a2){
		List<String> result = new ArrayList<>();
		List<Author> percorso = new ArrayList<>(this.getPath(a1, a2));
		for(int i=percorso.size()-1 ; i>0 ; i--) {
			Author primo = percorso.get(i);
			Author secondo = percorso.get(i-1);
			result.add(pDao.getArticolo(primo, secondo).toString()+" autore 1: "+primo.toString()+" autore 2: "+secondo.toString());
		}
		return result;
	}
}
