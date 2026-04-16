import { Card, Col, Empty,Progress, Row, Table, TableColumnsType, Typography } from "antd";
import { useStudent } from "../../store/student/StudentContext";
import {  useNavigate } from "react-router";
import Meta from "antd/es/card/Meta";
import { CourseHistory } from "../../types/types";
import { TrophyOutlined, BarChartOutlined } from "@ant-design/icons";
import { TOTAL_CREDITS_REQUIRED } from "../../utilities/constant";

const columns: TableColumnsType<CourseHistory> = [
            { 
                title: 'Course Name', 
                dataIndex: 'courseName',
                sorter: (a, b) => a.courseName.localeCompare(b.courseName),

            },
            { 
                title: 'Status', 
                dataIndex: 'status' ,
                filters: [
                    { text: "Passed", value: "passed" },
                    { text: "Failed", value: "failed" },
                ],
                onFilter: (value, record) => record.status === value,
                render: (status: string) => (
                                            <span style={{ color: status === "passed" ? "green" : "red" }}>
                                                {status.toUpperCase()}
                                            </span>
                                            ),
            },
    ];

export default function Dashboard()
{
    const navigate = useNavigate();
    const { state } = useStudent();

    if(!state.profile)
    {
        navigate("/");
    }
    const profile = state.profile;
    const progress = ((profile?.creditsEarned ?? 0) / TOTAL_CREDITS_REQUIRED) * 100;

    
   

    return (
        <Row
            justify={"center"}
            gutter={[16, 24]}
        >
            <Col className="gutter-row" xs={20} sm={20} lg={20} xl={20} xxl={20}>

                <Typography.Title level={3} style={{ margin: 0 }}>
                    <Typography.Text>
                        Welcome,
                    </Typography.Text>
                    {profile?.firstName} {profile?.lastName}
                </Typography.Title>

                <Typography.Text>
                    Your path to graduation
                </Typography.Text>

                <Progress percent={progress} status="active" />


            </Col>

            <Col className="gutter-row" xs={20} sm={20} lg={10} xl={10} xxl={10}>
                <Card
                    variant="borderless"
                >
                    <Meta 
                        avatar={<TrophyOutlined />}
                        title="GPA" 
                        description={profile?.gpa.toFixed(2)}
                    />
                </Card>
            </Col>
            <Col className="gutter-row" xs={20} sm={20} lg={10} xl={10} xxl={10}>
                <Card
                    variant="borderless"
                >
                    <Meta 
                        avatar={<BarChartOutlined />}
                        title="Credits" 
                        description={`${profile?.creditsEarned}/${TOTAL_CREDITS_REQUIRED}`}
                    />
                </Card>
            </Col>

            <Col className="gutter-row" xs={22} sm={22} lg={20} xl={20} xxl={20}>
                <Table<CourseHistory> 
                    bordered
                    columns={columns} 
                    dataSource={profile?.courseHistory ?? []} 
                    rowKey="id"
                    title={() => <h3>Course History</h3>}
                    locale={{
                        emptyText: <Empty description="No Course History Available" />,
                    }}
                />
            </Col>
            

        </Row>
    );
}