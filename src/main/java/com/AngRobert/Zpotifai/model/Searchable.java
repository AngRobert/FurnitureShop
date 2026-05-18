package com.AngRobert.Zpotifai.model;

// interfata ajuta, deoarece la cautare pot sa am un obiect de tip List<Searchable>
public interface Searchable extends Comparable<Searchable> {
    // seminifica faptul ca aceste clase pot fi cautate dupa nume
    public String getName();

    // pentru listarea detaliilor
    public int getId();

    @Override
    default int compareTo(Searchable other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }
}
