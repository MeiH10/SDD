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


}
