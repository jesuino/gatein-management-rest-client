package org.gateinnavigation.view.tree;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;

import org.gateinnavigation.client.GateInNavigationClient;
import org.gateinnavigation.client.NavigationNode;
import org.gateinnavigation.storage.LoginDataStorage;
import org.gateinnavigation.util.IOUtil;
import org.gateinnavigation.view.login.LoginData;
import org.gateinnavigation.view.util.MessageDialog;
import org.gateinnavigation.view.util.SimpleWebView;
import org.gateinnavigation.view.util.MessageDialog.MessageType;

/**
 * 
 * The Context Menu to perform some operations with nodes
 * 
 * @author William Antônio
 * 
 */
public class NavigationTreeContextMenu extends ContextMenu {

	final GateInNavigationClient client;
	final TreeView<?> target;
	LoginData loginData;

	public NavigationTreeContextMenu(LoginData loginData,
			GateInNavigationClient gateInNavigationClient, TreeView<?> tree) {
		this.client = gateInNavigationClient;
		this.target = tree;
		this.loginData = loginData;
		MenuItem saveAsZip = new MenuItem("Save as zip");
		MenuItem openPage = new MenuItem("Open Page");

		saveAsZip.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				NavigationNode n = (NavigationNode) target.getSelectionModel()
						.getSelectedItem().getValue();

				try {
					String fileName = n.getName() + ".zip";
					IOUtil.writeFile(client.getResourceZip(n.getPath()),
							fileName);
					MessageDialog.showMessage(null,
							"Node exported to file: "
									+ fileName, MessageType.WARNING);
				} catch (IOException e1) {
					MessageDialog.showMessage(null, "Error exporting node",
							MessageType.ERROR);
					e1.printStackTrace();

				}
			}
		});

		openPage.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				NavigationNode n = (NavigationNode) target.getSelectionModel()
						.getSelectedItem().getValue();
				LoginData ld = LoginDataStorage.retrieve();
				String baseUrl = ld.getHost() + "/" + ld.getPortalContext()
						+ "/" + ld.getSite();
				System.out.println(baseUrl + n.getPath());
				SimpleWebView.openUrl(baseUrl + "/" + n.getPath());
			}
		});
		getItems().addAll(saveAsZip, openPage);

	}
}
