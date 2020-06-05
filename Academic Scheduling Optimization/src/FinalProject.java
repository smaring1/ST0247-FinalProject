import javax.xml.transform.Source;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.*;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This program solves the final project
 * for Data Structures and Algoritms II course
 * @author Simón Marín Giraldo
 * @author Julián Ramírez Giraldo
 */
class FinalProject {

    // DECLARACION DE VARIABLES GLOBALES
    public static final String SEPARATOR=";";
    public static final String QUOTE="\"";

    static LinkedList<Group> groups = new LinkedList<Group>();
    static LinkedList<Classroom> classrooms =new LinkedList<Classroom>();
    static LinkedList<StudenMI> MI = new LinkedList<StudenMI>();
    static LinkedList<groupTypeClass> groupType = new LinkedList<groupTypeClass>();
    static LinkedList<String> unnecesary = new LinkedList<String>();
    static LinkedList<Student> studentCourseGroup = new LinkedList<Student>();
    static LinkedList<Edge> aux = new LinkedList<Edge>();
    static LinkedList<specialGroupClass> specialClassrooms = new LinkedList<specialGroupClass>();
    static LinkedList<Group> groupsStudentsS = new LinkedList<Group>();
    static LinkedList<Class> studentLists = new LinkedList<Class>();

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis(); // TIEMPO

        Graph map = dataFillMap("DistanciasBloques.csv");
        groups = dataFillGroup("grupos_semestre.csv");
        classrooms = dataFillClassroom("aulas.csv");             // LLAMADO A TODOS LOS FILL
        MI = dataFIllStudentMI("estudiantes_discapacitados.csv");
       // studentCourseGroup = dataFillStudent("estudiante_curso_grupo.csv");
        studentCourseGroup = dataFillStudent("estudiante_curso_grupo.csv");
        // OTROS
        groupType = classType();
       // map.printGraph();
        pruebaEstudianteHorario();
        long endTime = System.currentTimeMillis();
        long totalExecTime = endTime - startTime; // TIEMPO
        System.out.println("Total execution time: " + totalExecTime + " miliseconds");
        System.out.println("Total memory usage: ");
        System.out.println("KB: " + (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
        System.out.println("MB: " + (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024)/1024);

        for (Vertex x: map.getNodes()) {
            aux = x.getEdges();
            break;
        }

        specialClassroomsFill();
        studentGroupsSpecials();
        studentLists = fillGroupsList(studentCourseGroup, MI);
        //System.out.println(getDistance("19","27",map));


    }
    


    public static void studentGroupsSpecials(){
        for(Group g: groups){
            for(specialGroupClass s: specialClassrooms){
                if(g.getClassroom().equals(s.getClassroom())){
                    groupsStudentsS.addLast(g);
                    groups.remove(g);
                }
            }
        }
    }

    public static void specialClassroomsFill(){
        String flag;
        Pattern pat = Pattern.compile("^Laboratorio.*|^Cabina.*");
        for(Group g: groups){
            for(groupTypeClass t: groupType){
                if(g.getClassroom()==t.getClassroom()){
                    Matcher mat = pat.matcher(t.getType());
                    if(mat.matches()){
                        specialClassrooms.addLast(new specialGroupClass(g.getCourse(),g.getClassroomNumber()));
                        //System.out.println(specialClassrooms.size());
                    }
                }
            }
        }
    }

    public static LinkedList<Class> fillGroupsList(LinkedList<Student> studentCourseGroup,
                                                   LinkedList<StudenMI> disabled) {
        LinkedList<Class> list = new LinkedList<>();

        int i = 0;
        for (Student s: studentCourseGroup) {
            for (CourseGroup g: s.getClasses()) {
                if (s.classes.get(i).course.equals(g.course) && s.classes.get(i).group.equals(g.group)) {
                    //TODO: Implementar porque no me dio el ki cerebral o cambiar lo que llevo
                }
            }
            i++;
        }

        return list;
    }

    public static int getDistance(String a, String b, Graph map){


        LinkedList<Edge> edges = aux;
        int weight=0;
        for (Edge e: aux) {
            for (Vertex x: map.getNodes()) {
                if(e.destVertex.getName().equals(b) && x.getName().equals(a)){
                    weight = e.weight;
                }
            }
        }
        return weight;
    }

    /**
     * This method fills an Enrollment Linked List with
     * the dataset given.
     * @param file
     * @return a Linked List with the data ready
     * to be manipulated
     */

    public static LinkedList<Student> dataFillStudent(String file){

        LinkedList<Student> students = new LinkedList<Student>();
        LinkedList<CourseGroup> auxiliar = new LinkedList<CourseGroup>();
        LinkedList sec_list = new LinkedList();

        String flag = "1";
        BufferedReader br = null;
        int i = 0;

        try {

            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null!=line) {

                String [] fields = line.split(",");
                fields = removeTrailingQuotes(fields);

                if(fields[0].equals(flag)){
                    auxiliar.addLast(new CourseGroup(fields[1],fields[2]));
                    sec_list = (LinkedList) auxiliar.clone() ;
                    line = br.readLine();
                }else{
                    students.addLast(new Student(flag, sec_list));
                    flag = fields[0];

                    auxiliar.clear();

                }

            }


        } catch (Exception e) {
            System.out.println("ERROR "+e);
        }
        return students;
    }



