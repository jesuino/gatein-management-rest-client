package org.gateinmanager.test.client;

import java.io.IOException;

import org.gateinnavigation.client.GateInNavigationClient;
import org.gateinnavigation.client.NavigationNode;
import org.gateinnavigation.util.IOUtil;

public class TestNavigationNode {
	public static void main(String[] args) throws IOException {
		GateInNavigationClient client = GateInNavigationClient.create("root",
				"gtn", "http://localhost:8080", "classic");

		NavigationNode node = client.getRootNavigationNode(true);
		System.out.println(node.getName() + " " + node.getPath());
		printNavigatioTree(node, 0);
		// generate a .zip file of this node
		IOUtil.writeFile(client.getResourceZip(node.getPath()), "out.zip");
		
	}

	public static void printNavigatioTree(NavigationNode node, int depth) {
		++depth;
		if (node.getChildren() != null)
			for (NavigationNode navigationNode : node.getChildren()) {
				for (int i = 0; i < depth; i++) {
					System.out.print("\t");
				}
				System.out.println(navigationNode.getName() + " - "
						+ navigationNode.getPath());
				printNavigatioTree(navigationNode, depth);
			}
	}
}
