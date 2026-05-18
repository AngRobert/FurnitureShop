package com.AngRobert.Zpotifai.model;

// interfata ajuta, deoarece la cautare pot sa am un obiect de tip List<Searchable>
public interface Searchable extends Comparable<Searchable> {
    // seminifica faptul ca aceste clase pot fi cautate dupa nume
    public String getName();

    // pentru listarea detaliilor
    public int getId();

    // To show who created the item (Artist/Host) in search results
    default String getCreatorDisplayName() {
        return "";
    }

    @Override
    default int compareTo(Searchable other) {
        int nameCompare = this.getName().compareToIgnoreCase(other.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }
        // if names are the same, use ID to distinguish them
        return Integer.compare(this.getId(), other.getId());
    }
}
