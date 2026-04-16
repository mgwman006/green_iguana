import { useStudent } from "../../store/student/StudentContext";
import {Card, Grid, message, Table, TableColumnsType, Tag} from "antd";
import { Enrollment } from "../../types/types";

const { useBreakpoint } = Grid;

const columns: TableColumnsType<Enrollment> = [
  {
    title: "Course",
    render: (_:any,record: Enrollment) => (
        <>
            {record.section.courseName}
        </>
    )
  },
  {
    title: "Teacher",
    render: (_:any,record: Enrollment) => (
        <>
            {record.section.teacherName}
        </>
    )
  },
  {
    title: "Schedule",
    render: (_: any, record: Enrollment) => (
      <>
        {record.section.timeSlots.map((t, i) => (
          <Tag key={i}>
            {t} 
          </Tag>
        ))}
      </>
    ),
  },
  {
    title: "Status",
    render: () => <Tag color="green">Enrolled</Tag>,
  },
  // Future feature
  // {
  //   title: "Action",
  //   render: () => <Button danger>Drop</Button>
  // }
];
export default function EnrollmentList()
{
    const screens = useBreakpoint();
    const isMobile = !screens.md;
    const { state } = useStudent();
    const enrollments = state.profile?.enrollments || [];
    const [messageApi, messageContextHolder] = message.useMessage();

    return (
        <div>
            {messageContextHolder}
            {isMobile && (
                <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                    {enrollments.map((e) => (
                    <Card key={e.section.id} title={e.section.courseName}>
                        <p><b>Teacher:</b> {e.section.teacherName}</p>

                        <p>
                            <Tag color="green">Enrolled</Tag>
                        </p>

                        <div style={{ marginTop: 10 }}>
                        {e.section.timeSlots.map((t, i) => (
                            <Tag key={i}>
                                {t}
                            </Tag>
                        ))}
                        </div>

                        {/* Optional future */}
                        {/* <Button danger block style={{ marginTop: 10 }}>Drop</Button> */}
                    </Card>
                    ))}
                </div>
            )}
            {!isMobile && (
            <Card title="My Schedule">
                <Table
                    rowKey="sectionId"
                    dataSource={enrollments}
                    columns={columns}
                    pagination={false}
                />
            </Card>
            )}
        </div>
    );
}