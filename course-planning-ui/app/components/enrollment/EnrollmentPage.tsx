import { Outlet } from "react-router";
import { useEffect, useState } from "react";
import { SemesterDto } from "../../types/types";
import { semesterApi } from "../../api/api";
import { message, Card, Typography, Space, Tag, Spin, Row, Col} from "antd";

const { Title, Text } = Typography;

export default function EnrollmentPage() {
    const [semester, setSemester] = useState<SemesterDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [messageApi, messageContextHolder] = message.useMessage();

    useEffect(() => {
        const loadSemester = async () => {
            try {
                const data = await semesterApi.getActiveSemester();
                setSemester(data);
            } catch (err: any) {
                messageApi.error(err);
            } finally {
                setLoading(false);
            }
        };

        loadSemester();
    }, []);

    const formatDate = (date: string) =>
        new Date(date).toLocaleDateString();

    return (
        <div style={{ padding: 24 }}>
            {messageContextHolder}

            <Card
                style={{
                    marginBottom: 24,
                    borderRadius: 16,
                    boxShadow: "0 8px 24px rgba(0,0,0,0.08)",
                }}
            >
                {loading ? (
                    <Spin />
                ) : semester && (
                    <Space direction="vertical" size={16} style={{ width: "100%" }}>
                        <div>
                            <Text type="secondary">Current Enrollment Semester</Text>

                            <Title level={2} style={{ margin: 0 }}>
                                {semester.name} - {semester.year}
                            </Title>

                            <Space wrap>
                                <Tag color="blue">Semester {semester.orderInYear}</Tag>
                                <Tag color={semester.isActive ? "green" : "default"}>
                                    {semester.isActive ? "Active" : "Inactive"}
                                </Tag>
                            </Space>
                        </div>

                        <Row gutter={[16, 16]}>
                            <Col xs={24} md={12}>
                                <Card size="small">
                                    <Text type="secondary">Start Date</Text>
                                    <div>{formatDate(semester.startDate)}</div>
                                </Card>
                            </Col>

                            <Col xs={24} md={12}>
                                <Card size="small">
                                    <Text type="secondary">End Date</Text>
                                    <div>{formatDate(semester.endDate)}</div>
                                </Card>
                            </Col>
                        </Row>
                    </Space>
                )}
            </Card>

            <Outlet />
        </div>
    );
}