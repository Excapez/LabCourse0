package com.example.labcourse0;

public class Todo {
    String description;
    String key;
    String priority;

    public Todo(String description, String key) {
        this.description = description;
        this.key = key;
    }

    public Todo()
    {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


}
