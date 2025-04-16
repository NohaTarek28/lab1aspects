package com.example.demo.Courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepositery extends JpaRepository<Course, Long> {
    Course findByCode(String code);
    List<Course> findByCredits(Integer credits);
    List<Course> findByNameContaining(String name);
}