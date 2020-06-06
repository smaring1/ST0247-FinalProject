import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
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
    static LinkedList<Schedule> schedule = new LinkedList<Schedule>();

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis(); // TIME

        Graph map = dataFillMap("DistanciasBloques.csv");
        groups = dataFillGroup("grupos_semestre.csv");
        classrooms = dataFillClassroom("aulas.csv");             // LLAMADO A TODOS LOS FILL
        MI = dataFIllStudentMI("estudiantes_discapacitados.csv");
        studentCourseGroup = dataFillStudent("estudiante_curso_grupo.csv");
        groupType = classType();

        //-------------------------------------------------------------------------------------------------//


        long endTime = System.currentTimeMillis();
        long totalExecTime = endTime - startTime; // TIEMPO
        System.out.println("Total execution time: " + totalExecTime + " miliseconds");
        System.out.println("Total memory usage: ");
        System.out.println("KB: " + (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
        System.out.println("MB: " + (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024)/1024);

        //-------------------------------------------------------------------------------------------------//

        // SPECIAL CALLS
        for (Vertex x: map.getNodes()) {
            aux = x.getEdges();
            break;
        }

        specialClassroomsFill();
        studentGroupsSpecials();
        scheduleFill();
        nearestChange(map);

    }


    /**
     * This method finds the nearest block
     * given the current block information
     * @param map
     */
    public static void nearestChange(Graph map){
        String block = "";
        int aux3 = 0;
        int cont = 0;

        for(int i = 0; i<schedule.size();i++){
            for (int j = 0; j < 7; j++) {
                if(schedule.get(i).getPrueba()[j]!=null){
                    for (int k = 0; k < schedule.get(i).getPrueba()[j].size(); k++) {
                        block = schedule.get(i).getPrueba()[j].get(k).getBlock();
                        for (Vertex v : map.getNodes()) {
                            if (v.getName().equals(block)) {
                                if (isMI(schedule.get(i).getID())) {
                                    schedule.get(i).getPrueba()[j].get(k).setBlock(v.nearestAccessibleBlock(classrooms).getName());
                                    aux3+=getDistance(block,v.nearestAccessibleBlock(classrooms).getName(),map);
                                    cont++;
                                } else {
                                    schedule.get(i).getPrueba()[j].get(k).setBlock(v.nearestBlock(classrooms).getName());
                                    aux3+=getDistance(block,v.nearestAccessibleBlock(classrooms).getName(),map);
                                    cont++;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("PROMEDIO DE DISTANCIA POR ESTUDIANTE: "+aux3/cont);
    }

    /**
     * This method returns if a student have a mobility
     * impairment not.
     * @param ID
     * @return true if a student have a mobility impairment
     * false if a student don't have a mobility impairment
     */
    public static boolean isMI(String ID){
        for (StudenMI studenMI : MI) {
            if (ID.equals(studenMI.getStudent_ID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method fills the schedule of
     * a student depending on the day when the class
     * was scheduled.
     *
     * The information is stored in a 7-position array
     * that represents each day of the week.
     */
    public static void scheduleFill(){
        LinkedList<Schedule> cgaux = new LinkedList<>();
        LinkedList sec_list = new LinkedList();
        LinkedList<hours> [] schedulea = new LinkedList[7];
        LinkedList<hours> [] scaux = new LinkedList[7];

        for(int i = 0; i<200;i++){
            for(int j = 0; j < studentCourseGroup.get(i).getClasses().size();j++){
                for(Group g: groups){
                    if(studentCourseGroup.get(i).getClasses().get(j).getCourse().equals(g.getCourse())){
                      //  cgaux.addLast(new CourseGroup(g.getCourse(),g.getGroup()));
                        switch (g.getDay()){
                            case "lunes":
                                schedulea[0] = new LinkedList<hours>();
                                schedulea[0].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            case "martes":
                                schedulea[1] = new LinkedList<hours>();
                                schedulea[1].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            case "miércoles":
                                schedulea[2] = new LinkedList<hours>();
                                schedulea[2].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            case "jueves":
                                schedulea[3] = new LinkedList<hours>();
                                schedulea[3].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            case "viernes":
                                schedulea[4] = new LinkedList<hours>();
                                schedulea[4].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            case "sábado":
                                schedulea[5] = new LinkedList<hours>();
                                schedulea[5].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            case "domingo":
                                schedulea[6] = new LinkedList<hours>();
                                schedulea[6].addLast(new hours(new CourseGroup(g.getCourse(),g.getGroup()),g.getStartTime(),g.getEndTime(),g.getClassroomBlock()));
                                break;
                            default:
                                break;

                        }
                    }
                }
            }
            sec_list = (LinkedList) cgaux.clone();
            scaux = (LinkedList<hours> []) schedulea.clone();
            schedule.addLast(new Schedule(studentCourseGroup.get(i).getID(),scaux));
            cgaux.clear();
            for(int k = 0; k<schedulea.length;k++){
                schedulea[k] = null;
            }
        }
    }

    /**
     * This method discards all the special groups from
     * groups list. In order to prioritize them in their
     * specific classrooms
     */
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

    /**
     * This method defines if a classroom is special or not.
     * A special classroom means if the classroom is a laboratory or if the
     * classroom is a language center booth.
     */
    public static void specialClassroomsFill(){
        String flag;
        Pattern pat = Pattern.compile("^Laboratorio.*|^Cabina.*");
        for(Group g: groups){
            for(groupTypeClass t: groupType){
                if(g.getClassroom().equals(t.getClassroom())){
                    Matcher mat = pat.matcher(t.getType());
                    if(mat.matches()){
                        specialClassrooms.addLast(new specialGroupClass(g.getCourse(),g.getClassroomNumber()));
                        //System.out.println(specialClassrooms.size());
                    }
                }
            }
        }
    }

    /**
     * This method found the distance between two blocks.
     * @param a represents the first block
     * @param b represents the second block
     * @param map represents the graph
     * @return the distance between "a" and "b"
     */
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
     * @param file the dataset file
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




    /**
     * This method fills a Group Linked List with
     * the dataset given.
     * @param file the dataset file
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
     * @param file the dataset file
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
     * @param file the dataset file
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

    //TODO: Document this method
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
}


/**
 * This class represents each students
 * with their list of courses that the
 * students registered.
 */
class Student{

    String ID;
    LinkedList<CourseGroup> classes = new LinkedList<CourseGroup>();

    /**
     * Constructor for Student data type
     * @param ID the unique ID that each student has
     * @param classesList The classes in which a student is enrolled
     */
    public Student(String ID, LinkedList<CourseGroup> classesList) {
        this.ID = ID;
        classes = classesList;
    }

    /**
     * Getter for Student ID parameter
     * @return Student ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Setter for Student ID parameter
     * @param ID Student ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Getter for Student classes parameter
     * @return classes
     */
    public LinkedList<CourseGroup> getClasses() {
        return classes;
    }

    /**
     * Setter for Student classes parameter
     * @param classes for a Student
     */
    public void setClasses(LinkedList<CourseGroup> classes) {
        this.classes = classes;
    }

    /**
     * This method helps showing the data contained
     * in a Student instance
     * @return String with the data
     */
    @Override
    public String toString() {
        return "Student{" +
                "ID='" + ID + '\'' +
                ", classes=" + classes +
                '}';
    }
}

/**
 * This class represents each courses
 * with all the groups.
 */
class CourseGroup{
    String course;
    String group;

    /**
     * Constructor for CourseGroup data type
     * @param course the course ID
     * @param group the group number
     */
    public CourseGroup(String course, String group) {
        this.course = course;
        this.group = group;
    }

    /**
     * Getter for CourseGroup course parameter
     * @return course ID
     */
    public String getCourse() {
        return course;
    }

    /**
     * Setter for CourseGroup course parameter
     * @param course course ID
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Getter for CourseGroup group parameter
     * @return group number
     */
    public String getGroup() {
        return group;
    }

    /**
     * Setter for CourseGroup group parameter
     * @param group group number
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * This method helps showing the data contained
     * in a CourseGroup instance
     * @return String with the data
     */
    @Override
    public String toString() {
        return "CourseGroup{" +
                "course='" + course + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}

/**
 * This class defines a special course
 * for example, laboratories or language booths.
 */
class specialGroupClass{
    String course;
    String classroom;

    /**
     * Constructor for specialGroupClass
     * data type
     * @param course the course ID
     * @param classroom the number of the classroom
     */
    public specialGroupClass(String course, String classroom) {
        this.course = course;
        this.classroom = classroom;
    }

    /**
     * Getter for specialGroupClass course parameter
     * @return course ID
     */
    public String getCourse() {
        return course;
    }

    /**
     * Setter for specialGroupClass course parameter
     * @param course course ID
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Getter for specialGroupClass classroom parameter
     * @return classroom number
     */
    public String getClassroom() {
        return classroom;
    }

    /**
     * Setter for specialGroupClass classroom parameter
     * @param classroom classroom number
     */
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

    /**
     * Constructor for Group data type
     * @param course course ID
     * @param group group number
     * @param professor professor ID
     * @param day week day for the session
     * @param startTime time at which the session begins
     * @param endTime time at which the session ends
     * @param classroom classroom number at which the class happens
     */
    public Group(String course, String group, String professor, String day, String startTime, String endTime, String classroom) {
        this.course = course;
        this.group = group;
        this.professor = professor;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
    }

    /**
     * Getter for Group course parameter
     * @return course ID
     */
    public String getCourse() {
        return course;
    }

    /**
     * Setter for Group course parameter
     * @param course course ID
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Getter for Group group number parameter
     * @return group number
     */
    public String getGroup() {
        return group;
    }

    /**
     * Setter for Group group number parameter
     * @param group group number
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Getter for Group professor parameter
     * @return professor ID
     */
    public String getProfessor() {
        return professor;
    }

    /**
     * Setter for Group professor parameter
     * @param professor professor ID
     */
    public void setProfessor(String professor) {
        this.professor = professor;
    }

    /**
     * Getter for the Group day of class
     * @return day of class
     */
    public String getDay() {
        return day;
    }

    /**
     * Setter for the Group day of class
     * @param day day of class
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Getter for the Group class start time
     * @return start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter for the Group class start time
     * @param startTime start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for the Group class end time
     * @return end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Setter for the Group class end time
     * @param endTime end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Getter for the Group classroom number
     * @return classroom number
     */
    public String getClassroom() {
        return classroom;
    }

    /**
     * This method returns the block in which a classroom is
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

/**
 * hours class represents the schedule
 * of a specific class.
 *
 * Given by an object type "courseGroup", start time, end time
 * and the block.
 *
 * This information was taken from the dataset
 */

class hours{

    CourseGroup courseGroup;
    String in;
    String end;
    String block;

    /**
     * Constructor for hours data type
     * @param courseGroup the course and group for the class
     * @param in the time at which the class starts
     * @param end the time at which the class ends
     * @param block the block in which a class happens
     */
    public hours(CourseGroup courseGroup, String in, String end, String block) {
        this.courseGroup = courseGroup;
        this.in = in;
        this.end = end;
        this.block = block;
    }

    /**
     * Getter for CourseGroup object parameter
     * @return course and group for a class
     */
    public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    /**
     * Setter for CourseGroup parameter
     * @param courseGroup CourseGroup object
     */
    public void setCourseGroup(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
    }

    /**
     * Getter for the class start time parameter
     * @return the start time for the class
     */
    public String getIn() {
        return in;
    }

    /**
     * Setter for the class start time parameter
     * @param in the start time for the class
     */
    public void setIn(String in) {
        this.in = in;
    }

    /**
     * Getter for the class end time parameter
     * @return the end time for the class
     */
    public String getEnd() {
        return end;
    }

    /**
     * Setter for the class end time parameter
     * @param end the end time for the class
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Getter for the block in which
     * the class happens
     * @return block for the class
     */
    public String getBlock() {
        return block;
    }

    /**
     * Setter for the block in which
     * the class happens
     * @param block block for the class
     */
    public void setBlock(String block) {
        this.block = block;
    }

    /**
     * This method helps showing the data contained
     * in an hours instance
     * @return String with the data
     */
    @Override
    public String toString() {
        return "hours{" +
                "courseGroup=" + courseGroup +
                ", in='" + in + '\'' +
                ", end='" + end + '\'' +
                ", block='" + block + '\'' +
                '}';
    }

}

/**
 * This class represents a schedule of students
 * depending on the assigned ID and show a list
 * with their classes represented by class "hours".
 *
 * This information was taken from the dataset
 */

class Schedule{
    String ID;
    LinkedList<hours> [] hours = new LinkedList[7];

    /**
     * Constructor for Schedule data type
     * @param ID the student ID
     * @param prueba the busy hours for a student in each day
     */
    public Schedule(String ID, LinkedList<hours>[] prueba) {
        this.ID = ID;
        this.hours = prueba;
    }

    /**
     * Getter for the ID parameter
     * @return the student ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Setter for the ID parameter
     * @param ID the student ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Getter for the busy hours during the week parameter
     * @return busy hours for a student during the week
     */
    public LinkedList<hours>[] getPrueba() {
        return hours;
    }

    /**
     * Setter for the busy hours during the week parameter
     * @param prueba busy hours for a student during the week
     */
    public void setPrueba(LinkedList<hours>[] prueba) {
        this.hours = prueba;
    }

    /**
     * This method helps showing the data contained
     * in a Schedule instance
     * @return String with the data
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "ID='" + ID + '\'' +
                ", prueba=" + Arrays.toString(hours) +
                '}';
    }
}


/**
 * This class represents all the classrooms
 * in the dataset "aulas.csv".
 */

class Classroom {
    String classroomNum;
    String type;
    int capacity;
    int access;

    /**
     * Constructor for the Classroom data type
     * @param classroomNum the number for a classroom
     * @param type the type of classroom
     * @param capacity the capacity of a classroom
     * @param access means if a classroom is accessible for wheelchairs
     */
    public Classroom(String classroomNum, String type, int capacity, int access) {
        this.classroomNum = classroomNum;
        this.type = type;
        this.capacity = capacity;
        this.access = access;
    }

    /**
     * Getter for the classroom number parameter
     * @return the classroom number
     */
    public String getClassroomNum() {
        return classroomNum;
    }

    /**
     * This method returns the block in which a classroom is
     * @return block in which a classroom is
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

    /**
     * Setter for the classroom number parameter
     * @param classroomNum the classroom number
     */
    public void setClassroomNum(String classroomNum) {
        this.classroomNum = classroomNum;
    }

    /**
     * Getter for the classroom type parameter
     * @return the classroom type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the classroom type parameter
     * @param type the classroom type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for the classroom capacity
     * @return the classroom capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Setter for the classroom capacity
     * @param capacity the classroom capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Getter for the wheelchair accessibility
     * of a classroom
     * @return 1 if accessible, otherwise 0
     */
    public int getAccess() {
        return access;
    }

    /**
     * Setter for the wheelchair accessibility
     * of a classroom
     * @param access 1 if accessible, otherwise 0
     */
    public void setAccess(int access) {
        this.access = access;
    }

    /**
     * This method helps showing the data contained
     * in a Classroom instance
     * @return String with the data
     */
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

/**
 * studentMI represents students who
 * have mobility impairments and is represented by a Boolean variable.
 *
 * This information was taken from the dataset
 */
class StudenMI{
    String Student_ID;
    boolean MobilityImpairment;

    /**
     * Constructor for StudentMi data type
     * @param student_ID the unique Student ID
     * @param mobilityImpairment true if the student has mobility issues, otherwise false
     */
    public StudenMI(String student_ID, boolean mobilityImpairment) {
        Student_ID = student_ID;
        MobilityImpairment = mobilityImpairment;
    }

    /**
     * Getter for the Student ID parameter
     * @return the Student ID
     */
    public String getStudent_ID() {
        return Student_ID;
    }

    /**
     * Setter for the Student ID parameter
     * @param student_ID the Student ID
     */
    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    /**
     * Getter for the mobility status for a student
     * @return true if the student has mobility issues, otherwise false
     */
    public boolean isMobilityImpairment() {
        return MobilityImpairment;
    }

    /**
     * Setter for the mobility status for a student
     * @param mobilityImpairment true if the student has mobility issues, otherwise false
     */
    public void setMobilityImpairment(boolean mobilityImpairment) {
        MobilityImpairment = mobilityImpairment;
    }

    /**
     * This method helps showing the data contained
     * in a StudenMI instance
     * @return String with the data
     */
    @Override
    public String toString() {
        return "StudenMI{" +
                "Student_ID='" + Student_ID + '\'' +
                ", MobilityImpairment=" + MobilityImpairment +
                '}';
    }
}

/**
 * groupTypeClass represents all the classrooms
 * with their respective description.
 *
 * This information was taken from the dataset
 */
class groupTypeClass{
    String classroom;
    String type;

    /**
     * Constructor for groupTypeClass data type
     * @param classroom the classroom ID
     * @param type the classroom type
     */
    public groupTypeClass(String classroom, String type) {
        this.classroom = classroom;
        this.type = type;
    }

    /**
     * Getter for the classroom ID
     * @return the classroom ID
     */
    public String getClassroom() {
        return classroom;
    }

    /**
     * Setter for the classroom ID
     * @param classroom the classroom ID
     */
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    /**
     * Getter for the classroom type
     * @return the classroom type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the classroom type
     * @param type the classroom type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method helps showing the data contained
     * in a groupTypeClass instance
     * @return String wuth the data
     */
    @Override
    public String toString() {
        return "groupTypeClass{" +
                "classroom='" + classroom + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

/**
 * Vertex implementation found on the following link:
 * https://medium.com/@mithratalluri/basic-graph-implementation-in-java-9ed12e328c57
 */

class Vertex{
    public String name;
    public LinkedList<Edge> edgeList;

    /**
     * Constructor for a Vertex
     * in the graph
     * @param name the name for the Vertex
     */
    public Vertex(String name){
        this.name = name;
        edgeList = new LinkedList<>();
    }

    /**
     * Getter for the Edges that connect
     * this Vertex with some other Vertexes
     * @return list of edges
     */
    public LinkedList<Edge> getEdgeList() {
        return edgeList;
    }


    /**
     * This method finds the nearest block
     * that also has access for people with
     * mobility issues
     * @param classrooms the list of classrooms in the university
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
     * This method finds the nearest block
     * given a block represented in a Vertex
     * @param classrooms the list of classrooms in the university
     * @return
     */
    public Vertex nearestBlock(LinkedList<Classroom> classrooms) {
        LinkedList<Edge> edges = this.edgeList;
        Edge minValue = edges.peekFirst();
        for (int i = 1; i < edges.size(); i++) {
            if (edges.get(i).weight < minValue.weight) {
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

    /**
     * Getter for a Vertex name
     * @return the Vertex name
     */
    public String getName(){
        return name;
    }

    /**
     * Getter for the edges in a
     * Vertex.
     * @return the edge list
     */
    public LinkedList<Edge> getEdges(){
        return edgeList;
    }
}

/**
 * Edge implementation found on the following link:
 * https://medium.com/@mithratalluri/basic-graph-implementation-in-java-9ed12e328c57
 */

class Edge{
    public int weight;
    public Vertex destVertex;

    /**
     * Constructor for an Edge
     * between a couple of Vertexes
     * in a weighted graph
     * @param dest the destination Vertex
     * @param w the weight of the connection
     */
    public Edge(Vertex dest, int w){
        this.destVertex = dest;
        this.weight = w;
    }

    /* can use this approach for an unweighted graph
        or better remove variable weight altogether from Edge class */
    /**
     * Constructor for an Edge
     * between a couple of Vertexes
     * in an unweighted graph
     * @param dest the destination Vertex
     */
    public Edge(Vertex dest){
        this.destVertex = dest;
        this.weight = 1;
    }

    /**
     * Getter fot the weight
     * of a given Edge
     * @return the weight of the Edge
     */
    public int getWeight(){
        return weight;
    }

    /**
     * Getter for the destination
     * Vertex of a given Edge
     * @return the destination vertex of an Edge
     */
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

    /**
     * Constructor for the Graph data type
     */
    public Graph(){
        nodes = new HashSet<>();
    }

    /**
     * Getter for the nodes list
     * in a Graph
     * @return the nodes list
     */
    public HashSet<Vertex> getNodes() { return nodes; }

    /**
     * This method adds an edge between
     * two given Vertexes in the Graph
     * @param v1 first Vertex
     * @param v2 second Vertex
     * @param weight the weight of the connection
     * @return true if the addition could be done, otherwise false
     */
    public boolean AddEdge(Vertex v1, Vertex v2, int weight){
        //since it's a directed graph
        v2.getEdges().add(new Edge(v1, weight));
        return v1.getEdges().add(new Edge(v2, weight));
        /* If you want to implement an undirected graph,
            add v2.getEdges().add(new Edge(v1, weight)) also */
    }

    /**
     * This method adds a Vertex
     * to the Graph
     * @param v the Vertex object to ve added
     * @return true if the addition could be done, otherwise false
     */
    public boolean AddVertex(Vertex v){
        return nodes.add(v);
    }

    /**
     * This method prints the Graph
     * as the nodes and their edges
     */
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
