package enoir.graphvizapi;

/**
 * The Edge class like Graphviz's edge.
 * Created by frank on 2014/11/20.
 */
public class Edge extends BaseGraphObject {
    private Node fromNode;
    private Node toNode;
    private String label;

    /**
     * Edge constructor with edge id.
     * @param id Edge Id.
     * @param fromNode From Node.
     * @param toNode To Node.
     */
    public Edge(String id,Node fromNode,Node toNode){
        super(id);
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.label = "1";
    }

//    public Edge(String id,Node fromNode,Node toNode){
//        super(id);
//        this.fromNode = fromNode;
//        this.toNode = toNode;
//    }
    /**
     * Edge constructor without edge id. Default id is blank.
     * @param fromNode
     * @param toNode
     */
    public Edge(Node fromNode,Node toNode, String label){
        super("");
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.label = label;
    }
    public Edge(Node fromNode,Node toNode){
        super("");
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.label = "1";
    }

    /**
     * From node getter.
     * @return from Node
     */
    public Node getFromNode(){
        return this.fromNode;
    }

    /**
     * To node getter.
     * @return to node.
     */
    public Node getToNode(){
        return this.toNode;
    }
    
    public String getLabel() {
    	return this.label;
    }
    @Override
    public String genDotString() {
        StringBuilder dotString = new StringBuilder();
        dotString.append("["+this.genAttributeEdgeDotString()+"]");
        return dotString.toString();
    }
}
