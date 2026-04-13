import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private List<Condition<T>> conditions;
    private String name;
    private String otherwise;
    public Node(String name){
        this.name = name;
        this.conditions = new ArrayList<>();
        this.otherwise = null;
    }
}
