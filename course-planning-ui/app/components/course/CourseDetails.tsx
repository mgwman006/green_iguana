import {Button, Card, Empty, Grid, message, Table, Tag, Typography} from "antd";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeftOutlined } from '@ant-design/icons';
import { CourseDto, SectionDto } from "../../types/types";
import { coursesApi, enrollmentsApi } from "../../api/api";
import { useStudent } from "../../store/student/StudentContext";

const { useBreakpoint } = Grid;

  

export default function CourseDetails()
{
    const screens = useBreakpoint();
    const isMobile = !screens.md;
    const navigate = useNavigate();
    const { courseId } = useParams();
    const [course, setCourse] = useState<CourseDto | null>(null);
    const { state, dispatch } = useStudent();
    const studentId = state.profile?.id;
    const [enrollingId, setEnrollingId] = useState<number | null>(null);
    const [messageApi, messageContextHolder] = message.useMessage();


    useEffect(() => {
        const fetchCourse = async () => {
            try
            {
                const data = await coursesApi.getById(Number(courseId));
                setCourse(data)
            }
            catch(error)
            {
                console.error(error);
            }
        };
        void fetchCourse();
    }, [courseId]);
   
    if(!course)
        return;

    const handleEnroll = async (sectionId: number) => {
        if (!studentId) return;

        try 
        {
            setEnrollingId(sectionId);

            const data = await enrollmentsApi.enroll(studentId,sectionId);
            dispatch({
                type:"ADD_ENROLLMENT",
                payload:data
            })            
            messageApi.success("Enrolled successfully");
            //Refresh course data
            const updated = await coursesApi.getById(Number(courseId));
            setCourse(updated);

        } catch (error: any) {
            messageApi.error(error);
        } finally {
            setEnrollingId(null);
        }
    };

    const isEnrolled = (sectionId: number) => state.profile?.enrollments.some(e => e.section.id === sectionId);
    const isFull = (section: SectionDto) => section.enrolledCount >= section.capacity;
    const canEnroll = (section: SectionDto) =>  !isEnrolled(section.id) && !isFull(section);
    const getEnrollButtonLabel = (section: SectionDto) => {
        if (isEnrolled(section.id)) return "Enrolled";
        if (isFull(section)) return "Full";
        return "Enroll";
    };
    const columns = [
    {
      title: "Teacher",
      dataIndex: "teacherName"
    },
    {
      title: "Classroom",
      dataIndex: "classroomName"
    },
    {
      title: "Available Seats",
      render: (_: any, record: SectionDto) =>
        <Tag color={record.availableSeats === 0 ? "red" : "green"}>
            {record.availableSeats} seats left
        </Tag>
    },
    {
      title: "Time Slots",
      render: (_: any, record: SectionDto) => (
        <>
          {record.timeSlots.map((t) => (
            <Tag key={t}>
                {t}
            </Tag>
          ))}
        </>
      )
    },
    {
      title: "Action",
      render: (_: any, record: SectionDto) => {
        

        return (
          <Button
                type="primary"
                disabled={!canEnroll(record)}
                loading={enrollingId === record.id}
                onClick={() => handleEnroll(record.id)}
          >
              {getEnrollButtonLabel(record)}
          </Button>
        );
      }
    }
  ];
 

  return (
   <div>
        {messageContextHolder}
        <Button onClick={() => navigate(-1)}><ArrowLeftOutlined />Back </Button>
        <Card>
            <h2>{course.code} - {course.name}</h2>
            <p>{course.description}</p>

            <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                <Tag color="blue">{course.credits} Credits</Tag>
                <Tag color="green">
                    Grade {course.gradeLevelMin}-{course.gradeLevelMax}
                </Tag>
                <Tag color="purple">Semester {course.semesterOrder}</Tag>
                <Tag color="orange">{course.courseType}</Tag>
                <Tag>
                    {course.prerequisiteName ? (
                        <span>
                            Requires: <b>{course.prerequisiteName}</b>
                        </span>
                        ) : (
                        <span>No prerequisite required</span>
                    )}
                </Tag>
            </div>
        </Card>

         

        {/* MOBILE VIEW */}
        {isMobile && course?.sections && (
            <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>

                <Typography.Title level={4} style={{ marginBottom: 8 }}>
                    Available Sections
                </Typography.Title>

                {course.sections.length === 0 ? (
                    <Empty description="No sections available" />
                ) : (
                    course.sections.map((section) => (
                        <Card key={section.id} title={section.teacherName} >
                            <p>{section.classroomName}</p>

                            <p>
                                Available Seats:{" "}
                                <Tag color={section.availableSeats === 0 ? "red" : "green"}>
                                    {section.availableSeats} seats left
                                </Tag>
                            </p>

                            <div style={{ marginBottom: 10 }}>
                                {section.timeSlots.map((t) => (
                                    <Tag key={t}>{t}</Tag>
                                ))}
                            </div>

                            <Button
                                type="primary"
                                disabled={!canEnroll(section)}
                                loading={enrollingId === section.id}
                                onClick={() => handleEnroll(section.id)}
                                block
                            >
                                {getEnrollButtonLabel(section)}
                            </Button>
                        </Card>
                    ))
                )}
            </div>
        )}

        {/* DESKTOP VIEW */}
        {!isMobile && course?.sections && (
            <Card title="Available Sections">
                <Table
                    rowKey="id"
                    dataSource={course.sections}
                    columns={columns}
                    pagination={false}
                />
            </Card>
        )}


   </div>
  );
}
