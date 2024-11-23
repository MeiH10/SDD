package Pucknotes.Server.School;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Semester.SemesterService;

import java.util.Collections;
import java.util.List;

class SchoolControllerTest {

    @Mock
    private SchoolService schoolService;

    @Mock
    private SemesterService semesterService;

    @InjectMocks
    private SchoolController schoolController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSchoolsFull_ShouldThrowException_WhenSemesterIdDoesNotExist() {
        String semesterID = "nonexistentSemester";

        when(semesterService.existsById(semesterID)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schoolController.getSchoolsFull(null, null, semesterID, "name", "asc", "id"));

        assertEquals("A semester with 'semesterID' does not exist.", exception.getMessage());
    }

    @Test
    void getSchoolsFull_ShouldThrowException_WhenSemesterNameDoesNotExist() {
        String semesterName = "nonexistentSemester";

        when(semesterService.existsByName(semesterName)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schoolController.getSchoolsFull(null, semesterName, null, "name", "asc", "id"));

        assertEquals("A semester with 'semesterName' does not exist.", exception.getMessage());
    }
}