    public static void pruebaEstudianteHorario(){
      // 19 - 38

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
                  unnecesary.addLast(fields[0]);
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
            br = new BufferedReader(new FileReader(file));
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

}


class studendPGroups{
    String ID;
    LinkedList<Group> groups = new LinkedList<Group>();

    public studendPGroups(String ID, LinkedList<Group> groups) {
        this.ID = ID;
        this.groups = groups;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public LinkedList<Group> getGroups() {
        return groups;
    }

    public void setGroups(LinkedList<Group> groups) {
        this.groups = groups;
    }

}

class Student{

    String ID;
    LinkedList<CourseGroup> classes = new LinkedList<CourseGroup>();

    public Student(String ID, LinkedList<CourseGroup> classesList) {
        this.ID = ID;
        classes = classesList;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public LinkedList<CourseGroup> getClasses() {
        return classes;
    }

    public void setClasses(LinkedList<CourseGroup> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID='" + ID + '\'' +
                ", classes=" + classes +
                '}';
    }
}

class CourseGroup{
    String course;
    String group;

    public CourseGroup(String course, String group) {
        this.course = course;
        this.group = group;
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

    @Override
    public String toString() {
        return "CourseGroup{" +
                "course='" + course + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}

class specialGroupClass{
    String course;
    String classroom;

    public specialGroupClass(String course, String classroom) {
        this.course = course;
        this.classroom = classroom;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
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
        b = classroom.substring(0,2);
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

class Class {
    CourseGroup courseGroup;
    LinkedList<Student> students;
    LinkedList<StudenMI> reducedMobilityStudents;

    public boolean hasDisabledStudents() {
        return !reducedMobilityStudents.isEmpty();
    }

    public Class(CourseGroup courseGroup, LinkedList<Student> students, LinkedList<StudenMI> reducedMobilityStudents) {
        this.courseGroup = courseGroup;
        this.students = students;
        this.reducedMobilityStudents = reducedMobilityStudents;
    }

    public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    public void setCourseGroup(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
    }

    public LinkedList<Student> getStudents() {
        return students;
    }

    public void setStudents(LinkedList<Student> students) {
        this.students = students;
    }

    public LinkedList<StudenMI> getReducedMobilityStudents() {
        return reducedMobilityStudents;
    }

    public void setReducedMobilityStudents(LinkedList<StudenMI> reducedMobilityStudents) {
        this.reducedMobilityStudents = reducedMobilityStudents;
    }
}

class Classroom {
    String classroomNum;
    String type;
    int capacity;
    int access;

    public Classroom(String classroomNum, String type, int capacity, int access) {
        this.classroomNum = classroomNum;
        this.type = type;
        this.capacity = capacity;
        this.access = access;
    }

    public String getClassroomNum() {
        return classroomNum;
    }

    /**
     * This method returns the block in whick a classroom is
     * @return block
     */
    public String getClassroomBlock() {
        String b = classroomNum;
        b = classroomNum.substring(0,2);
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
        String b = classroomNum;
        b = b.substring(2);
        return b;
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
    public String name;
    public LinkedList<Edge> edgeList;


    public Vertex(String name){
        this.name = name;
        edgeList = new LinkedList<>();
    }

    public LinkedList<Edge> getEdgeList() {
        return edgeList;
    }


    /**
     * This method finds the nearest block
     * that also has access for people with
     * mobility issues
     * @return a Vertex in the graph which
     * represents the block
     */
    public Vertex nearestAccessibleBlock(LinkedList<Classroom> classrooms) {
        LinkedList<Edge> edges = this.edgeList;
        Edge minValue = edges.peekFirst();
        for (int i = 1; i < edges.size(); i++) {
            if (edges.get(i).weight < minValue.weight && edges.get(i).destVertex.isAccessible(classrooms)) {
                minValue = edges.get(i);

            }
        }
        return minValue.destVertex;
    }

    /**
     * This method determines if a certain
     * block has access for people with
     * mobility problems
     * @param classrooms the classroom list
     * @return true if it has, false otherwise.
     */
    private boolean isAccessible(LinkedList<Classroom> classrooms) {
        for (Classroom c: classrooms) {
            if (this.name.equals(c.getClassroomBlock()) && c.access == 0) {
                return true;
            }
        }
        return false;
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

    public HashSet<Vertex> getNodes() { return nodes; }

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
