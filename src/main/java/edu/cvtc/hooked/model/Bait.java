package edu.cvtc.hooked.model;

public class Bait {

    private Integer id;
    private String name;
    private String notes;
    private Integer createdByUserId;

    public Bait() {
    }

    // Full constructor with id + createdByUserId
    public Bait(Integer id, String name, String notes, Integer createdByUserId) {
        this.id = id;
        setName(name);
        this.notes = notes;
        this.createdByUserId = createdByUserId;
    }

    // Convenience: id + no creator
    public Bait(Integer id, String name, String notes) {
        this(id, name, notes, null);
    }

    // Convenience: no id (for inserts) + no creator
    public Bait(String name, String notes) {
        this(null, name, notes, null);
    }

    // Convenience: no id (for inserts) + creator
    public Bait(String name, String notes, Integer createdByUserId) {
        this(null, name, notes, createdByUserId);
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = formatName(name);
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = formatName(notes);
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }
    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    private String formatName(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();
    }
}
