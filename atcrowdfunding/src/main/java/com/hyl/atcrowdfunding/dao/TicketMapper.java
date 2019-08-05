package com.hyl.atcrowdfunding.dao;


import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    Ticket selectByPrimaryKey(Integer id);

    List<Ticket> selectAll();

    Ticket getTicketByMemberId(Integer id);

    void saveTicket(Ticket ticket);

    void updatePstep(Ticket ticket);

    void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}