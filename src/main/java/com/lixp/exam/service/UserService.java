package com.lixp.exam.service;

import com.lixp.exam.bean.*;
import com.lixp.exam.dao.StudentUserMapper;
import com.lixp.exam.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentUserMapper studentUserMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExamInfoService examInfoService;

    /**
     * 用户登录，从数据表查数据
     * @param username 用户名
     * @param password 密码
     * @param usertype 身份 1为学生2为老师
     * @return
     */
    public UserInfo selectUser(String username, String password, String usertype){
        UserInfo userInfo=new UserInfo();

        //如果是学生
        if (usertype.equals("1")){
            StudentUser studentUser = new StudentUser();
            studentUser.setsUsername(username);
            List<StudentUser> studentUsers = studentUserMapper.select(studentUser);//查询学生

            if (CollectionUtils.isEmpty(studentUsers)){//如果为空
                return userInfo;
            }
            StudentUser s1 = studentUsers.get(0);
            if (password.equals(s1.getsPassword())){
                userInfo.setId(s1.getId());
                userInfo.setUsername(username);
                userInfo.setRole("user");
            }
        }

        //如果是老师
        if (usertype.equals("2")){
            User user = userMapper.selectOne(new User(username,password));//查询出这个用户
            if (user==null){return userInfo;}
            userInfo.setId(user.getUid());
            userInfo.setUsername(user.getUsername());
            userInfo.setRole(user.getRoles());
        }

        return userInfo;
    }

//    public String selectIdcardBy(){
//
//    }
    /**
     * 添加新学生用户，用于学生注册
     * @param studentUser 学生注册信息
     * @return
     */
    @Transactional
    public Boolean addStudentUser(StudentUser studentUser) {

        StudentUser su=new StudentUser();
        su.setsUsername(studentUser.getsUsername());
        List<StudentUser> users = studentUserMapper.select(su);//查询邮箱是否被用过

        if (CollectionUtils.isEmpty(users)){//没被用过的话
            try {
                studentUserMapper.insertSelective(studentUser);//插入学生用户

                Student student=new Student();
                student.setsCardNo(studentUser.getsCardNo());
                studentService.saveStudent(student);//同时插入考生
            }catch(Exception e){
                return false;
            }
            return true;
        }

        return false;
    }


    /**
     * 添加新学生用户，用于学生找回密码
     * @param studentUser 学生注册信息
     * @return
     */
    public Boolean findStudentUserPs(StudentUser studentUser) {

        StudentUser su=new StudentUser();
        su.setsUsername(studentUser.getsUsername());
        List<StudentUser> users = studentUserMapper.select(su);//查询邮箱是否被用过

        if (!CollectionUtils.isEmpty(users)){//被用过
            try {
                StudentUser olduser = users.get(0);
                Example example=new Example(StudentUser.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("sUsername",studentUser.getsUsername());//查询邮箱一样的
                studentUser.setId(olduser.getId());
                studentUser.setCreated(new Date());
                studentUser.setsRole("user");
                studentUserMapper.updateByExample(studentUser,example);//更新
            }catch(Exception e){
                return false;
            }

            return true;
        }

        return false;
    }
}
