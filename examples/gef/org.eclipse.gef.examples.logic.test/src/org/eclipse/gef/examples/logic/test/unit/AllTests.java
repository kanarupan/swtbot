/*******************************************************************************
 * Copyright (c) 2009, 2010 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Mariot Chauvin <mariot.chauvin@obeo.fr> - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.logic.test.unit;

import static org.eclipse.swtbot.eclipse.gef.finder.matchers.IsInstanceOf.instanceOf;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.examples.logic.test.CreateLogicDiagram;
import org.eclipse.gef.examples.logic.test.NewEmptyEmfProject;
import org.eclipse.gef.examples.logicdesigner.edit.CircuitEditPart;
import org.eclipse.gef.examples.logicdesigner.edit.LogicLabelEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.PlatformUI;

public class AllTests extends SWTBotGefForUnitTestsTestCase implements LogicModeler {

	private static final String PROJECT_NAME= "Test";
	
	private static final String FILE_NAME= "test.logic";
	
	private NewEmptyEmfProject emfProject = new NewEmptyEmfProject();
	
	private CreateLogicDiagram logicDiagram = new CreateLogicDiagram();

	private SWTBotGefForUnitTestsEditor editor;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		closeWelcomePage();
		emfProject.createProject(PROJECT_NAME);
		logicDiagram.createFile(PROJECT_NAME, FILE_NAME);
		editor = bot.gefEditor(FILE_NAME);
	}

	@Override
	protected void tearDown() throws Exception {		
		//saveCurrentEditor();
		editor.close();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		project.delete(true, null);
		super.tearDown();
	}

	private void closeWelcomePage() {
		try {
			bot.viewByTitle("Welcome").close();
		} catch (WidgetNotFoundException e) {
			// do nothing
		}
	}

	public void saveCurrentEditor() throws Exception {
		bot.menu("File").menu("Save").click();
	}
	
	public void testActivateTool() {	
		editor.activateTool(TOOL_CIRCUIT);
		assertEquals(TOOL_CIRCUIT, getActiveToolLabel());
		
		editor.activateTool(TOOL_CONNECTION);
		assertEquals(TOOL_CONNECTION, getActiveToolLabel());
		
		editor.activateTool(TOOL_OR_GATE);
		assertEquals(TOOL_OR_GATE, getActiveToolLabel());

		editor.activateTool(TOOL_CIRCUIT);
		assertEquals(TOOL_CIRCUIT, getActiveToolLabel());
	}

	private String getActiveToolLabel() {
		return  editor.forTestGetEditDomain().getPaletteViewer().getActiveTool().getLabel();
	}
	
	public void testGetEditPartWithLabelOnCanvas() throws Exception {
		editor.activateTool(TOOL_LABEL);
		editor.click(10, 10);
		SWTBotGefEditPart botPart = editor.getEditPart("Label");
		assertNotNull(botPart);
		assertTrue(botPart.part() instanceof LogicLabelEditPart);
	}
	

	public void testGetEditPartWithLabelInsideNode() throws Exception {	
		editor.activateTool(TOOL_CIRCUIT);
		editor.click(10, 10);		
		editor.activateTool(TOOL_LABEL);
		editor.click(10 + 3, 10 + 3);
		
		SWTBotGefEditPart botPart = editor.getEditPart("Label");
		assertNotNull(botPart);
		assertTrue(botPart.part() instanceof LogicLabelEditPart);
	}	
	

	public void testDrag() throws Exception {
		editor.activateTool(TOOL_LABEL);
		editor.click(10, 10);				
		editor.drag("Label", 100, 110);
		
		Rectangle bounds = ((GraphicalEditPart) editor.getEditPart("Label").part()).getFigure().getBounds();
		assertEquals(100, bounds.x);
		assertEquals(110, bounds.y);
	}


	public void testDragOnResizableElement() throws Exception {
		editor.activateTool(TOOL_CIRCUIT);
		editor.click(30, 30);
		SWTBotGefEditPart circuitEditPart = editor.editParts(instanceOf(CircuitEditPart.class)).get(0);
		Rectangle boundsBeforeDrag = getBounds(circuitEditPart);
		editor.drag(circuitEditPart, 50, 50);
		syncWithUIThread();
		checkSize(getBounds(circuitEditPart), boundsBeforeDrag.width, boundsBeforeDrag.height);
	}
	
	public void testResize() throws Exception {
		editor.activateTool(TOOL_CIRCUIT);
		editor.click(30, 30);
		SWTBotGefEditPart circuitEditPart = editor.editParts(instanceOf(CircuitEditPart.class)).get(0);
		Rectangle boundsBeforeResize = getBounds(circuitEditPart);
		
		circuitEditPart.resize(PositionConstants.SOUTH_WEST, 200, 200);
		syncWithUIThread();
		Rectangle boundsAfterResize = getBounds(circuitEditPart);
		checkLocation(boundsAfterResize, boundsBeforeResize.x, boundsBeforeResize.y);
		checkSize(boundsAfterResize, 200, 200);
		
		circuitEditPart.resize(PositionConstants.EAST, 150, 200);
		syncWithUIThread();
		boundsAfterResize = getBounds(circuitEditPart);
		checkLocation(boundsAfterResize, boundsBeforeResize.x + 50, boundsBeforeResize.y);
		checkSize(boundsAfterResize, 150, 200);
		
		circuitEditPart.resize(PositionConstants.NORTH, 150, 50);
		syncWithUIThread();
		boundsAfterResize = getBounds(circuitEditPart);
		checkLocation(boundsAfterResize, boundsBeforeResize.x + 50, boundsBeforeResize.y + 150);
		checkSize(boundsAfterResize, 150, 50);
		
		circuitEditPart.resize(PositionConstants.NORTH_EAST, 175, 75);
		syncWithUIThread();
		boundsAfterResize = getBounds(circuitEditPart);
		checkLocation(boundsAfterResize, boundsBeforeResize.x + 25, boundsBeforeResize.y + 125);
		checkSize(boundsAfterResize, 175, 75);
		
		
		
	}
	
	public void testDirectEdit() throws Exception {
		//TODO
	}
	
	/* Deprecated methods */
	
	public void testDeprecatedGetEditPartWithLabelOnCanvas() throws Exception {
		editor.activateTool(TOOL_LABEL);
		editor.mouseMoveLeftClick(10, 10);
		SWTBotGefEditPart botPart = editor.getEditPart("Label");
		assertNotNull(botPart);
		assertTrue(botPart.part() instanceof LogicLabelEditPart);
	}
	

	public void testDeprecatedGetEditPartWithLabelInsideNode() throws Exception {	
		editor.activateTool(TOOL_CIRCUIT);
		editor.mouseMoveLeftClick(10, 10);		
		editor.activateTool("Label");
		editor.mouseMoveLeftClick(10 + 3, 10 + 3);
		
		SWTBotGefEditPart botPart = editor.getEditPart("Label");
		assertNotNull(botPart);
		assertTrue(botPart.part() instanceof LogicLabelEditPart);
	}	
	

	public void testDeprecatedDrag() throws Exception {
		editor.activateTool(TOOL_LABEL);
		editor.mouseMoveLeftClick(10, 10);				
		editor.mouseDrag("Label", 100, 110);
		
		Rectangle bounds = ((GraphicalEditPart) editor.getEditPart("Label").part()).getFigure().getBounds();
		assertEquals(100, bounds.x);
		assertEquals(110, bounds.y);
	}
	
	private void syncWithUIThread() {
		UIThreadRunnable.syncExec(new VoidResult(){
			public void run() {
				while (PlatformUI.getWorkbench().getDisplay().readAndDispatch()) {
				}
			}			
		});
	}
	
	private Rectangle getBounds (SWTBotGefEditPart editPart) throws Exception {
		return ((GraphicalEditPart) editPart.part()).getFigure().getBounds().getCopy();
	}

	private void checkLocation(final Rectangle bounds , int x, int y) throws Exception {
		assertEquals(x, bounds.x);
		assertEquals(y, bounds.y);
	}
	
	private void checkSize(final Rectangle bounds , int width, int height) throws Exception {
		assertEquals(width, bounds.width);
		assertEquals(height, bounds.height);
	}
	
	
}
