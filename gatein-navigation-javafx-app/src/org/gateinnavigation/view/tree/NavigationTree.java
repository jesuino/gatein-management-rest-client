package org.gateinnavigation.view.tree;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import org.gateinnavigation.client.NavigationNode;
import org.gateinnavigation.view.login.LoginData;

public class NavigationTree extends TreeView<NavigationNode> {
	public NavigationTree(LoginData loginData, NavigationNode rootNode) {
		List<TreeItem<NavigationNode>> children = generateTree(rootNode);
		TreeItem<NavigationNode> rootItem = new TreeItem<NavigationNode>(
				rootNode);		
		if (children != null)
			rootItem.getChildren().addAll(children);

		setRoot(rootItem);
		setContextMenu(new NavigationTreeContextMenu(loginData, rootNode.getClient(), this));

	}

	// TODO: Improve this!
	public List<TreeItem<NavigationNode>> generateTree(NavigationNode node) {
		if (node.getChildren() != null) {
			List<TreeItem<NavigationNode>> items = new ArrayList<TreeItem<NavigationNode>>();
			for (NavigationNode n : node.getChildren()) {
				TreeItem<NavigationNode> item = new TreeItem<NavigationNode>(n);				
				List<TreeItem<NavigationNode>> children = generateTree(n);
				if (children != null)
					item.getChildren().addAll(children);
				items.add(item);
			}
			return items;
		}
		return null;
	}

}
