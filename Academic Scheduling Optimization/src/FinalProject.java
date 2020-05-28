import javax.swing.*;
import javax.xml.transform.Source;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This program solves the final proyect
 * for Data Structures and Algoritms II course
 * @author Simón Marín Giraldo
 * @author Julián Ramírez Giraldo
 */
class FinalProject {

    public static final String SEPARATOR=";";
    public static final String QUOTE="\"";

    static LinkedList<Group> groups = new LinkedList<Group>();
    static LinkedList<Enrollment> enrollments =new LinkedList<Enrollment>();
    static LinkedList<Classroom> classrooms =new LinkedList<Classroom>();
    static LinkedList<StudenMI> MI = new LinkedList<StudenMI>();
    static LinkedList<groupTypeClass> groupType = new LinkedList<groupTypeClass>();

    public static void main(String[] args) {
        //LLAMADOS A TODOS LOS FILL
        Graph map = dataFillMap("DistanciasBloques.csv");
        groups = dataFillGroup("grupos_semestre.csv");
        enrollments = dataFillEnrollment("estudiante_curso_grupo.csv");
        classrooms = dataFillClassroom("aulas.csv");
        MI = dataFIllStudentMI("estudiantes_discapacitados.csv");
        groupType = classType();
        //map.printGraph();
    }

    /**
     * This method fills a Group Linked List with
     * the dataset given.
     * @param file
     * @return a Linked List with the data ready
     * to be manipulated
     */
    public static LinkedList<Group> dataFillGroup(String file){
        BufferedReader br = null;
        LinkedList<Group> group = new LinkedList<Group>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");
                fields = removeTrailingQuotes(fields);

                // Limpieza de datos innecesarios
                if(!fields[6].equals("00000")) {
                  //  System.out.println(Arrays.toString(fields));
                    group.addLast(new Group(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]));
                }
                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println("ERROR "+e);
        }
        return group;
    }

    /**
     * This method fills an Enrollment Linked List with
     * the dataset given.
     * @param file
     * @return a Linked List with the data ready
     * to be manipulated
     */
    public static LinkedList<Enrollment> dataFillEnrollment(String file){
        BufferedReader br = null;
        LinkedList<Enrollment> enroll = new LinkedList<Enrollment>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");

                fields = removeTrailingQuotes(fields);

                enroll.addLast(new Enrollment(fields[0],fields[1],fields[2]));

                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println("ERROR "+e);
        }
        return enroll;
    }

    /**
     * This method fills a Classroom Linked List with
     * the dataset given.
     * @param file
     * @return a Linked List with the data ready
     * to be manipulated
     */
    public static LinkedList<Classroom> dataFillClassroom(String file){
        BufferedReader br = null;
        LinkedList<Classroom> classrooms = new LinkedList<Classroom>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");

                fields = removeTrailingQuotes(fields);
                // System.out.println(Arrays.toString(fields));
                if(fields[2].equals("N/A")){
                    classrooms.addLast(new Classroom(fields[0],fields[1],0,Integer.parseInt(fields[3])));
                }else{
                    classrooms.addLast(new Classroom(fields[0],fields[1],Integer.parseInt(fields[2]),Integer.parseInt(fields[3])));
                }

                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println("Eche no joda mano "+e);
        }
        return classrooms;
    }

    /**
     * This method fills a Reduced Movility Students
     * Linked List with the given dataset.
     * @param file
     * @return a Linked List with the data ready
     * to be manipulated
     */
    public static LinkedList<StudenMI> dataFIllStudentMI(String file){
        BufferedReader br = null;
        LinkedList<StudenMI> MI = new LinkedList<StudenMI>();
        int cont = 0;
        try {
            br =new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(",");

                fields = removeTrailingQuotes(fields);


                MI.addLast(new StudenMI(fields[0],Boolean.parseBoolean(fields[1])));

                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println("ERROR "+e);
        }
        return MI;
    }

    /**
     * This method fills a weighted and undirected Graph
     * representing the blocks in the university and
     * the distances between them.
     * @param file
     * @return a weighted and undirected Graph with the data ready
     * to be manipulated
     */
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
            System.out.println("ERROR "+e);
        }
        return map;
    }


    public static LinkedList<groupTypeClass> classType(){
        LinkedList<groupTypeClass> groupType1 = new LinkedList<groupTypeClass>();

        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < classrooms.size(); j++) {
                if(groups.get(i).getClassroom().equals(classrooms.get(j).getClassroomNum())){
                    groupType1.addLast(new groupTypeClass(groups.get(i).getClassroom(),classrooms.get(j).getType()));
                    System.out.println("SALON: "+groups.get(i).getClassroom()+" = "+classrooms.get(j).getType());
                }
            }
        }
        return groupType1;
    }

    /**
     * This method removes the unnecessary quotation
     * marks in data lines.
     * @param fields
     * @return
     */
    private static String[] removeTrailingQuotes(String[] fields) {

        String result[] = new String[fields.length];

        for (int i=0;i<result.length;i++){
            result[i] = fields[i].replaceAll("^"+QUOTE, "").replaceAll(QUOTE+"$", "");
        }
        return result;
    }

    /**
     * This method changes the location for a group
     * @param source the classroom in which a group is assigned.
     * @param destination the classroom where the group will be moved.
     * @return true if the change was succesfully done, false otherwise.
     */
    public boolean changeClassroom(String source, Source destination) {
        return true; //TODO: Implementar este método
        //La idea es usar este método por si queremos cambiar a un grupo de
        //Salón, los parámetros serán dados finalmente al invocarse, pues
        //El algoritmo determinará el salón origen del grupo a cambiar
        //Y el destino a donde se debe mover y hacer los cambios en las
        //Listas enlazadas a las que corresponde esta información.
        //Es booleano para que si se logró cambiar retorne true, de lo
        //contrario, retorna false.
    }

    //TODO: crear un método que dado un curso, si este tiene estudiantes
    //con problemas de movilidad, los trastee para el bloque más cercano con un salón disponible
    //que a la vez tenga accesibilidad para sillas de ruedas
}

