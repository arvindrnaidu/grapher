package com.grapher;

import java.util.HashMap;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import com.core.Edge;
import com.core.Generation;
import com.core.Node;
import com.core.RealFinalDemographicLanguage;
import com.core.Size;
import com.grapher.processor.FileProcessor;
import com.grapher.ui.GenerationGraph;
import com.grapher.ui.MyTitleAreaDialog;
import com.grapher.ui.NewEdgeDialog;

public class View extends ViewPart {
	
	public static final String ID = "grapher.view";
	private Graph graph;

	private int layout = 1;

	FileProcessor fp = new FileProcessor();
	RealFinalDemographicLanguage rfdl;
	HashMap<String, GraphNode> myNodes = new HashMap<String, GraphNode>();
	HashMap<String, GraphNode> myEdges = new HashMap<String, GraphNode>();
	
	GraphNode currentNode;
		
	public HashMap<String, GraphNode> getMyNodes() {
		return myNodes;
	}

	public void setMyNodes(HashMap<String, GraphNode> myNodes) {
		this.myNodes = myNodes;
	}

	public HashMap<String, GraphNode> getMyEdges() {
		return myEdges;
	}

	public void setMyEdges(HashMap<String, GraphNode> myEdges) {
		this.myEdges = myEdges;
	}

	public void addNode(String nodeId, String genId, String genVal, String edgeId, String startSize, String endSize){	
		if(GenerationGraph.getInstance().getCurrentNode() != null){
			Generation g = rfdl.getGenerations().get(genId);
			if(g == null){
//				Constant grid points
				g = new Generation(Double.parseDouble(genVal), 2);
				g.setName(genId);
				rfdl.getGenerations().put(genId, g);
				rfdl.getAllVariables().put(genId, g);
			}
			Node n = new Node(g);
			n.setId(nodeId);
			rfdl.getNodes().put(nodeId, n);			
			rfdl.getAllVariables().put(nodeId, n);
			GraphNode gn = new GraphNode(graph, SWT.NONE, nodeId + "-" + genId, n);
			myNodes.put(nodeId, gn);
			
			Size startSz = new Size(Double.parseDouble(startSize), 1);
			Size endSz = new Size(Double.parseDouble(endSize), 1);
			
		//	Edge e = new Edge((Node)GenerationGraph.getInstance().getCurrentNode().getData(), n, startSz, endSz);
			Edge e = new Edge(n,(Node)GenerationGraph.getInstance().getCurrentNode().getData(),endSz,startSz);

			e.setId(edgeId);		
			rfdl.getEdges().put(edgeId, e);
			
			GraphConnection conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, GenerationGraph.getInstance().getCurrentNode(), gn);
			conn.setText(edgeId);
			conn.setData(e);
			graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(), true);
//			
		}else{
			GenerationGraph.getInstance().setCurrentNode(null);
		}
	}
	public void addEdge(GraphNode node1, GraphNode node2, String edgeId, String startSize, String endSize){	
		if(GenerationGraph.getInstance().getCurrentNode() != null){			
			Size startSz = new Size(Double.parseDouble(startSize), 1);
			Size endSz = new Size(Double.parseDouble(endSize), 1);
			
		//	Edge e = new Edge((Node)GenerationGraph.getInstance().getCurrentNode().getData(), n, startSz, endSz);
			Edge e = new Edge((Node)node1.getData(),(Node)node2.getData(),endSz,startSz);

			e.setId(edgeId);		
			rfdl.getEdges().put(edgeId, e);
			
			GraphConnection conn = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node1, node2);
			conn.setText(edgeId);
			conn.setData(e);
			graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(), true);
