package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> idMAp;
	public Model() {
		// viene creato unasola volta se lo metto nel setmodel
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao=new ArtsmiaDAO();
		this.idMAp= new HashMap<Integer,ArtObject>();
	}
	public void creaGrafo() {
		// lo innvoco ogni volta che l'utente vuole
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//List<ArtObject> vertici=dao.listObjects()
		// uso la id map per evitare di creare troppi oggetti;
		dao.listObjects(idMAp);
		Graphs.addAllVertices(this.grafo, idMAp.values());
		
		// uso una identity map per creare gli oggetti una sola volta è buona norma usarla
		// aggiungo gli archi
		
		// appproccio 1 CHE NON VA USATO DOPPIO FOR TROPPO INEFFICENTE
		/*
		for(ArtObject a1:this.grafo.vertexSet()) {
			for(ArtObject a2:this.grafo.vertexSet()) {
			// chiedo al db se sono collegati
				if(!a1.equals(a2) && this.grafo.containsEdge(a1,a2)) {
					int peso=dao.getPeso(a1,a2);
					if(peso>0) {
						Graphs.addEdgeWithVertices(this.grafo, a1, a2,peso);
						
					}
				}
				
			}
		System.out.println("grafo creato");
		System.out.println("vertici "+this.grafo.vertexSet().size());
		System.out.println("vertici "+this.grafo.edgeSet().size());
		*/
		// approccio 2
		for(Adiacenza a : this.dao.getAdiacenze(idMAp)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		System.out.println("grafo creato"+"\n");
		System.out.println("vertici "+this.grafo.vertexSet().size()+"\n");
		System.out.println("vertici "+this.grafo.edgeSet().size()+"\n");
	}
	public int  nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int  nArchi() {
		return this.grafo.edgeSet().size();
	}
	public ArtObject getObject(int objectId) {

		return idMAp.get(objectId);
	}
	public int getCompoenteConnessa(ArtObject vertice) {
		Set<ArtObject> visitati= new HashSet<>();
		DepthFirstIterator<ArtObject,DefaultWeightedEdge> it= new DepthFirstIterator<ArtObject,DefaultWeightedEdge>(this.grafo,vertice);
		while(it.hasNext()) {
			visitati.add(it.next());
		}
		
		
		return visitati.size();
	}
}
