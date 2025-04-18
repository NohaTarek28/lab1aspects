package com.example.demo.Student;
import com.example.demo.Courses.Course;
import com.example.demo.Courses.CourseRepositery;
import com.example.demo.Student.dto.CreateStudentDTO;
import com.example.demo.Student.dto.UpdateStudentDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private StudentRepositery studentRepository;

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }



    @Autowired
    private CourseRepositery courseRepository;

    public Student addCourseToStudent(String studentNumber, String courseCode) {
        Student student = studentRepository.findByStudentNumber(studentNumber);
        Course course = courseRepository.findByCode(courseCode);

        if (student != null && course != null) {
            student.getCourses().add(course);
            return studentRepository.save(student);
        } else {
            throw new RuntimeException("Student or Course not found");
        }
    }



    // Create a new student
    public Student createStudent(CreateStudentDTO studentDTO) {
        Student student = new Student(
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getStudentNumber(),
                studentDTO.getMajor(),
                studentDTO.getGpa()
        );
        return studentRepository.save(student);
    }

    // Update an existing student
    public Student updateStudent(Long id, UpdateStudentDTO studentDTO) {
        // Find the existing student or throw an exception if not found
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Copy only non-null properties from DTO to entity
        BeanUtils.copyProperties(studentDTO, existingStudent, getNullPropertyNames(studentDTO));

        // Save and return the updated student
        return studentRepository.save(existingStudent);
    }

    // Delete a student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        studentRepository.delete(student);
    }

    // Find students by major
    public List<Student> findStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    // Find students with GPA greater than specified value
    public List<Student> findStudentsWithGpaGreaterThan(Double gpa) {
        return studentRepository.findByGpaGreaterThan(gpa);
    }

    // Find student by student number
    public Student findStudentByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    public Student removeCourseFromStudent(String studentNumber, String courseCode) {
        Student student = studentRepository.findByStudentNumber(studentNumber);
        Course course = courseRepository.findByCode(courseCode);

        if (student != null && course != null) {
            student.getCourses().remove(course);
            return studentRepository.save(student);
        }

        throw new RuntimeException("Student or Course not found");
    }


    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
