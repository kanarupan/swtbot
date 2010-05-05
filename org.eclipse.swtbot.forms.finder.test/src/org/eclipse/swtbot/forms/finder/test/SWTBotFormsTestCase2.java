package org.eclipse.swtbot.forms.finder.test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.forms.finder.finders.SWTFormsBot;
import org.eclipse.swtbot.forms.finder.widgets.SWTBotExpandableComposite;
import org.eclipse.swtbot.forms.finder.widgets.SWTBotHyperlink;
import org.eclipse.swtbot.forms.finder.widgets.SWTBotImageHyperlink;
import org.eclipse.swtbot.forms.finder.widgets.SWTBotSection;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotFormsTestCase2 {

	public static SWTFormsBot bot;
	public static SWTWorkbenchBot wbot;
	
	
	@Test
	public void findsHyperlink() {
		SWTBotHyperlink link = bot.hyperlink();
		Assert.assertNotNull(link);
		Assert.assertEquals(link.getText(), "This is an example of a form that is much longer and will need to wrap.");
	}
	
	@Test
	public void findsExpandableComposite() {
		SWTBotExpandableComposite ec = bot.expandableComposite("Expandable Composite title");
		Assert.assertNotNull(ec);
		Assert.assertEquals(ec.getText(), "Expandable Composite title");
	}
	
	@Test
	public void findsSection() {
		SWTBotSection section = bot.section("Section title");
		Assert.assertNotNull(section);
		Assert.assertEquals(section.getText(), "Section title");
	}
	
	@Test
	public void findsImageHyperlink() {
		SWTBotView view = wbot.viewByTitle("FormsView");
		view.show();
		SWTBotImageHyperlink link = bot.imageHyperlink("Link with image and text");
		Assert.assertNotNull(link);
		Assert.assertEquals(link.getText(), "Link with image and text");
	}

	@Before
	public void setUp() {
		bot = new SWTFormsBot();
		wbot = new SWTWorkbenchBot();
		SWTBotView welcome = wbot.viewByTitle("Welcome");
		welcome.close();
		SWTBotView view = wbot.viewByTitle("FormsView");
		view.show();
	}

	@After
	public void tearDown() {
		// TODO
	}

}
