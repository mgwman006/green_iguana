import { Button, Card, Empty, Grid, List, Table, Tabs, TabsProps, Tag } from "antd";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeftOutlined } from '@ant-design/icons';
import { CourseDto, SectionDto } from "../../types/types";
import { coursesApi } from "../../api/api";
import { useStudent } from "../../store/student/StudentContext";

const { useBreakpoint } = Grid;

  

export default function CourseDetails()
{
    const screens = useBreakpoint();
    const isMobile = !screens.md;
    const navigate = useNavigate();
    const { courseId } = useParams();
    const [course, setCourse] = useState<CourseDto | null>(null);
    const { state } = useStudent();
    const studentId = state.profile?.id;
    const [enrollingId, setEnrollingId] = useState<number | null>(null);

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
        fetchCourse();
    }, [courseId]);
   
    if(!course)
        return;

    const fullCheck = (section: SectionDto) => section.enrolledCount >= section.capacity;
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
      title: "Availabe City",
      render: (_: any, record: SectionDto) =>
        <Tag color={record.availableSeats === 0 ? "red" : "green"}>
            {record.availableSeats} seats left
        </Tag>
    },
    {
      title: "Time Slots",
      render: (_: any, record: SectionDto) => (
        <>
          {record.timeSlots.map((t, i) => (
            <Tag key={i}>
                {t}
            </Tag>
          ))}
        </>
      )
    },
    {
      title: "Action",
      render: (_: any, record: SectionDto) => {
        const full = fullCheck(record);

        return (
          <Button type="primary" disabled={full}>
            {full ? "Full" : "Enroll"}
          </Button>
        );
      }
    }
  ];
 

  return (
   <div>
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

         
        <>
            {/* MOBILE VIEW */}
            {isMobile && course?.sections && (
            <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                {course.sections.map((section) => {
                const full = fullCheck(section);

                return (
                    <Card key={section.id} title={section.teacherName} bordered>
                    <p>{section.classroomName}</p>

                    <p>
                        Available Seats:{" "}
                        <Tag color={section.availableSeats === 0 ? "red" : "green"}>
                        {section.availableSeats} seats left
                        </Tag>
                    </p>

                    <div style={{ marginBottom: 10 }}>
                        {section.timeSlots.map((t, i) => (
                        <Tag key={i}>
                            {t}
                        </Tag>
                        ))}
                    </div>

                    <Button type="primary" disabled={full} block>
                        {full ? "Full" : "Enroll"}
                    </Button>
                    </Card>
                );
                })}
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
            {/* Empty */}
            {course?.sections?.length === 0 && (
                <Empty description="No sections available" />
            )}
        </>

   </div>
  );
}
