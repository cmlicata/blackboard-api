package com.blackboard.api.dao;

import com.blackboard.api.core.Season;
import com.blackboard.api.core.Subject;
import com.blackboard.api.core.model.*;
import com.blackboard.api.dao.impl.*;
import com.blackboard.api.dao.impl.interfaces.*;
import com.blackboard.api.dao.util.MySQLDao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by ChristopherLicata on 11/28/15.
 */
public class BlackboardApi
{
    final AssignmentDao assignmentDao;

    final CourseDao courseDao;

    final GradeDao gradeDao;

    final InstructorDao instructorDao;

    final SchoolDao schoolDao;

    final StudentDao studentDao;

    final SubmissionDao submissionDao;

    final TranscriptDao transcriptDao;

    final UserDao userDao;

    final MySQLDao dao;


    public BlackboardApi(MySQLDao dao)
    {

        this.assignmentDao = new AssignmentMySQLDao(dao);
        this.courseDao = new CourseMySQLDao(dao);
        this.gradeDao = new GradeMySQLDao(dao);
        this.instructorDao = new InstructorMySQLDao(dao);
        this.schoolDao = new SchoolMySQLDao(dao);
        this.studentDao = new StudentMySQLDao(dao);
        this.submissionDao = new SubmissionMySQLDao(dao);
        this.transcriptDao = new TranscriptMySQLDao(dao);
        this.userDao = new UserMySQLDao(dao);
        this.dao = dao;
    }


    /*
     * STUDENT API
     */
    public Student createStudent(
            String firstName, String lastName, String email, String pw, int
            schoolId)
    {
        if (studentDao.findStudentByEmail(email).isPresent())
        {
            throw new IllegalArgumentException("User with the email " + email + " is already in the system");
        }

        return studentDao.createStudent(Student.createStudent(firstName, lastName, email, pw,
                                                              schoolId));

    }


    public Optional<Student> getStudentAccountByEmail(String email)
    {
        return studentDao.findStudentByEmail(email);
    }


    public List<Student> getAllStudents()
    {
        return studentDao.findAllStudents();
    }


    public List<Student> getStudentsInCourse(int courseId)
    {

        if (!courseDao.findCourseById(courseId).isPresent())
        {
            throw new IllegalArgumentException("No course could be found with the id of " + courseId);
        }

        return studentDao.findStudentsByCourseId(courseId);
    }

    /*
     * INSTRUCTOR API
     */


    public List<Instructor> getAllInstructors()
    {
        return instructorDao.findAllInstructors();
    }


    public Optional<Instructor> getInstructorAccountByEmail(String instructorEmail)
    {
        return instructorDao.findInstructorByEmail(instructorEmail);
    }


    public Instructor createInstructor(
            String firstName, String lastName, String email, String pw, int
            schoolId)
    {
        if (instructorDao.findInstructorByEmail(email).isPresent())
        {
            throw new IllegalArgumentException("User with the email " + email + " is already in the system");
        }

        return instructorDao.createInstructor(Instructor.createInstructor(firstName, lastName, email, pw,
                                                                          schoolId));
    }


    /*
     * USER API
     */
    public User updateInstructorAccount(
            String firstName, String lastName, String email, String password,
            int schoolId)
    {

        User user = Instructor.createInstructor(firstName, lastName, email, password, schoolId);
        return userDao.updateUser(user);
    }


    public User updateStudentAccount(
            String firstName, String lastName, String email, String password,
            double gpa, int schoolId)
    {

        User user = Student.createStudent(firstName, lastName, email, password, schoolId, gpa);
        return userDao.updateUser(user);
    }


    public Optional<User> deleteUserAccount(String userEmail)
    {
        return userDao.deleteUser(userEmail);
    }


    public Optional<User> getUser(String email)
    {
        return userDao.findUserByEmail(email);
    }


    /*
     * COURSE API
     */
    public List<Course> getAllInstructorCourses(String instructorEmail)
    {

        return courseDao.findCoursesByInstructor(instructorEmail);
    }
    //TODO implement search by course-prefix-subject and course number and school


