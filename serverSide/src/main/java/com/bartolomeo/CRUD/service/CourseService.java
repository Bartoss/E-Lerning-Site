package com.bartolomeo.CRUD.service;

import com.bartolomeo.CRUD.model.Course;

import java.util.List;

public interface CourseService {
    Course addCourse(Course course);

    Course updateCourse(Course course);

    void deleteCourse(Long courseId);

    List<Course> findAllCourses();
}
