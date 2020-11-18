package com.lixp.exam.controller;

import com.lixp.exam.bean.Msg;
import com.lixp.exam.bean.Specialities;
import com.lixp.exam.bean.Subject;
import com.lixp.exam.service.SpecialitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: SpecialitiesController
 * @Description: TODO
 * @Author: zsl
 * @Date: 2020/10/4 11:01
 * @Version: v1.0
 */
@RestController
public class SpecialitiesController {

    @Autowired
    SpecialitiesService specialitiesService;

    /**
     * @Description: selectSpecialities方法是查询全部专业内容
     * @param:
     * @return:
     * @auther: zsl
     * @date: 2020/10/4 16:10
     */

    @GetMapping(value ="/selectSpecialities",name="admin")
    public Msg selectSpecialities(){
        List<Specialities> specialitiesList = specialitiesService.selectSpecialities();
        return Msg.success().add("specialities",specialitiesList);
    }

    /**
     * 新增专业
     * @param specialities
     * @return
     */
    @PostMapping(value ="addSpecialities",name="admin")
    public Msg addSpecialities(Specialities specialities){
        if (specialities==null){
            return Msg.fail();
        }
        boolean b = specialitiesService.addSpecialities(specialities);
        if (b){
            return Msg.success();
        }else {
            return Msg.fail();
        }
    }

    /**
     * 修改专业
     * @param specialities
     * @return
     */
    @PostMapping(value ="updateSpecialities",name="admin")
    public Msg updateSpecialities(Specialities specialities){
        try{

            specialitiesService.updataSpecialities(specialities);

        }catch (Exception e){

            return Msg.fail().add("info","更新失败");

        }
        return Msg.success().add("subject",specialities);
    }

    @PostMapping(value ="deleteSpecialities",name="admin")
    public Msg deleteSpecialities(@RequestParam("pid") Integer pid){
        if (pid==null){
            return Msg.fail();
        }
        specialitiesService.deleteSpecialities(pid);
        return Msg.success();
    }
}
