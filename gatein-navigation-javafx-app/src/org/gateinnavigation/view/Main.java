package org.gateinnavigation.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.gateinnavigation.client.NavigationNode;
import org.gateinnavigation.view.login.LoginAction;
import org.gateinnavigation.view.login.LoginData;
import org.gateinnavigation.view.login.LoginPane;
import org.gateinnavigation.view.tree.NavigationTree;
import org.gateinnavigation.view.util.MessageDialog;
import org.gateinnavigation.view.util.MessageDialog.MessageType;

public class Main extends Application implements LoginAction {
	LoginPane loginPane;
	BorderPane rootNode;
	MenuBar menuBar;
	NavigationTree tree;
	LoginData loginData;
	NavigationNode rootNavigationNode;

	@Override
	public void start(Stage stage) throws Exception {
		// stage basic setup
		stage.setTitle("GateIn Navigation");
		stage.setWidth(600);
		stage.setHeight(400);

		// builds the application menu
		menuBar = MenuBarBuilder
				.create()
				.visible(false)
				.menus(MenuBuilder
						.create()
						.items(MenuItemBuilder.create().text("Logout")
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										rootNode.setCenter(loginPane);
										menuBar.setVisible(false);
										loginPane.show(1000, null);
									}
								}).build(),
								MenuItemBuilder
										.create()
										.text("Refresh")
										.onAction(
												new EventHandler<ActionEvent>() {

													@Override
													public void handle(
															ActionEvent e) {
														createTree();
													}
												}).build(),
								MenuItemBuilder
										.create()
										.text("Exit Application")
										.onAction(
												new EventHandler<ActionEvent>() {

													@Override
													public void handle(
															ActionEvent e) {
														Platform.exit();
													}
												}).build()).build()).build();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				System.exit(0);
			}
		});
		loginPane = new LoginPane(this);
		stage.setScene(SceneBuilder
				.create().stylesheets("./main.css").fill(Color.LIGHTGREY)
				.root(rootNode = BorderPaneBuilder.create().center(loginPane)
						.top(menuBar).build()).build());
		stage.show();
	}

	@Override
	public void onSucess(final NavigationNode node, final LoginData loginData) {
		System.out.println("Logged");
		this.loginData = loginData;
		this.rootNavigationNode = node;
		loginPane.hide(1000, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// Modifies the menu to show the logged user
				menuBar.getMenus().get(0)
						.setText("Logged as " + loginData.getUsername());
				menuBar.setVisible(true);
				createTree();
			}
		});
	}

	@Override
	public void onFail(final String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				MessageDialog.showMessage("Error on login", message,
						MessageType.ERROR);
			}
		});
	}

	private void createTree() {
		tree = new NavigationTree(loginData, rootNavigationNode);
		rootNode.setCenter(tree);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
