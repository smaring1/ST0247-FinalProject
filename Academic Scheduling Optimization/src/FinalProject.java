import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

class FinalProject {

    public static final String SEPARATOR=";";
    public static final String QUOTE="\"";

    public static void main(String[] args) {
        //Llenar con la lectura de los datos
        Graph map = dataFillMap("DistanciasBloques.csv");
        System.out.println("Map: ");
        map.printGraph();
        dataFillGroup();
    }


    public static void dataFillGroup(){
        BufferedReader br = null;
        LinkedList<Group> group = new LinkedList<Group>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader("estudiante_curso_grupo.csv"));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");

                fields = removeTrailingQuotes(fields);
                System.out.println(Arrays.toString(fields));
                Group g1;
                group.addLast(g1 = new Group(fields[0],fields[1],fields[2],fields[3],fields[4],fields[5],fields[6]));

                line = br.readLine();
            }

        } catch (Exception e) {

        }
    }

    public static void dataFillEnrollment(){
        BufferedReader br = null;
        LinkedList<Enrollment> enroll = new LinkedList<Enrollment>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader("grupos_semestre.csv"));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");

                fields = removeTrailingQuotes(fields);
                System.out.println(Arrays.toString(fields));
                Enrollment e1;
                enroll.addLast(e1 = new Enrollment(fields[0],fields[1],fields[2]));

                line = br.readLine();
            }

        } catch (Exception e) {

        }
    }

    // TODO: NO TOCAR TODAVIA
    public static void dataFillClassroom(){
        BufferedReader br = null;
        LinkedList<Classroom> classrooms = new LinkedList<Classroom>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader("grupos_semestre.csv"));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");

                fields = removeTrailingQuotes(fields);
                System.out.println(Arrays.toString(fields));
                Classroom c1;
                classrooms.addLast(c1 = new Classroom());

                line = br.readLine();
            }

        } catch (Exception e) {

        }
    }

    public static Graph dataFillMap(String file) {
        Graph map = new Graph();
        Vertex [] blocks = new Vertex[19];
        blocks[0] = new Vertex("1");
        blocks[1] = new Vertex("7");
        blocks[2] = new Vertex("13");
        blocks[3] = new Vertex("14");
        blocks[4] = new Vertex("15");
        blocks[5] = new Vertex("16");
        blocks[6] = new Vertex("17");
        blocks[7] = new Vertex("18");
        blocks[8] = new Vertex("19");
        blocks[9] = new Vertex("23");
        blocks[10] = new Vertex("26");
        blocks[11] = new Vertex("27");
        blocks[12] = new Vertex("29");
        blocks[13] = new Vertex("30");
        blocks[14] = new Vertex("31");
        blocks[15] = new Vertex("33");
        blocks[16] = new Vertex("34");
        blocks[17] = new Vertex("35");
        blocks[18] = new Vertex("38");

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            //Adding block vertexes to graph
            for (int i = 0; i < blocks.length; i++) {
                map.AddVertex(blocks[i]);
            }

            //Adding distance weight values between block
            while (null != line) {
                String [] fields = line.split(",");
                fields = removeTrailingQuotes(fields);
                //System.out.println(Arrays.toString(fields));
                for (Vertex v: map.nodes) {
                    for (Vertex w: map.nodes) {
                        if ((v.getName().equals(fields[0]) && w.getName().equals(fields[1])) ||
                                (v.getName().equals(fields[1]) && w.getName().equals(fields[0]))) {
                            map.AddEdge(v, w, Integer.parseInt(fields[2]));
                        }
                    }
                }

                line = br.readLine();
            }

        } catch (Exception e) {

        }
        return map;
    }


    public static void bloques(){

    }

    public static void camino(){

    }
    private static String[] removeTrailingQuotes(String[] fields) {

        String result[] = new String[fields.length];

        for (int i=0;i<result.length;i++){
            result[i] = fields[i].replaceAll("^"+QUOTE, "").replaceAll(QUOTE+"$", "");
        }
        return result;
    }

}


class Group {
    String course;
    String group;
    String professor;
    String day;
    String startTime; //TODO: Revisar cómo se mete este dato en int pues el dataset lo da como hh:mm
    String endTime; //TODO: Lo mismo con este dato
    String classroom;

    public Group(String course, String group, String professor, String classroom, String day, String startTime, String endTime) {
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

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
    String student;
    String course;
    String group;

    public Enrollment(String student, String course, String group) {
        this.student = student;
        this.course = course;
        this.group = group;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
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
}

class Classroom {
    String classroomNum;
    //TODO: Completar los atributos (dudas con el tipo de aula)
    int capacity;
    int access;
    //TODO: crear metodos Constructor, getters y setters
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
    public HashSet<Vertex> nodes;

    public Graph(){
        nodes = new HashSet<>();
    }

    public boolean AddEdge(Vertex v1, Vertex v2, int weight){
        //since it's a directed graph
        v2.getEdges().add(new Edge(v1, weight));
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

    public boolean contains(Vertex v) {
        for (Vertex w: nodes) {
            if (v.getName().equals(w.getName())) {
                return true;
            }
        }
        return false;
    }
}
