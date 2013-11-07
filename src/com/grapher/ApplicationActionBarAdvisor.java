package com.grapher;

import java.rmi.server.ExportException;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.grapher.actions.CreateNodeAction;
import com.grapher.actions.ExportAction;
import com.grapher.actions.NewGraphAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	
	private CreateNodeAction newNodeAction;
	private ExportAction exportAction;
	private NewGraphAction newGraphAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}
	
	@Override
	protected void makeActions(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		super.makeActions(window);
		newNodeAction = new CreateNodeAction(window, "New Node");
		exportAction = new ExportAction(window, "Export");
		newGraphAction = new NewGraphAction(window, "New Graph");
        register(newNodeAction);
        register(exportAction);
	}
	
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		// TODO Auto-generated method stub
		super.fillMenuBar(menuBar);
		MenuManager fileMenu = new MenuManager("&Nodes", IWorkbenchActionConstants.M_EDIT);
		menuBar.add(fileMenu);
		fileMenu.add(newNodeAction);
		fileMenu.add(newGraphAction);
		fileMenu.add(exportAction);
		
	}
}
