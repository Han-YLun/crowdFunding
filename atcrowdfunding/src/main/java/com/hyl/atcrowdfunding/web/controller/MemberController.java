package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.listener.PassListener;
import com.hyl.atcrowdfunding.listener.RefuseListener;
import com.hyl.atcrowdfunding.model.Cert;
import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.MemberCert;
import com.hyl.atcrowdfunding.model.Ticket;
import com.hyl.atcrowdfunding.service.CertService;
import com.hyl.atcrowdfunding.service.MemberService;
import com.hyl.atcrowdfunding.service.TicketService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import com.hyl.atcrowdfunding.utils.Const;
import com.hyl.atcrowdfunding.utils.Data;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.jfree.chart.axis.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * @author: hyl
 * @date: 2019/07/27
 **/
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private TicketService ticketService;

    @Autowired
    private CertService certService;

    String[] location = {"spring-flow.xml" , "spring-context.xml"};
    ApplicationContext ioc = new ClassPathXmlApplicationContext(location);
    ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");

    RepositoryService repositoryService = processEngine.getRepositoryService();
    private TaskService taskService = processEngine.getTaskService();
    RuntimeService runtimeService = processEngine.getRuntimeService();



    @RequestMapping("/accttype")
    public String accttype(){
        return "member/accttype";
    }


    @RequestMapping("/basicinfo")
    public String basicinfo(){
        return "member/basicinfo";
    }

    @RequestMapping("/uploadCert")
    public String uploadCert(){
        return "member/uploadCert";
    }

    @RequestMapping("/checkemail")
    public String checkemail(){
        return "member/checkemail";
    }

    @RequestMapping("/checkauthcode")
    public String checkauthcode(){
        return "member/checkauthcode";
    }


    @RequestMapping("/apply")
    public String apply(HttpSession session){

        Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);

        Ticket ticket = ticketService.getTicketByMemberId(member.getId());



        if (ticket == null){
            ticket = new Ticket();

            ticket.setMemberid(member.getId());
            ticket.setPstep("apply");
            ticket.setStatus("0");

            ticketService.saveTicket(ticket);

        }else{
            String pstep = ticket.getPstep();

            if ("accttype".equals(pstep)){
                return "redirect:/member/basicinfo.htm";
            }else if("basicinfo".equals(pstep)){

                //根据当前用户查询账户类型,根据账户的类型查询需要上传的资质
                String accttype = member.getAccttype();
                System.out.println("accttype " + accttype);
                List<Cert> queryCertByAcctType = certService.queryCertByAcctType(accttype);

                session.setAttribute("queryCertByAcctType",queryCertByAcctType);

                return "redirect:/member/uploadCert.htm";
            }else if("uploadcert".equals(pstep)){
                return "redirect:/member/checkemail.htm";
            }else if("checkemail".equals(pstep)){
                return "redirect:/member/checkauthcode.htm";
            }
        }


        return "member/accttype";
    }



    @ResponseBody
    @RequestMapping("/finishApply")
    public Object finishApply(HttpSession session, String authcode){
        


        AjaxResult result = new AjaxResult();

        try {

            //获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            //让当前系统用户完成验证码审核任务
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());

            if (ticket.getAuthcode().equals(authcode)){

                Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid())
                        .taskAssignee(loginMember.getLoginacct()).singleResult();

                taskService.complete(task.getId());

                //更新用户申请状态
                loginMember.setAuthstatus("1");
                memberService.updateAuthStatus(loginMember);

                //记录流程步骤
                ticket.setPstep("finishapply");
                ticketService.updatePstep(ticket);
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setMessage("验证码不正确,请重新输入！");
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/startProcess")
    public Object startProcess(HttpSession session, String email){


        AjaxResult result = new AjaxResult();

        try {

            //获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            //如果用户输入了新的邮箱,将旧的邮箱地址进行替换
            if (!loginMember.getEmail().equals(email)){
                loginMember.setEmail(email);
                memberService.updateEmail(loginMember);
            }


            //启动实名认证流程——系统自动发邮件,生成验证码,验证邮箱地址是否合法
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey("auth").singleResult();


            StringBuilder authCode = new StringBuilder();
            for (int i = 0; i < 4 ; i++) {
                authCode.append(new Random().nextInt(10));
            }

            Map<String,Object> variables = new HashMap<String, Object>();
            variables.put("toEmail",email);
            variables.put("loginacct",loginMember.getLoginacct());
            variables.put("passListener",new PassListener());
            variables.put("refuseListener",new RefuseListener());
            variables.put("authcode",authCode.toString());

            ProcessInstance processInstance =
                    runtimeService.startProcessInstanceById(processDefinition.getId(),variables);
            

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("checkemail");
            ticket.setPiid(processInstance.getId());
            ticket.setAuthcode(authCode.toString());
            ticketService.updatePiidAndPstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/doUploadCert")
    public Object doUploadCert(HttpSession session, Data data){

        AjaxResult result = new AjaxResult();

        try {

            //获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);



            String realPath = session.getServletContext().getRealPath("/pics");
            System.out.println(realPath);

            List<MemberCert> certimgs = data.getCertimgs();
            for (MemberCert memberCert : certimgs) {
                //资质文件上传
                MultipartFile fileImg = memberCert.getFileImg();
                String extName = fileImg.getOriginalFilename().substring(fileImg.getOriginalFilename().lastIndexOf("."));
                System.out.println(extName);
                String tmpName = UUID.randomUUID().toString() + extName;
                String fileName = realPath + "\\cert\\" + tmpName;

                System.out.println(fileName);

                fileImg.transferTo(new File(fileName));
                
                //保存文件名到数据库
                memberCert.setIconpath(tmpName);
                memberCert.setMemberid(loginMember.getId());
            }

            //保存会员与资质关系数据
            certService.saveMemberCert(certimgs);
            

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("uploadcert");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    @ResponseBody
    @RequestMapping("/updateBasicInfo")
    public Object updateBasicInfo(HttpSession session,Member member){

        AjaxResult result = new AjaxResult();

        try {

            //获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setRealname(member.getRealname());
            loginMember.setCardnum(member.getCardnum());
            loginMember.setTel(member.getTel());


            //更新账户类型
            memberService.updateBasicInfo(loginMember);

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("basicinfo");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 更新账户类型
     * @param session
     * @param accttype 账户类型
     * @return  
     */
    @ResponseBody
    @RequestMapping("/updateAcctType")
    public Object updateAcctType(HttpSession session,String accttype){

        AjaxResult result = new AjaxResult();
        try {

            //获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setAccttype(accttype);

            //更新账户类型
            memberService.updateAcctType(loginMember);

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("accttype");
            ticketService.updatePstep(ticket);
            
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }

}
