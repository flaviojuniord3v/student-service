package dev.flaviojunior.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.flaviojunior.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentSettings.class);
        StudentSettings studentSettings1 = new StudentSettings();
        studentSettings1.setId(1L);
        StudentSettings studentSettings2 = new StudentSettings();
        studentSettings2.setId(studentSettings1.getId());
        assertThat(studentSettings1).isEqualTo(studentSettings2);
        studentSettings2.setId(2L);
        assertThat(studentSettings1).isNotEqualTo(studentSettings2);
        studentSettings1.setId(null);
        assertThat(studentSettings1).isNotEqualTo(studentSettings2);
    }
}