//			
		}else{
			GenerationGraph.getInstance().setCurrentNode(null);
		}
	}
	public void newGraph(){
		myNodes.clear();		
		myEdges.clear();
		
		for(Object gn: graph.getNodes().toArray()){
			((GraphNode) gn).dispose();
		}
		
		rfdl.getGenerations().clear();
		rfdl.getAllVariables().clear();
		rfdl.getNodes().clear();
		rfdl.getEdges().clear();
		
		Generation Ginf = new Generation(Double.POSITIVE_INFINITY, 0);
		Ginf.setName("Ginf");
		Ginf.setId("Ginf");
		rfdl.getGenerations().put("Ginf", Ginf);
		rfdl.getAllVariables().put("Ginf", Ginf);
		
		Node n = new Node(Ginf);
		n.setId("gnode");
		rfdl.getNodes().put("gnode", n);
		rfdl.getAllVariables().put("gnode", n);
				
		GraphNode gn = new GraphNode(graph, SWT.NONE, "Ginf", n);
		
		myNodes.put("Ginf", gn);
//		new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, GenerationGraph.getInstance().getCurrentNode(), gn);
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(), true);
	}
	
	public StringBuilder getString(){
		StringBuilder sb = new StringBuilder();
		for(String genId : rfdl.getGenerations().keySet()){
			if(!genId.toUpperCase().equals("GINF") && !genId.toUpperCase().equals("G0")){
				Generation gen = rfdl.getGenerations().get(genId);			  
				sb.append(gen.getId() + "=gen(" + gen.getValue().intValue() + "," + gen.getGridPoints() + ");\n");				
			}
		}
		sb.append("\n");
		for(String nodId: rfdl.getNodes().keySet()){
			Node n = rfdl.getNodes().get(nodId);	
			String genId = n.getGen().toString().split("_")[0];
		//	if(!genId.toUpperCase().equals("GI") && !genId.toUpperCase().equals("G0")){
				sb.append(n.getId() + "=node(" + genId + ");\n");	
		//	}						
		}
		
		sb.append("\n");
		
		for(String edgeId: rfdl.getEdges().keySet()){
			Edge e = rfdl.getEdges().get(edgeId);
			if(e.getN2().getGen().toString().split("_")[0].equals("Ginf")){
                sb.append(e.getId() + "=edge(" + e.getN1().getId() + "," + e.getN2().getId() + ",size(" + e.gets1().getValue().intValue() + "));\n");

            } 
            else  if (e.gets1().getValue().intValue() != e.gets2().getValue().intValue() )
            {
                sb.append(e.getId() + "=edge(" + e.getN1().getId() +"," + e.getN2().getId() + ",size(" + e.gets1().getValue().intValue() + "),size(" + e.gets2().getValue().intValue()  + "));\n");

            }			
//			if(!e.toUpperCase().equals("GI") && !e.toUpperCase().equals("G0")){
//				sb.append(e.getId() + "=edge(" + e.getN1().getId() + "," + e.getN2().getId() + ",size(" + e.getN1().getSumOfOutGoing() + "),size(" + e.getN2().getSumOfIncoming() + "));\n");
//			}						
		}
		
		return sb;
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
//	Important - Function - First to fire
	public void createPartControl(Composite parent) {
				
		rfdl = fp.process("/Users/arvindnaidu/jws/realfinaldemographiclanguage/src/d3.txt");
		// Graph will hold all other objects
		graph = new Graph(parent, SWT.NONE);
				
		GenerationGraph.getInstance().setMainView(this);	
		
		for(String nodeId: rfdl.getNodes().keySet()){
			Node n = rfdl.getNodes().get(nodeId);			
			String s = nodeId + " - " + n.getGen().toString().substring(0,n.getGen().toString().indexOf("_")).toUpperCase();
			GraphNode gn = new GraphNode(graph, SWT.NONE, s, n);			
			gn.setBackgroundColor(graph.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			myNodes.put(nodeId, gn);
		}		
		for (String edgeId :rfdl.getEdges().keySet()){
			Edge e = rfdl.getEdges().get(edgeId);
			GraphConnection cn = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, myNodes.get(e.getN2().getId()),
					myNodes.get(e.getN1().getId())); 
			cn.setText(e.getId());
			cn.setData(e);
		}
		
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(), true);
		graph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {								
				if(graph.getSelection().size() == 2){
					try{
					NewEdgeDialog dialog = new NewEdgeDialog(GenerationGraph.getInstance().getMainView().getSite().getWorkbenchWindow().getShell());
			    	dialog.create();
			    	if (dialog.open() == Window.OK) {			    		
				      	View v = GenerationGraph.getInstance().getMainView();
			    		v.addEdge((GraphNode)graph.getSelection().get(0), (GraphNode)graph.getSelection().get(1), dialog.getEdgeId(), dialog.getEndSize(), dialog.getStartSize());
			    	}
			    	}catch(Exception ex){
			    		ex.printStackTrace();
			    	}
				}else if(e.item instanceof GraphNode){
					GenerationGraph.getInstance().setCurrentNode((GraphNode)e.item);
				}
			}
		});
	}

	public void setLayoutManager() {
		switch (layout) {
		case 1:
			graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
					LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			layout++;
			break;
		case 2:
			graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(
					LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			layout = 1;
			break;

		}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */

	public void setFocus() {
	}

}