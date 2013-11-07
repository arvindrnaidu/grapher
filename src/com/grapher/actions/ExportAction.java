package com.grapher.actions;

import java.io.File;
import java.io.FileWriter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;

import com.grapher.View;
import com.grapher.ui.GenerationGraph;
import com.grapher.ui.MyTitleAreaDialog;

public class ExportAction extends Action {
	
    public ExportAction(IWorkbenchWindow window, String label) {
            setText(label);
            setId("grapher.export");
    }
    
    public void run() {
//    	Nasty code .. will change when I understand eclipse plugins
    	View v = GenerationGraph.getInstance().getMainView();
    	try{
    		File f = new File("/Users/arvindnaidu/jws/realfinaldemographiclanguage/src/d3.txt");
    		f.createNewFile();
    	    FileWriter writer = new FileWriter(f); 
    	    writer.write(v.getString().toString()); 
    	    writer.flush();
    	    writer.close();    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
