package com.fmi.evelina.unimobileapp.network;

import com.android.volley.Response;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.User;
import com.fmi.evelina.unimobileapp.model.UserRole;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.Room;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationCategoryContacts;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationContactData;
import com.fmi.evelina.unimobileapp.model.contacts_model.ContactData;
import com.fmi.evelina.unimobileapp.model.contacts_model.LecturerContact;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Course;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Grade;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Semester;
import com.fmi.evelina.unimobileapp.model.student_plan_model.StudentPlan;

import org.joda.time.LocalDate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * The network API Mock class. It is used for testing purposes.
 */
public class NetworkAPIMock {

    public static void signIn(final String userId, final String password, final ICallBack<User> onCallBack) {
        User testUser = new User("testUserName", "testUserPassword");
        testUser.Role = UserRole.STUD;
        onCallBack.onSuccess(testUser);
    }

    public static void getStudentSchedule(final ICallBack<List<RecurringStudentCalendarEvent>> onCallBack) {

        List<RecurringStudentCalendarEvent> result = new ArrayList<>();

        RecurringStudentCalendarEvent event1 = new RecurringStudentCalendarEvent();
        event1.Lecturer = "Lecturer";
        event1.StartDate = new LocalDate(2016, 1, 1).toDate();
        event1.EndDate = new LocalDate(2016, 2, 2).toDate();
        event1.StartTime = new Time(12, 00, 00);
        event1.EndTime = new Time(15, 00, 00);
        event1.Abbreviation = "Abbr";
        event1.Name = "Student Event Name";
        event1.Description = "Student Event Description";
        event1.TypeCode = "LECT";
        event1.IsElective = 'Y';
        event1.WeekDay = 3;
        event1.Location = "101/FMI";
        result.add(event1);

        onCallBack.onSuccess(result);


    }

    public static void getLecturerSchedule(final ICallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        List<RecurringLecturerCalendarEvent> result = new ArrayList<>();

        RecurringLecturerCalendarEvent event1 = new RecurringLecturerCalendarEvent();
        event1.StartDate = new LocalDate(2016, 1, 1).toDate();
        event1.EndDate = new LocalDate(2016, 2, 2).toDate();
        event1.StartTime = new Time(12, 00, 00);
        event1.EndTime = new Time(15, 00, 00);
        event1.Abbreviation = "Abbr";
        event1.Name = "Lecturer Event Name";
        event1.Description = "Lecturer Event Description";
        event1.TypeCode = "LECT";
        event1.IsElective = 'Y';
        event1.WeekDay = 3;
        event1.Location = "101/FMI";
        result.add(event1);

        onCallBack.onSuccess(result);
    }

    public static void getEvents(final ICallBack<List<CalendarEvent>> onCallBack) {
        List<CalendarEvent> result = new ArrayList<>();

        CalendarEvent event1 = new CalendarEvent();
        event1.Id = 1;
        event1.EventDate = new LocalDate(2016, 1, 1).toDate();
        event1.StartTime = new Time(12, 00, 00);
        event1.EndTime = new Time(17, 00, 00);
        event1.Abbreviation = "Event";
        event1.Name = "Event Name";
        event1.Description = "Event Description";
        event1.DbKey = 1;
        event1.TypeCode = "EVENT";
        result.add(event1);

        onCallBack.onSuccess(result);
    }

    public static void getNews(final Integer newsId, final int chunkSize, final ICallBack<List<News>> onCallBack) {
        List<News> result = new ArrayList<>();

        News news = new News();
        news.Title = "News Title";
        news.Text = " News text";
        news.Date = new LocalDate(2016, 1, 1).toDate();

        result.add(news);

        onCallBack.onSuccess(result);
    }

    public static void getNewsImage(final News news, final ICallBack<News> onCallBack) {
        onCallBack.onSuccess(news);
    }

    public static void getNewsImages(final List<News> newsList, final ICallBack<News> onCallBack) {
        for (News n : newsList) {
            onCallBack.onSuccess(n);
        }
    }

    public static void getNewsDetails(final int newsId, final ICallBack<News> onCallBack) {
        News news = new News();
        news.Title = "News Title";
        news.Text = " News text";
        news.Date = new LocalDate(2016, 1, 1).toDate();

        onCallBack.onSuccess(news);
    }

