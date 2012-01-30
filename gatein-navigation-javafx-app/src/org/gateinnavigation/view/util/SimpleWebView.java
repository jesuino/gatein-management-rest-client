package org.gateinnavigation.view.util;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebViewBuilder;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SimpleWebView {

	String url;
	WebView webView;
	static SimpleWebView instance;
	Stage stage;

	public SimpleWebView(String url) {

		Scene scene = SceneBuilder
				.create()
				.root(webView = WebViewBuilder.create().prefHeight(450)
						.prefWidth(1000).build()).build();
		webView.getEngine().load(url);
		webView.getEngine().setOnStatusChanged(
				new EventHandler<WebEvent<String>>() {

					@Override
					public void handle(WebEvent<String> event) {
						System.out.println(event.getEventType());
					}
				});
		stage = new Stage();
		stage.setTitle(url);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent we) {
				((Stage) we.getTarget()).hide();

			}
		});
		stage.setScene(scene);
		stage.show();
	}

	public void loadUrl(String url) {
		stage.show();
		webView.getEngine().load(url);
	}

	public static void openUrl(String url) {
		if (instance == null)
			instance = new SimpleWebView(url);
		instance.loadUrl(url);
	}
}
