package com.zhiliao.module.web.system;

import com.zhiliao.common.base.BaseController;
import com.zhiliao.mybatis.model.master.TSysScheduleJob;
import org.springframework.ui.Model;

import java.sql.SQLException;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-07
 **/
public class ScheduleJobController extends BaseController<TSysScheduleJob>{
    @Override
    public String index(Integer pageNumber, Integer pageSize, TSysScheduleJob pojo, Model model) {
        return null;
    }

    @Override
    public String input(Integer Id, Model model) {
        return null;
    }

    @Override
    public String save(TSysScheduleJob pojo) throws SQLException {
        return null;
    }

    @Override
    public String delete(Integer[] ids) throws SQLException {
        return null;
    }
}
