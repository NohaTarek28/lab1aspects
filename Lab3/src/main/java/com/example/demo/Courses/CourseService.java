package com.example.demo.Courses;

import com.example.demo.Courses.dto.CreateCourseDTO;
import com.example.demo.Courses.dto.UpdateCourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepositery courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(CreateCourseDTO dto) {
        Course course = new Course();
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, UpdateCourseDTO dto) {
        Course course = getCourseById(id);
        if (course != null) {
            course.setName(dto.getName());
            course.setDescription(dto.getDescription());
            course.setCredits(dto.getCredits());
            return courseRepository.save(course);
        }
        return null;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
