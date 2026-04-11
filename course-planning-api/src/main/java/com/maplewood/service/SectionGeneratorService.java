package com.maplewood.service;

import org.springframework.stereotype.Service;

@Service
public class SectionGeneratorService {

//  private final TeacherRepository teacherRepo;
//  private final ClassroomRepository classroomRepo;
//  private final SectionRepository sectionRepo;
//
//  public SectionGeneratorService(
//    TeacherRepository teacherRepo,
//    ClassroomRepository classroomRepo,
//    SectionRepository sectionRepo) {
//    this.teacherRepo = teacherRepo;
//    this.classroomRepo = classroomRepo;
//    this.sectionRepo = sectionRepo;
//  }
//
//  public void generateSections(List<Course> courses, Semester semester) {
//
//    for (Course course : courses) {
//
//      int hours = course.getHoursPerWeek();
//
//      List<List<TimeSlot>> patterns =
//        TimeSlotProvider.getPatterns(hours);
//
//      for (List<TimeSlot> pattern : patterns) {
//
//        Optional<Teacher> teacherOpt =
//          findAvailableTeacher(course, pattern);
//
//        Optional<Classroom> roomOpt =
//          findAvailableRoom(course, pattern);
//
//        if (teacherOpt.isEmpty() || roomOpt.isEmpty()) {
//          continue; // skip this pattern
//        }
//
//        Section section = new Section();
//        section.setCourse(course);
//        section.setSemester(semester);
//        section.setTeacher(teacherOpt.get());
//        section.setClassroom(roomOpt.get());
//        section.setCapacity(roomOpt.get().getCapacity());
//        section.setEnrolledCount(0);
//
//        section = sectionRepo.save(section);
//
//        // attach time slots
//        List<TimeSlot> slots = new ArrayList<>();
//
//        for (TimeSlot ts : pattern) {
//          TimeSlot slot = new TimeSlot();
//          slot.setSection(section);
//          slot.setDay(ts.getDay());
//          slot.setHour(ts.getHour());
//          slots.add(slot);
//        }
//
//        section.setTimeSlots(slots);
//
//        sectionRepo.save(section);
//      }
//    }
//
//    @Service
//    public class SectionGeneratorService {
//
//      private final TeacherRepository teacherRepo;
//      private final ClassroomRepository classroomRepo;
//      private final SectionRepository sectionRepo;
//
//      public SectionGeneratorService(
//        TeacherRepository teacherRepo,
//        ClassroomRepository classroomRepo,
//        SectionRepository sectionRepo) {
//        this.teacherRepo = teacherRepo;
//        this.classroomRepo = classroomRepo;
//        this.sectionRepo = sectionRepo;
//      }
//
//      public void generateSections(List<Course> courses, Semester semester) {
//
//        for (Course course : courses) {
//
//          int hours = course.getHoursPerWeek();
//
//          List<List<TimeSlot>> patterns =
//            TimeSlotProvider.getPatterns(hours);
//
//          for (List<TimeSlot> pattern : patterns) {
//
//            Optional<Teacher> teacherOpt =
//              findAvailableTeacher(course, pattern);
//
//            Optional<Classroom> roomOpt =
//              findAvailableRoom(course, pattern);
//
//            if (teacherOpt.isEmpty() || roomOpt.isEmpty()) {
//              continue; // skip this pattern
//            }
//
//            Section section = new Section();
//            section.setCourse(course);
//            section.setSemester(semester);
//            section.setTeacher(teacherOpt.get());
//            section.setClassroom(roomOpt.get());
//            section.setCapacity(roomOpt.get().getCapacity());
//            section.setEnrolledCount(0);
//
//            section = sectionRepo.save(section);
//
//            // attach time slots
//            List<TimeSlot> slots = new ArrayList<>();
//
//            for (TimeSlot ts : pattern) {
//              TimeSlot slot = new TimeSlot();
//              slot.setSection(section);
//              slot.setDay(ts.getDay());
//              slot.setHour(ts.getHour());
//              slots.add(slot);
//            }
//
//            section.setTimeSlots(slots);
//
//            sectionRepo.save(section);
//          }
//        }
//      }
//
//      private Optional<Classroom> findAvailableRoom(Course course, List<TimeSlot> slots) {
//
//        List<Classroom> rooms =
//          classroomRepo.findByRoomTypeId(course.getSpecializationId());
//
//        for (Classroom r : rooms) {
//
//          boolean conflict = false;
//
//          List<Section> assigned = sectionRepo.findByClassroom(r);
//
//          for (Section s : assigned) {
//            for (TimeSlot a : s.getTimeSlots()) {
//              for (TimeSlot b : slots) {
//
//                if (a.getDay() == b.getDay() &&
//                  a.getHour() == b.getHour()) {
//                  conflict = true;
//                  break;
//                }
//              }
//            }
//          }
//
//          if (!conflict) return Optional.of(r);
//        }
//
//        return Optional.empty();
//      }
  }