    public List<Course> getAllCoursesOffered(School school)
    {
        if (!schoolDao.findSchoolById(school.getSchoolId()).isPresent())
        {
            throw new IllegalArgumentException("No school with  could be found with the id " + school
                    .getSchoolId() + " and name " + school.getName());
        }
        return courseDao.findCoursesOffered(school);
    }


    public Optional<Course> getCourse(int courseId)
    {

        return courseDao.findCourseById(courseId);
    }


    public Optional<Course> deleteCourse(Course course)
    {
        if (!courseDao.findCourseById(course.getCourseId()).isPresent())
        {
            throw new IllegalArgumentException(
                    "No Course with the id " + course.getCourseId() + " could be found");
        }
        return courseDao.deleteCourseById(course.getCourseId());
    }


    public Course createCourse(
            School school, Instructor instructor, String courseName, Subject subject,
            int courseNumber, int credits, String syllabusFileName, int maxCapacity)
    {
        Course course = new Course(school, instructor, courseName, subject, courseNumber, credits,
                                   syllabusFileName, maxCapacity);

        return courseDao.createCourse(course.getSchool(), course);
    }


    /*
     * SCHOOL API
     */
    public Optional<School> getSchool(int schoolId)
    {
        return schoolDao.findSchoolById(schoolId);
    }


    public List<School> getAllSchools()
    {
        return schoolDao.findAllSchools();
    }


    /*
     * Assignment API
     */
    public Assignment createAssignment(
            Course course, String assignmentName, String assignmentFileName,
            String instructions, double weight, int totalPoints, Date
                    dateAssigned, Date dueDate)
    {
        Assignment assignment = new Assignment(course, assignmentName, assignmentFileName,
                                               instructions, weight, totalPoints, dateAssigned,
                                               dueDate);

        return assignmentDao.createAssignment(assignment);
    }


    public Optional<Assignment> getAssignmentById(int assignmentId)
    {
        return assignmentDao.findAssignmentById(assignmentId);
    }


    public Optional<Assignment> deleteAssignmentById(int assignmentId)
    {
        if (!assignmentDao.findAssignmentById(assignmentId).isPresent())
        {
            throw new IllegalArgumentException(
                    "No Course with the id " + assignmentId + " could be found");
        }
        return assignmentDao.deleteAssignmentById(assignmentId);
    }


    public Assignment updateAssignment(
            int assignmentId, Course course, String assignmentName, String
            assignmentFileName, String instructions, double weight, int totalPoints, Date
                    dateAssigned, Date dueDate)
    {
        Assignment assignment = new Assignment(assignmentId, course, assignmentName, assignmentFileName,
                                               instructions, weight, totalPoints, dateAssigned,
                                               dueDate);
        return assignmentDao.updateAssignment(assignment);
    }


    public List<Assignment> getAllCourseAssignmentsById(int courseId)
    {
        return assignmentDao.findAllAssignmentsByCourseId(courseId);
    }

    /*
     * GRADE API
     */


    public Grade createGrade(int score, Assignment assignment, Submission submission, String studentEmail)
    {
        Grade grade = new Grade(score, assignment, submission.getSubmissionId(), studentEmail);
        return gradeDao.createGrade(grade);
    }


    public Optional<Grade> deleteGrade(Grade grade)
    {

        if (!gradeDao.findGradeById(grade.getGradeId()).isPresent())
        {
            throw new IllegalArgumentException(
                    "No Grade with the id " + grade.getGradeId() + " could be found");
        }
        return gradeDao.deleteGradeById(grade.getGradeId());
    }


    public Optional<Grade> getGradeById(int gradeId)
    {
        return gradeDao.findGradeById(gradeId);
    }


    public List<Grade> getAllGradesForAssignment(Assignment assignment)
    {
        if (!assignmentDao.findAssignmentById(assignment.getAssignmentId()).isPresent())
        {
            throw new IllegalArgumentException("There is no assignment with the id " + assignment
                    .getAssignmentId() + "in the System");
        }
        return gradeDao.findGradesByAssignment(assignment);
    }


