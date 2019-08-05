package com.hyl.atcrowdfunding.service;

import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.Ticket;

/**
 * @author: hyl
 * @date: 2019/07/27
 **/
public interface TicketService {
    Ticket getTicketByMemberId(Integer id);

    void saveTicket(Ticket ticket);

    void updatePstep(Ticket ticket);

    void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
