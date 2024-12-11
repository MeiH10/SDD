# QuACS semester_data/202409/courses.json data format

- **code**: `string` (e.g., `"CSCI"`)  
  The department code.

  - **courses**: `array`  
  A list of courses offered by the department.

    - **crse**: `number` (e.g., `1200`)  
      The course number.

    - **id**: `string` (e.g., `"CSCI-1200"`)  
      A unique identifier for the course.

    - **sections**: `array`  
      A list of sections for the course.

      - **act**: `number` (e.g., `23`)  
        The current number of students enrolled in the section.

      - **attribute**: `string`  (e.g, `Introductory Level Course`)
        A description of the course's level and type.

      - **cap**: `number` (e.g., `27`)  
        The maximum number of students allowed in the section.

      - **credMax**: `number` (e.g., `4.0`)  
        The maximum credit hours for the course.

      - **credMin**: `number` (e.g., `4.0`)  
        The minimum credit hours for the course.

      - **crn**: `number` (e.g., `65029`)  
        The Course Reference Number (CRN).

      - **sec**: `string` (e.g., `"01"`)  
        The section number.

      - **subj**: `string` (e.g., `"CSCI"`)  
        The subject code.

      - **timeslots**: `array`  
        A list of timeslots for the section.

        - **dateEnd**: `string` (e.g., `"12/20"`)  
          The date when the timeslot ends.

        - **dateStart**: `string` (e.g., `"08/28"`)  
          The date when the timeslot starts.

        - **days**: `array of strings`  
          The days of the week the class meets (e.g., `["T", "F"]`).

        - **instructor**: `string`  
          The names of the instructors (e.g., `"Barbara Cutler, Meredith Elizabeth Widman"`).

        - **location**: `string`  
          The location where the class is held (e.g., `"Russell Sage Laboratory 3303"`).

        - **timeEnd**: `number` (e.g., `1550`)  
          The time the class ends (in military time).

        - **timeStart**: `number` (e.g., `1400`)  
          The time the class starts (in military time).
