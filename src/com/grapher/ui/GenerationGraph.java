package com.grapher.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;

import com.grapher.View;

public class GenerationGraph extends Object {

	private static GenerationGraph me;
	
	
	View mainView = null;
	GraphNode currentNode = null;
	
	
	
	public View getMainView() {
		return mainView;
	}

	public void setMainView(View mainView) {
		this.mainView = mainView;
	}

	
	public GraphNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(GraphNode currentNode) {
		this.currentNode = currentNode;
	}

	private GenerationGraph() {
		super();		
	}
	
	public static synchronized GenerationGraph getInstance(){
		if(me == null){
			me = new GenerationGraph();
		}
		return me;
	}

}
