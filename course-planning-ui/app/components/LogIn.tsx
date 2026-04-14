import { Button, Col, Flex, Form, Input, notification, Row } from "antd";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RightOutlined, UserOutlined } from '@ant-design/icons';
import { useStudent } from "../store/student/StudentContext";
import { fetchStudentProfile } from "../service/studentService";



export default function LogIn()
{
    const navigate = useNavigate();
    const { state, dispatch } = useStudent();
    

    const onFinish =  async (values:any) => {

        const studentId = Number(values.studentId);
        if (!studentId || studentId<= 0) 
        {
            return;
        }
        await fetchStudentProfile(dispatch, studentId);
    }

    useEffect(() => {
        if (state.profile) {
            navigate("/dashboard");
        }
    }, [state.profile, navigate]);

    return (
        <div
            style={
                {
                    height:"100vh",
                    alignContent:"center"
                }
            }
        >
            {/* Error State */}
            {state.error && (
                <p style={{ color: "red", marginTop: "10px",textAlign:'center' }}>
                    {state.error}
                </p>
            )}
            
            <Row
                justify={"center"}
            >
                <Col xs={20} sm={6} lg={6} xl={6} xxl={6}>

                    <Flex vertical align="center" style={{ marginBottom: "24px" }}>
                        <p>Please enter Student ID to proceed</p>
                    </Flex>
                    
                    <Form
                        size="large"
                        name="login"
                        onFinish={onFinish}
                        >
                            <Form.Item
                                name="studentId"
                                rules={[
                                    { required: true, message: 'Student Id is rquired!' },
                                    {
                                        type: "number",
                                        min: 1,
                                        message: "Please enter a valid Student ID",
                                    }
                                ]}
                            >
                                <Input
                                    prefix={<UserOutlined />}
                                    placeholder="Enter Student ID"
                                />
                            </Form.Item>

                            <Form.Item>
                                <Button 
                                    block 
                                    type="primary" 
                                    htmlType="submit"
                                    loading={state.loading}
                                >
                                    Next {<RightOutlined />}
                                </Button>                                    
                            </Form.Item>


                    </Form>
                </Col>
            </Row>
        </div>
    );
}