    public static void addNews(final News newsToSave, final Response.Listener onSuccess, final Response.ErrorListener onError) {
        onSuccess.onResponse(new Object());
    }

    public static void deleteNews(final Integer newsId, final String imageName, final Response.Listener onSuccess, final Response.ErrorListener onError) {
        onSuccess.onResponse(new Object());
    }

    public static void getStudentPlan(final ICallBack<StudentPlan> onCallBack) {
        StudentPlan plan = new StudentPlan();
        plan.Grades = new ArrayList<>();
        Grade grade = new Grade();
        grade.ID = 1;
        grade.Semesters = new ArrayList<>();
        Semester sem = new Semester();
        sem.ID = "Winter";
        sem.Courses = new ArrayList<>();
        Course c = new Course();
        c.Name = "Course Name";
        c.Credits = 5.0;
        c.Hours = "3+3";
        sem.Courses.add(c);
        grade.Semesters.add(sem);
        plan.Grades.add(grade);
        onCallBack.onSuccess(plan);
    }

    public static void getCurrentElectionCampaign(final ICallBack<ElectionCampaign> onCallBack) {
        ElectionCampaign camp = new ElectionCampaign();

        camp.OpenDate = new LocalDate(2016, 1, 1).toDate();
        camp.CloseDate = new LocalDate(2016, 2, 2).toDate();
        camp.EndDate = new LocalDate(2016, 3, 3).toDate();

        onCallBack.onSuccess(camp);
    }

    public static void getElectionCampaignCourses(final ICallBack<List<ElectionCourse>> onCallBack) {
        List<ElectionCourse> result = new ArrayList<>();

        ElectionCourse c = new ElectionCourse();
        c.Name = "Name";
        c.Description = "Description";
        c.Category = "OKN";
        c.Credits = 5.0;
        c.Hours = "3+3";
        c.Id = 1;
        c.Enrolled = false;
        c.MinGrade = 1;
        c.Lecturer = "Lecturer";
        c.IsForMyGrade = true;
        c.IsForMyProgram = true;
        c.HasFreeSpaces = true;

        result.add(c);

        onCallBack.onSuccess(result);
    }

    public static void enrollCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {
        onSuccess.onResponse(new Object());
    }

    public static void cancelCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {
        onSuccess.onResponse(new Object());
    }

    public static void getAdministrationContacts(final ICallBack<List<AdministrationCategoryContacts>> onCallBack) {
        List<AdministrationCategoryContacts> result = new ArrayList<>();

        AdministrationCategoryContacts c = new AdministrationCategoryContacts();

        c.Category = "Category";

        c.Contacts = new ArrayList<>();

        AdministrationContactData data = new AdministrationContactData();
        data.Job = "Job";
        data.Name = "Name";
        ContactData d = new ContactData();
        d.Phone = "0888 123456";
        d.Time = "12:00 15:00";
        d.Email = "email@domain.com";
        data.ContactInfo = d;

        c.Contacts.add(data);
        result.add(c);

        onCallBack.onSuccess(result);

    }

    public static void getLecturersContacts(final ICallBack<List<LecturerContact>> onCallBack) {
        List<LecturerContact> result = new ArrayList<>();

        LecturerContact c = new LecturerContact();
        c.Name = "Name";
        c.Department = "Department";

        ContactData d = new ContactData();
        d.Phone = "0888 123456";
        d.Time = "12:00 15:00";
        d.Email = "email@domain.com";
        c.ContactData = d;

        result.add(c);

        onCallBack.onSuccess(result);
    }

    public static void getRooms(final ICallBack<List<Room>> onCallBack) {
        List<Room> result = new ArrayList<>();

        Room r = new Room();
        r.Id = 201;
        r.Location = "FMI";

        result.add(r);

        onCallBack.onSuccess(result);

    }

    public static void createEvent(final CalendarEvent event, Response.Listener onSuccess, Response.ErrorListener onError) {
        onSuccess.onResponse(new Object());
    }

    public static void deleteEvent(final Integer eventId, Response.Listener onSuccess, Response.ErrorListener onError) {
        onSuccess.onResponse(new Object());
    }

}
