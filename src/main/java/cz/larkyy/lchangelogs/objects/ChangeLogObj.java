package cz.larkyy.lchangelogs.objects;

import java.util.List;

public class ChangeLogObj {

    private List<String> changes;
    private String name;
    private boolean published;
    private int id;

    public ChangeLogObj(String name, List<String> changes,int id) {
        this.name = name;
        this.changes = changes;
        this.published = false;
        this.id = id;
    }
    public ChangeLogObj(String name, List<String> changes,int id,Boolean published) {
        this.name = name;
        this.changes = changes;
        this.published = published;
        this.id = id;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<String> getChanges() {
        return changes;
    }

    public void removeChange(int i) {
        this.changes.remove(i);
    }

    public void addChange(String change) {
        this.changes.add(change);
    }
}
