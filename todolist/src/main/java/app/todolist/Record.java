package app.todolist;

public class Record {
    private String todo;
    private String note;
    private String deadline;
    private Integer id;
    private static Integer idNext = 0;

    public Record (String todo, String note, String deadline) {
        this.todo = todo;
        this.note = note;
        this.deadline = deadline;
        this.id = idNext;
        idNext += 1;
    }

    public String getTodo() {
        return todo;
    }
    public String getNote() {
        return note;
    }
    public String getDeadline() {
        return deadline;
    }
    public Integer getId() {
        return id;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
