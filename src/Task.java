public class Task {
    private int id;
    private String name;
    private boolean completed;
    public Task(int id, String name, boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public int getName() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "* " + name + (completed ? " [Completa]" : " [Não Completa]");
    }
}