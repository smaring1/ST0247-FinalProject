class FinalProject {
    //TODO: Implementar grafo para las distancias entre bloques
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