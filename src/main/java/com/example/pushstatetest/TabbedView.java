package com.example.pushstatetest;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;

public class TabbedView extends Composite implements View {
	private static final String TAB_ID_2 = "second-tab";
	private static final String TAB_ID_3 = "third-tab";
	public static String VIEW_ID = "tabbed-view";
	private CssLayout tab1;
	private CssLayout tab2;
	private CssLayout tab3;
	private TabSheet tabSheet;
	/**
	 * Flag to disable pushState when tab is changed via Navigator View's enter
	 * method (URL is already correct)
	 */
	private boolean pushState = true;

	public TabbedView() {
		System.out.println("TabbedView Constructor");

		tab1 = new CssLayout();
		tab1.setCaption("Tab 1");

		tab2 = new CssLayout();
		tab2.setCaption(TAB_ID_2);
		tab3 = new CssLayout();
		tab3.setCaption(TAB_ID_3);
		tabSheet = new TabSheet(tab1, tab2, tab3);
		tabSheet.setSizeFull();

		setCompositionRoot(tabSheet);

		tabSheet.addSelectedTabChangeListener(e -> {
			updateTitleAndPushstate();
		});
	}

	private void updateTitleAndPushstate() {
		String state;
		if (tab2 == tabSheet.getSelectedTab()) {
			state = TAB_ID_2;
		} else if (tab3 == tabSheet.getSelectedTab()) {
			state = TAB_ID_3;
		} else {
			state = "";
		}
		Page page = Page.getCurrent();
		if (pushState) {
			page.pushState("/" + VIEW_ID + "/" + state);
		}
		String title = state + (state.isEmpty() ? "" : " - ") + "Tabs - PushStateTest";
		setTitleViaPageApi(title);
		// setTitleViaBGThread(title);
		// setTitleViaJSApi(title);

	}

	/**
	 * One should ideally be able to use this, but because it set's the title before
	 * history.pushState() get's called, the old URL gets marked in to the browser's
	 * history with the new title.
	 */
	private void setTitleViaPageApi(String title) {
		Page page = Page.getCurrent();
		page.setTitle(title);
	}

	/**
	 * Push workaround for setting the page title later.
	 */
	private void setTitleViaBGThread(String title) {
		Page page = Page.getCurrent();
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			page.getUI().access(() -> {
				page.setTitle(title);
			});

		});
		thread.start();
	}

	/**
	 * JS API workaround for setting the page title later.
	 */
	private void setTitleViaJSApi(String title) {
		Page.getCurrent().getJavaScript().execute("document.title = '" + title + "';");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// select's a tab in the TabSheet based on the URL parameters

		pushState = false;
		updateTitleAndPushstate();
		String params = event.getParameters();
		Notification.show("TabbedView#enter(ViewChangeEvent) " + (params.isEmpty() ? "no params" : "params: " + params),
				Type.TRAY_NOTIFICATION);
		System.out.println("@enter " + params);
		if (TAB_ID_2.equals(params)) {
			tabSheet.setSelectedTab(tab2);
		} else if (TAB_ID_3.equals(params)) {
			tabSheet.setSelectedTab(tab3);
		} else {
			tabSheet.setSelectedTab(tab1);
		}
		pushState = true;
	}

}
