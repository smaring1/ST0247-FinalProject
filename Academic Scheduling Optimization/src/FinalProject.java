import java.util.HashSet;
import java.util.LinkedList;

class FinalProject {
    public static void main(String[] args) {
        //Implenemtar grafo para distncias entre bloques
        System.out.println("Hola");
    }
}

class Group {
    String course;
    String group;
    int professor;
    String day;
    int startTime; //TODO: Revisar cómo se mete este dato en int pues el dataset lo da como hh:mm
    int endTime; //TODO: Lo mismo con este dato
    String classroom;

    public Group(String course, String group, int professor, String day, int startTime, int endTime, String classroom) {
        this.course = course;
        this.group = group;
        this.professor = professor;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getProfessor() {
        return professor;
    }

    public void setProfessor(int professor) {
        this.professor = professor;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}

class Enrollment {
    int student;
    String course;
    int group;

    public Enrollment(int student, String course, int group) {
        this.student = student;
        this.course = course;
        this.group = group;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}

class Classroom {
    String classroomNum;
    //TODO: Completar los atributos (dudas con el tipo de aula)
    int capacity;
    int access;
    //TODO: crear metodos Constructor, getters y setters
}

//TODO: Muy importante, meter las distancias en un grafo
class Distance {
    String block_1;
    String block_2;
    float distance;

    public Distance(String block_1, String block_2, float distance) {
        this.block_1 = block_1;
        this.block_2 = block_2;
        this.distance = distance;
    }

    public String getBlock_1() {
        return block_1;
    }

    public void setBlock_1(String block_1) {
        this.block_1 = block_1;
    }

    public String getBlock_2() {
        return block_2;
    }

    public void setBlock_2(String block_2) {
        this.block_2 = block_2;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}

class Vertex{
    private String name;
    private LinkedList<Edge> edgeList;

    public Vertex(String name){
        this.name = name;
        edgeList = new LinkedList<>();
    }

    public String getName(){
        return name;
    }

    public LinkedList<Edge> getEdges(){
        return edgeList;
    }
}

class Edge{
    private int weight;
    private Vertex destVertex;

    public Edge(Vertex dest, int w){
        this.destVertex = dest;
        this.weight = w;
    }

    /* can use this approach for an unweighted graph
        or better remove variable weight altogether from Edge class */
    public Edge(Vertex dest){
        this.destVertex = dest;
        this.weight = 1;
    }

    public int getWeight(){
        return weight;
    }

    public Vertex getDestVertex(){
        return destVertex;
    }
}

/**
 * Graph implementation found on the following link:
 * https://medium.com/@mithratalluri/basic-graph-implementation-in-java-9ed12e328c57
 */
class Graph{
    private HashSet<Vertex> nodes;

    public Graph(){
        nodes = new HashSet<>();
    }

    public boolean AddEdge(Vertex v1, Vertex v2, int weight){
        //since it's a directed graph
        return v1.getEdges().add(new Edge(v2, weight));
        /* If you want to implement an undirected graph,
            add v2.getEdges().add(new Edge(v1, weight)) also */
    }

    public boolean AddVertex(Vertex v){
        return nodes.add(v);
    }

    public void printGraph(){
        //I printed it like this. You can print it however you want though
        for(Vertex v : nodes){
            System.out.print("vertex name: "+ v.getName() + ": ");
            for(Edge e : v.getEdges()){
                System.out.print("destVertex: " + e.getDestVertex().getName() + " weight: " + e.getWeight() + " | ");
            }
            System.out.print("\n");
        }
    }
}
