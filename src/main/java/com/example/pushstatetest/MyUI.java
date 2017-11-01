package com.example.pushstatetest;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@PushStateNavigation
@Push
public class MyUI extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		Panel mainContentPanel = new Panel();
		new Navigator(this, mainContentPanel);
		UI.getCurrent().getNavigator().addView("", LandingView.class);
		UI.getCurrent().getNavigator().addView(TabbedView.VIEW_ID, new TabbedView());
		VerticalLayout navigationLayout = new VerticalLayout(
				new Button("View 1", e -> UI.getCurrent().getNavigator().navigateTo("")),
				new Button("View 2", e -> UI.getCurrent().getNavigator().navigateTo("tabbed-view")));
		
		navigationLayout.setWidth("200px");
		HorizontalLayout rootLayout = new HorizontalLayout(navigationLayout);
		rootLayout.setSizeFull();
		mainContentPanel.setSizeFull();
		rootLayout.addComponentsAndExpand(mainContentPanel);
		setContent(rootLayout);
		System.out.println("UI init!");
		
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
