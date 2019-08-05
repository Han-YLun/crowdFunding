package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.dao.TicketMapper;
import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.Ticket;
import com.hyl.atcrowdfunding.service.MemberService;
import com.hyl.atcrowdfunding.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: hyl
 * @date: 2019/07/27
 **/
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper ticketMapper;


    public Ticket getTicketByMemberId(Integer id) {
        return ticketMapper.getTicketByMemberId(id);
    }

    public void saveTicket(Ticket ticket) {
        ticketMapper.saveTicket(ticket);
    }

    public void updatePstep(Ticket ticket) {
        ticketMapper.updatePstep(ticket);
    }

    @Override
    public void updatePiidAndPstep(Ticket ticket) {
        ticketMapper.updatePiidAndPstep(ticket);
    }

    @Override
    public Member getMemberByPiid(String processInstanceId) {
        return ticketMapper.getMemberByPiid(processInstanceId);
    }

    @Override
    public void updateStatus(Member member) {
        ticketMapper.updateStatus(member);
    }
}
