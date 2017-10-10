/** Free */
package com.rtzan.model.rule;

import java.util.List;
import java.util.Objects;


public class Rule {

    private String name;
    private List<Criteria> criterias;

    public Rule(String name, List<Criteria> criterias) {
        this.name = name;
        this.criterias = criterias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        Rule rule = (Rule) o;
        return Objects.equals(name, rule.name) && Objects.equals(criterias, rule.criterias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, criterias);
    }

    @Override
    public String toString() {
        return "Rule{" +
            "name='" + name + '\'' +
            ", criterias=" + criterias + '}';
    }
}
