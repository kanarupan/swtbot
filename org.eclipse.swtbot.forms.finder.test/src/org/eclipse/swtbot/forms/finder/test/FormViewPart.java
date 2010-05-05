package org.eclipse.swtbot.forms.finder.test;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class FormViewPart extends ViewPart {
	
	FormView view;
	
	public FormViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		view = new FormView(parent);
	}

	@Override
	public void setFocus() {
		view.setFocus();
	}

}
