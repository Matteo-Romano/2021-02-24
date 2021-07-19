package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.Adiacenza;
import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private Graph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;

	public Model() {
		dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
	}

	public void creaGrafo(Match m) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

		// add all vertices
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertices(m, idMap));

		// add all edges
		for (Adiacenza a : dao.getAdiacenze(m, idMap)) {
			if (a.getPeso() >= 0) {
				// p1 meglio di p2
				if (grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				}

			} else {
				// p2 meglio di p1
				if (grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), a.getPeso() * (-1));
				}
			}
		}
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<Match> getAllMatches() {
		List<Match> matches = dao.listAllMatches();
		Collections.sort(matches, new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				// TODO Auto-generated method stub
				return o1.getMatchID().compareTo(o2.matchID);
			}
		});
		return matches;
	}

	public GiocatoreMigliore getMigliore() {

		if (grafo == null) {
			return null;
		}

		Player best = null;
		double maxDelta = -99999.0;

		for (Player p : this.grafo.vertexSet()) {
			double pesoUscente = 0.0;

			for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(e);
			}

			double pesoEntrante = 0.0;

			for (DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				pesoEntrante += this.grafo.getEdgeWeight(e);
			}
			
			if ((pesoUscente-pesoEntrante)>maxDelta) {
				maxDelta=pesoUscente-pesoEntrante;
				best = p;
			}
		}
		return new GiocatoreMigliore(best, maxDelta);
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
}
