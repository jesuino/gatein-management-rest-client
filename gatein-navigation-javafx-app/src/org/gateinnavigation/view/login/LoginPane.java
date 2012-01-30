package org.gateinnavigation.view.login;

import javafx.animation.FadeTransitionBuilder;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Duration;

import org.gateinnavigation.client.GateInNavigationClient;
import org.gateinnavigation.client.NavigationNode;
import org.gateinnavigation.storage.LoginDataStorage;
import org.gateinnavigation.view.util.MessageDialog;
import org.gateinnavigation.view.util.MessageDialog.MessageType;

public class LoginPane extends TitledPane {
	private TextField txtUsername;
	private PasswordField txtPassword;
	private TextField txtSite;
	private TextField txtHost;
	private TextField txtPortalContext;
	private Button btnAction;
	private BooleanProperty isValidatingLogin;
	private final LoginAction loginAction;

	public LoginPane(LoginAction localLoginAction) {
		setText("Navigation Login");
		setMaxWidth(300);
		setCollapsible(false);
		this.loginAction = localLoginAction;

		isValidatingLogin = new SimpleBooleanProperty(false);

		ProgressIndicator loginProgressIndicator = ProgressIndicatorBuilder
				.create().maxHeight(USE_PREF_SIZE).maxWidth(USE_PREF_SIZE)
				.build();

		final GridPane gpFields = GridPaneBuilder.create().hgap(10).vgap(5)
				.alignment(Pos.CENTER).padding(new Insets(10)).build();

		gpFields.add(new Label("Host (*)"), 0, 0);
		gpFields.add(new Label("User (*)"), 0, 1);
		gpFields.add(new Label("Password (*)"), 0, 2);
		gpFields.add(new Label("Portal (*)"), 0, 3);
		gpFields.add(new Label("Portal Context"), 0, 4);
		gpFields.add(txtHost = new TextField(LoginDataStorage.retrieve()
				.getHost()), 1, 0);
		gpFields.add(txtUsername = new TextField(LoginDataStorage.retrieve()
				.getUsername()), 1, 1);
		gpFields.add(txtPassword = new PasswordField(), 1, 2);
		gpFields.add(txtSite = new TextField(LoginDataStorage.retrieve()
				.getSite()), 1, 3);
		gpFields.add(txtPortalContext = new TextField(LoginDataStorage
				.retrieve().getPortalContext()), 1, 4);

		gpFields.add(btnAction = new Button("Login"), 1, 5);

		loginProgressIndicator.visibleProperty().bind(isValidatingLogin);
		gpFields.disableProperty().bind(isValidatingLogin);
		btnAction.disableProperty().bind(isValidatingLogin);

		setContent(StackPaneBuilder
				.create()
				.alignment(Pos.CENTER)
				.children(
						VBoxBuilder.create().padding(new Insets(10))
								.alignment(Pos.CENTER)
								.children(gpFields, btnAction).build(),
						loginProgressIndicator).build());
		setAlignment(Pos.CENTER);

		btnAction.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent evt) {
				// TODO: validate fields
				if (txtHost.getText().trim().isEmpty()
						|| txtPassword.getText().trim().isEmpty()
						|| txtSite.getText().isEmpty()
						|| txtUsername.getText().trim().isEmpty()) {
					MessageDialog.showMessage("Fill ALL fields", "Fill all required fields", MessageType.ERROR);
					return;
				}
				isValidatingLogin.setValue(true);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							NavigationNode rootNode = GateInNavigationClient
									.create(txtUsername.getText(),
											txtPassword.getText(),
											txtHost.getText(),
											txtSite.getText())
									.getRootNavigationNode(false);

							// Get the login data and saves it
							LoginData loginData = new LoginData(txtUsername
									.getText(), txtPassword.getText(), txtHost
									.getText(), txtSite.getText(),
									txtPortalContext.getText());
							LoginDataStorage.save(loginData);
							loginAction.onSucess(rootNode, loginData);
						} catch (Exception e) {
							loginAction
									.onFail("Login Failed. Check your credentials or if the server is running...");
						} finally {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									isValidatingLogin.setValue(false);
								}
							});
						}
					}
				}).start();
			}
		});

	}

	// transactions are being created on each method call. Sure it will not
	// destroy the JVM memory
	public void hide(double duration, EventHandler<ActionEvent> callback) {
		FadeTransitionBuilder.create().fromValue(100).toValue(0).node(this)
				.duration(new Duration(duration)).onFinished(callback).build()
				.play();
	}

	public void show(double duration, EventHandler<ActionEvent> callback) {
		FadeTransitionBuilder.create().fromValue(0).toValue(100).node(this)
				.duration(new Duration(duration)).onFinished(callback).build()
				.play();
	}

}
