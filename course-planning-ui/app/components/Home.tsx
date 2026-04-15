import { Button, Col, Flex, Progress, Row,Image, Layout, Menu, Drawer, Typography, Avatar, notification, MenuProps, Dropdown, Space } from 'antd';
import { Content, Footer, Header } from 'antd/es/layout/layout';
import { isMobile, isTablet, isBrowser } from 'react-device-detect';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { LikeOutlined, MenuOutlined, MessageOutlined, ShoppingCartOutlined, StarOutlined, MailOutlined, LogoutOutlined, SettingOutlined, DownOutlined, RightOutlined } from '@ant-design/icons';
import { useEffect, useState } from 'react';
import { useStudent } from '../store/student/StudentContext';
import { STUDENT_STORAGE_KEY } from '../utilities/constant';



export default function Home() {
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);
  const { state, dispatch } = useStudent();


  useEffect(() => {
        if (!state.profile) {
            navigate("/");
        }
  }, [state.profile, navigate]);

  const goHome = () => {
    navigate("/");
  }
  const logOut = () => {
    dispatch({ type: "LOGOUT" });
    localStorage.removeItem(STUDENT_STORAGE_KEY);
    navigate("/");
  }
  const browseCourse = () => {
    navigate("/courses");
  }
   const goToEnrollment = () => {
    navigate("/enrollments");
  }


  const userItems: MenuProps['items'] = [
    {
      key: '1',
      label: 'My Account',
      disabled: true,
    },
    {
      key: '2',
      label: 'Home',
      onClick:goHome,
    },
    {
      key: '3',
      label: 'Browse Courses',
      onClick:browseCourse,
    },
    {
      key: '4',
      label: 'Enrollments',
      onClick: goToEnrollment,
    },
    {
      key: '5',
      label: state.profile?'LogOut':'LogIn',
      onClick:logOut,
    }
  ];


  return (
    <Layout >
    
      <Header
            style={{
              position: 'sticky',
              top: 0,
              zIndex: 1,
              width: '100%',
              display: 'flex',
              alignItems: 'center',
              backgroundColor:'white',
              justifyContent: 'space-between'
            
            }}
          >

             <div 
                className="demo-logo" 
                style={
                  {
                    fontSize:'20px', 
                    fontWeight:'bold'
                  }
                }
              >
                maplewood
            </div>
            
            <div>
                <MenuOutlined  onClick={() => setShowMenu(true)} style={{ fontSize:'25px'}}/>
                <Drawer
                    title="Menu"
                    placement="right"
                    onClose={() => setShowMenu(false)}
                    open={showMenu}
                    size='large'
                    >

                    <Menu
                      theme="light"
                      mode="vertical"
                      defaultSelectedKeys={['1']}
                      items={userItems}
                      style={{ flex: 1, minWidth: 0}}
                      onClick={() => setShowMenu(false)}
                    />
                </Drawer>
            </div>

          </Header>
        
      <Content >
      
        <Outlet />
        
      </Content>
      <Footer style={{textAlign:'center'}}>

        ©{new Date().getFullYear()} Created by Maneno
            
      </Footer>
    </Layout>
  );
}
