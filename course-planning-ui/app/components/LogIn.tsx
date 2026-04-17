import { Button, Col, Flex, Form, Input, Row } from "antd";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { RightOutlined, UserOutlined } from '@ant-design/icons';
import { useStudent } from "../store/student/StudentContext";
import { studentsApi } from "../api/api";



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
        
        dispatch({ type: "FETCH_START" });
        try {
            const profile = await studentsApi.getById(studentId);
            dispatch({type: "FETCH_SUCCESS",payload: profile,});
        } catch (err: any) {
            dispatch({type: "FETCH_ERROR",payload: err.message ?? err,});
        }
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
                                    { required: true, message: 'Student Id is required!' },
                                    {
                                        validator: (_, value) => {
                                            if (!value) return Promise.resolve();

                                            const num = Number(value);
                                            if (!isNaN(num) && num > 0) {
                                            return Promise.resolve();
                                            }

                                            return Promise.reject(new Error("Please enter a valid Student ID"));

                                        },
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