package dev.flaviojunior.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.flaviojunior.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentRelationshipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentRelationship.class);
        StudentRelationship studentRelationship1 = new StudentRelationship();
        studentRelationship1.setId(1L);
        StudentRelationship studentRelationship2 = new StudentRelationship();
        studentRelationship2.setId(studentRelationship1.getId());
        assertThat(studentRelationship1).isEqualTo(studentRelationship2);
        studentRelationship2.setId(2L);
        assertThat(studentRelationship1).isNotEqualTo(studentRelationship2);
        studentRelationship1.setId(null);
        assertThat(studentRelationship1).isNotEqualTo(studentRelationship2);
    }
}
