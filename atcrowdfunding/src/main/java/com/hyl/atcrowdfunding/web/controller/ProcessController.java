package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.utils.AjaxResult;
import com.hyl.atcrowdfunding.utils.Page;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/25
 **/
@Controller
@RequestMapping("/process")
public class ProcessController {

    
/*    @Autowired
    private RepositoryService repositoryService;*/

    String[] location = {"spring-flow.xml" , "spring-context.xml"};
    ApplicationContext ioc = new ClassPathXmlApplicationContext(location);
    ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");
    RepositoryService repositoryService = processEngine.getRepositoryService();






    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }



    @RequestMapping("/showimg")
    public String showimg(){
        return "process/showimg";
    }



    @ResponseBody
    @RequestMapping("doShowImgProDef")
    public void doShowImgProDef(String id, HttpServletResponse response) throws IOException {

        ProcessDefinition processDefinition =
                repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();


        InputStream resourceAsStream = 
                repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());

        ServletOutputStream outputStream = response.getOutputStream();

        IOUtils.copy(resourceAsStream,outputStream);
    }


    @ResponseBody
    @RequestMapping("doDelete")
    public Object doDelete(String id){  //流程定义Id

        AjaxResult result = new AjaxResult();

        try {

            ProcessDefinition processDefinition =
                    repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

            //部署id
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);

            result.setSuccess(true);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("部署流程定义失败!");
            e.printStackTrace();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("deploy")
    public Object deploy(HttpServletRequest request){

        AjaxResult result = new AjaxResult();
        
        try {
           MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

           MultipartFile multipartFile = multipartHttpServletRequest.getFile("processDefFile");

           repositoryService.createDeployment().addInputStream(multipartFile.getOriginalFilename(),
                   multipartFile.getInputStream()).deploy();

           result.setSuccess(true);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("部署流程定义失败!");
            e.printStackTrace();
        }

        return result;
    }





    @ResponseBody
    @RequestMapping("doIndex")
    public Object doIndex(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                          @RequestParam(value = "pagesize",required = false,defaultValue = "1") Integer pagesize){

        AjaxResult result = new AjaxResult();
        try {

            Page page = new Page(pageno,pagesize);
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            //查询流程定义集合数据,可能出现了自关联,导致JackSon组件无法将集合序列化为JSON串
            List<ProcessDefinition> listPage = processDefinitionQuery.listPage(page.getStartIndex(), pagesize);

            List<Map<String, Object>> myListPage = new ArrayList<Map<String, Object>>();

            for (ProcessDefinition processDefinition : listPage) {

                Map<String, Object> pd = new HashMap<String, Object>();
                pd.put("id",processDefinition.getId());
                pd.put("name",processDefinition.getName());
                pd.put("key",processDefinition.getKey());
                pd.put("version",processDefinition.getVersion());

                myListPage.add(pd);
            }

            Long totalsize = processDefinitionQuery.count();



            page.setData(myListPage);

            page.setTotalsize(totalsize.intValue());

            System.out.println("myListPage  :  " + myListPage);
            System.out.println("myListPageSize  :  " + myListPage.size());
            System.out.println("listPageSize  :  " + listPage.size());
            System.out.println("listPage  :  " + listPage);
            System.out.println("totalsize  :  " + totalsize);

            result.setPage(page);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("查询流程定义失败!");
            e.printStackTrace();
        }

        return result;
    }

    
}
