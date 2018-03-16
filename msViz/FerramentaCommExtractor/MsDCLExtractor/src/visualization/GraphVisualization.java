package visualization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enoir.graphvizapi.Attribute;
import enoir.graphvizapi.Edge;
import enoir.graphvizapi.Graph;
import enoir.graphvizapi.GraphType;
import enoir.graphvizapi.Graphviz;
import enoir.graphvizapi.Node;

public class GraphVisualization {
	
	private static final Map<String, Node> allNodes = new HashMap<>();

	public GraphVisualization() {
	}

	
	public Graph createAllNodes(ArrayList<DependencyCommunication> communications) {
		Graph graph = new Graph("g1", GraphType.DIGRAPH); // Create New Gpaph.
		for (DependencyCommunication dep : communications) {

			if (!verifyNodeCreated(dep.getMsName())) {
				if (dep.getMsName() == null) 
					continue;
				else {
					Node node = new Node(dep.getMsName());
					node.addAttribute(new Attribute("color", "turquoise"));
					graph.addNode(node);
					allNodes.put(dep.getMsName(), node);
				}
			}

		}
		return graph;

	}

	public boolean verifyNodeCreated(String name) {
		for (String n : allNodes.keySet()) {
			Node no = allNodes.get(n);
			if (no.getId().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Node getNode(String name) {
		for (String n : allNodes.keySet()) {
			Node no = allNodes.get(n);
			if (no.getId().equals(name)) {
				return no;
			}
		}
		return null;
	}

	public void generateGraph(String path, ArrayList<DependencyCommunication> info2) throws IOException {

		Graphviz gv = new Graphviz(); // Graphviz Object.
		Graph graph = createAllNodes(info2);
		
		graph.addAttribute(new Attribute("rankdir", "LR")); // Add some attribute.
		graph.addAttribute(new Attribute("fixedsize", "false"));
		graph.addAttribute(new Attribute("fontsize", "15"));

		for (DependencyCommunication d : info2) {
			if (d.getDestin() == null)
				continue;
			else {
				Node node1 = getNode(d.getOrigin());
				Node node2 = getNode(d.getDestin());
				Edge e = new Edge(node1, node2); 
				String value = "" + d.getValue();
				e.addAttribute(new Attribute("label", value));
				graph.addEdge(e);
			}
		}
		String type = "png";
		File out = new File(path + "/graphs." + type); // Output File.
		this.writeGraphToFile(gv.getGraphByteArray(graph, type, "100"), out);
		System.out.println("Visualização em Grafos gerada com sucesso! ");
	}

	public int writeGraphToFile(byte[] img, File to) throws IOException {
		FileOutputStream fos = new FileOutputStream(to);
		fos.write(img);
		fos.close();
		return 1;
	}
}
