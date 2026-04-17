import { useStudent } from "../../store/student/StudentContext";
import {Button, Card, Empty, Grid, message, Popconfirm, Table, TableColumnsType, Tag, Typography} from "antd";
import { Enrollment } from "../../types/types";
import {enrollmentsApi} from "../../api/api";

const { useBreakpoint } = Grid;


export default function EnrollmentList()
{
    const screens = useBreakpoint();
    const isMobile = !screens.md;
    const { state, dispatch } = useStudent();
    const enrollments = state.profile?.enrollments || [];
    const [messageApi, messageContextHolder] = message.useMessage();

    const deregister = async (enrollmentId: number) => {
        try {
            await enrollmentsApi.deregister(enrollmentId);
            dispatch({
                type: "REMOVE_ENROLLMENT",
                payload: enrollmentId,
            });
            messageApi.success("Course deregistered successfully");
        } catch (err: any) {
            messageApi.error(err?.message ?? "Failed to deregister");
        }
    };

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
                    {record.section.timeSlots.map((t) => (
                        <Tag key={t}>
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
        {
            title: "Action",
            render: (_, record) => (
                <Popconfirm
                    title="Deregister Course"
                    description="Are you sure you want to remove this course?"
                    okText="Yes"
                    cancelText="No"
                    onConfirm={() => deregister(record.id)}
                >
                    <Button danger size="small">
                        Deregister
                    </Button>
                </Popconfirm>
            ),
        },
    ];
    return (
        <div>
            {messageContextHolder}

            {isMobile && (
                <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>

                    <Typography.Title level={4} style={{ marginBottom: 8 }}>
                        My Schedule
                    </Typography.Title>

                    {enrollments.length === 0 ? (
                        <Empty description="No courses enrolled yet" />
                    ) : (
                        enrollments.map((e) => (
                            <Card key={e.id} title={e.section.courseName}>
                                <p><b>Teacher:</b> {e.section.teacherName}</p>

                                <p>
                                    <Tag color="green">Enrolled</Tag>
                                </p>

                                <div style={{ marginTop: 10 }}>
                                    {e.section.timeSlots.map((t) => (
                                        <Tag key={t}>{t}</Tag>
                                    ))}
                                </div>

                                <Popconfirm
                                    title="Deregister Course"
                                    description="Are you sure?"
                                    okText="Yes"
                                    cancelText="No"
                                    onConfirm={() => deregister(e.id)}
                                >
                                    <Button danger block>
                                        Deregister
                                    </Button>
                                </Popconfirm>
                            </Card>
                        ))
                    )}
                </div>
            )}

            {!isMobile && (
            <Card title="My Schedule">
                <Table
                    rowKey="id"
                    dataSource={enrollments}
                    columns={columns}
                    pagination={false}
                />
            </Card>
            )}
        </div>
    );
}