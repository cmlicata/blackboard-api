package com.blackboard.api.dao;

import com.blackboard.api.core.model.Course;
import com.blackboard.api.core.model.School;

import java.util.List;
import java.util.Optional;

/**
 * The internal API for the persistence layer operations. These operations listed only pertain to the
 * Creation, Retrieval and Deletion of Course objects.
 *
 * Created by ChristopherLicata on 11/18/15.
 */
public interface CourseDao
{
    List<Course> findCoursesOffered(School school);

    Optional<Course> findCourseById(int course_id);

    Course createCourse(School school, Course course);

    Optional<Course> deleteCourseById(int courseId);

}
