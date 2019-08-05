package com.hyl.atcrowdfunding.listener;

import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.service.MemberService;
import com.hyl.atcrowdfunding.service.TicketService;
import com.hyl.atcrowdfunding.utils.ApplicationContextUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

/**
 * @author: hyl
 * @date: 2019/07/27
 **/

//实名认证审核通过执行的操作
public class PassListener implements ExecutionListener {

    public void notify(DelegateExecution delegateExecution) throws Exception {

        //更新t_member表的authstatus字段
        // 2 ： 已实名认证
        Integer memberid = (Integer) delegateExecution.getVariable("memberid");

        ApplicationContext applicationContext = ApplicationContextUtils.applicationContext;

        TicketService ticketService = applicationContext.getBean(TicketService.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);

        Member member = memberService.getMemberById(memberid);
        member.setAuthstatus("2");
        memberService.updateAuthStatus(member);
        
        //更新t_ticket表的status字段
        //1 ： 流程结束
        ticketService.updateStatus(member);


    }
}
