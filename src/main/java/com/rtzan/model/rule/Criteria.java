/** Free */
package com.rtzan.model.rule;

import java.util.List;
import java.util.Objects;


public class Criteria {

    private int rank;

    private String name;

    private List<String> productNames;

    public Criteria(int rank, String name, List<String> productNames) {
        this.rank = rank;
        this.name = name;
        this.productNames = productNames;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public void setProductNames(List<String> productNames) {
        this.productNames = productNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        Criteria criteria = (Criteria) o;
        return (rank == criteria.rank) && Objects.equals(name, criteria.name) && Objects.equals(productNames, criteria.productNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, name, productNames);
    }

    @Override
    public String toString() {
        return "Criteria{" +
            "rank=" + rank +
            ", name='" + name + '\'' +
            ", productNames=" + productNames + '}';
    }
}
