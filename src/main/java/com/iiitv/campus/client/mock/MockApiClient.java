package com.iiitv.campus.client.mock;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile({"mock", "default"})
public class MockApiClient implements ApiClient {

    @Override
    public StudentInfo getStudentProfile() {
        return new StudentInfo(
                "Raghav A.",
                "B.Tech (CSE)",
                "Semester V",
                "Section A",
                "202201045",
                3
        );
    }

    @Override
    public AttendanceSummary getAttendanceSummary() {
        return new AttendanceSummary(
                160,
                132,
                28,
                0,
                82.5,
                75.0,
                AttendanceStatus.SAFE,
                8,
                4,
                85.0,
                83.8
        );
    }

    @Override
    public List<CourseAttendance> getCourseAttendanceList() {
        return List.of(
                new CourseAttendance("CS301", "Data Structures", 40, 33, 7, 0, 82.5, AttendanceStatus.SAFE),
                new CourseAttendance("CS302", "Operating Systems", 40, 31, 9, 0, 77.5, AttendanceStatus.WATCH),
                new CourseAttendance("CS303", "Computer Networks", 40, 34, 6, 0, 85.0, AttendanceStatus.SAFE),
                new CourseAttendance("CS304", "DBMS", 40, 34, 6, 0, 85.0, AttendanceStatus.SAFE)
        );
    }

