package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			conn.close();
			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(Author primo, Author secondo) {

		final String sql = "SELECT * " + 
				"FROM paper " + 
				"WHERE eprintid IN ( " + 
				"SELECT c1.eprintid " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.eprintid=c2.eprintid AND c1.authorid= ? AND c2.authorid= ? )";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, primo.getId());
			st.setInt(2, secondo.getId());
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				conn.close();
				return paper;
			}
			else {
				conn.close();
				return null;
			}

			

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public void getAutori(Map<Integer, Author> autori) {
		
		final String sql = "SELECT id, lastname, firstname " + 
				"FROM author";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author a = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				autori.put(a.getId(), a);
			}
			conn.close();

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
	
	public void creaArchi(Graph<Author, DefaultEdge> grafo, Map<Integer, Author> autori) {
		
		final String sql = "SELECT c1.authorid AS id1, c2.authorid AS id2 " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.eprintid=c2.eprintid AND c1.authorid > c2.authorid " + 
				"GROUP BY id1,id2";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				grafo.addEdge(autori.get(rs.getInt("id1")), autori.get(rs.getInt("id2")));
			}

			conn.close();
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
}