    public Grade updateGrade(
            int gradeId, int score, Assignment assignment, int submissionId, String
            studentEmail)
    {
        if (!assignmentDao.findAssignmentById(assignment.getAssignmentId()).isPresent())
        {
            throw new IllegalArgumentException("There is no assignment with the id " + assignment
                    .getAssignmentId() + "in the System");
        }
        else if (!submissionDao.findStudentSubmission(studentEmail, assignment.getAssignmentId()).isPresent())
        {
            throw new IllegalArgumentException(
                    "There is no submission with the id " + submissionId + "in the System");
        }
        Grade grade = new Grade(gradeId, score, assignment, submissionId, studentEmail);
        return gradeDao.updateGrade(grade);
    }


    /*
     * Submission API
     */
    public Submission updateSubmission(
            int submissionId, Grade grade, Assignment assignment, String
            studentEmail, String
                    submissionFileName)
    {
        if (!assignmentDao.findAssignmentById(assignment.getAssignmentId()).isPresent())
        {
            throw new IllegalArgumentException("There is no assignment with the id " + assignment
                    .getAssignmentId() + "in the System");
        }
        else if (!gradeDao.findGradeById(grade.getGradeId()).isPresent())
        {
            throw new IllegalArgumentException("There is no grade with the id " +
                                                       grade.getGradeId() + "in the System");
        }

        Submission submission = new Submission(submissionId, grade, assignment, studentEmail,
                                               submissionFileName, dao.generateTimeStamp());
        return submissionDao.updateSubmission(submission);

    }


    /**
     * In the current design, the choice was made to exclude update functionality from submissions, due to the
     * fact that it is easier to keep
     */
    public Submission createSubmission(Assignment assignment, String studentEmail, String submissionFileName)
    {
        if (!assignmentDao.findAssignmentById(assignment.getAssignmentId()).isPresent())
        {
            throw new IllegalArgumentException("There is no assignment with the id " + assignment
                    .getAssignmentId() + "in the System");
        }

        Submission submission = new Submission(assignment, studentEmail, submissionFileName, dao
                .generateTimeStamp());

        return submissionDao.createSubmission(submission);
    }


    public List<Submission> getSubmissionsForAssignment(Assignment assignment)
    {

        if (!assignmentDao.findAssignmentById(assignment.getAssignmentId()).isPresent())
        {
            throw new IllegalArgumentException("There is no assignment with the id " + assignment
                    .getAssignmentId() + "in the System");
        }
        return submissionDao.findSubmissionsByAssignment(assignment);
    }


    public Optional<Submission> getStudentSubmission(String studentEmail, int assignmentId)
    {
        if (!assignmentDao.findAssignmentById(assignmentId).isPresent())
        {
            throw new IllegalArgumentException(
                    "There is no assignment with the id " + assignmentId + "in the System");
        }
        return submissionDao.findStudentSubmission(studentEmail, assignmentId);
    }


    public Optional<Submission> deleteStudentSubmission(String studentEmail, int submissionId)
    {
        if (!submissionDao.findStudentSubmission(studentEmail, submissionId).isPresent())
        {
            throw new IllegalArgumentException(
                    "There is no Submission for that student in the system.");
        }
        return submissionDao.deleteStudentSubmission(studentEmail, submissionId);
    }

    /*
     * Transcript API
     */


    public List<Transcript> getStudentTranscripts(String studentEmail)
    {
        return transcriptDao.findTranscriptsByStudentEmail(studentEmail);
    }


    public Optional<Transcript> getTranscriptById(int transcriptId)
    {
        return transcriptDao.findTranscriptByTranscriptId(transcriptId);
    }


    public Transcript createTranscript(String studentEmail, Season semester, int year, Course course)
    {
        Transcript transcript = new Transcript(studentEmail, semester, year, course);

        return transcriptDao.createTranscript(transcript);
    }


    public Transcript updateTranscript(
            int transcriptId, String studentEmail, Season season, int year,
            Course course, double grade)
    {
        Transcript transcript = new Transcript(transcriptId, studentEmail, season, year, course, grade);
        return transcriptDao.updateTranscript(transcript);
    }


    public Optional<Transcript> deleteTranscript(int transcriptId)
    {

        if (!transcriptDao.findTranscriptByTranscriptId(transcriptId).isPresent())
        {
            throw new IllegalArgumentException(
                    "There is no Transcript that matches that id in the system.");
        }
        return transcriptDao.deleteTranscript(transcriptId);
    }


    public static void main(String[] args)
    {

    }
}