/**
 * This data type defines a group
 * for a class in the university.
 */
class Group {
    String course;
    String group;
    String professor;
    String day;
    String startTime; //TODO: Revisar cómo se mete este dato en int pues el dataset lo da como hh:mm
    String endTime; //TODO: Lo mismo con este dato
    String classroom;

    public Group(String course, String group, String professor, String day, String startTime, String endTime, String classroom) {
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

    /**
     * This method returns the block in whick a classroom is
     * @return block
     */
    public String getClassroomBlock() {
        String b = classroom;
        b = classroom.charAt(0) + classroom.charAt(1) + "";
        if (b.startsWith("0")) {
            return b.charAt(1) + "";
        } else {
            return b;
        }
    }

    /**
     * This method returns the classroom number from
     * a clasroom id.
     * @return classroom number
     */
    public String getClassroomNumber() {
        String b = classroom;
        b = b.substring(2);
        return b;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}

/**
 * This data type defines the
 * enrollment of students in a
 * course at the university
 */
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
    String type;
    //TODO: Completar los atributos (dudas con el tipo de aula)
    int capacity;
    int access;
    //TODO: crear metodos Constructor, getters y setters

    public Classroom(String classroomNum, String type, int capacity, int access) {
        this.classroomNum = classroomNum;
        this.type = type;
        this.capacity = capacity;
        this.access = access;
    }

    public String getClassroomNum() {
        return classroomNum;
    }

    public void setClassroomNum(String classroomNum) {
        this.classroomNum = classroomNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "classroomNum='" + classroomNum + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", access=" + access +
                '}';
    }
}


class StudenMI{
    String Student_ID;
    boolean MobilityImpairment;

    public StudenMI(String student_ID, boolean mobilityImpairment) {
        Student_ID = student_ID;
        MobilityImpairment = mobilityImpairment;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    public boolean isMobilityImpairment() {
        return MobilityImpairment;
    }

    public void setMobilityImpairment(boolean mobilityImpairment) {
        MobilityImpairment = mobilityImpairment;
    }

    @Override
    public String toString() {
        return "StudenMI{" +
                "Student_ID='" + Student_ID + '\'' +
                ", MobilityImpairment=" + MobilityImpairment +
                '}';
    }
}


class groupTypeClass{
    String classroom;
    String type;

    public groupTypeClass(String classroom, String type) {
        this.classroom = classroom;
        this.type = type;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "groupTypeClass{" +
                "classroom='" + classroom + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

class Vertex{
    private String name;
    private LinkedList<Edge> edgeList;

    public Vertex(String name){
        this.name = name;
        edgeList = new LinkedList<>();
    }

    /**
     * This method finds the nearest block
     * that also has access for people with
     * mobility issues
     * @return a Vertex in the graph which
     * represents the block
     */
    public Vertex nearestAccessibleBlock() {
        LinkedList<Edge> edges = this.edgeList;
        Edge minValue = edges.peekFirst();
        for (int i = 1; i < edges.size(); i++) {
            if (edges.get(i).weight < minValue.weight && edges.get(i).destVertex.isAccessible()) {
                minValue = edges.get(i);
            }
        }
        return minValue.destVertex;
    }

    /**
     * This method determines if a certain
     * block has access for people with
     * mobility problems.
     * @return true if it has, false otherwise.
     */
    private boolean isAccessible() { //TODO: Implementar un método que determine si un bloque tiene accesibilidad para movilidad reducida
        return true;
    }

    public String getName(){
        return name;
    }

    public LinkedList<Edge> getEdges(){
        return edgeList;
    }
}

class Edge{
    public int weight;
    public Vertex destVertex;

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
