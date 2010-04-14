package org.eclipse.swtbot.eclipse.ui.wizards;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swtbot.eclipse.ui.project.ProjectCreator;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewSWTBotTestPluginWizard extends Wizard implements INewWizard {

	private NewPluginProjectWizardPage	wizardPage;

	public NewSWTBotTestPluginWizard() {
		setWindowTitle("New SWTBot Test Plugin");
		wizardPage = new NewPluginProjectWizardPage();
	}

	public void addPages() {
		addPage(wizardPage);
	}

	@Override
	public boolean performFinish() {
		String pluginName = wizardPage.pluginName();
		String pluginId = wizardPage.pluginId();
		String pluginVersion = wizardPage.pluginVersion();
		String pluginProvider = wizardPage.pluginProvider();

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		ProjectCreator projectCreator = new ProjectCreator(pluginId, pluginName, pluginVersion, pluginProvider, root);
		try {
			projectCreator.create();
			return true;
		} catch (CoreException e) {
			projectCreator.delete();
			return false;
		}
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}
