package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.service.MemberService;
import com.hyl.atcrowdfunding.service.TicketService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import com.hyl.atcrowdfunding.utils.Page;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/29
 **/

@Controller
@RequestMapping("/authcert")
public class AuthCertController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MemberService memberService;


    String[] location = {"spring-flow.xml" , "spring-context.xml"};
    ApplicationContext ioc = new ClassPathXmlApplicationContext(location);
    ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");

    RepositoryService repositoryService = processEngine.getRepositoryService();
    private TaskService taskService = processEngine.getTaskService();
    RuntimeService runtimeService = processEngine.getRuntimeService();

    @RequestMapping("/index")
    public String index(){
        return "authcert/index";
    }


    @RequestMapping("/show")
    public String show(Integer memberid,Map<String,Object> map){

        Member member = memberService.getMemberById(memberid);

        List<Map<String,Object>> list = memberService.queryCertByMemberId(memberid);

        map.put("member",member);
        map.put("certimgs",list);
        return "authcert/show";
    }


    @ResponseBody
    @RequestMapping("pass")
    public Object pass(String taskid,Integer memberid) {

        AjaxResult result = new AjaxResult();
        try {


            taskService.setVariable(taskid,"flag",true);
            taskService.setVariable(taskid,"memberid",memberid);

            //传递参数,让流程继续执行
            taskService.complete(taskid);
            
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("任务查询列表失败！");
            e.printStackTrace();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("refuse")
    public Object refuse(String taskid,Integer memberid) {

        AjaxResult result = new AjaxResult();
        try {


            taskService.setVariable(taskid,"flag",false);
            taskService.setVariable(taskid,"memberid",memberid);

            //传递参数,让流程继续执行
            taskService.complete(taskid);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("任务查询列表失败！");
            e.printStackTrace();
        }

        return result;
    }



    @ResponseBody
    @RequestMapping("pageQuery")
    public Object pageQuery(@RequestParam(value="pageno",required = false,defaultValue = "1") Integer pageno,
                            @RequestParam(value="pagesize",required = false,defaultValue = "10") Integer pagesize) {

        AjaxResult result = new AjaxResult();
        try {

            Page page = new Page(pageno,pagesize);

            //查询后台backuser委托组的任务
            TaskQuery taskQuery = taskService.createTaskQuery()
                    .processDefinitionKey("auth")
                    .taskCandidateGroup("backuser");

            List<Task> listPage = taskQuery.listPage(page.getStartIndex(), pagesize);


            List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();

            for (Task task : listPage) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("taskid",task.getId());
                map.put("taskName",task.getName());

                //根据任务查询流程定义(获取流程定义的名称和版本)

                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(task.getParentTaskId())
                        .singleResult();


                map.put("procDefName",processDefinition.getName());
                map.put("procDefVersion",processDefinition.getVersion());

                //根据任务查询流程实例(根据流程实例的id查询流程单,查询用户信息)
                Member member = ticketService.getMemberByPiid(task.getProcessInstanceId());
                
                map.put("member",member);

                data.add(map);
            }

            page.setData(data);

            Long count = taskQuery.count();
            page.setTotalsize(count.intValue());


            result.setPage(page);
            

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("任务查询列表失败！");
            e.printStackTrace();
        }

        return result;
    }

    
}
