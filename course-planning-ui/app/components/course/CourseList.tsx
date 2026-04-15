import { Button, Card, Col, Grid, Row, Select, Space, Table } from "antd";
import { useEffect, useState } from "react";
import { useNavigate, useOutletContext } from "react-router-dom";
import { CourseDto } from "../../types/types";
import { coursesApi } from "../../api/api";


const { useBreakpoint } = Grid;

export default function CourseList()
{
    const navigate = useNavigate();
    const screens = useBreakpoint();
    const isMobile = !screens.md; // md breakpoint = tablet+
    const [courses, setCourses] = useState<CourseDto[]>([]);
    const [loading, setLoading] = useState(false);
    const [grade, setGrade] = useState<number | undefined>();
    const [semester, setSemester] = useState<number | undefined>();

  

    useEffect(() => {
        const fetchCourses = async () => {
            setLoading(true);
            try {
                const data = await coursesApi.getAll(grade, semester);
                setCourses(data);
            } catch (err) {
                console.error(err);
            }
            setLoading(false);
         };
        fetchCourses();
    }, [grade, semester]);

    const columns = [
        { title: "Code", dataIndex: "code" },
        { title: "Name", dataIndex: "name" },
        { title: "Credits", dataIndex: "credits" },
        {
            title: "Grade",
            render: (r: CourseDto) => `${r.gradeLevelMin}-${r.gradeLevelMax}`,
        },
        { title: "Semester", dataIndex: "semesterOrder" },
        {
            title: "Action",
            dataIndex: "action",
            render: (_: any, record: CourseDto) => (
                <a onClick={() => navigate(`/courses/${record.id}`)}>
                    View
                </a>
    ),
  },
    ];

  return (
    <Card>
        {/* Filters */}
        <Space style={{ marginBottom: 16 }} wrap>
            <Select
                placeholder="Select Grade"
                onChange={(value) => setGrade(value)}
                allowClear
                style={{ width: 150 }}
                >
                <Select.Option value={9}>Grade 9</Select.Option>
                <Select.Option value={10}>Grade 10</Select.Option>
                <Select.Option value={11}>Grade 11</Select.Option>
                <Select.Option value={12}>Grade 12</Select.Option>
            </Select>

            <Select
                placeholder="Semester"
                allowClear
                style={{ width: 150 }}
                onChange={setSemester}
            >
                <Select.Option value={1}>Semester 1</Select.Option>
                <Select.Option value={2}>Semester 2</Select.Option>
            </Select>
        </Space>
      {isMobile ? (
        <Row gutter={[16, 16]}>
          {courses.map((course) => (
            <Col xs={24} key={course.id}>
              <Card 
                size="small" 
                onClick={() => navigate(`/courses/${course.id}`)}
                hoverable
                extra={<Button type="link">More</Button>}
              >
                <h3>{course.name}</h3>
                <p><strong>Code:</strong> {course.code}</p>
                <p><strong>Credits:</strong> {course.credits}</p>
                <p>
                  <strong>Grade:</strong>{" "}
                  {course.gradeLevelMin} - {course.gradeLevelMax}
                </p>
                <p><strong>Semester:</strong> {course.semesterOrder}</p>
                <p>
                  <strong>Prerequisite:</strong>{" "}
                  {course.prerequisiteName || "None"}
                </p>
              </Card>
            </Col>
          ))}
        </Row>
      ) : (
        <Table
          rowKey="id"
          columns={columns}
          dataSource={courses}
          loading={loading}
          bordered
        />
      )}
    </Card>
  );
}
