package org.gateinnavigation.client.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.gateinnavigation.client.NavigationNode;

public class NavigationNodeCache {
	private static Map<String, WeakReference<NavigationNode>> cache;

	static {
		cache = new HashMap<String, WeakReference<NavigationNode>>();

	}

	// TODO: Implement this cache using WeakReference
	public static NavigationNode getNode(String URL) {
		WeakReference<NavigationNode> r = cache.get(URL);
		if (r == null)
			return null;
		else
			return r.get();
	}

	public static void cacheNode(NavigationNode navigationNode) {
		cache.put(navigationNode.getSelf(), new WeakReference<NavigationNode>(
				navigationNode));
	}
}
