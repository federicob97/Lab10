package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

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
}
