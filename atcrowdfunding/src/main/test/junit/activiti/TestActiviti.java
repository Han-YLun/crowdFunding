package junit.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author: hyl
 * @date: 2019/07/23
 **/
public class TestActiviti {

    String[] location = {"spring-flow.xml" , "spring-context.xml"};
    ApplicationContext ioc = new ClassPathXmlApplicationContext(location);
    ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");

    //创建流程引擎,创建23张表
    @Test
    public void test01(){
        ProcessEngine processEngine =
                (ProcessEngine) ioc.getBean("processEngine");

        System.out.println("ProcessEngine :"+ processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        //部署流程定义
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("junit/resources/MyProcess1.bpmn").deploy();
        System.out.println("Deployment  : "+deploy);
    }

    //查询部署的流程定义
    @Test
    public void test02(){
        
        System.out.println("ProcessEngine :"+ processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> list = processDefinitionQuery.list();

        System.out.println("start");
        for (ProcessDefinition processDefinition : list) {
            System.out.println("Id : " + processDefinition.getId());
            System.out.println("Key : "+processDefinition.getKey());
            System.out.println("Version : "+processDefinition.getVersion());
            
        }
        System.out.println("end");
    }

    //创建流程实例
    @Test
    public void test03(){

        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println("ProcessInstance :  "+processDefinition);
    }
}
