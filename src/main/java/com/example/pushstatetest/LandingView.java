package com.example.pushstatetest;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class LandingView extends CustomComponent implements View {
	public LandingView() {
		setCompositionRoot(new Label("LandingView: Hello world"));
	}
	@Override
	public void enter(ViewChangeEvent event) {
		Page.getCurrent().setTitle("Landing - PushStateTest");
	}
}
