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

    @Test
    void getSchoolsFull_ShouldResolveSemesterId_FromSemesterName() {
        String semesterName = "Spring 2024";
        String semesterID = "semesterId123";

        when(semesterService.existsByName(semesterName)).thenReturn(true);
        when(semesterService.getByName(semesterName)).thenReturn(new Semester(semesterID, semesterName));

        when(schoolService.getSchool(null, semesterID, "name", "asc")).thenReturn(Collections.emptyList());

        ResponseEntity<APIResponse<Object>> response = schoolController.getSchoolsFull(null, semesterName, null, "name", "asc", "id");

        verify(semesterService).getByName(semesterName);
        verify(schoolService).getSchool(null, semesterID, "name", "asc");

        assertNotNull(response);
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void getSchoolsFull_ShouldReturnIds_WhenTypeIsDefault() {
        String semesterID = "semesterId123";
        List<School> mockSchools = List.of(
                new School("1", "School 1"),
                new School("2", "School 2")
        );

        when(semesterService.existsById(semesterID)).thenReturn(true);
        when(schoolService.getSchool(null, semesterID, "name", "asc")).thenReturn(mockSchools);

        ResponseEntity<APIResponse<Object>> response = schoolController.getSchoolsFull(null, null, semesterID, "name", "asc", "id");

        assertNotNull(response);
        assertTrue(response.getBody().isSuccess());
        assertEquals(List.of("1", "2"), response.getBody().getData());
    }

    @Test
    void getSchoolsFull_ShouldReturnCount_WhenTypeIsCount() {
        String semesterID = "semesterId123";
        List<School> mockSchools = List.of(
                new School("1", "School 1"),
                new School("2", "School 2")
        );

        when(semesterService.existsById(semesterID)).thenReturn(true);
        when(schoolService.getSchool(null, semesterID, "name", "asc")).thenReturn(mockSchools);

        ResponseEntity<APIResponse<Object>> response = schoolController.getSchoolsFull(null, null, semesterID, "name", "asc", "count");

        assertNotNull(response);
        assertTrue(response.getBody().isSuccess());
        assertEquals(2, response.getBody().getData());
    }

    @Test
    void getSpecificSchool_ShouldReturnSchool_WhenValidId() {
        String schoolId = "schoolId123";
        School mockSchool = new School(schoolId, "Test School");

        when(schoolService.getById(schoolId)).thenReturn(mockSchool);

        ResponseEntity<APIResponse<Object>> response = schoolController.getSpecificSchool(schoolId);

        assertNotNull(response);
        assertTrue(response.getBody().isSuccess());
        assertEquals(mockSchool, response.getBody().getData());
    }

    @Test
    void getSpecificSchool_ShouldThrowException_WhenSchoolNotFound() {
        String schoolId = "nonexistentSchoolId";

        when(schoolService.getById(schoolId)).thenThrow(new IllegalArgumentException("School not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schoolController.getSpecificSchool(schoolId));

        assertEquals("School not found", exception.getMessage());
    }
}
