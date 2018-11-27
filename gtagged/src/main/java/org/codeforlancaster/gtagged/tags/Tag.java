package org.codeforlancaster.gtagged.tags;

<<<<<<< HEAD
import javax.persistence.Embeddable;

import org.springframework.data.annotation.PersistenceConstructor;

import lombok.Data;

@Data
@Embeddable
public final class Tag implements Comparable<Tag> {

    private final String value;

    @PersistenceConstructor
    public Tag(final String value) {

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Invalid tag string");
        }
        this.value = value.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

    }

    public String toString() {
        return this.value;
    }

    public String toHashTagString() {
        return "#" + this.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tag other = (Tag) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public int compareTo(Tag o) {

        return this.value.compareTo(o.value);
    }
=======
public class Tag {

>>>>>>> f40064dd94444530f535c516abc3632960102f86
}
