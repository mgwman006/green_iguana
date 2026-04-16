import {Row, Col, DescriptionsProps, Descriptions} from "antd";
import {useStudent} from "../../store/student/StudentContext";



export default function ProfileCard() {
    const {state} = useStudent();
    const student = state.profile;
    if (!student)
    {
        return ;
    }

    const items: DescriptionsProps['items'] = [
        {
            key: '1',
            label: 'First Name',
            children: student.firstName,
        },
        {
            key: '2',
            label: 'Email',
            children: student.email,
        },
        {
            key: '3',
            label: 'Grade Level',
            children: student.gradeLevel,
        },
        {
            key: '4',
            label: 'Gpa',
            children: student.gpa,
        },
        {
            key: '5',
            label: 'Address',
            children: 'No. 18, Wantang Road, Xihu District, Hangzhou, Zhejiang, China',
        },
    ];

    return (
            <Row gutter={[24, 24]} align="middle">

                <Col xs={24} sm={24} md={18} lg={18} xl={18}>
                    <Descriptions title="User Info" items={items} />
                </Col>
            </Row>
    );
}