    @Override
    public List<ClassOccurrence> getTodaySchedule() {
        return List.of(
                new ClassOccurrence("tod1", "BREAK", "Lunch Break", "Dining Hall", "", "Tuesday", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("tod2", "CS303", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Tuesday", "15:15", "16:45", "Lecture", true, "in 45 min", "CSE"),
                new ClassOccurrence("tod3", "CS305", "Formal Language and Automata Theory", "ICD-2125", "Dr. Venkata Phanikrishna", "Tuesday", "16:45", "18:15", "Lecture", false, null, "CSE")
        );
    }

    @Override
    public List<ClassOccurrence> getWeeklyTimetable(String weekIdentifier) {
        if (weekIdentifier != null) {
            String lower = weekIdentifier.toLowerCase();
            if (lower.contains("all") || lower.contains("master")) {
                List<ClassOccurrence> all = new ArrayList<>();
                all.addAll(getSem1Timetable());
                all.addAll(getSem2Timetable());
                all.addAll(getSem3Timetable());
                all.addAll(getSem4Timetable());
                all.addAll(getSem5Timetable());
                all.addAll(getSem6Timetable());
                all.addAll(getSem7Timetable());
                all.addAll(getSem8Timetable());
                all.addAll(getMca1Timetable());
                all.addAll(getMca2Timetable());
                all.addAll(getMca3Timetable());
                all.addAll(getMca4Timetable());
                all.addAll(getMtech1Timetable());
                all.addAll(getMtech2Timetable());
                all.addAll(getMtech34Timetable());
                return all;
            } else if (lower.contains("mca")) {
                if (lower.contains("sem 1") || lower.contains("sem-1") || lower.contains("semester 1") || lower.contains("semester i") || lower.contains("mca 1") || lower.contains("mca-1") || lower.contains("mca-i") || lower.contains("mca i")) {
                    return getMca1Timetable();
                } else if (lower.contains("sem 2") || lower.contains("sem-2") || lower.contains("semester 2") || lower.contains("semester ii") || lower.contains("mca 2") || lower.contains("mca-2") || lower.contains("mca-ii") || lower.contains("mca ii")) {
                    return getMca2Timetable();
                } else if (lower.contains("sem 3") || lower.contains("sem-3") || lower.contains("semester 3") || lower.contains("semester iii") || lower.contains("mca 3") || lower.contains("mca-3") || lower.contains("mca-iii") || lower.contains("mca iii")) {
                    return getMca3Timetable();
                } else if (lower.contains("sem 4") || lower.contains("sem-4") || lower.contains("semester 4") || lower.contains("semester iv") || lower.contains("mca 4") || lower.contains("mca-4") || lower.contains("mca-iv") || lower.contains("mca iv")) {
                    return getMca4Timetable();
                }
                return getMcaTimetable();
            } else if (lower.contains("mtech") || lower.contains("m.tech")) {
                if (lower.contains("sem 1") || lower.contains("sem-1") || lower.contains("semester 1") || lower.contains("semester i") || lower.contains("mtech 1") || lower.contains("mtech-1") || lower.contains("mtech i")) {
                    return getMtech1Timetable();
                } else if (lower.contains("sem 2") || lower.contains("sem-2") || lower.contains("semester 2") || lower.contains("semester ii") || lower.contains("mtech 2") || lower.contains("mtech-2") || lower.contains("mtech ii")) {
                    return getMtech2Timetable();
                } else if (lower.contains("sem 3") || lower.contains("sem 4") || lower.contains("sem-3") || lower.contains("sem-4") || lower.contains("semester 3") || lower.contains("semester 4") || lower.contains("semester iii") || lower.contains("semester iv") || lower.contains("mtech 3") || lower.contains("mtech-3")) {
                    return getMtech34Timetable();
                }
                return getMtechTimetable();
            } else if (lower.contains("sem-8") || lower.contains("sem 8") || lower.contains("sem viii") || lower.contains("semester 8") || lower.contains("semester viii")) {
                return getSem8Timetable();
            } else if (lower.contains("sem-7") || lower.contains("sem 7") || lower.contains("sem vii") || lower.contains("semester 7") || lower.contains("semester vii")) {
                return getSem7Timetable();
            } else if (lower.contains("sem-6") || lower.contains("sem 6") || lower.contains("sem vi") || lower.contains("semester 6") || lower.contains("semester vi")) {
                return getSem6Timetable();
            } else if (lower.contains("sem-5") || lower.contains("sem 5") || lower.contains("sem v") || lower.contains("semester 5") || lower.contains("semester v")) {
                return getSem5Timetable();
            } else if (lower.contains("sem-4") || lower.contains("sem 4") || lower.contains("sem iv") || lower.contains("semester 4") || lower.contains("semester iv")) {
                return getSem4Timetable();
            } else if (lower.contains("sem-3") || lower.contains("sem 3") || lower.contains("sem iii") || lower.contains("semester 3") || lower.contains("semester iii")) {
                return getSem3Timetable();
            } else if (lower.contains("sem-2") || lower.contains("sem 2") || lower.contains("sem ii") || lower.contains("semester 2") || lower.contains("semester ii")) {
                return getSem2Timetable();
            } else if (lower.contains("sem-1") || lower.contains("sem 1") || lower.contains("sem i") || lower.contains("semester 1") || lower.contains("semester i")) {
                return getSem1Timetable();
            }
        }
        return getSem5Timetable(); // Default to B.Tech Sem V (enrolled)
    }

    private List<ClassOccurrence> getSem5Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("m1", "CS305", "Formal Language & Automata Theory (Tut)", "ICD-2113", "Dr. Venkata Phanikrishna", "Mon", "11:30", "12:30", "Tutorial", false, null, "CSE"),
                new ClassOccurrence("m2", "BREAK", "Lunch Break", "Dining Hall", "", "Mon", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("m3", "CS301", "Computer Networks", "ICD-2125", "Dr. Abhisek Paul", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("m4", "CS309", "Introduction to Cryptography", "ICD-2125", "Dr. Gaurav Pareek", "Mon", "16:45", "18:15", "Lecture", false, null, "CSE"),

                // Tuesday (Today)
                new ClassOccurrence("t1", "BREAK", "Lunch Break", "Dining Hall", "", "Tue", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("t2", "CS303", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Tue", "15:15", "16:45", "Lecture", true, "in 45 min", "CSE"),
                new ClassOccurrence("t3", "CS305", "Formal Language and Automata Theory", "ICD-2125", "Dr. Venkata Phanikrishna", "Tue", "16:45", "18:15", "Lecture", false, null, "CSE"),

                // Wednesday
                new ClassOccurrence("w1a", "CS361", "Computer Networks Lab", "ICD-2102-CS", "Dr. Abhisek Paul", "Wed", "11:15", "13:15", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("w1b", "CS361", "Computer Networks Lab", "ICD-2103-CS", "Dr. Abhisek Paul", "Wed", "11:15", "13:15", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("w2", "BREAK", "Lunch Break", "Dining Hall", "", "Wed", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("w3", "CS307", "Artificial Intelligence", "ICD-2125", "Dr. Deepika Gupta", "Wed", "15:15", "16:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("w4", "CS301", "Computer Networks", "ICD-2125", "Dr. Abhisek Paul", "Wed", "16:45", "18:15", "Lecture", false, null, "CSE"),

                // Thursday
                new ClassOccurrence("th1a", "CS369", "Cryptography Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Thu", "09:00", "11:00", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("th1b", "CS369", "Cryptography Lab", "ICD-2103-CS", "Dr. Gaurav Pareek", "Thu", "09:00", "11:00", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("th2a", "CS363", "Operating Systems Lab", "ICD-2102-CS", "Dr. Deepika Gupta", "Thu", "11:15", "13:15", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("th2b", "CS363", "Operating Systems Lab", "ICD-2103-CS", "Dr. Deepika Gupta", "Thu", "11:15", "13:15", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("th3", "BREAK", "Lunch Break", "Dining Hall", "", "Thu", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("th4", "CS309", "Introduction to Cryptography", "ICD-2125", "Dr. Gaurav Pareek", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("th5", "CS303", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Thu", "16:45", "18:15", "Lecture", false, null, "CSE"),

                // Friday
                new ClassOccurrence("f1a", "CS367", "Artificial Intelligence Lab", "ICD-2102-CS", "Dr. Deepika Gupta", "Fri", "11:15", "13:00", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("f1b", "CS367", "Artificial Intelligence Lab", "ICD-2103-CS", "Dr. Deepika Gupta", "Fri", "11:15", "13:00", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("f2", "BREAK", "Lunch Break", "Dining Hall", "", "Fri", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("f3", "CS305", "Formal Language and Automata Theory", "ICD-2125", "Dr. Venkata Phanikrishna", "Fri", "15:15", "16:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("f4", "CS307", "Artificial Intelligence", "ICD-2125", "Dr. Deepika Gupta", "Fri", "16:45", "18:15", "Lecture", false, null, "CSE"),

                // Saturday
                new ClassOccurrence("s1", "CS301", "Networks Problem Solving Session", "ICD-2125", "Dr. Abhisek Paul", "Sat", "10:00", "12:00", "Tutorial", false, null, "CSE")
        );
    }

    private List<ClassOccurrence> getSem1Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s1_m1", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2113", "Dr. Abhishek Kesarwani & Dr. Pranjal Srivastava", "Mon", "08:00", "09:30", "Lecture", false, null, "ECE/AI/CS"),
                new ClassOccurrence("s1_m2", "CS101", "Computer Programming & Problem Solving", "ICD-2118", "Dr. Pranjal Srivastava & Dr. Radhika Lama", "Mon", "08:00", "09:30", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_m3", "CS101", "Computer Programming & Problem Solving", "ICD-2113", "Dr. Pranjal Srivastava & Dr. Radhika Lama", "Mon", "09:30", "11:00", "Lecture", false, null, "ECE/AI/CS"),
                new ClassOccurrence("s1_m4", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2118", "Dr. Abhishek Kesarwani & Dr. Pranjal Srivastava", "Mon", "09:30", "11:00", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_m5", "CS161", "Computer Programming Lab", "ICD-2102-CS", "Dr. Pranjal Srivastava & Dr. Radhika Lama", "Mon", "14:00", "16:00", "Lab", false, null, "CSE Lab 1"),
                new ClassOccurrence("s1_m6", "CS161", "Computer Programming Lab", "ICD-2105-CS", "Dr. Pranjal Srivastava & Dr. Radhika Lama", "Mon", "14:00", "16:00", "Lab", false, null, "CSE Lab 2"),

                // Tuesday
                new ClassOccurrence("s1_t1", "PH101", "Physics-I", "ICD-2113", "Dr. Mukesh Singh & Dr. Suvam Singh", "Tue", "08:00", "09:30", "Lecture", false, null, "CSE/ECE"),
                new ClassOccurrence("s1_t2", "HS101", "Spoken and Written Communication", "ICD-2113", "Dr. Amal Tom", "Tue", "09:30", "11:30", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_t3", "AI101", "Intro to Artificial Intelligence", "ICD-2108", "Dr. Deepika Gupta & Dr. Mukesh Singh", "Tue", "09:30", "11:30", "Lecture", false, null, "AI"),
                new ClassOccurrence("s1_t4", "CCS101", "Cybersecurity Essentials", "ICD-2118", "Dr. Naveen Kumar", "Tue", "09:30", "11:00", "Lecture", false, null, "CSE(CS)"),
                new ClassOccurrence("s1_t5", "PH101", "Physics-I Tutorial", "ICD-2113", "Dr. Mukesh Singh", "Tue", "11:30", "12:30", "Tutorial", false, null, "All"),
                new ClassOccurrence("s1_t6", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Uday Panwar & Dr. Varun Kumar", "Tue", "13:30", "16:30", "Lab", false, null, "ECE/AI Lab 1"),
                new ClassOccurrence("s1_t7", "EC161", "Basic Electronics Lab-I", "ICD-2223-EL", "Dr. Uday Panwar & Dr. Varun Kumar", "Tue", "13:30", "16:30", "Lab", false, null, "ECE/AI Lab 2"),

                // Wednesday
                new ClassOccurrence("s1_w1", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2113", "Dr. Abhishek Kesarwani & Dr. Pranjal Srivastava", "Wed", "08:00", "09:30", "Lecture", false, null, "ECE/AI/CS"),
                new ClassOccurrence("s1_w2", "CS101", "Computer Programming", "ICD-2118", "Dr. Pranjal Srivastava & Dr. Radhika Lama", "Wed", "08:00", "09:30", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_w3", "EC101", "Basic Electronics-I", "ICD-2113", "Dr. Uday Panwar & Dr. Varun Kumar", "Wed", "09:30", "11:00", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s1_w4", "EC101", "Basic Electronics-I", "ICD-2118", "Dr. Uday Panwar & Dr. Varun Kumar", "Wed", "09:30", "11:00", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_w5", "CCS103", "System Administration Lab", "ICD-2109", "Dr. Ravi Nahta", "Wed", "11:15", "15:00", "Lab", false, null, "CSE(CS)"),
                new ClassOccurrence("s1_w6", "CS161", "Computer Programming Lab", "ICD-2102-CS", "Dr. Pranjal Srivastava", "Wed", "14:00", "16:00", "Lab", false, null, "CSE Lab 3"),
                new ClassOccurrence("s1_w7", "CS161", "Computer Programming Lab", "ICD-2103-CS", "Dr. Pranjal Srivastava", "Wed", "14:00", "16:00", "Lab", false, null, "CSE Lab 4"),
                new ClassOccurrence("s1_w8", "CS161", "Computer Programming Lab", "ICD-2105-CS", "Dr. Radhika Lama", "Wed", "14:00", "16:00", "Lab", false, null, "CSE Lab 5"),
                new ClassOccurrence("s1_w9", "CCS105", "Digital Design & Architecture", "ICD-2113", "Dr. Uday Panwar", "Wed", "15:30", "17:00", "Lecture", false, null, "CSE(CS)"),

                // Thursday
                new ClassOccurrence("s1_th1", "EC103", "Introduction to ECE", "ICD-2118", "Dr. Mukesh Singh & Dr. Varun Kumar", "Thu", "08:00", "09:30", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s1_th2", "CCS101", "Cybersecurity Essentials", "ICD-2125", "Dr. Naveen Kumar", "Thu", "08:00", "09:30", "Lecture", false, null, "CSE(CS)"),
                new ClassOccurrence("s1_th3", "HS103", "Artificial Intelligence & Ethics", "ICD-2113", "Dr. Abhishek Kesarwani & Dr. Gaurav Pareek", "Thu", "08:00", "10:00", "Lecture", false, null, "AI"),
                new ClassOccurrence("s1_th4", "PH101", "Physics-I", "ICD-2118", "Dr. Mukesh Singh & Dr. Suvam Singh", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE/ECE"),
                new ClassOccurrence("s1_th5", "CCS105", "Digital Design & Architecture", "ICD-2125", "Dr. Uday Panwar", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE(CS)"),
                new ClassOccurrence("s1_th6", "EC101", "Basic Electronics Tutorial", "ICD-2113", "Dr. Uday Panwar & Dr. Varun Kumar", "Thu", "11:15", "12:15", "Tutorial", false, null, "All"),
                new ClassOccurrence("s1_th7", "PH161", "Physics Lab", "ICD-1111-PH", "Dr. Mukesh Singh", "Thu", "14:00", "16:00", "Lab", false, null, "Lab 2"),
                new ClassOccurrence("s1_th8", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Uday Panwar & Dr. Varun Kumar", "Thu", "14:00", "17:00", "Lab", false, null, "Lab 3"),
                new ClassOccurrence("s1_th9", "EC161", "Basic Electronics Lab-I", "ICD-2223-EL", "Dr. Uday Panwar & Dr. Varun Kumar", "Thu", "14:00", "17:00", "Lab", false, null, "Lab 4"),
                new ClassOccurrence("s1_th10", "PH161", "Physics Lab", "ICD-1111-PH", "Dr. Mukesh Singh", "Thu", "16:00", "18:00", "Lab", false, null, "Lab 1"),

                // Friday
                new ClassOccurrence("s1_f1", "EC101", "Basic Electronics-I", "ICD-2113", "Dr. Uday Panwar & Dr. Varun Kumar", "Fri", "08:00", "09:30", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s1_f2", "EC101", "Basic Electronics-I", "ICD-2118", "Dr. Uday Panwar & Dr. Varun Kumar", "Fri", "08:00", "09:30", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_f3", "CS101", "Computer Programming", "ICD-2113", "Dr. Pranjal Srivastava & Dr. Radhika Lama", "Fri", "09:30", "11:00", "Lecture", false, null, "AI/ECE/CS"),
                new ClassOccurrence("s1_f4", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2118", "Dr. Abhishek Kesarwani & Dr. Pranjal Srivastava", "Fri", "09:30", "11:00", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s1_f5", "EC101", "Basic Electronics Tutorial", "ICD-2113", "Dr. Uday Panwar & Dr. Varun Kumar", "Fri", "14:00", "15:00", "Tutorial", false, null, "All"),
                new ClassOccurrence("s1_f6", "HS101", "Communication Skills", "ICD-2113", "Dr. Amal Tom", "Fri", "15:15", "17:15", "Lecture", false, null, "AI/ECE/CS")
        );
    }

    private List<ClassOccurrence> getSem3Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s3_m1", "CS263", "Design & Analysis of Algorithms Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Mon", "08:45", "10:45", "Lab", false, null, "ECE"),
                new ClassOccurrence("s3_m2", "SC201", "Environmental Science", "ICD-2118", "Dr. Vikas Kumar", "Mon", "11:15", "13:15", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_m3", "MA201", "Mathematics-III (RV, RP, Stat)", "ICD-2125", "Dr. Suvam Singh", "Mon", "11:15", "13:15", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s3_m4", "EC201", "Digital Logic Design", "ICD-2118", "Dr. Varun Kumar", "Mon", "13:45", "15:15", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_m5", "EC201", "Digital Logic Design", "ICD-2125", "Dr. Varun Kumar", "Mon", "13:45", "15:15", "Lecture", false, null, "ECE"),

                // Tuesday
                new ClassOccurrence("s3_t1a", "EC261", "Digital Logic Design Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Tue", "08:00", "10:00", "Lab", false, null, "ECE"),
                new ClassOccurrence("s3_t1b", "EC261", "Digital Logic Design Lab", "ICD-2222-EL", "Dr. Varun Kumar", "Tue", "08:00", "10:00", "Lab", false, null, "AI"),
                new ClassOccurrence("s3_t2", "MA201", "Mathematics-III (RV, RP, Stat)", "ICD-2118", "Dr. Suvam Singh", "Tue", "11:15", "13:15", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_t3", "HS201", "Technical Writing", "ICD-2125", "Dr. Amal Tom", "Tue", "11:15", "13:15", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s3_t4", "CS203", "Design and Analysis of Algorithms", "ICD-2109", "Dr. Gaurav Pareek", "Tue", "13:45", "15:15", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s3_t5", "EC205", "Antenna & Wave Propagation", "ICD-2118", "Dr. Ranjeet Pratap Singh", "Tue", "13:45", "15:15", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s3_t6", "AI201", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Tue", "15:15", "16:45", "Lecture", false, null, "AI"),

                // Wednesday
                new ClassOccurrence("s3_w1a", "CS263", "Algorithms Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Wed", "09:00", "11:00", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("s3_w1b", "CS263", "Algorithms Lab", "ICD-2103-CS", "Dr. Gaurav Pareek", "Wed", "09:00", "11:00", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("s3_w2", "EC265", "Antenna Lab", "ICD-2223-EL", "Dr. Mukesh Singh & Dr. Ranjeet Pratap Singh", "Wed", "09:00", "11:00", "Lab", false, null, "ECE"),
                new ClassOccurrence("s3_w3", "HS201", "Technical Writing", "ICD-2125", "Dr. Amal Tom", "Wed", "11:15", "13:15", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_w4", "SC201", "Environmental Science", "ICD-2118", "Dr. Vikas Kumar", "Wed", "11:30", "13:00", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s3_w5", "MA201", "Mathematics-III", "ICD-2113", "Dr. Suvam Singh", "Wed", "13:45", "15:15", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s3_w6", "CS201", "Object Oriented Design & Programming", "ICD-2125", "Dr. Radhika Lama", "Wed", "13:45", "15:15", "Lecture", false, null, "CSE"),

                // Thursday
                new ClassOccurrence("s3_th1a", "CS261", "Object Oriented Design Lab", "ICD-2104-CS", "Dr. Radhika Lama", "Thu", "08:00", "10:00", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("s3_th1b", "CS261", "Object Oriented Design Lab", "ICD-2105-CS", "Dr. Radhika Lama", "Thu", "08:00", "10:00", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("s3_th1c", "AI261", "Operating Systems Lab", "ICD-2109", "Dr. Deepika Gupta", "Thu", "08:00", "10:00", "Lab", false, null, "AI"),
                new ClassOccurrence("s3_th2a", "EC201", "Digital Logic Design", "ICD-2118", "Dr. Varun Kumar", "Thu", "11:15", "12:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_th2b", "EC201", "Digital Logic Design", "ICD-2125", "Dr. Varun Kumar", "Thu", "11:30", "13:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s3_th3", "AI203", "Linear Systems and Signal Processing", "ICD-2125", "Dr. Pratik Shah", "Thu", "13:15", "15:15", "Lecture", false, null, "AI/ECE"),
                new ClassOccurrence("s3_th4", "MA201", "Mathematics-III", "ICD-2113", "Dr. Suvam Singh", "Thu", "13:45", "15:15", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_th5", "CS201", "Object Oriented Design & Programming", "ICD-2113", "Dr. Radhika Lama", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s3_th6", "AI201", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Thu", "16:45", "18:15", "Lecture", false, null, "AI"),

                // Friday
                new ClassOccurrence("s3_f1a", "EC261", "Digital Logic Design Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Fri", "09:00", "11:00", "Lab", false, null, "CSE Sec 1"),
                new ClassOccurrence("s3_f1b", "EC261", "Digital Logic Design Lab", "ICD-2222-EL", "Dr. Varun Kumar", "Fri", "09:00", "11:00", "Lab", false, null, "CSE Sec 2"),
                new ClassOccurrence("s3_f2a", "EC205", "Antenna & Wave Propagation", "ICD-2118", "Dr. Ranjeet Pratap Singh", "Fri", "11:15", "12:45", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s3_f2b", "CS203", "Design and Analysis of Algorithms", "ICD-2125", "Dr. Gaurav Pareek", "Fri", "11:15", "12:45", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s3_f3", "EC207", "Network Analysis & Synthesis", "ICD-2118", "Dr. Isha Chauhan", "Fri", "13:15", "15:15", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s3_f4", "MA201", "Math-III Tutorial", "ICD-2231", "Dr. Suvam Singh", "Fri", "15:15", "16:15", "Tutorial", false, null, "CSE"),
                new ClassOccurrence("s3_f5", "AI203", "Signal Processing Lab", "ICD-2127", "Dr. Pratik Shah", "Fri", "15:15", "17:15", "Lab", false, null, "AI/ECE"),
                new ClassOccurrence("s3_f6", "MA201", "Math-III Tutorial", "ICD-2231", "Dr. Suvam Singh", "Fri", "17:30", "18:30", "Tutorial", false, null, "AI/ECE")
        );
    }

    private List<ClassOccurrence> getSem7Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s7_m1", "CS447", "Software Project Management Lab", "ICD-2108", "Dr. Karan Verma", "Mon", "13:00", "15:00", "Lab", false, null, "CSE/IT"),
                new ClassOccurrence("s7_m2", "CS433", "Topics in Deep Learning", "ICD-2127", "Dr. Jignesh Bhatt", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s7_m3", "CS445", "Software and Cyber Security", "ICD-2229", "Dr. Naveen Kumar", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s7_m4", "IT403", "Cloud System Management", "ICD-2127", "Prof. Harendra", "Mon", "16:45", "18:15", "Lecture", false, null, "IT"),

                // Tuesday
                new ClassOccurrence("s7_t1", "CS445", "Software and Cyber Security Lab", "ICD-2109", "Dr. Naveen Kumar", "Tue", "11:15", "13:15", "Lab", false, null, "CSE/IT"),
                new ClassOccurrence("s7_t2", "CS433", "Deep Learning Lab", "ICD-2102-CS", "Dr. Jignesh Bhatt", "Tue", "11:30", "13:30", "Lab", false, null, "CSE/IT"),
                new ClassOccurrence("s7_t3", "CS449", "Introduction to Remote Sensing", "ICD-2109", "Prof. Atul Verma", "Tue", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),

                // Wednesday
                new ClassOccurrence("s7_w1", "EC425", "Computer Graphics and Multimedia", "ICD-2127", "Dr. Jignesh Patel", "Wed", "13:45", "15:15", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s7_w2", "CS447", "Software Project Management", "ICD-2109", "Dr. Karan Verma", "Wed", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s7_w3", "CS445", "Software and Cyber Security", "ICD-2109", "Dr. Naveen Kumar", "Wed", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s7_w4", "CS433", "Topics in Deep Learning", "ICD-2127", "Dr. Jignesh Bhatt", "Wed", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),

                // Thursday
                new ClassOccurrence("th_break", "BREAK", "Lunch Break", "Dining Hall", "", "Thu", "13:15", "14:00", "Break", false, null),
                new ClassOccurrence("s7_th1", "CS447", "Software Project Management", "ICD-2109", "Dr. Karan Verma", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s7_th2", "CS449", "Introduction to Remote Sensing", "ICD-2109", "Prof. Atul Verma", "Thu", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),

                // Friday
                new ClassOccurrence("s7_f1", "EC425", "Computer Graphics and Multimedia", "ICD-2229", "Dr. Jignesh Patel", "Fri", "13:15", "14:45", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s7_f2", "IT403", "Cloud System Management", "ICD-2109", "Prof. Harendra", "Fri", "15:15", "16:45", "Lecture", false, null, "IT")
        );
    }

    private List<ClassOccurrence> getSem2Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s2_m1", "CS102", "Data Structures & Algorithms", "ICD-2118", "Dr. Deepika Gupta", "Mon", "08:00", "09:30", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s2_m2", "EC102", "Basic Electronics", "ICD-2113", "Dr. Varun Kumar", "Mon", "08:00", "09:30", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s2_m3", "MA102", "Linear Algebra & Differential Equations", "ICD-2118", "Dr. Radhika Lama", "Mon", "09:30", "11:00", "Lecture", false, null, "All"),
                new ClassOccurrence("s2_m4", "CS162", "Data Structures Lab", "ICD-2102-CS", "Dr. Deepika Gupta", "Mon", "14:00", "16:00", "Lab", false, null, "CSE"),

                // Tuesday
                new ClassOccurrence("s2_t1", "HS102", "Professional Communication", "ICD-2118", "Dr. Barnali Chetia", "Tue", "08:00", "09:30", "Lecture", false, null, "All"),
                new ClassOccurrence("s2_t2", "CS104", "Computer Organization & Architecture", "ICD-2118", "Dr. Gaurav Pareek", "Tue", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s2_t3", "EC104", "Signals & Networks", "ICD-2113", "Dr. Jignesh S Bhatt", "Tue", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s2_t4", "EC162", "Electronics Circuit Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Tue", "13:30", "16:30", "Lab", false, null, "ECE"),

                // Wednesday
                new ClassOccurrence("s2_w1", "MA102", "Linear Algebra & Differential Equations", "ICD-2118", "Dr. Radhika Lama", "Wed", "08:00", "09:30", "Lecture", false, null, "All"),
                new ClassOccurrence("s2_w2", "CS102", "Data Structures & Algorithms", "ICD-2118", "Dr. Deepika Gupta", "Wed", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s2_w3", "EC102", "Basic Electronics", "ICD-2113", "Dr. Varun Kumar", "Wed", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s2_w4", "CS162", "Data Structures Lab", "ICD-2103-CS", "Dr. Deepika Gupta", "Wed", "14:00", "16:00", "Lab", false, null, "AI"),

                // Thursday
                new ClassOccurrence("s2_th1", "CS104", "Computer Organization & Architecture", "ICD-2118", "Dr. Gaurav Pareek", "Thu", "08:00", "09:30", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s2_th2", "EC104", "Signals & Networks", "ICD-2113", "Dr. Jignesh S Bhatt", "Thu", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s2_th3", "MA102", "Mathematics-II Tutorial", "ICD-2118", "Dr. Radhika Lama", "Thu", "11:15", "12:15", "Tutorial", false, null, "All"),
                new ClassOccurrence("s2_th4", "IT162", "IT Workshop & Python Lab", "ICD-2105-CS", "Dr. Karan Verma", "Thu", "14:00", "16:00", "Lab", false, null, "All"),

                // Friday
                new ClassOccurrence("s2_f1", "HS102", "Professional Communication", "ICD-2118", "Dr. Barnali Chetia", "Fri", "08:00", "09:30", "Lecture", false, null, "All"),
                new ClassOccurrence("s2_f2", "CS102", "Data Structures & Algorithms", "ICD-2118", "Dr. Deepika Gupta", "Fri", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s2_f3", "CS102", "Data Structures Tutorial", "ICD-2118", "Dr. Deepika Gupta", "Fri", "11:15", "12:15", "Tutorial", false, null, "CSE/AI"),
                new ClassOccurrence("s2_f4", "EC164", "Signal Processing Lab", "ICD-2222-EL", "Dr. Jignesh S Bhatt", "Fri", "14:00", "16:00", "Lab", false, null, "ECE")
        );
    }

    private List<ClassOccurrence> getSem4Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s4_m1", "CS204", "Operating Systems", "ICD-2118", "Dr. Deepika Gupta", "Mon", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s4_m2", "EC204", "Microprocessors & Interfacing", "ICD-2125", "Dr. Varun Kumar", "Mon", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s4_m3", "MA202", "Discrete Mathematics & Optimization", "ICD-2118", "Dr. Radhika Lama", "Mon", "11:15", "13:15", "Lecture", false, null, "All"),
                new ClassOccurrence("s4_m4", "CS264", "Operating Systems Lab", "ICD-2102-CS", "Dr. Deepika Gupta", "Mon", "14:00", "16:00", "Lab", false, null, "CSE"),

                // Tuesday
                new ClassOccurrence("s4_t1", "CS202", "Database Management Systems", "ICD-2118", "Dr. Venkata Phanikrishna", "Tue", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s4_t2", "EC202", "Analog Communication Systems", "ICD-2125", "Dr. Jignesh S Bhatt", "Tue", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s4_t3", "CS206", "Theory of Computation", "ICD-2118", "Dr. Gaurav Pareek", "Tue", "11:15", "13:15", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s4_t4", "EC264", "Microprocessor Interfacing Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Tue", "14:00", "16:00", "Lab", false, null, "ECE"),

                // Wednesday
                new ClassOccurrence("s4_w1", "CS204", "Operating Systems", "ICD-2118", "Dr. Deepika Gupta", "Wed", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s4_w2", "EC204", "Microprocessors & Interfacing", "ICD-2125", "Dr. Varun Kumar", "Wed", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s4_w3", "MA202", "Discrete Mathematics & Optimization", "ICD-2118", "Dr. Radhika Lama", "Wed", "11:15", "13:15", "Lecture", false, null, "All"),
                new ClassOccurrence("s4_w4", "CS262", "Database Management Systems Lab", "ICD-2104-CS", "Dr. Venkata Phanikrishna", "Wed", "14:00", "16:00", "Lab", false, null, "CSE/AI"),

                // Thursday
                new ClassOccurrence("s4_th1", "CS202", "Database Management Systems", "ICD-2118", "Dr. Venkata Phanikrishna", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s4_th2", "EC202", "Analog Communication Systems", "ICD-2125", "Dr. Jignesh S Bhatt", "Thu", "09:30", "11:00", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s4_th3", "CS206", "Theory of Computation", "ICD-2118", "Dr. Gaurav Pareek", "Thu", "11:15", "12:45", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s4_th4", "EC262", "Analog Communication Lab", "ICD-2222-EL", "Dr. Jignesh S Bhatt", "Thu", "14:00", "16:00", "Lab", false, null, "ECE"),

                // Friday
                new ClassOccurrence("s4_f1", "CS204", "Operating Systems", "ICD-2118", "Dr. Deepika Gupta", "Fri", "09:30", "11:00", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s4_f2", "HS202", "Economics for Engineers", "ICD-2118", "Dr. Barnali Chetia", "Fri", "11:15", "12:45", "Lecture", false, null, "All"),
                new ClassOccurrence("s4_f3", "CS202", "DBMS Tutorial", "ICD-2118", "Dr. Venkata Phanikrishna", "Fri", "14:00", "15:30", "Tutorial", false, null, "CSE/AI")
        );
    }

    private List<ClassOccurrence> getSem6Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s6_m1", "CS302", "Software Engineering", "ICD-2125", "Dr. Karan Verma", "Mon", "11:15", "12:45", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s6_m2", "CS304", "Compiler Design", "ICD-2125", "Dr. Gaurav Pareek", "Mon", "14:00", "15:30", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s6_m3", "EC302", "Digital Signal Processing", "ICD-2118", "Dr. Jignesh S Bhatt", "Mon", "14:00", "15:30", "Lecture", false, null, "ECE"),
                new ClassOccurrence("s6_m4", "CS362", "Compiler Design Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Mon", "16:00", "18:00", "Lab", false, null, "CSE"),

                // Tuesday
                new ClassOccurrence("s6_t1", "CS306", "Cloud Computing & Distributed Systems", "ICD-2125", "Dr. Abhisek Paul", "Tue", "11:15", "12:45", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s6_t2", "CS308", "Machine Learning", "ICD-2125", "Dr. Deepika Gupta", "Tue", "14:00", "15:30", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s6_t3", "EC362", "Digital Signal Processing Lab", "ICD-2221-EL", "Dr. Jignesh S Bhatt", "Tue", "16:00", "18:00", "Lab", false, null, "ECE"),

                // Wednesday
                new ClassOccurrence("s6_w1", "CS302", "Software Engineering", "ICD-2125", "Dr. Karan Verma", "Wed", "11:15", "12:45", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s6_w2", "CS304", "Compiler Design", "ICD-2125", "Dr. Gaurav Pareek", "Wed", "14:00", "15:30", "Lecture", false, null, "CSE"),
                new ClassOccurrence("s6_w3", "CS364", "Software Engineering Lab", "ICD-2104-CS", "Dr. Karan Verma", "Wed", "16:00", "18:00", "Lab", false, null, "CSE"),

                // Thursday
                new ClassOccurrence("s6_th1", "CS306", "Cloud Computing & Distributed Systems", "ICD-2125", "Dr. Abhisek Paul", "Thu", "11:15", "12:45", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s6_th2", "CS308", "Machine Learning", "ICD-2125", "Dr. Deepika Gupta", "Thu", "14:00", "15:30", "Lecture", false, null, "CSE/AI"),
                new ClassOccurrence("s6_th3", "CS368", "Machine Learning Lab", "ICD-2105-CS", "Dr. Deepika Gupta", "Thu", "16:00", "18:00", "Lab", false, null, "CSE/AI"),

                // Friday
                new ClassOccurrence("s6_f1", "HS302", "Technology, Law and Ethics", "ICD-2125", "Dr. Barnali Chetia", "Fri", "11:15", "12:45", "Lecture", false, null, "All"),
                new ClassOccurrence("s6_f2", "CS390", "B.Tech Mini-Project Review", "ICD-2109", "Dr. Venkata Phanikrishna", "Fri", "14:00", "16:00", "Lab", false, null, "All")
        );
    }

    private List<ClassOccurrence> getMcaTimetable() {
        return List.of(
                // Monday
                new ClassOccurrence("mca_m1", "MS441", "Discrete Mathematics", "ICD-2127", "Dr. Abhishek Kesarwani", "Mon", "08:00", "09:30", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_m2", "MS541", "Probability & Statistics", "ICD-2231", "Dr. Mukesh Singh", "Mon", "08:00", "09:30", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_m3", "CS547", "Computer Networks", "ICD-2231", "Dr. Abhisek Paul", "Mon", "09:30", "11:00", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_m4", "MS541", "Probability & Statistics Tutorial", "ICD-2231", "Dr. Mukesh Singh", "Mon", "11:15", "12:15", "Tutorial", false, null, "MCA-III"),
                new ClassOccurrence("mca_m5", "EC441", "Digital Designs and Architecture Lab", "ICD-2127", "Dr. Uday Panwar", "Mon", "13:15", "15:15", "Lab", false, null, "MCA-I"),
                new ClassOccurrence("mca_m6", "CS541", "Design & Analysis of Algorithms Lab", "ICD-2231", "Dr. Venkata Phanikrishna", "Mon", "13:15", "15:15", "Lab", false, null, "MCA-III"),

                // Tuesday
                new ClassOccurrence("mca_t1", "CS441", "Computer Programming & Problem Solving", "ICD-2127", "Dr. Sanjay Saxena", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_t2", "CS541", "Design & Analysis of Algorithms", "ICD-2231", "Dr. Venkata Phanikrishna", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_t3", "HS541", "Science, Technology and Society", "ICD-2231", "Dr. Vikas Kumar", "Tue", "09:30", "11:00", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_t4", "CS445", "Basics of Web Programming", "ICD-2127", "Dr. Radhika Lama & Dr. Pramit Mazumder", "Tue", "09:30", "11:30", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_t5", "CS547", "Computer Networks Lab", "ICD-2231", "Dr. Abhisek Paul", "Tue", "11:15", "13:15", "Lab", false, null, "MCA-III"),
                new ClassOccurrence("mca_t6", "CS445", "Web Programming Lab", "ICD-2127", "Dr. Radhika Lama & Dr. Pramit Mazumder", "Tue", "13:00", "15:00", "Lab", false, null, "MCA-I"),

                // Wednesday
                new ClassOccurrence("mca_w1", "CS443", "System Administration & Maintenance", "ICD-2127", "Dr. Ravi Nahta", "Wed", "08:00", "09:30", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_w2", "CS543", "Web Services", "ICD-2231", "Dr. Pramit Mazumder", "Wed", "08:00", "10:00", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_w3", "MS441", "Discrete Mathematics", "ICD-2127", "Dr. Abhishek Kesarwani", "Wed", "09:30", "11:00", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_w4", "MS541", "Probability & Statistics", "ICD-2231", "Dr. Mukesh Singh", "Wed", "10:00", "11:30", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_w5", "CS443", "System Administration Lab", "ICD-2127", "Dr. Ravi Nahta", "Wed", "11:15", "13:15", "Lab", false, null, "MCA-I"),
                new ClassOccurrence("mca_w6", "CS543", "Web Services Tutorial", "ICD-2231", "Dr. Pramit Mazumder", "Wed", "11:30", "12:30", "Tutorial", false, null, "MCA-III"),
                new ClassOccurrence("mca_w7", "CS543", "Web Services Lab", "ICD-2231", "Dr. Pramit Mazumder", "Wed", "13:15", "15:15", "Lab", false, null, "MCA-III"),
                new ClassOccurrence("mca_w8", "EC441", "Digital Designs & Architecture", "ICD-2113", "Dr. Uday Panwar", "Wed", "15:30", "17:00", "Lecture", false, null, "MCA-I"),

                // Thursday
                new ClassOccurrence("mca_th1", "CS441", "Computer Programming", "ICD-2127", "Dr. Sanjay Saxena", "Thu", "08:00", "09:30", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_th2", "CS547", "Computer Networks", "ICD-2231", "Dr. Abhisek Paul", "Thu", "08:00", "09:30", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_th3", "EC441", "Digital Designs", "ICD-2125", "Dr. Uday Panwar", "Thu", "09:30", "11:00", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_th4", "CS541", "Design & Analysis of Algorithms", "ICD-2231", "Dr. Venkata Phanikrishna", "Thu", "09:30", "11:00", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_th5", "CS441", "Programming Lab", "ICD-2127", "Dr. Sanjay Saxena", "Thu", "11:15", "12:15", "Lab", false, null, "MCA-I"),
                new ClassOccurrence("mca_th6", "CS545", "Software Engineering Tutorial", "ICD-2231", "Dr. Rishabh Gupta", "Thu", "11:15", "12:15", "Tutorial", false, null, "MCA-III"),
                new ClassOccurrence("mca_th7", "CS441", "Programming Lab", "ICD-2127", "Dr. Sanjay Saxena", "Thu", "13:00", "15:00", "Lab", false, null, "MCA-I"),
                new ClassOccurrence("mca_th8", "CS545", "Software Engineering Lab", "ICD-2231", "Dr. Rishabh Gupta", "Thu", "13:00", "15:00", "Lab", false, null, "MCA-III"),

                // Friday
                new ClassOccurrence("mca_f1", "HS441", "Spoken & Written Communication", "ICD-2127", "Dr. Amal Tom", "Fri", "08:00", "09:30", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_f2", "HS541", "Science, Tech & Society", "ICD-2231", "Dr. Vikas Kumar", "Fri", "08:00", "09:30", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_f3", "CS443", "System Administration", "ICD-2127", "Dr. Ravi Nahta", "Fri", "09:30", "11:00", "Lecture", false, null, "MCA-I"),
                new ClassOccurrence("mca_f4", "CS545", "Software Engineering", "ICD-2231", "Dr. Rishabh Gupta", "Fri", "09:30", "11:30", "Lecture", false, null, "MCA-III"),
                new ClassOccurrence("mca_f5", "MS441", "Discrete Mathematics Tutorial", "ICD-2231", "Dr. Abhishek Kesarwani", "Fri", "11:30", "12:30", "Tutorial", false, null, "MCA-I"),
                new ClassOccurrence("mca_f6", "MS541", "Probability & Statistics Tutorial", "ICD-2127", "Dr. Mukesh Singh", "Fri", "13:00", "14:00", "Tutorial", false, null, "MCA-III"),
                new ClassOccurrence("mca_f7", "HS441", "Spoken Communication Lab", "ICD-2231", "Dr. Amal Tom", "Fri", "13:00", "15:00", "Lab", false, null, "MCA-I")
        );
    }

    private List<ClassOccurrence> getSem8Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("s8_m1", "CS404", "Distributed Systems & Algorithms", "ICD-2127", "Dr. Abhisek Paul", "Mon", "09:30", "11:00", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s8_m2", "CS499", "B.Tech Major Project Phase-II", "ICD-2108", "Dr. Karan Verma", "Mon", "14:00", "17:00", "Lab", false, null, "All"),

                // Tuesday
                new ClassOccurrence("s8_t1", "CS406", "Information Retrieval & Web Search", "ICD-2127", "Dr. Radhika Lama", "Tue", "09:30", "11:00", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s8_t2", "CS499", "B.Tech Major Project Phase-II", "ICD-2109", "Dr. Venkata Phanikrishna", "Tue", "14:00", "17:00", "Lab", false, null, "All"),

                // Wednesday
                new ClassOccurrence("s8_w1", "CS404", "Distributed Systems & Algorithms", "ICD-2127", "Dr. Abhisek Paul", "Wed", "09:30", "11:00", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s8_w2", "CS464", "Distributed Systems Lab", "ICD-2102-CS", "Dr. Abhisek Paul", "Wed", "14:00", "16:00", "Lab", false, null, "CSE/IT"),

                // Thursday
                new ClassOccurrence("s8_th1", "CS406", "Information Retrieval & Web Search", "ICD-2127", "Dr. Radhika Lama", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE/IT"),
                new ClassOccurrence("s8_th2", "CS499", "Major Project Progress Review", "ICD-2127", "Faculty Advisory Committee", "Thu", "14:00", "17:00", "Lab", false, null, "All"),

                // Friday
                new ClassOccurrence("s8_f1", "HS402", "Entrepreneurship & IP Rights", "ICD-2127", "Dr. Barnali Chetia", "Fri", "10:00", "12:00", "Lecture", false, null, "All"),
                new ClassOccurrence("s8_f2", "CS499", "Industry Seminars & Project Mentorship", "ICD-2127", "Dr. Gaurav Pareek", "Fri", "14:00", "17:00", "Lab", false, null, "All")
        );
    }

    private List<ClassOccurrence> getMca1Timetable() {
        return getMcaTimetable().stream()
                .filter(occ -> "MCA-I".equalsIgnoreCase(occ.branch()))
                .toList();
    }

    private List<ClassOccurrence> getMca2Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("mca2_m1", "CS442", "Data Structures & Algorithms", "ICD-2127", "Dr. Deepika Gupta", "Mon", "09:30", "11:00", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_m2", "CS442", "Data Structures Lab", "ICD-2127", "Dr. Deepika Gupta", "Mon", "13:15", "15:15", "Lab", false, null, "MCA-II"),

                // Tuesday
                new ClassOccurrence("mca2_t1", "CS444", "Database Management Systems", "ICD-2127", "Dr. Venkata Phanikrishna", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_t2", "MS442", "Optimization Techniques", "ICD-2127", "Dr. Radhika Lama", "Tue", "09:30", "11:00", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_t3", "CS444", "DBMS Lab", "ICD-2127", "Dr. Venkata Phanikrishna", "Tue", "13:00", "15:00", "Lab", false, null, "MCA-II"),

                // Wednesday
                new ClassOccurrence("mca2_w1", "CS446", "Computer Networks", "ICD-2127", "Dr. Abhisek Paul", "Wed", "08:00", "09:30", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_w2", "CS442", "Data Structures & Algorithms", "ICD-2127", "Dr. Deepika Gupta", "Wed", "09:30", "11:00", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_w3", "CS446", "Computer Networks Lab", "ICD-2127", "Dr. Abhisek Paul", "Wed", "11:15", "13:15", "Lab", false, null, "MCA-II"),

                // Thursday
                new ClassOccurrence("mca2_th1", "CS444", "Database Management Systems", "ICD-2127", "Dr. Venkata Phanikrishna", "Thu", "08:00", "09:30", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_th2", "MS442", "Optimization Techniques", "ICD-2127", "Dr. Radhika Lama", "Thu", "09:30", "11:00", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_th3", "CS446", "Computer Networks Tutorial", "ICD-2127", "Dr. Abhisek Paul", "Thu", "11:15", "12:15", "Tutorial", false, null, "MCA-II"),

                // Friday
                new ClassOccurrence("mca2_f1", "CS446", "Computer Networks", "ICD-2127", "Dr. Abhisek Paul", "Fri", "08:00", "09:30", "Lecture", false, null, "MCA-II"),
                new ClassOccurrence("mca2_f2", "HS442", "Organizational Behavior", "ICD-2127", "Dr. Barnali Chetia", "Fri", "11:30", "13:00", "Lecture", false, null, "MCA-II")
        );
    }

    private List<ClassOccurrence> getMca3Timetable() {
        return getMcaTimetable().stream()
                .filter(occ -> "MCA-III".equalsIgnoreCase(occ.branch()))
                .toList();
    }

    private List<ClassOccurrence> getMca4Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("mca4_m1", "CS542", "Cloud Infrastructure & DevOps", "ICD-2231", "Dr. Abhisek Paul", "Mon", "09:30", "11:00", "Lecture", false, null, "MCA-IV"),
                new ClassOccurrence("mca4_m2", "CS542", "Cloud Infrastructure Lab", "ICD-2231", "Dr. Abhisek Paul", "Mon", "13:15", "15:15", "Lab", false, null, "MCA-IV"),

                // Tuesday
                new ClassOccurrence("mca4_t1", "CS544", "Cyber Security & Forensics", "ICD-2231", "Dr. Naveen Kumar", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA-IV"),
                new ClassOccurrence("mca4_t2", "CS544", "Cyber Security Lab", "ICD-2231", "Dr. Naveen Kumar", "Tue", "13:00", "15:00", "Lab", false, null, "MCA-IV"),

                // Wednesday
                new ClassOccurrence("mca4_w1", "CS546", "Mobile Application Development", "ICD-2231", "Dr. Gaurav Pareek", "Wed", "08:00", "09:30", "Lecture", false, null, "MCA-IV"),
                new ClassOccurrence("mca4_w2", "CS542", "Cloud Infrastructure & DevOps", "ICD-2231", "Dr. Abhisek Paul", "Wed", "09:30", "11:00", "Lecture", false, null, "MCA-IV"),
                new ClassOccurrence("mca4_w3", "CS546", "Mobile Apps Lab", "ICD-2231", "Dr. Gaurav Pareek", "Wed", "11:15", "13:15", "Lab", false, null, "MCA-IV"),

                // Thursday
                new ClassOccurrence("mca4_th1", "CS544", "Cyber Security & Forensics", "ICD-2231", "Dr. Naveen Kumar", "Thu", "08:00", "09:30", "Lecture", false, null, "MCA-IV"),
                new ClassOccurrence("mca4_th2", "CS599", "MCA Major Project Mentorship", "ICD-2231", "Dr. Karan Verma", "Thu", "13:00", "16:00", "Lab", false, null, "MCA-IV"),

                // Friday
                new ClassOccurrence("mca4_f1", "CS546", "Mobile Application Development", "ICD-2231", "Dr. Gaurav Pareek", "Fri", "08:00", "09:30", "Lecture", false, null, "MCA-IV"),
                new ClassOccurrence("mca4_f2", "CS599", "MCA Project Defense & Seminar", "ICD-2231", "Dr. Karan Verma", "Fri", "13:00", "16:00", "Lab", false, null, "MCA-IV")
        );
    }

    private List<ClassOccurrence> getMtech1Timetable() {
        return getMtechTimetable();
    }

    private List<ClassOccurrence> getMtech2Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("mt2_m1", "CS602", "Advanced Computer Networks & IoT", "ICD-2125", "Dr. Abhisek Paul", "Mon", "09:30", "11:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt2_m2", "CS602", "Advanced Networks Lab", "ICD-2118", "Dr. Abhisek Paul", "Mon", "15:15", "17:15", "Lab", false, null, "M.Tech CSE"),

                // Tuesday
                new ClassOccurrence("mt2_t1", "CS604", "Deep Learning for Computer Vision", "ICD-2125", "Dr. Jignesh S Bhatt", "Tue", "09:30", "11:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt2_t2", "CS604", "Computer Vision Lab", "ICD-2118", "Dr. Jignesh S Bhatt", "Tue", "15:15", "17:15", "Lab", false, null, "M.Tech CSE"),

                // Wednesday
                new ClassOccurrence("mt2_w1", "CS602", "Advanced Computer Networks & IoT", "ICD-2108", "Dr. Abhisek Paul", "Wed", "09:30", "11:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt2_w2", "CS606", "Information Security & Cryptography", "ICD-2125", "Dr. Gaurav Pareek", "Wed", "11:15", "12:45", "Lecture", false, null, "M.Tech CSE"),

                // Thursday
                new ClassOccurrence("mt2_th1", "CS604", "Deep Learning for Computer Vision", "ICD-2118", "Dr. Jignesh S Bhatt", "Thu", "09:30", "11:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt2_th2", "CS606", "Information Security Lab", "ICD-2118", "Dr. Gaurav Pareek", "Thu", "15:15", "17:15", "Lab", false, null, "M.Tech CSE"),

                // Friday
                new ClassOccurrence("mt2_f1", "CS606", "Information Security & Cryptography", "ICD-2109", "Dr. Gaurav Pareek", "Fri", "11:15", "12:45", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt2_f2", "CS662", "Advanced Computing Research Lab", "ICD-2118", "Dr. Venkata Phanikrishna", "Fri", "14:00", "17:00", "Lab", false, null, "M.Tech CSE")
        );
    }

    private List<ClassOccurrence> getMtech34Timetable() {
        return List.of(
                // Monday
                new ClassOccurrence("mt34_m1", "CS699", "M.Tech Dissertation & Research Phase-I/II", "ICD-2108", "Faculty Advisory Committee", "Mon", "09:30", "13:00", "Lab", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt34_m2", "CS699", "Research Lab & Simulation", "ICD-2108", "Dr. Abhisek Paul", "Mon", "14:00", "17:00", "Lab", false, null, "M.Tech CSE"),

                // Tuesday
                new ClassOccurrence("mt34_t1", "CS699", "Research Seminar & Topic Review", "ICD-2109", "Dr. Gaurav Pareek", "Tue", "10:00", "13:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt34_t2", "CS699", "Experimental Validation", "ICD-2118", "Dr. Deepika Gupta", "Tue", "14:00", "17:00", "Lab", false, null, "M.Tech CSE"),

                // Wednesday
                new ClassOccurrence("mt34_w1", "CS699", "Thesis Progress Evaluation", "ICD-2125", "Faculty Committee", "Wed", "10:00", "13:00", "Lecture", false, null, "M.Tech CSE"),

                // Thursday
                new ClassOccurrence("mt34_th1", "CS699", "M.Tech Research Lab", "ICD-2108", "Dr. Jignesh S Bhatt", "Thu", "09:30", "13:00", "Lab", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt34_th2", "CS699", "Dissertation Writing & Defense Prep", "ICD-2127", "Faculty Advisory Committee", "Thu", "14:00", "17:00", "Lecture", false, null, "M.Tech CSE"),

                // Friday
                new ClassOccurrence("mt34_f1", "CS699", "Departmental Colloquium", "ICD-2125", "Dr. Venkata Phanikrishna", "Fri", "14:00", "17:00", "Lecture", false, null, "M.Tech CSE")
        );
    }

    private List<ClassOccurrence> getMtechTimetable() {
        return List.of(
                // Monday
                new ClassOccurrence("mt_m1", "MA603", "Linear Algebra and Matrices", "ICD-2125", "Dr. Pranjal Srivastava", "Mon", "09:30", "11:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_m2", "MA601", "Probability Statistics and Random Processes", "ICD-2125", "Dr. Suvam Singh", "Mon", "11:15", "13:15", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_m3", "CS601", "Advanced Algorithms Lab", "ICD-2118", "Dr. Gaurav Pareek", "Mon", "15:15", "17:15", "Lab", false, null, "M.Tech CSE"),

                // Tuesday
                new ClassOccurrence("mt_t1", "CS601", "Advanced Algorithms", "ICD-2125", "Dr. Gaurav Pareek", "Tue", "13:45", "15:15", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_t2", "CS603", "Data Analytics Lab", "ICD-2118", "Dr. Rahul Maurya", "Tue", "15:15", "17:15", "Lab", false, null, "M.Tech CSE"),

                // Wednesday
                new ClassOccurrence("mt_w1", "MA603", "Linear Algebra and Matrices", "ICD-2108", "Dr. Pranjal Srivastava", "Wed", "09:30", "11:00", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_w2", "MA601", "Probability Statistics and Random Processes", "ICD-2113", "Dr. Suvam Singh", "Wed", "13:45", "15:15", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_w3", "CS605", "Artificial Intelligence (PE2)", "ICD-2125", "Dr. Deepika Gupta", "Wed", "15:15", "16:45", "Lecture", false, null, "M.Tech CSE"),

                // Thursday
                new ClassOccurrence("mt_th1", "CS605", "Artificial Intelligence Lab", "ICD-2118", "Dr. Deepika Gupta", "Thu", "15:15", "17:15", "Lab", false, null, "M.Tech CSE"),

                // Friday
                new ClassOccurrence("mt_f1", "CS603", "Data Analytics", "ICD-2109", "Dr. Rahul Maurya", "Fri", "09:30", "11:30", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_f2", "CS601", "Advanced Algorithms", "ICD-2125", "Dr. Gaurav Pareek", "Fri", "11:15", "12:45", "Lecture", false, null, "M.Tech CSE"),
                new ClassOccurrence("mt_f3", "CS605", "Artificial Intelligence (PE2)", "ICD-2125", "Dr. Deepika Gupta", "Fri", "16:45", "18:15", "Lecture", false, null, "M.Tech CSE")
        );
    }

    @Override
    public List<Task> getPendingTasks() {
        return List.of(
                new Task(
                        "t1",
                        "OS Assignment 3: POSIX Sync",
                        "CS302",
                        "Due tomorrow 10:00",
                        "4 h",
                        "CRITICAL",
                        false,
                        1,
                        "Implement multi-threaded producer-consumer problem with POSIX semaphores and mutexes. Verify thread safety under heavy race conditions.",
                        List.of(
                                "Design bounded buffer shared memory queue",
                                "Implement semaphore wait/post synchronization",
                                "Run stress test with 10 concurrent worker threads",
                                "Generate Gantt execution timeline and summary PDF"
                        )
                ),
                new Task(
                        "t2",
                        "CN Wireshark Packet Report",
                        "CS303",
                        "Due Tuesday 18:00",
                        "3 h",
                        "HIGH",
                        false,
                        2,
                        "Analyze captured Wireshark pcap trace for TCP 3-way handshake, sequence numbers, and retransmission window sizing.",
                        List.of(
                                "Filter TCP stream in Wireshark",
                                "Annotate SYN, SYN-ACK, and ACK frames",
                                "Calculate RTT and window size progression",
                                "Write analysis conclusions in report"
                        )
                ),
                new Task(
                        "t3",
                        "DBMS Normalization Practice",
                        "CS304",
                        "Due Friday 23:59",
                        "2 h",
                        "NORMAL",
                        false,
                        3,
                        "Decompose given relation schemas into BCNF and 3NF while preserving all non-trivial functional dependencies.",
                        List.of(
                                "Compute attribute closures and candidate keys",
                                "Identify BCNF violation dependencies",
                                "Construct dependency-preserving 3NF decomposition",
                                "Verify lossless-join property"
                        )
                ),
                new Task(
                        "t4",
                        "DS Balanced Trees Review",
                        "CS301",
                        "Due Saturday 14:00",
                        "3 h",
                        "NORMAL",
                        false,
                        4,
                        "Revise AVL tree single and double rotations, balance factors, and Red-Black tree insertion re-coloring properties.",
                        List.of(
                                "Review LL, RR, LR, RL rotation steps",
                                "Solve practice insertion test cases",
                                "Verify height balance guarantees"
                        )
                )
        );
    }

    private final List<Announcement> mockAnnouncements = new ArrayList<>(List.of(
            new Announcement(
                    "a1",
                    "Mid-Sem Examination Schedule Published – Autumn 2026",
                    "Exams",
                    "Today, 10 Nov 2026",
                    "Examination Cell",
                    "The timetable for Mid-Semester Examinations for Autumn 2026 has been published. Examinations commence on 20 November 2026. Hall tickets and seating arrangements will be available 48 hours prior to each paper.",
                    true,
                    false,
                    "MidSem_Autumn_2026_Schedule.pdf",
                    "340 KB"
            ),
            new Announcement(
                    "a2",
                    "B.Tech Capstone Project Phase 1 Submission Deadline Extended",
                    "Academic",
                    "09 Nov 2026",
                    "Academic Affairs Office",
                    "Upon request from the student academic council, the deadline for Phase 1 code submission and SRS documentation has been extended to 18 November 2026, 23:59 IST.",
                    true,
                    false,
                    "Capstone_Phase1_Guidelines_v2.pdf",
                    "520 KB"
            ),
            new Announcement(
                    "a3",
                    "Placement Notice: Summer 2027 Pre-Placement Talks (Google & Microsoft)",
                    "Placement",
                    "08 Nov 2026",
                    "Training & Placement Cell",
                    "Pre-placement talks and online assessment rounds for B.Tech CSE Semester V students will be held in the Auditorium from 15 November onwards. Verified resumes must be uploaded on the portal by 13 Nov.",
                    false,
                    false,
                    "PPT_Schedule_Syllabus.pdf",
                    "180 KB"
            ),
            new Announcement(
                    "a4",
                    "Hostel Maintenance & Core Fiber Network Upgrade",
                    "Administrative",
                    "06 Nov 2026",
                    "Hostel Administration",
                    "Scheduled core switch replacement and fiber optic maintenance will take place between 01:00 and 05:00 on Wednesday, 12 Nov. LAN and Wi-Fi services will face intermittent disruption.",
                    false,
                    true,
                    null,
                    null
            ),
            new Announcement(
                    "a5",
                    "Guest Lecture: Generative AI and Systems Architecture",
                    "Events",
                    "04 Nov 2026",
                    "Department of CSE",
                    "Distinguished lecture on efficient inference engines, speculative decoding, and scalable GPU clusters by Dr. Rajesh Sharma (Director of Systems Research, IBM Labs).",
                    false,
                    true,
                    "Guest_Lecture_Abstract.pdf",
                    "210 KB"
            ),
            new Announcement(
                    "a6",
                    "Course Registration for Spring 2027 Electives",
                    "Academic",
                    "01 Nov 2026",
                    "Academic Affairs Office",
                    "Elective choice allocation for Semester VI will open on 25 November. Detailed syllabi and faculty allocations are available in the academic handbook.",
                    false,
                    true,
                    "Electives_Catalog_Spring2027.pdf",
                    "680 KB"
            )
    ));

    @Override
    public List<Announcement> getAnnouncements() {
        return Collections.unmodifiableList(mockAnnouncements);
    }

    @Override
    public void markAllAnnouncementsRead() {
        for (int i = 0; i < mockAnnouncements.size(); i++) {
            Announcement a = mockAnnouncements.get(i);
            if (!a.isRead()) {
                mockAnnouncements.set(i, new Announcement(
                        a.id(), a.title(), a.category(), a.date(), a.publisher(),
                        a.summary(), a.isPinned(), true, a.attachmentName(), a.attachmentSize()
                ));
            }
        }
    }

    @Override
    public void markAnnouncementRead(String id) {
        for (int i = 0; i < mockAnnouncements.size(); i++) {
            Announcement a = mockAnnouncements.get(i);
            if (a.id().equalsIgnoreCase(id)) {
                mockAnnouncements.set(i, new Announcement(
                        a.id(), a.title(), a.category(), a.date(), a.publisher(),
                        a.summary(), a.isPinned(), true, a.attachmentName(), a.attachmentSize()
                ));
                break;
            }
        }
    }

    private final List<Event> mockEvents = new ArrayList<>(List.of(
            // TODAY
            new Event(
                    "e1",
                    "Hands-on Rust Systems Workshop",
                    "Technical Workshop",
                    "Today, 10 Nov 2026",
                    "10 Nov 2026",
                    "15:00 – 17:00",
                    "ICD-2102-CS (Computer Lab 1)",
                    "Coding Club & IEEE",
                    "Deep dive into memory safety, ownership, borrow checker semantics, and concurrency with hands-on systems exercises in Rust.",
                    Event.Timeframe.TODAY,
                    true,
                    "Clashes with CS303 Computer Networks Lecture (15:15 – 16:45, Room ICD-2125)",
                    true,
                    68,
                    80
            ),
            new Event(
                    "e2",
                    "Alumni AMA: Distributed Systems at Scale",
                    "Tech Talk",
                    "Today, 10 Nov 2026",
                    "10 Nov 2026",
                    "17:30 – 19:00",
                    "Seminar Hall 1",
                    "Placement & Training Cell",
                    "Interactive session with IIIT Vadodara alumni working at Google and Uber on designing high-throughput consensus protocols.",
                    Event.Timeframe.TODAY,
                    false,
                    null,
                    false,
                    114,
                    150
            ),
            // THIS WEEK
            new Event(
                    "e3",
                    "CodeGolf & Speed Algorithmics Round 3",
                    "Competitive Programming",
                    "Thu, 12 Nov 2026",
                    "12 Nov 2026",
                    "16:00 – 18:00",
                    "Lab-1",
                    "Algorithmics Chapter",
                    "Timed algorithmic sprint solving optimization problems with shortest source length and optimal complexity bounds.",
                    Event.Timeframe.THIS_WEEK,
                    true,
                    "Clashes with CS302 Extra Lecture (14:00 – 15:30, Room B-102)",
                    false,
                    92,
                    120
            ),
            new Event(
                    "e4",
                    "Technical Symposium: Cerebro 2026 Keynote",
                    "Symposium",
                    "Fri, 13 Nov 2026",
                    "13 Nov 2026",
                    "09:30 – 13:00",
                    "Auditorium A-101",
                    "Technical Affairs Committee",
                    "Inaugural keynote on Quantum Machine Learning, Edge AI acceleration, and undergraduate research showcase.",
                    Event.Timeframe.THIS_WEEK,
                    false,
                    null,
                    true,
                    280,
                    350
            ),
            // NEXT 30 DAYS
            new Event(
                    "e5",
                    "HackIIITV 2026: 36-Hour Hackathon",
                    "Hackathon",
                    "21–22 Nov 2026",
                    "21–22 Nov 2026",
                    "10:00 (21 Nov) – 22:00 (22 Nov)",
                    "Academic Block B & Labs",
                    "Developer Student Clubs",
                    "Annual flagship inter-college hackathon with tracks in Web3, AI Agents, HealthTech, and Open Source Innovation.",
                    Event.Timeframe.NEXT_30_DAYS,
                    false,
                    null,
                    true,
                    410,
                    500
            ),
            new Event(
                    "e6",
                    "Annual Inter-Batch Sports Meet (Kreeda 2026)",
                    "Sports",
                    "28–30 Nov 2026",
                    "28–30 Nov 2026",
                    "08:00 – 18:00",
                    "Campus Sports Ground & Arena",
                    "Sports Committee",
                    "Football, Cricket, Badminton, Table Tennis, and Athletics tournament across all CSE and IT batches.",
                    Event.Timeframe.NEXT_30_DAYS,
                    false,
                    null,
                    false,
                    185,
                    300
            )
    ));

    @Override
    public List<Event> getUpcomingEvents() {
        return Collections.unmodifiableList(mockEvents);
    }

    @Override
    public List<Event> getEventsByTimeframe(Event.Timeframe timeframe) {
        if (timeframe == null) {
            return getUpcomingEvents();
        }
        return mockEvents.stream()
                .filter(e -> e.timeframe() == timeframe)
                .toList();
    }

    @Override
    public boolean toggleEventRsvp(String eventId) {
        for (int i = 0; i < mockEvents.size(); i++) {
            Event e = mockEvents.get(i);
            if (e.id().equalsIgnoreCase(eventId)) {
                boolean newRsvp = !e.isRsvp();
                int newCount = newRsvp ? e.rsvpCount() + 1 : Math.max(0, e.rsvpCount() - 1);
                Event updated = new Event(
                        e.id(), e.title(), e.category(), e.date(), e.dateRange(), e.timeRange(),
                        e.location(), e.organizer(), e.description(), e.timeframe(),
                        e.hasTimetableClash(), e.clashDetails(), newRsvp, newCount, e.capacity()
                );
                mockEvents.set(i, updated);
                return newRsvp;
            }
        }
        return false;
    }

    @Override
    public MealMenu getTodayMealMenu() {
        return new MealMenu(
                "Tue, 10 Nov 2026",
                "Lunch",
                true,
                "Bhindi do pyaza, Masoor masala, Chana dal, Steem rice, Roti, Mix pickle, Curd",
                "Next: Snacks 16:30 (Aloo Matar Sandwich)"
        );
    }

    @Override
    public List<WeeklyMealDay> getWeeklyMealMenu() {
        return getWeek2MealMenu(); // Current week: 09 Nov – 15 Nov 2026
    }

    @Override
    public List<WeeklyMealDay> getWeeklyMealMenu(String weekIdentifier) {
        if (weekIdentifier != null && (weekIdentifier.contains("1") || weekIdentifier.toLowerCase().contains("week 1"))) {
            return getWeek1MealMenu();
        }
        return getWeek2MealMenu();
    }

    private List<WeeklyMealDay> getWeek1MealMenu() {
        return List.of(
                new WeeklyMealDay(
                        "Monday",
                        "02 Nov",
                        false,
                        "Uttapam, Coconut chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon, Boiled egg",
                        "Aloo gobhi, Rajma masala, Dal fry, Rice, Roti, Buttermilk, Mix salad",
                        "Bhajiya, Green chutney, Sweet chutney, Tea",
                        "Dal fry, Mix veg, Rice papad, Roti, Mix salad, Rice, Rice kheer",
                        "Special: Rice kheer & Boiled egg"
                ),
                new WeeklyMealDay(
                        "Tuesday",
                        "03 Nov",
                        false,
                        "Vermicelli, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon, Omelette",
                        "Cabbage tomato, Mutter makhana, Mix dal, Butter rice, Roti, Mix pickle, Curd, Beet salad",
                        "Chinese bhel, Tea",
                        "Aloo chana, Dal fry, Steem rice, Roti, Mix pickle, Boondi raita, Gulab jamun",
                        "Special: Gulab jamun & Omelette"
                ),
                new WeeklyMealDay(
                        "Wednesday",
                        "04 Nov",
                        false,
                        "Dal pakwan, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Banana",
                        "Paneer tikka masala, Egg fried rice, Dal fry, Plain rice, Roti, Mix pickle, Onion salad, Jalebi",
                        "Bombay bhel, Tea",
                        "Bhindi do pyaza, Moong dal badi, Mix dal, Jeera rice, Roti, Mix pickle, Onion salad",
                        "Special: Paneer tikka, Egg fried rice & Jalebi"
                ),
                new WeeklyMealDay(
                        "Thursday",
                        "05 Nov",
                        false,
                        "Puri sabji, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon, Omelette",
                        "Dudhi dry, Chana masala, Tadka dal, Jeera rice, Roti, Mix pickle, Buttermilk, Mix salad",
                        "Ragda patties, Sweet chutney, Tea",
                        "Veg jaipuri, Veg biryani, Roti, Mix pickle, Dahi tikari, Pulses salad, Mohanthal",
                        "Special: Veg biryani, Mohanthal & Omelette"
                ),
                new WeeklyMealDay(
                        "Friday",
                        "06 Nov",
                        false,
                        "Idli sambhar, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon",
                        "Mutter paneer, Chicken masala, Dal fry, Plain rice, Roti, Mix pickle, Onion salad",
                        "Pani puri, Ragda, Teekha paani, Sweet pani, Tea",
                        "Aloo parwal, Besan gatta, Chana dal, Steam rice, Roti, Beet salad, Mix pickle, Shahi tukda",
                        "Special: Chicken masala / Mutter paneer & Shahi tukda"
                ),
                new WeeklyMealDay(
                        "Saturday",
                        "07 Nov",
                        false,
                        "Aloo paneer paratha, Green chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Banana, Egg bhurji",
                        "Veg kolhapuri, Sabut masoor dal, Steem rice, Roti, Mix pickle, Butter milk, Pulses salad",
                        "Maggi, Ketchup, Tea",
                        "Chole, Poori, Steam rice, Mint raita, Mix pickle, Fry mirchi",
                        "Special: Aloo paneer paratha, Egg bhurji & Poori chole"
                ),
                new WeeklyMealDay(
                        "Sunday",
                        "08 Nov",
                        false,
                        "Aloo pyaz kachori, Green chutney, Sweet chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder",
                        "Mix veg, Dal fry, Steam rice, Roti, Onion salad, Mix pickle, Kala jamun",
                        "Idra, Lesun chutney, Tea",
                        "Aloo tomato dry, Rajma masala, Dal fry, Steam rice, Roti, Onion salad, Mix pickle, Curd",
                        "Special: Aloo pyaz kachori & Kala jamun"
                )
        );
    }

    private List<WeeklyMealDay> getWeek2MealMenu() {
        return List.of(
                new WeeklyMealDay(
                        "Monday",
                        "09 Nov",
                        false,
                        "Moong dal chilla, Green chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon, Boiled egg",
                        "White choli, Aloo palak, Tadka dal, Roti, Jeera rice, Mix salad, Mix pickle, Buttermilk",
                        "Ragda patties, Sweet chutney, Tea",
                        "Chana dal, Corn capsicum, Rice, Roti, Mix pickle, Boondi raita, Mix salad, Mohanthal",
                        "Special: Moong dal chilla & Mohanthal"
                ),
                new WeeklyMealDay(
                        "Tuesday",
                        "10 Nov",
                        true,
                        "Idli sambhar, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Banana, Boiled egg",
                        "Bhindi do pyaza, Masoor masala, Chana dal, Steem rice, Roti, Mix pickle, Curd",
                        "Aloo matar sandwich, Sweet chutney, Green chutney, Tea",
                        "Mix veg, Jeera rice, Dal makhani, Roti, Rice papad, Mix pickle, Green salad, Pineapple seera",
                        "Special: Pineapple seera & Boiled egg"
                ),
                new WeeklyMealDay(
                        "Wednesday",
                        "11 Nov",
                        false,
                        "Singh poha, Fried chana, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon",
                        "Paneer bhurji, Egg curry, Arhar dal, Rice, Roti, Mix pickle, Onion salad",
                        "Papdi chaat, Sweet chutney, Tea",
                        "Bati, Baigun bartha, Steem rice, Veg raita, Roti, Onion lemon, Mix dal, Boondi",
                        "Special: Paneer bhurji, Egg curry & Dal bati"
                ),
                new WeeklyMealDay(
                        "Thursday",
                        "12 Nov",
                        false,
                        "Puri sabji, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Banana, Bull's eye",
                        "White choli, Aloo palak, Tadka dal, Jeera rice, Roti, Mix salad, Mix pickle",
                        "Bombay bhel, Tea",
                        "Besan gatta, Chana dal, Aloo gobhi, Roti, Mix pickle, Mix salad, Jeera rice, Gulab jamun",
                        "Special: Gulab jamun & Bull's eye"
                ),
                new WeeklyMealDay(
                        "Friday",
                        "13 Nov",
                        false,
                        "Uttapam, Coconut chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon",
                        "Paneer tikka masala, Chicken masala, Dal tadka, Plain rice, Roti, Mix pickle, Onion salad, Jalebi",
                        "Dabeli, Sweet chutney, Green chutney, Tea",
                        "Aloo soyabeen, Moong masala, Steem rice, Veg raita, Roti, Onion lemon, Mix dal",
                        "Special: Chicken masala / Paneer tikka & Jalebi"
                ),
                new WeeklyMealDay(
                        "Saturday",
                        "14 Nov",
                        false,
                        "Aloo paratha, Green chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder, Watermelon, Omelette",
                        "Chana masala, Dudhi dry, Dal tadka, Jeera rice, Roti, Mix pickle, Dahi, Pulses salad",
                        "Chinese bhel, Ketchup, Tea",
                        "Chhole, Bhature, Masala rice, Mix pickle, Boondi raita, Fry mirchi, Coconut ladoo",
                        "Special: Chhole bhature & Coconut ladoo"
                ),
                new WeeklyMealDay(
                        "Sunday",
                        "15 Nov",
                        false,
                        "Samosa, Green chutney, Sweet chutney, Bread butter/jam, Milk, Chocolate powder, Corn flakes, Tea, Coffee powder",
                        "Aloo tomato dry, Rajma masala, Dal fry, Steam rice, Roti, Onion salad, Mix pickle, Buttermilk",
                        "Pani puri, Ragda, Teekha paani, Sweet pani, Tea",
                        "Pav bhaji, Dal tadka, Plain rice, Curd, Lehsun chutny, Mix pickle, Onion salad",
                        "Special: Sunday Feast: Samosa, Pani puri & Pav bhaji"
                )
        );
    }

    private final List<MealRating> mockMealRatings = new ArrayList<>();

    @Override
    public boolean submitMealRating(MealRating rating) {
        if (rating != null) {
            mockMealRatings.add(rating);
            return true;
        }
        return false;
    }

    private final List<Suggestion> mockSuggestions = new ArrayList<>(List.of(
            new Suggestion(
                    "sg-1",
                    "Extend Reading Room and Library Hours to 02:00 AM During Mid-Sems",
                    "Library & Reading Rooms",
                    "During mid-semester and end-semester examination cycles, students require silent late-night study spaces. The current 23:00 closing leaves limited options in hostels.",
                    "Raghav A. (B.Tech CSE)",
                    false,
                    "05 Nov 2026",
                    74,
                    true,
                    "ACCEPTED",
                    "SAFE · ACCEPTED",
                    "Library Committee: Approved from 10 Nov onwards. Guard posted till 02:00 AM."
            ),
            new Suggestion(
                    "sg-5",
                    "Include High-Protein Options (Eggs / Soya / Paneer) in Evening Mess Snacks",
                    "Mess & Dining",
                    "Evening snacks frequently consist only of deep-fried carbohydrates. Adding boiled eggs, roasted chana, or paneer would assist students pursuing fitness and sports.",
                    "Anonymous Student",
                    true,
                    "26 Oct 2026",
                    89,
                    true,
                    "ACCEPTED",
                    "SAFE · ACCEPTED",
                    "Mess Committee: Added boiled eggs and sprout bowls to Wednesday & Friday snacks."
            ),
            new Suggestion(
                    "sg-2",
                    "Upgrade Python 3.10 to 3.12 and PyTorch GPU drivers in Lab 2",
                    "Campus IT & Wi-Fi",
                    "Current CUDA toolkit version in Lab 2 fails to build modern PyTorch 2.4 wheels for CS701 and deep learning assignments.",
                    "Anonymous Student",
                    true,
                    "04 Nov 2026",
                    58,
                    false,
                    "RESOLVED",
                    "SAFE · RESOLVED",
                    "Systems Admin: Python 3.12.3 and CUDA 12.4 installed across all 45 workstations."
            ),
            new Suggestion(
                    "sg-3",
                    "Add More Power Sockets and Surge Protectors in Block B Lecture Halls",
                    "Hostel & Facilities",
                    "Laptops running compilers and virtual machines drain battery during back-to-back 3-hour lab and lecture sessions. Rows 3–6 have no charging points.",
                    "Anonymous Student",
                    true,
                    "02 Nov 2026",
                    42,
                    false,
                    "UNDER_REVIEW",
                    "WATCH · UNDER REVIEW",
                    "Estate Office: Electrical load assessment in progress with CPWD engineers."
            ),
            new Suggestion(
                    "sg-4",
                    "Install Filter Water Chiller Unit on 2nd Floor Academic Block",
                    "Hostel & Facilities",
                    "The 2nd-floor dispenser has low flow rate during break times between 11:00 and 14:00.",
                    "Prateek S. (B.Tech IT)",
                    false,
                    "28 Oct 2026",
                    31,
                    true,
                    "PLANNED",
                    "PLANNED",
                    "Procurement PO placed for 150L/hr RO chiller unit. Delivery expected 16 Nov."
            )
    ));

    @Override
    public List<Suggestion> getSuggestions() {
        return Collections.unmodifiableList(mockSuggestions);
    }

    @Override
    public boolean toggleSuggestionUpvote(String suggestionId) {
        for (int i = 0; i < mockSuggestions.size(); i++) {
            Suggestion s = mockSuggestions.get(i);
            if (s.id().equalsIgnoreCase(suggestionId)) {
                boolean newUpvoted = !s.hasUpvoted();
                int newVotes = newUpvoted ? s.upvotes() + 1 : Math.max(0, s.upvotes() - 1);
                Suggestion updated = new Suggestion(
                        s.id(), s.title(), s.category(), s.description(), s.author(),
                        s.isAnonymous(), s.date(), newVotes, newUpvoted, s.status(),
                        s.statusLabel(), s.officialResponse()
                );
                mockSuggestions.set(i, updated);
                return newUpvoted;
            }
        }
        return false;
    }

    @Override
    public Suggestion createSuggestion(String title, String category, String description, boolean isAnonymous) {
        int nextNum = mockSuggestions.size() + 1;
        String author = isAnonymous ? "Anonymous Student" : "Raghav A. (B.Tech CSE)";
        Suggestion newSg = new Suggestion(
                "sg-" + nextNum,
                title,
                category != null ? category : "Campus Facilities",
                description,
                author,
                isAnonymous,
                "Today, 10 Nov 2026",
                1,
                true,
                "UNDER_REVIEW",
                "WATCH · UNDER REVIEW",
                "Student Affairs: Logged and assigned ticket #SG-2026-" + (900 + nextNum) + " for review."
        );
        mockSuggestions.add(0, newSg);
        return newSg;
    }

    @Override
    public List<Classroom> getClassrooms() {
        return List.of(
                new Classroom("CS301", "CS301", "Computer Networks", "B.Tech (CSE) · Sem V · ICD-2125 · Dr. Abhisek Paul", "2 new materials", null),
                new Classroom("CS303", "CS303", "Operating Systems", "B.Tech (CSE) · Sem V · ICD-2125 · Dr. Deepika Gupta", null, "1 assignment due"),
                new Classroom("CS305", "CS305", "Formal Language & Automata Theory", "B.Tech (CSE) · Sem V · ICD-2125 · Dr. Venkata Phanikrishna", null, null),
                new Classroom("CS307", "CS307", "Artificial Intelligence", "B.Tech (CSE) · Sem V · ICD-2125 · Dr. Deepika Gupta", null, null),
                new Classroom("CS309", "CS309", "Cryptography & Network Security", "B.Tech (CSE) · Sem V · ICD-2125 · Dr. Gaurav Pareek", null, null),
                new Classroom("CS201", "CS201", "Object Oriented Design & Programming", "B.Tech (CSE) · Sem III · ICD-2125 · Dr. Radhika Lama", null, null),
                new Classroom("EC201", "EC201", "Digital Logic Design", "B.Tech (ECE/CSE) · Sem III · ICD-2118 · Dr. Varun Kumar", null, null),
                new Classroom("CS433", "CS433", "Topics in Deep Learning", "B.Tech (CSE/IT) · Sem VII · ICD-2127 · Dr. Jignesh Bhatt", null, null),
                new Classroom("CS445", "CS445", "Software & Cyber Security", "B.Tech (CSE/IT) · Sem VII · ICD-2109 · Dr. Naveen Kumar", null, null),
                new Classroom("CS447", "CS447", "Software Project Management", "B.Tech (CSE/IT) · Sem VII · ICD-2109 · Dr. Karan Verma", null, null),
                new Classroom("CS547", "CS547", "Computer Networks (MCA)", "MCA · Sem III · ICD-2231 · Dr. Abhisek Paul", null, null),
                new Classroom("CS541", "CS541", "Design & Analysis of Algorithms", "MCA · Sem III · ICD-2231 · Dr. Venkata Phanikrishna", null, null)
        );
    }

    @Override
    public List<StreamPost> getStreamPosts(String courseCode) {
        String teacher = "Dr. Abhisek Paul";
        if (courseCode != null) {
            String upper = courseCode.toUpperCase();
            if (upper.contains("CS303") || upper.contains("CS307")) teacher = "Dr. Deepika Gupta";
            else if (upper.contains("CS305") || upper.contains("CS541")) teacher = "Dr. Venkata Phanikrishna";
            else if (upper.contains("CS309") || upper.contains("CS203")) teacher = "Dr. Gaurav Pareek";
            else if (upper.contains("CS201")) teacher = "Dr. Radhika Lama";
            else if (upper.contains("EC201")) teacher = "Dr. Varun Kumar";
            else if (upper.contains("CS433")) teacher = "Dr. Jignesh Bhatt";
            else if (upper.contains("CS445")) teacher = "Dr. Naveen Kumar";
            else if (upper.contains("CS447")) teacher = "Dr. Karan Verma";
        }
        return List.of(
                new StreamPost(
                        "p1",
                        teacher,
                        "6 Nov, 2026",
                        "ASSIGNMENT",
                        "Assignment 2 Released",
                        "The second assignment for " + (courseCode != null ? courseCode : "this course") + " is now available in Classwork. Due 13 Nov, 2026 (11:59 PM).",
                        "Assignment_2_Problems.pdf",
                        "452 KB",
                        12,
                        4
                ),
                new StreamPost(
                        "p2",
                        teacher,
                        "4 Nov, 2026",
                        "MATERIAL",
                        "Lecture Slides & Problem Sets",
                        "Lecture slides and reference study material have been uploaded to the stream.",
                        "Course_Lecture_Notes.pdf",
                        "1.8 MB",
                        8,
                        1
                )
        );
    }

    @Override
    public List<Assignment> getAssignments(String courseCode) {
        return List.of(
                new Assignment(
                        "as2",
                        "Assignment 2: Trees and Graphs",
                        "CS301",
                        "Due 13 Nov, 2026, 11:59 PM",
                        "5 days left",
                        null,
                        true,
                        Assignment.AssignmentGroup.UPCOMING,
                        "Implement Binary Search Tree, AVL Tree balancing rotations, and Graph traversal algorithms (BFS and DFS). Submit code and complexity report in PDF format.",
                        "Assignment_2_Trees.pdf",
                        "452 KB",
                        null,
                        20,
                        false,
                        null,
                        null
                ),
                new Assignment(
                        "as3",
                        "Lab Exercise 3: Stack & Queue Applications",
                        "CS301",
                        "Due 31 Oct, 2026, 11:59 PM",
                        "Past due",
                        null,
                        false,
                        Assignment.AssignmentGroup.PAST_DUE,
                        "Implement infix to postfix conversion and monotonic stack applications in C++.",
                        "Lab_3_Stack_Queue.pdf",
                        "210 KB",
                        null,
                        10,
                        false,
                        null,
                        null
                ),
                new Assignment(
                        "as1",
                        "Assignment 1: Arrays and Linked Lists",
                        "CS301",
                        "Due 28 Oct, 2026, 11:59 PM",
                        "Graded",
                        "18 / 20",
                        false,
                        Assignment.AssignmentGroup.DONE,
                        "Solve pointer manipulation, doubly linked lists, and amortized array resizing operations.",
                        "Assignment_1_Arrays.pdf",
                        "380 KB",
                        "Excellent implementation of balanced tree rotations. Unit tests passed all edge cases.",
                        20,
                        true,
                        "Assignment_1_Raghav.zip",
                        "28 Oct 2026, 21:30"
                )
        );
    }

    @Override
    public boolean joinClassroom(String code) {
        return code != null && code.trim().length() == 6;
    }

    private final List<Remark> mockRemarks = new ArrayList<>(List.of(
            new Remark(
                    "rem-1",
                    "CS301",
                    "Data Structures",
                    "Dr. A. Mehta",
                    "Course Instructor & HoD",
                    "05 Nov 2026",
                    "COMMENDATION",
                    "SAFE · COMMENDATION",
                    "Exemplary Algorithmic Problem Solving in Assignment 1",
                    "Demonstrated outstanding rigor in balanced binary tree rotation proofs and amortized dynamic array analysis. Unit tests passed all edge cases with zero memory leaks. Highly recommended for nomination as Undergraduate Teaching Assistant next academic semester.",
                    "Action: Nominated for Teaching Assistantship (Spring 2027)",
                    true,
                    "06 Nov 2026"
            ),
            new Remark(
                    "rem-2",
                    "CS302",
                    "Operating Systems",
                    "Dr. R. Iyer",
                    "Associate Professor",
                    "28 Oct 2026",
                    "ADVISORY",
                    "WATCH · ATTENDANCE ADVISORY",
                    "Attendance Vigilance Notice (Currently 78.4% in CS302)",
                    "Course attendance is approaching the mandatory 75% institutional threshold. With upcoming mid-semester examinations, please maintain regular attendance in memory management and page replacement lectures to avoid examination debarment.",
                    "Action Required: Meet instructor during Tuesday office hours if medical leave documentation is pending.",
                    false,
                    null
            ),
            new Remark(
                    "rem-3",
                    "CS303",
                    "Computer Networks",
                    "Dr. S. Kulkarni",
                    "Course Instructor",
                    "15 Oct 2026",
                    "COMMENDATION",
                    "SAFE · COMMENDATION",
                    "Exemplary Wireshark Packet Sniffing & TCP Analysis",
                    "Submitted an exceptionally detailed packet capture trace with clear protocol breakdown for three-way handshake, sliding window flow control, and Reno congestion control state transitions.",
                    "Commendation recorded in department academic ledger.",
                    true,
                    "16 Oct 2026"
            ),
            new Remark(
                    "rem-4",
                    "GENERAL",
                    "Academic Mentorship",
                    "Dr. A. Mehta",
                    "Faculty Academic Mentor",
                    "20 Sep 2026",
                    "MENTOR_NOTE",
                    "MENTOR NOTE",
                    "Semester V Mid-Term Academic Progress Review",
                    "Raghav continues to demonstrate strong conceptual aptitude across core computing streams. Recommended exploring open-source contributions in Linux kernel subsystems and preparation for competitive programming regional contests.",
                    "Next Review Scheduled: Post Mid-Semester Exams (Week 12)",
                    true,
                    "22 Sep 2026"
            )
    ));

    @Override
    public List<Remark> getFacultyRemarks() {
        return Collections.unmodifiableList(mockRemarks);
    }

    @Override
    public boolean acknowledgeRemark(String id) {
        for (int i = 0; i < mockRemarks.size(); i++) {
            Remark r = mockRemarks.get(i);
            if (r.id().equalsIgnoreCase(id)) {
                Remark updated = new Remark(
                        r.id(), r.courseCode(), r.courseName(), r.facultyName(),
                        r.facultyRole(), r.date(), r.type(), r.typeLabel(),
                        r.title(), r.content(), r.actionItem(), true, "10 Nov 2026"
                );
                mockRemarks.set(i, updated);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Course> getEnrolledCourses() {
        return List.of(
                new Course("CS301", "Data Structures & Algorithms", "Section A", "Dr. A. Mehta", "ICD-2125", 4),
                new Course("CS303", "Computer Networks", "Section A", "Dr. S. Kulkarni", "ICD-2125", 4),
                new Course("CS305", "Database Management Systems", "Section A", "Dr. N. Patel", "ICD-2113", 4),
                new Course("CS307", "System Software & Compilers", "Section A", "Dr. N. Patel", "ICD-2125", 4),
                new Course("CS309", "Theory of Computation", "Section A", "Dr. R. Iyer", "ICD-2125", 4)
        );
    }

    @Override
    public List<WeeklyAttendanceRecord> getWeeklyAttendanceRegister(String semester, String weekIdentifier) {
        return List.of(
                new WeeklyAttendanceRecord(
                        "CS301",
                        "Data Structures & Algorithms",
                        List.of(
                                new DayAttendanceMark("Mon", "2 Nov", "P", "15:15 Lecture (ICD-2125) - Present"),
                                new DayAttendanceMark("Tue", "3 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Wed", "4 Nov", "P", "16:45 Lecture (ICD-2125) - Present"),
                                new DayAttendanceMark("Thu", "5 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Fri", "6 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Sat", "7 Nov", "P", "10:00 Tutorial (ICD-2125) - Present"),
                                new DayAttendanceMark("Sun", "8 Nov", "-", "Weekend")
                        ),
                        3, 3, 0
                ),
                new WeeklyAttendanceRecord(
                        "CS303",
                        "Computer Networks",
                        List.of(
                                new DayAttendanceMark("Mon", "2 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Tue", "3 Nov", "P", "15:15 Lecture (ICD-2125) - Present"),
                                new DayAttendanceMark("Wed", "4 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Thu", "5 Nov", "P", "11:15 Lab (ICD-2102-CS) - Present"),
                                new DayAttendanceMark("Fri", "6 Nov", "P", "16:45 Lecture (ICD-2125) - Present"),
                                new DayAttendanceMark("Sat", "7 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Sun", "8 Nov", "-", "Weekend")
                        ),
                        3, 3, 0
                ),
                new WeeklyAttendanceRecord(
                        "CS305",
                        "Database Management Systems",
                        List.of(
                                new DayAttendanceMark("Mon", "2 Nov", "P", "11:30 Tutorial (ICD-2113) - Present"),
                                new DayAttendanceMark("Tue", "3 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Wed", "4 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Thu", "5 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Fri", "6 Nov", "A", "15:15 Lecture (ICD-2125) - Absent"),
                                new DayAttendanceMark("Sat", "7 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Sun", "8 Nov", "-", "Weekend")
                        ),
                        2, 1, 1
                ),
                new WeeklyAttendanceRecord(
                        "CS309",
                        "Theory of Computation",
                        List.of(
                                new DayAttendanceMark("Mon", "2 Nov", "P", "16:45 Lecture (ICD-2125) - Present"),
                                new DayAttendanceMark("Tue", "3 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Wed", "4 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Thu", "5 Nov", "P", "15:15 Lecture (ICD-2125) - Present"),
                                new DayAttendanceMark("Fri", "6 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Sat", "7 Nov", "-", "No session scheduled"),
                                new DayAttendanceMark("Sun", "8 Nov", "-", "Weekend")
                        ),
                        2, 2, 0
                )
        );
    }

    @Override
    public List<FacultyMember> getFacultyDirectory() {
        return List.of(
                // 1. Dr. Abhisek Paul (ICD)
                new FacultyMember(
                        "dr-abhisek-paul",
                        "Dr. Abhisek Paul",
                        "Assistant Professor",
                        "Computer Science & Engineering",
                        "ICD-2125-F",
                        "abhisek_paul@diu.iiitvadodara.ac.in",
                        "6033398253",
                        "ICD",
                        "Mon & Wed 14:00–15:00",
                        "In class until 16:45 · CS301 · ICD-2125",
                        "IN_CLASS",
                        new ClassOccurrence("ap_nc", "CS301", "Computer Networks", "ICD-2125", "Dr. Abhisek Paul", "Mon", "15:15", "16:45", "Lecture", true, "in 45 min", "CSE"),
                        List.of(
                                new ClassOccurrence("ap_s1", "CS547", "Computer Networks", "ICD-2231", "Dr. Abhisek Paul", "Mon", "09:30", "11:00", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("ap_s2", "CS301", "Computer Networks", "ICD-2125", "Dr. Abhisek Paul", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ap_s3", "CS547", "Computer Networks Lab", "ICD-2231", "Dr. Abhisek Paul", "Tue", "11:15", "13:15", "Lab", false, null, "MCA"),
                                new ClassOccurrence("ap_s4", "CS361", "Computer Networks Lab", "ICD-2102-CS", "Dr. Abhisek Paul", "Wed", "11:15", "13:15", "Lab", false, null, "CSE"),
                                new ClassOccurrence("ap_s5", "CS301", "Computer Networks", "ICD-2125", "Dr. Abhisek Paul", "Wed", "16:45", "18:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ap_s6", "CS547", "Computer Networks", "ICD-2231", "Dr. Abhisek Paul", "Thu", "08:00", "09:30", "Lecture", false, null, "MCA")
                        ),
                        List.of("CS301 Computer Networks", "CS361 Computer Networks Lab", "CS547 Computer Networks (MCA)"),
                        "Computer Networks, Wireless Ad-hoc Networks, IoT Protocols, Distributed Computing"
                ),

                // 2. Dr. Abhishek Kesarwani (ICD)
                new FacultyMember(
                        "dr-abhishek-kesarwani",
                        "Dr. Abhishek Kesarwani",
                        "Assistant Professor",
                        "Mathematics & Humanities",
                        "ICD-2113-F",
                        "abhishek_kesarwani@iiitvadodara.ac.in",
                        "7499221918",
                        "ICD",
                        "Tue & Thu 10:00–11:00",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("ak_nc", "MA101", "Mathematics-I", "ICD-2113", "Dr. Abhishek Kesarwani", "Wed", "08:00", "09:30", "Lecture", false, null, "ECE/AI/CS"),
                        List.of(
                                new ClassOccurrence("ak_s1", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2113", "Dr. Abhishek Kesarwani", "Mon", "08:00", "09:30", "Lecture", false, null, "ECE/AI/CS"),
                                new ClassOccurrence("ak_s2", "MS441", "Discrete Mathematics", "ICD-2127", "Dr. Abhishek Kesarwani", "Mon", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("ak_s3", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2118", "Dr. Abhishek Kesarwani", "Mon", "09:30", "11:00", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ak_s4", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2113", "Dr. Abhishek Kesarwani", "Wed", "08:00", "09:30", "Lecture", false, null, "ECE/AI/CS"),
                                new ClassOccurrence("ak_s5", "MS441", "Discrete Mathematics", "ICD-2127", "Dr. Abhishek Kesarwani", "Wed", "09:30", "11:00", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("ak_s6", "HS103", "AI and Ethics", "ICD-2113", "Dr. Abhishek Kesarwani", "Thu", "08:00", "10:00", "Lecture", false, null, "AI"),
                                new ClassOccurrence("ak_s7", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2118", "Dr. Abhishek Kesarwani", "Fri", "09:30", "11:00", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ak_s8", "MS441", "Discrete Mathematics Tutorial", "ICD-2231", "Dr. Abhishek Kesarwani", "Fri", "11:30", "12:30", "Tutorial", false, null, "MCA")
                        ),
                        List.of("MA101 Mathematics-I (Linear Algebra)", "MS441 Discrete Mathematics (MCA)", "HS103 AI and Ethics"),
                        "Linear Algebra, Discrete Mathematics, Matrix Theory, Functional Analysis"
                ),

                // 3. Dr. Amal Tom (ICD)
                new FacultyMember(
                        "dr-amal-tom",
                        "Dr. Amal Tom",
                        "Assistant Professor",
                        "Humanities & Social Sciences",
                        "ICD-2125-F",
                        "amal_tom@iiitvadodara.ac.in",
                        "8848097953",
                        "ICD",
                        "Mon & Fri 14:00–15:00",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("at_nc", "HS201", "Technical Writing", "ICD-2125", "Dr. Amal Tom", "Wed", "11:15", "13:15", "Lecture", false, null, "CSE"),
                        List.of(
                                new ClassOccurrence("at_s1", "HS101", "Spoken and Written Communication", "ICD-2113", "Dr. Amal Tom", "Tue", "09:30", "11:30", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("at_s2", "HS201", "Technical Writing", "ICD-2125", "Dr. Amal Tom", "Tue", "11:15", "13:15", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("at_s3", "HS201", "Technical Writing", "ICD-2125", "Dr. Amal Tom", "Wed", "11:15", "13:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("at_s4", "HS441", "Spoken & Written Communication", "ICD-2127", "Dr. Amal Tom", "Fri", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("at_s5", "HS441", "Communication Lab", "ICD-2231", "Dr. Amal Tom", "Fri", "13:00", "15:00", "Lab", false, null, "MCA"),
                                new ClassOccurrence("at_s6", "HS101", "Spoken and Written Communication", "ICD-2113", "Dr. Amal Tom", "Fri", "15:15", "17:15", "Lecture", false, null, "AI/ECE")
                        ),
                        List.of("HS101 Spoken and Written Communication", "HS201 Technical Writing", "HS441 Spoken & Written Comm (MCA)"),
                        "Technical Communication, Applied Linguistics, Discourse Analysis, Pedagogy"
                ),

                // 4. Dr. Deepika Gupta (ICD)
                new FacultyMember(
                        "dr-deepika-gupta",
                        "Dr. Deepika Gupta",
                        "Assistant Professor",
                        "Computer Science & Engineering",
                        "ICD-2108-F",
                        "deepika_gupta@diu.iiitvadodara.ac.in",
                        "9015556697",
                        "ICD",
                        "Tue & Thu 14:00–15:00",
                        "In class until 16:45 · CS303 · ICD-2125",
                        "IN_CLASS",
                        new ClassOccurrence("dg_nc", "CS303", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Tue", "15:15", "16:45", "Lecture", true, "in 45 min", "CSE"),
                        List.of(
                                new ClassOccurrence("dg_s1", "AI101", "Intro to Artificial Intelligence", "ICD-2108", "Dr. Deepika Gupta", "Tue", "09:30", "11:30", "Lecture", false, null, "AI"),
                                new ClassOccurrence("dg_s2", "AI201", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Tue", "15:15", "16:45", "Lecture", false, null, "AI"),
                                new ClassOccurrence("dg_s3", "CS303", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Tue", "15:15", "16:45", "Lecture", true, "in 45 min", "CSE"),
                                new ClassOccurrence("dg_s4", "CS307", "Artificial Intelligence", "ICD-2125", "Dr. Deepika Gupta", "Wed", "15:15", "16:45", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("dg_s5", "AI261", "Operating Systems Lab", "ICD-2109", "Dr. Deepika Gupta", "Thu", "08:00", "10:00", "Lab", false, null, "AI"),
                                new ClassOccurrence("dg_s6", "CS363", "Operating Systems Lab", "ICD-2102-CS", "Dr. Deepika Gupta", "Thu", "11:15", "13:15", "Lab", false, null, "CSE"),
                                new ClassOccurrence("dg_s7", "CS605", "Artificial Intelligence Lab", "ICD-2118", "Dr. Deepika Gupta", "Thu", "15:15", "17:15", "Lab", false, null, "M.Tech"),
                                new ClassOccurrence("dg_s8", "CS303", "Operating Systems", "ICD-2125", "Dr. Deepika Gupta", "Thu", "16:45", "18:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("dg_s9", "CS367", "Artificial Intelligence Lab", "ICD-2102-CS", "Dr. Deepika Gupta", "Fri", "11:15", "13:00", "Lab", false, null, "CSE"),
                                new ClassOccurrence("dg_s10", "CS307", "Artificial Intelligence", "ICD-2125", "Dr. Deepika Gupta", "Fri", "16:45", "18:15", "Lecture", false, null, "CSE")
                        ),
                        List.of("CS303 Operating Systems", "CS307 Artificial Intelligence", "CS363 OS Lab", "CS367 AI Lab", "AI101 Intro to AI", "CS605 AI (M.Tech)"),
                        "Artificial Intelligence, Operating Systems, Machine Learning, Cognitive Systems"
                ),

                // 5. Dr. Gaurav Pareek (ICD)
                new FacultyMember(
                        "dr-gaurav-pareek",
                        "Dr. Gaurav Pareek",
                        "Assistant Professor",
                        "Computer Science & Engineering",
                        "ICD-2109-F",
                        "gaurav_pareek@diu.iiitvadodara.ac.in",
                        "9837169800",
                        "ICD",
                        "Wed & Fri 13:00–14:00",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("gp_nc", "CS309", "Introduction to Cryptography", "ICD-2125", "Dr. Gaurav Pareek", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE"),
                        List.of(
                                new ClassOccurrence("gp_s1", "CS263", "Algorithms Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Mon", "08:45", "10:45", "Lab", false, null, "ECE"),
                                new ClassOccurrence("gp_s2", "CS601", "Advanced Algorithms Lab", "ICD-2118", "Dr. Gaurav Pareek", "Mon", "15:15", "17:15", "Lab", false, null, "M.Tech"),
                                new ClassOccurrence("gp_s3", "CS309", "Cryptography & Network Security", "ICD-2125", "Dr. Gaurav Pareek", "Mon", "16:45", "18:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("gp_s4", "CS203", "Design and Analysis of Algorithms", "ICD-2109", "Dr. Gaurav Pareek", "Tue", "13:45", "15:15", "Lecture", false, null, "CSE/AI"),
                                new ClassOccurrence("gp_s5", "CS263", "Algorithms Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Wed", "09:00", "11:00", "Lab", false, null, "CSE"),
                                new ClassOccurrence("gp_s6", "CS369", "Cryptography Lab", "ICD-2102-CS", "Dr. Gaurav Pareek", "Thu", "09:00", "11:00", "Lab", false, null, "CSE"),
                                new ClassOccurrence("gp_s7", "CS309", "Cryptography & Network Security", "ICD-2125", "Dr. Gaurav Pareek", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("gp_s8", "CS203", "Design and Analysis of Algorithms", "ICD-2125", "Dr. Gaurav Pareek", "Fri", "11:15", "12:45", "Lecture", false, null, "CSE/AI")
                        ),
                        List.of("CS203 Design & Analysis of Algorithms", "CS309 Cryptography & Network Security", "CS369 Cryptography Lab", "CS601 Advanced Algorithms"),
                        "Cryptography, Network Security, Algorithm Design, Blockchain Security"
                ),

                // 6. Dr. Mukesh Singh (ICD)
                new FacultyMember(
                        "dr-mukesh-singh",
                        "Dr. Mukesh Singh",
                        "Assistant Professor",
                        "Physics & Electronics",
                        "ICD-1111-PH",
                        "mukesh@iiitvadodara.ac.in",
                        "9717029905",
                        "ICD",
                        "Mon & Thu 13:00–14:00",
                        "In Physics Lab · ICD-1111-PH",
                        "AVAILABLE",
                        new ClassOccurrence("ms_nc", "PH101", "Physics-I", "ICD-2118", "Dr. Mukesh Singh", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE/ECE"),
                        List.of(
                                new ClassOccurrence("ms_s1", "MS541", "Probability & Statistics", "ICD-2231", "Dr. Mukesh Singh", "Mon", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("ms_s2", "PH161", "Physics Lab", "ICD-1111-PH", "Dr. Mukesh Singh", "Mon", "08:00", "10:00", "Lab", false, null, "LD3"),
                                new ClassOccurrence("ms_s3", "PH101", "Physics-I", "ICD-2113", "Dr. Mukesh Singh", "Tue", "08:00", "09:30", "Lecture", false, null, "CSE/ECE"),
                                new ClassOccurrence("ms_s4", "PH101", "Physics Tutorial", "ICD-2113", "Dr. Mukesh Singh", "Tue", "11:30", "12:30", "Tutorial", false, null, "All"),
                                new ClassOccurrence("ms_s5", "EC265", "Antenna Lab", "ICD-2223-EL", "Dr. Mukesh Singh", "Wed", "09:00", "11:00", "Lab", false, null, "ECE"),
                                new ClassOccurrence("ms_s6", "MS541", "Probability & Statistics", "ICD-2231", "Dr. Mukesh Singh", "Wed", "10:00", "11:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("ms_s7", "PH101", "Physics-I", "ICD-2118", "Dr. Mukesh Singh", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE/ECE"),
                                new ClassOccurrence("ms_s8", "PH161", "Physics Lab", "ICD-1111-PH", "Dr. Mukesh Singh", "Thu", "14:00", "16:00", "Lab", false, null, "LD2")
                        ),
                        List.of("PH101 Physics-I", "PH161 Experimental Physics-I Lab", "MS541 Probability & Statistics (MCA)", "EC265 Antenna Lab"),
                        "Applied Physics, Material Science, Statistical Modeling, Antenna Waveguides"
                ),

                // 7. Dr. Pranjal Srivastava (ICD)
                new FacultyMember(
                        "dr-pranjal-srivastava",
                        "Dr. Pranjal Srivastava",
                        "Assistant Professor",
                        "Electronics & Communication",
                        "ICD-2118-F",
                        "pranjal_srivastava@iiitvadodara.ac.in",
                        "8601587631",
                        "ICD",
                        "Tue & Fri 13:00–14:00",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("ps_nc", "CS101", "Computer Programming", "ICD-2118", "Dr. Pranjal Srivastava", "Wed", "08:00", "09:30", "Lecture", false, null, "CSE"),
                        List.of(
                                new ClassOccurrence("ps_s1", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2113", "Dr. Pranjal Srivastava", "Mon", "08:00", "09:30", "Lecture", false, null, "ECE/AI/CS"),
                                new ClassOccurrence("ps_s2", "CS101", "Computer Programming", "ICD-2118", "Dr. Pranjal Srivastava", "Mon", "08:00", "09:30", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ps_s3", "MA603", "Linear Algebra (M.Tech)", "ICD-2125", "Dr. Pranjal Srivastava", "Mon", "09:30", "11:00", "Lecture", false, null, "M.Tech"),
                                new ClassOccurrence("ps_s4", "CS161", "Programming Lab", "ICD-2102-CS", "Dr. Pranjal Srivastava", "Mon", "14:00", "16:00", "Lab", false, null, "CSE"),
                                new ClassOccurrence("ps_s5", "CS101", "Computer Programming", "ICD-2118", "Dr. Pranjal Srivastava", "Wed", "08:00", "09:30", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ps_s6", "CS161", "Programming Lab", "ICD-2102-CS", "Dr. Pranjal Srivastava", "Wed", "14:00", "16:00", "Lab", false, null, "CSE"),
                                new ClassOccurrence("ps_s7", "MA101", "Mathematics-I (Linear Algebra)", "ICD-2118", "Dr. Pranjal Srivastava", "Fri", "09:30", "11:00", "Lecture", false, null, "CSE")
                        ),
                        List.of("CS101 Computer Programming", "CS161 Programming Lab", "MA101 Mathematics-I", "MA603 Linear Algebra (M.Tech)"),
                        "Signal Processing, Wireless Communications, Linear Algebra Applications, Embedded Systems"
                ),

                // 8. Dr. Radhika Lama (ICD)
                new FacultyMember(
                        "dr-radhika-lama",
                        "Dr. Radhika Lama",
                        "Assistant Professor",
                        "Computer Science & Engineering",
                        "ICD-2104-F",
                        "radhika_lama@iiitvadodara.ac.in",
                        "7810807814",
                        "ICD",
                        "Mon & Thu 11:00–12:00",
                        "Office Hours · ICD-2104-F",
                        "OFFICE_HOURS",
                        new ClassOccurrence("rl_nc", "CS201", "Object Oriented Design & Programming", "ICD-2125", "Dr. Radhika Lama", "Wed", "13:45", "15:15", "Lecture", false, null, "CSE"),
                        List.of(
                                new ClassOccurrence("rl_s1", "CS101", "Computer Programming", "ICD-2118", "Dr. Radhika Lama", "Mon", "08:00", "09:30", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("rl_s2", "CS445", "Basics of Web Programming", "ICD-2127", "Dr. Radhika Lama", "Tue", "09:30", "11:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("rl_s3", "CS445", "Web Programming Lab", "ICD-2127", "Dr. Radhika Lama", "Tue", "13:00", "15:00", "Lab", false, null, "MCA"),
                                new ClassOccurrence("rl_s4", "CS201", "Object Oriented Design & Programming", "ICD-2125", "Dr. Radhika Lama", "Wed", "13:45", "15:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("rl_s5", "CS261", "OODP Lab", "ICD-2104-CS", "Dr. Radhika Lama", "Thu", "08:00", "10:00", "Lab", false, null, "CSE"),
                                new ClassOccurrence("rl_s6", "CS201", "Object Oriented Design & Programming", "ICD-2113", "Dr. Radhika Lama", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE")
                        ),
                        List.of("CS201 Object Oriented Design & Programming", "CS261 OODP Lab", "CS101 Programming", "CS445 Web Programming (MCA)"),
                        "Object Oriented Architectures, Web Technologies, Software Engineering, Mobile Computing"
                ),

                // 9. Dr. Suvam Singh (ICD)
                new FacultyMember(
                        "dr-suvam-singh",
                        "Dr. Suvam Singh",
                        "Assistant Professor",
                        "Computer Science & Engineering / AI-IoT",
                        "ICD-2118-F",
                        "suvam_singh@iiitvadodara.ac.in",
                        "9836533161",
                        "ICD",
                        "Wed & Fri 10:00–11:00",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("ss_nc", "MA201", "Mathematics-III", "ICD-2118", "Dr. Suvam Singh", "Tue", "11:15", "13:15", "Lecture", false, null, "CSE"),
                        List.of(
                                new ClassOccurrence("ss_s1", "MA201", "Mathematics-III (RV, RP, Stat)", "ICD-2125", "Dr. Suvam Singh", "Mon", "11:15", "13:15", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("ss_s2", "MA201", "Mathematics-III (RV, RP, Stat)", "ICD-2118", "Dr. Suvam Singh", "Tue", "11:15", "13:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ss_s3", "MA201", "Mathematics-III", "ICD-2113", "Dr. Suvam Singh", "Wed", "13:45", "15:15", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("ss_s4", "MA201", "Mathematics-III", "ICD-2113", "Dr. Suvam Singh", "Thu", "13:45", "15:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("ss_s5", "MA201", "Math-III Tutorial", "ICD-2231", "Dr. Suvam Singh", "Fri", "15:15", "16:15", "Tutorial", false, null, "CSE"),
                                new ClassOccurrence("ss_s6", "MA201", "Math-III Tutorial", "ICD-2231", "Dr. Suvam Singh", "Fri", "17:30", "18:30", "Tutorial", false, null, "AI/ECE")
                        ),
                        List.of("MA201 Mathematics-III (RV, RP, Stat)", "MA601 Probability & Statistics (M.Tech)", "PH101 Physics-I"),
                        "Probability & Stochastic Processes, Random Processes, IoT Analytics, Statistical Learning"
                ),

                // 10. Dr. Uday Panwar (ICD)
                new FacultyMember(
                        "dr-uday-panwar",
                        "Dr. Uday Panwar",
                        "Assistant Professor",
                        "Electronics & Communication / VLSI",
                        "ICD-2221-EL",
                        "uday_panwar@iiitvadodara.ac.in",
                        "9826752267",
                        "ICD",
                        "Mon & Wed 11:00–12:00",
                        "Available in VLSI Lab",
                        "AVAILABLE",
                        new ClassOccurrence("up_nc", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Uday Panwar", "Tue", "13:30", "16:30", "Lab", false, null, "ECE/AI"),
                        List.of(
                                new ClassOccurrence("up_s1", "EC441", "Digital Designs Lab", "ICD-2127", "Dr. Uday Panwar", "Mon", "13:15", "15:15", "Lab", false, null, "MCA"),
                                new ClassOccurrence("up_s2", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Uday Panwar", "Tue", "13:30", "16:30", "Lab", false, null, "ECE/AI"),
                                new ClassOccurrence("up_s3", "EC101", "Basic Electronics-I", "ICD-2113", "Dr. Uday Panwar", "Wed", "09:30", "11:00", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("up_s4", "CCS105", "Digital Design & Architecture", "ICD-2113", "Dr. Uday Panwar", "Wed", "15:30", "17:00", "Lecture", false, null, "CSE(CS)"),
                                new ClassOccurrence("up_s5", "CCS105", "Digital Design & Architecture", "ICD-2125", "Dr. Uday Panwar", "Thu", "09:30", "11:00", "Lecture", false, null, "CSE(CS)"),
                                new ClassOccurrence("up_s6", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Uday Panwar", "Thu", "14:00", "17:00", "Lab", false, null, "ECE/AI"),
                                new ClassOccurrence("up_s7", "EC101", "Basic Electronics-I", "ICD-2118", "Dr. Uday Panwar", "Fri", "08:00", "09:30", "Lecture", false, null, "CSE")
                        ),
                        List.of("CCS105 Digital Design and Architecture", "EC101 Basic Electronics-I", "EC161 Basic Electronics Lab-I", "EC441 Digital Design (MCA)"),
                        "VLSI Design, Low-Power Digital Systems, Computer Architecture, FPGA Prototyping"
                ),

                // 11. Dr. Varun Kumar (ICD)
                new FacultyMember(
                        "dr-varun-kumar",
                        "Dr. Varun Kumar",
                        "Assistant Professor",
                        "Electronics & Communication",
                        "ICD-2221-EL",
                        "varun_kumar@diu.iiitvadodara.ac.in",
                        "8249065437",
                        "ICD",
                        "Wed & Fri 16:00–17:00",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("vk_nc", "EC261", "Digital Logic Design Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Tue", "08:00", "10:00", "Lab", false, null, "ECE"),
                        List.of(
                                new ClassOccurrence("vk_s1", "EC201", "Digital Logic Design", "ICD-2118", "Dr. Varun Kumar", "Mon", "13:45", "15:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("vk_s2", "EC261", "Digital Logic Design Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Tue", "08:00", "10:00", "Lab", false, null, "ECE"),
                                new ClassOccurrence("vk_s3", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Varun Kumar", "Tue", "13:30", "16:30", "Lab", false, null, "ECE/AI"),
                                new ClassOccurrence("vk_s4", "EC101", "Basic Electronics-I", "ICD-2118", "Dr. Varun Kumar", "Wed", "09:30", "11:00", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("vk_s5", "EC201", "Digital Logic Design", "ICD-2118", "Dr. Varun Kumar", "Thu", "11:15", "12:45", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("vk_s6", "EC161", "Basic Electronics Lab-I", "ICD-2221-EL", "Dr. Varun Kumar", "Thu", "14:00", "17:00", "Lab", false, null, "ECE/AI"),
                                new ClassOccurrence("vk_s7", "EC101", "Basic Electronics-I", "ICD-2113", "Dr. Varun Kumar", "Fri", "08:00", "09:30", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("vk_s8", "EC261", "Digital Logic Design Lab", "ICD-2221-EL", "Dr. Varun Kumar", "Fri", "09:00", "11:00", "Lab", false, null, "CSE")
                        ),
                        List.of("EC201 Digital Logic Design", "EC261 Digital Logic Design Lab", "EC101 Basic Electronics-I", "EC161 Basic Electronics Lab-I"),
                        "Digital Electronics, Microelectronics, Embedded System Design, Signal Integrity"
                ),

                // 12. Dr. Venkata Phanikrishna (ICD)
                new FacultyMember(
                        "dr-venkata-phanikrishna",
                        "Dr. Venkata Phanikrishna",
                        "Assistant Professor",
                        "Computer Science & Engineering / MAC",
                        "ICD-2113-F",
                        "venkata_phanikrishna@diu.iiitvadodara.ac.in",
                        "9908261261",
                        "ICD",
                        "Mon & Thu 14:00–15:00",
                        "In class until 18:15 · CS305 · ICD-2125",
                        "IN_CLASS",
                        new ClassOccurrence("vp_nc", "CS305", "Formal Language and Automata Theory", "ICD-2125", "Dr. Venkata Phanikrishna", "Tue", "16:45", "18:15", "Lecture", true, "in 1 hour", "CSE"),
                        List.of(
                                new ClassOccurrence("vp_s1", "CS305", "Formal Language & Automata Theory (Tut)", "ICD-2113", "Dr. Venkata Phanikrishna", "Mon", "11:30", "12:30", "Tutorial", false, null, "CSE"),
                                new ClassOccurrence("vp_s2", "CS541", "Design & Analysis of Algorithms Lab", "ICD-2231", "Dr. Venkata Phanikrishna", "Mon", "13:15", "15:15", "Lab", false, null, "MCA"),
                                new ClassOccurrence("vp_s3", "CS541", "Design & Analysis of Algorithms", "ICD-2231", "Dr. Venkata Phanikrishna", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("vp_s4", "CS305", "Formal Language and Automata Theory", "ICD-2125", "Dr. Venkata Phanikrishna", "Tue", "16:45", "18:15", "Lecture", true, "in 1 hour", "CSE"),
                                new ClassOccurrence("vp_s5", "CS541", "Design & Analysis of Algorithms", "ICD-2231", "Dr. Venkata Phanikrishna", "Thu", "09:30", "11:00", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("vp_s6", "CS305", "Formal Language and Automata Theory", "ICD-2125", "Dr. Venkata Phanikrishna", "Fri", "15:15", "16:45", "Lecture", false, null, "CSE")
                        ),
                        List.of("CS305 Formal Language and Automata Theory", "CS541 Design & Analysis of Algorithms (MCA)"),
                        "Theoretical Computer Science, Automata Theory, Computational Complexity, Graph Theory"
                ),

                // 13. Dr. Vikas Kumar (ICD)
                new FacultyMember(
                        "dr-vikas-kumar",
                        "Dr. Vikas Kumar",
                        "Assistant Professor",
                        "Environmental Science & Computing",
                        "ICD-2118-F",
                        "vikas_kumar@diu.iiitvadodara.ac.in",
                        "9106370423",
                        "ICD",
                        "Tue & Fri 11:30–12:30",
                        "Available in Office",
                        "AVAILABLE",
                        new ClassOccurrence("vk2_nc", "HS541", "Science, Technology and Society", "ICD-2231", "Dr. Vikas Kumar", "Tue", "09:30", "11:00", "Lecture", false, null, "MCA"),
                        List.of(
                                new ClassOccurrence("vk2_s1", "SC201", "Environmental Science", "ICD-2118", "Dr. Vikas Kumar", "Mon", "11:15", "13:15", "Lecture", false, null, "CSE"),
                                new ClassOccurrence("vk2_s2", "HS541", "Science, Technology and Society", "ICD-2231", "Dr. Vikas Kumar", "Tue", "09:30", "11:00", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("vk2_s3", "SC201", "Environmental Science", "ICD-2118", "Dr. Vikas Kumar", "Wed", "11:30", "13:00", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("vk2_s4", "HS103", "AI and Ethics", "ICD-2113", "Dr. Vikas Kumar", "Thu", "08:00", "10:00", "Lecture", false, null, "AI"),
                                new ClassOccurrence("vk2_s5", "HS541", "Science, Technology and Society", "ICD-2231", "Dr. Vikas Kumar", "Fri", "08:00", "09:30", "Lecture", false, null, "MCA")
                        ),
                        List.of("SC201 Environmental Science", "HS541 Science, Technology and Society (MCA)", "HS103 AI & Ethics"),
                        "Environmental Informatics, Remote Sensing Ecology, Sustainability Studies"
                ),

                // 14. Dr. Isha Chauhan (GN-Online)
                new FacultyMember(
                        "dr-isha-chauhan",
                        "Dr. Isha Chauhan",
                        "Assistant Professor (GN Campus)",
                        "Electronics & Communication",
                        "GN Campus (Online to ICD)",
                        "isha_chauhan@iiitvadodara.ac.in",
                        "9760477445",
                        "GN-Online",
                        "Fri 11:00–12:00 (Online Meet)",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("ic_nc", "EC207", "Network Analysis and Synthesis", "ICD-2118", "Dr. Isha Chauhan", "Fri", "13:15", "15:15", "Lecture", false, null, "ECE"),
                        List.of(
                                new ClassOccurrence("ic_s1", "EC207", "Network Analysis and Synthesis", "ICD-2118", "Dr. Isha Chauhan", "Fri", "13:15", "15:15", "Lecture", false, null, "ECE")
                        ),
                        List.of("EC207 Network Analysis and Synthesis"),
                        "Network Synthesis, RF Circuits, Microwave Engineering"
                ),

                // 15. Dr. Jignesh Bhatt (GN-Online)
                new FacultyMember(
                        "dr-jignesh-bhatt",
                        "Dr. Jignesh Bhatt",
                        "Associate Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Online to ICD)",
                        "jignesh.bhatt@iiitvadodara.ac.in",
                        "9409288312",
                        "GN-Online",
                        "Mon & Wed 14:00–15:00",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("jb_nc", "CS433", "Topics in Deep Learning", "ICD-2127", "Dr. Jignesh Bhatt", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                        List.of(
                                new ClassOccurrence("jb_s1", "CS433", "Topics in Deep Learning", "ICD-2127", "Dr. Jignesh Bhatt", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                                new ClassOccurrence("jb_s2", "CS433", "Deep Learning Lab", "ICD-2102-CS", "Dr. Jignesh Bhatt", "Tue", "11:30", "13:30", "Lab", false, null, "CSE/IT"),
                                new ClassOccurrence("jb_s3", "CS433", "Topics in Deep Learning", "ICD-2127", "Dr. Jignesh Bhatt", "Wed", "16:45", "18:15", "Lecture", false, null, "CSE/IT")
                        ),
                        List.of("CS433 / IT433 Topics in Deep Learning"),
                        "Deep Learning, Neural Networks, Computer Vision, Pattern Recognition"
                ),

                // 16. Dr. Jignesh Patel (GN-Online)
                new FacultyMember(
                        "dr-jignesh-patel",
                        "Dr. Jignesh Patel",
                        "Assistant Professor (GN Campus)",
                        "Electronics & Communication",
                        "GN Campus (Online to ICD)",
                        "jignesh_patel@diu.iiitvadodara.ac.in",
                        "9067229799",
                        "GN-Online",
                        "Wed 11:00–12:00",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("jp_nc", "EC425", "Computer Graphics & Multimedia", "ICD-2127", "Dr. Jignesh Patel", "Wed", "13:45", "15:15", "Lecture", false, null, "ECE"),
                        List.of(
                                new ClassOccurrence("jp_s1", "EC425", "Computer Graphics & Multimedia", "ICD-2127", "Dr. Jignesh Patel", "Wed", "13:45", "15:15", "Lecture", false, null, "ECE"),
                                new ClassOccurrence("jp_s2", "EC425", "Computer Graphics & Multimedia", "ICD-2229", "Dr. Jignesh Patel", "Fri", "13:15", "14:45", "Lecture", false, null, "ECE")
                        ),
                        List.of("EC425 Computer Graphics and Multimedia"),
                        "Computer Graphics, Image Processing, Multimedia Systems"
                ),

                // 17. Dr. Naveen Kumar (GN-Online)
                new FacultyMember(
                        "dr-naveen-kumar",
                        "Dr. Naveen Kumar",
                        "Assistant Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Online to ICD)",
                        "naveen_kumar@iiitvadodara.ac.in",
                        "8000086132",
                        "GN-Online",
                        "Tue 14:00–15:00",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("nk_nc", "CCS101", "Cybersecurity Essentials", "ICD-2118", "Dr. Naveen Kumar", "Tue", "09:30", "11:00", "Lecture", false, null, "CSE(CS)"),
                        List.of(
                                new ClassOccurrence("nk_s1", "CS445", "Software & Cyber Security", "ICD-2229", "Dr. Naveen Kumar", "Mon", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                                new ClassOccurrence("nk_s2", "CCS101", "Cybersecurity Essentials", "ICD-2118", "Dr. Naveen Kumar", "Tue", "09:30", "11:00", "Lecture", false, null, "CSE(CS)"),
                                new ClassOccurrence("nk_s3", "CS445", "Cyber Security Lab", "ICD-2109", "Dr. Naveen Kumar", "Tue", "11:15", "13:15", "Lab", false, null, "CSE/IT"),
                                new ClassOccurrence("nk_s4", "CS445", "Software & Cyber Security", "ICD-2109", "Dr. Naveen Kumar", "Wed", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),
                                new ClassOccurrence("nk_s5", "CCS101", "Cybersecurity Essentials", "ICD-2125", "Dr. Naveen Kumar", "Thu", "08:00", "09:30", "Lecture", false, null, "CSE(CS)")
                        ),
                        List.of("CCS101 Cybersecurity Essentials", "CS445 / IT445 Software & Cyber Security"),
                        "Cyber Security, Information Security, Malware Analysis, Network Defense"
                ),

                // 18. Dr. Pratik Shah (GN-Online)
                new FacultyMember(
                        "dr-pratik-shah",
                        "Dr. Pratik Shah",
                        "Associate Professor (GN Campus)",
                        "Electronics & Communication",
                        "GN Campus (Online to ICD)",
                        "pratik@iiitvadodara.ac.in",
                        "9978918492",
                        "GN-Online",
                        "Thu 11:00–12:00",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("ps2_nc", "AI203", "Linear Systems and Signal Processing", "ICD-2125", "Dr. Pratik Shah", "Thu", "13:15", "15:15", "Lecture", false, null, "AI/ECE"),
                        List.of(
                                new ClassOccurrence("ps2_s1", "AI203", "Linear Systems and Signal Processing", "ICD-2125", "Dr. Pratik Shah", "Thu", "13:15", "15:15", "Lecture", false, null, "AI/ECE"),
                                new ClassOccurrence("ps2_s2", "AI203", "Signal Processing Lab", "ICD-2127", "Dr. Pratik Shah", "Fri", "15:15", "17:15", "Lab", false, null, "AI/ECE")
                        ),
                        List.of("AI203 Linear Systems & Signal Processing", "EC203 Linear Systems & Signal Processing"),
                        "Signal Processing, Compressed Sensing, Image Reconstruction, Statistical Inference"
                ),

                // 19. Dr. Rahul Maurya (GN-Online)
                new FacultyMember(
                        "dr-rahul-maurya",
                        "Dr. Rahul Maurya",
                        "Assistant Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Online to ICD)",
                        "rahul_maurya@iiitvadodara.ac.in",
                        "9161216285",
                        "GN-Online",
                        "Fri 14:00–15:00",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("rm_nc", "CS603", "Data Analytics Lab", "ICD-2118", "Dr. Rahul Maurya", "Tue", "15:15", "17:15", "Lab", false, null, "M.Tech"),
                        List.of(
                                new ClassOccurrence("rm_s1", "CS603", "Data Analytics Lab", "ICD-2118", "Dr. Rahul Maurya", "Tue", "15:15", "17:15", "Lab", false, null, "M.Tech"),
                                new ClassOccurrence("rm_s2", "CS603", "Data Analytics", "ICD-2109", "Dr. Rahul Maurya", "Fri", "09:30", "11:30", "Lecture", false, null, "M.Tech")
                        ),
                        List.of("CS603 Data Analytics (M.Tech PE1)"),
                        "Data Analytics, Big Data Systems, Machine Learning Pipelines"
                ),

                // 20. Dr. Ranjeet Pratap Singh (GN-Online)
                new FacultyMember(
                        "dr-ranjeet-pratap-singh",
                        "Dr. Ranjeet Pratap Singh",
                        "Assistant Professor (GN Campus)",
                        "Electronics & Communication",
                        "GN Campus (Online to ICD)",
                        "ranjeet_singh@iiitvadodara.ac.in",
                        "9926257872",
                        "GN-Online",
                        "Tue 11:00–12:00",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("rps_nc", "EC205", "Antenna & Wave Propagation", "ICD-2118", "Dr. Ranjeet Pratap Singh", "Tue", "13:45", "15:15", "Lecture", false, null, "ECE"),
                        List.of(
                                new ClassOccurrence("rps_s1", "EC205", "Antenna & Wave Propagation", "ICD-2118", "Dr. Ranjeet Pratap Singh", "Tue", "13:45", "15:15", "Lecture", false, null, "ECE"),
                                new ClassOccurrence("rps_s2", "EC265", "Antenna Lab", "ICD-2223-EL", "Dr. Ranjeet Pratap Singh", "Wed", "09:00", "11:00", "Lab", false, null, "ECE"),
                                new ClassOccurrence("rps_s3", "EC205", "Antenna & Wave Propagation", "ICD-2118", "Dr. Ranjeet Pratap Singh", "Fri", "11:15", "12:45", "Lecture", false, null, "ECE")
                        ),
                        List.of("EC205 Antenna and Wave Propagation", "EC265 Antenna Lab"),
                        "Electromagnetics, Antennas, Microwave Circuits"
                ),

                // 21. Dr. Ravi Nahta (GN-Hybrid + GN-Online)
                new FacultyMember(
                        "dr-ravi-nahta",
                        "Dr. Ravi Nahta",
                        "Assistant Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Hybrid/Online to ICD)",
                        "ravi_nahta@iiitvadodara.ac.in",
                        "9680852684",
                        "GN-Hybrid + GN-Online",
                        "Wed 15:30–16:30",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("rn_nc", "CS443", "System Administration", "ICD-2127", "Dr. Ravi Nahta", "Wed", "08:00", "09:30", "Lecture", false, null, "MCA"),
                        List.of(
                                new ClassOccurrence("rn_s1", "CS443", "System Administration", "ICD-2127", "Dr. Ravi Nahta", "Wed", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("rn_s2", "CCS103", "System Administration Lab", "ICD-2109", "Dr. Ravi Nahta", "Wed", "11:15", "15:00", "Lab", false, null, "CSE(CS)"),
                                new ClassOccurrence("rn_s3", "CS443", "System Administration Lab", "ICD-2127", "Dr. Ravi Nahta", "Wed", "11:15", "13:15", "Lab", false, null, "MCA"),
                                new ClassOccurrence("rn_s4", "CS443", "System Administration", "ICD-2127", "Dr. Ravi Nahta", "Fri", "09:30", "11:00", "Lecture", false, null, "MCA")
                        ),
                        List.of("CCS103 System Administration & Maintenance", "CS443 System Administration (MCA)"),
                        "System Administration, Linux Kernels, DevOps, Cloud Infrastructure"
                ),

                // 22. Dr. Pramit Mazumder (GN-Hybrid)
                new FacultyMember(
                        "dr-pramit-mazumder",
                        "Dr. Pramit Mazumder",
                        "Assistant Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Hybrid to ICD)",
                        "pramit.mazumdar@iiitvadodara.ac.in",
                        "9337167027",
                        "GN-Hybrid",
                        "Wed 15:30–16:30",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("pm_nc", "CS445", "Basics of Web Programming", "ICD-2127", "Dr. Pramit Mazumder", "Tue", "09:30", "11:30", "Lecture", false, null, "MCA"),
                        List.of(
                                new ClassOccurrence("pm_s1", "CS445", "Basics of Web Programming", "ICD-2127", "Dr. Pramit Mazumder", "Tue", "09:30", "11:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("pm_s2", "CS445", "Web Programming Lab", "ICD-2127", "Dr. Pramit Mazumder", "Tue", "13:00", "15:00", "Lab", false, null, "MCA"),
                                new ClassOccurrence("pm_s3", "CS543", "Web Services", "ICD-2231", "Dr. Pramit Mazumder", "Wed", "08:00", "10:00", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("pm_s4", "CS543", "Web Services Tutorial", "ICD-2231", "Dr. Pramit Mazumder", "Wed", "11:30", "12:30", "Tutorial", false, null, "MCA"),
                                new ClassOccurrence("pm_s5", "CS543", "Web Services Lab", "ICD-2231", "Dr. Pramit Mazumder", "Wed", "13:15", "15:15", "Lab", false, null, "MCA")
                        ),
                        List.of("CS445 Basics of Web Programming (MCA)", "CS543 Web Services (MCA)"),
                        "Service-Oriented Architectures, Web Services, Distributed Software Systems"
                ),

                // 23. Dr. Rishabh Gupta (GN-Hybrid)
                new FacultyMember(
                        "dr-rishabh-gupta",
                        "Dr. Rishabh Gupta",
                        "Assistant Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Hybrid to ICD)",
                        "rishabh_gupta@iiitvadodara.ac.in",
                        "7065485086",
                        "GN-Hybrid",
                        "Thu 15:30–16:30",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("rg_nc", "CS545", "Software Engineering Tutorial", "ICD-2231", "Dr. Rishabh Gupta", "Thu", "11:15", "12:15", "Tutorial", false, null, "MCA"),
                        List.of(
                                new ClassOccurrence("rg_s1", "CS545", "Software Engineering Tutorial", "ICD-2231", "Dr. Rishabh Gupta", "Thu", "11:15", "12:15", "Tutorial", false, null, "MCA"),
                                new ClassOccurrence("rg_s2", "CS545", "Software Engineering Lab", "ICD-2231", "Dr. Rishabh Gupta", "Thu", "13:00", "15:00", "Lab", false, null, "MCA"),
                                new ClassOccurrence("rg_s3", "CS545", "Software Engineering", "ICD-2231", "Dr. Rishabh Gupta", "Fri", "09:30", "11:30", "Lecture", false, null, "MCA")
                        ),
                        List.of("CS545 Software Engineering (MCA Sem-III)"),
                        "Software Engineering, Agile Methodologies, Software Testing and Reliability"
                ),

                // 24. Dr. Sanjay Saxena (GN-Hybrid)
                new FacultyMember(
                        "dr-sanjay-saxena",
                        "Dr. Sanjay Saxena",
                        "Assistant Professor (GN Campus)",
                        "Computer Science & Engineering",
                        "GN Campus (Hybrid to ICD)",
                        "sanjay_saxena@iiitvadodara.ac.in",
                        "9839677691",
                        "GN-Hybrid",
                        "Thu 15:30–16:30",
                        "Available Online",
                        "AVAILABLE",
                        new ClassOccurrence("sax_nc", "CS441", "Computer Programming", "ICD-2127", "Dr. Sanjay Saxena", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA"),
                        List.of(
                                new ClassOccurrence("sax_s1", "CS441", "Computer Programming", "ICD-2127", "Dr. Sanjay Saxena", "Tue", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("sax_s2", "CS441", "Computer Programming", "ICD-2127", "Dr. Sanjay Saxena", "Thu", "08:00", "09:30", "Lecture", false, null, "MCA"),
                                new ClassOccurrence("sax_s3", "CS441", "Programming Lab", "ICD-2127", "Dr. Sanjay Saxena", "Thu", "11:15", "12:15", "Lab", false, null, "MCA"),
                                new ClassOccurrence("sax_s4", "CS441", "Programming Lab", "ICD-2127", "Dr. Sanjay Saxena", "Thu", "13:00", "15:00", "Lab", false, null, "MCA")
                        ),
                        List.of("CS441 Computer Programming & Problem Solving (MCA)"),
                        "High Performance Computing, Programming Models, Parallel Algorithms"
                ),

                // 25. Dr. Karan Verma (External-Online)
                new FacultyMember(
                        "dr-karan-verma",
                        "Dr. Karan Verma",
                        "Guest Faculty (NIT Delhi)",
                        "Computer Science & Engineering",
                        "Online Guest (NIT Delhi)",
                        "karan_verma@nitdelhi.ac.in",
                        "External",
                        "External-Online",
                        "Wed 17:00–18:00 (Google Meet)",
                        "External Guest",
                        "AVAILABLE",
                        new ClassOccurrence("kv_nc", "CS447", "Software Project Management", "ICD-2109", "Dr. Karan Verma", "Wed", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                        List.of(
                                new ClassOccurrence("kv_s1", "CS447", "Software Project Management Lab", "ICD-2108", "Dr. Karan Verma", "Mon", "13:00", "15:00", "Lab", false, null, "CSE/IT"),
                                new ClassOccurrence("kv_s2", "CS447", "Software Project Management", "ICD-2109", "Dr. Karan Verma", "Wed", "15:15", "16:45", "Lecture", false, null, "CSE/IT"),
                                new ClassOccurrence("kv_s3", "CS447", "Software Project Management", "ICD-2109", "Dr. Karan Verma", "Thu", "15:15", "16:45", "Lecture", false, null, "CSE/IT")
                        ),
                        List.of("CS447 / IT447 Software Project Management (B.Tech Sem-7)"),
                        "Software Project Management, Empirical Software Engineering"
                ),

                // 26. Prof. Atul Verma (External-Online)
                new FacultyMember(
                        "prof-atul-verma",
                        "Prof. Atul Verma",
                        "Guest Professor (ISRO)",
                        "Remote Sensing & Space Technology",
                        "Online Guest (ISRO SAC)",
                        "atul_verma@isro.gov.in",
                        "External",
                        "External-Online",
                        "Thu 18:30–19:30 (Virtual)",
                        "External Guest",
                        "AVAILABLE",
                        new ClassOccurrence("av_nc", "CS449", "Introduction to Remote Sensing", "ICD-2109", "Prof. Atul Verma", "Tue", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),
                        List.of(
                                new ClassOccurrence("av_s1", "CS449", "Remote Sensing", "ICD-2109", "Prof. Atul Verma", "Tue", "16:45", "18:15", "Lecture", false, null, "CSE/IT"),
                                new ClassOccurrence("av_s2", "CS449", "Remote Sensing", "ICD-2109", "Prof. Atul Verma", "Thu", "16:45", "18:15", "Lecture", false, null, "CSE/IT")
                        ),
                        List.of("CS449 / IT449 Introduction to Remote Sensing"),
                        "Satellite Image Processing, Remote Sensing, Geospatial Intelligence, GIS"
                ),

                // 27. Prof. Harendra (External-Online)
                new FacultyMember(
                        "prof-harendra",
                        "Prof. Harendra",
                        "Visiting Professor (Delhi University)",
                        "Computer Science & Systems",
                        "Online Guest (Delhi University)",
                        "harendra@du.ac.in",
                        "External",
                        "External-Online",
                        "Mon 18:30–19:30 (Virtual)",
                        "External Guest",
                        "AVAILABLE",
                        new ClassOccurrence("hnd_nc", "IT403", "Cloud System Management", "ICD-2127", "Prof. Harendra", "Mon", "16:45", "18:15", "Lecture", false, null, "IT"),
                        List.of(
                                new ClassOccurrence("hnd_s1", "IT403", "Cloud System Management", "ICD-2127", "Prof. Harendra", "Mon", "16:45", "18:15", "Lecture", false, null, "IT"),
                                new ClassOccurrence("hnd_s2", "IT403", "Cloud System Management", "ICD-2109", "Prof. Harendra", "Fri", "15:15", "16:45", "Lecture", false, null, "IT")
                        ),
                        List.of("IT403 Cloud System Management"),
                        "Cloud Computing Architectures, Distributed Virtualization, Infrastructure Reliability"
                )
        );
    }

    @Override
    public FacultyMember getFacultyMember(String id) {
        if (id != null) {
            String lower = id.toLowerCase();
            if (lower.contains("mehta")) id = "dr-abhisek-paul";
            else if (lower.contains("iyer")) id = "dr-gaurav-pareek";
            else if (lower.contains("kulkarni")) id = "dr-deepika-gupta";
            else if (lower.contains("n-patel") || lower.contains("patel")) id = "dr-venkata-phanikrishna";
        }
        final String searchId = id;
        return getFacultyDirectory().stream()
                .filter(f -> searchId != null && (f.id().equalsIgnoreCase(searchId) || f.name().toLowerCase().contains(searchId.toLowerCase())))
                .findFirst()
                .orElse(getFacultyDirectory().getFirst());
    }

    @Override
    public DashboardData getDashboardData() {
        return new DashboardData(
                getStudentProfile(),
                "Tue, 10 Nov 2026",
                "Week 10 · Odd Semester",
                getAttendanceSummary(),
                getTodaySchedule(),
                getPendingTasks(),
                2,
                1,
                getAnnouncements(),
                getClassrooms(),
                getUpcomingEvents(),
                getTodayMealMenu()
        );
    }
}
