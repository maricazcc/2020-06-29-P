package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private List<String> mesi;
	private Graph <Match, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map <Integer, Match> idMap;
	private List <Match> percorsoMigliore;

	
	public Model() {
		this.mesi = new ArrayList<>();		
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
	}
	
	public List<String> getMesi(){
		mesi.add("Gennaio");
		mesi.add("Febbraio");
		mesi.add("Marzo");
		mesi.add("Aprile");
		mesi.add("Maggio");
		mesi.add("Giugno");
		mesi.add("Luglio");
		mesi.add("Agosto");
		mesi.add("Settembre");
		mesi.add("Ottobre");
		mesi.add("Novembre");
		mesi.add("Dicembre");
		return mesi;
	}
	
	public int  convertiStringaInNumero(String mese) {
		int indice = 0;
		for(int i=0;i<mesi.size();i++) {
			if(mese.compareTo(mesi.get(i))==0) {
				indice=i+1;
			}
		}
		return indice;
	}
	
public String creaGrafo(String meseSel, int minuti) {
	    grafo = new SimpleWeightedGraph <> (DefaultWeightedEdge.class);
		int mese = this.convertiStringaInNumero(meseSel);
		int min = minuti;
		
		//Aggiungo i vertici
		this.dao.PartiteDelMese(mese, idMap);
		for(Match m : idMap.values())
			this.grafo.addVertex(m);	
		
		//Aggiungo gli archi
		List <Adiacenza> archi = this.dao.getArchi(mese, min, idMap);
		
		for(Adiacenza a : archi) {
			if(grafo.containsVertex(a.getMatch1()) && grafo.containsVertex(a.getMatch2()))
			Graphs.addEdgeWithVertices(this.grafo, a.getMatch1(), a.getMatch2(), a.getPeso());
		}

		return String.format("Grafo creato con %d vertici e %d archi.\n", 
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
}

public String connessioneMax() {
	String coppia="";
	double max = 0.0;
	
	for(DefaultWeightedEdge e : grafo.edgeSet()) {
		if(grafo.getEdgeWeight(e) > max)
			max = grafo.getEdgeWeight(e);
	}
	
	for(DefaultWeightedEdge e : grafo.edgeSet()) {
		if(grafo.getEdgeWeight(e) == max)
			coppia += grafo.getEdgeSource(e) + " - " + grafo.getEdgeTarget(e)+ " (" + (int) max + ")\n";
	}
	
	return coppia;
}

public Graph<Match, DefaultWeightedEdge> getGrafo() {
	return grafo;
}

public List<Match> getVertici() {
	List<Match> lista = new ArrayList<>();
	
	for(Match m : grafo.vertexSet())
		lista.add(m);
	
	Collections.sort(lista);	
	return lista;	
}

public List<Match> trovaPercorso(Match partenza, Match arrivo) {
	this.percorsoMigliore = new ArrayList<>();
	List <Match> parziale = new ArrayList<>();
	parziale.add(partenza);
	this.cerca(parziale, arrivo);
	return percorsoMigliore;
}

private void cerca(List <Match> parziale, Match arrivo) {
	//Caso terminale
	if(parziale.get(parziale.size()-1).equals(arrivo)) {
		if(parziale.size() > this.percorsoMigliore.size())
		   this.percorsoMigliore = new ArrayList<Match> (parziale);
    return;
	}
	
	//Altrimenti
	for(Match m : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
		if(!parziale.contains(m) & this.controlloSquadre(parziale.get(parziale.size()-1), m) == true) {
			parziale.add(m);
			cerca(parziale, arrivo);
			parziale.remove(parziale.size()-1);
		}
	}
		
}

private boolean controlloSquadre(Match m1, Match m2) {
	if( (m1.getTeamHomeID()==m2.getTeamAwayID()) & (m1.getTeamAwayID()==m2.getTeamHomeID()))
		return false;
	return true;
}
	
}
