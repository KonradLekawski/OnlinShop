package com.codecool.wot.model;

import com.codecool.wot.dao.PersonDAO;
import com.codecool.wot.dao.QuestDAO;
import com.codecool.wot.dao.WalletDAO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bill {
    private final DateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private static Integer lastId = 1;
    private Integer id;
    private Account person;
    private Quest quest;
    private Boolean status;
    private Date achieveDate;

    public Bill(Integer personId, Integer questId) throws ParseException {
        this.id = ++lastId;
        this.person = PersonDAO.getInstance().getPerson(personId);
        this.quest = QuestDAO.getInstance().getQuest(questId);
        this.status = false;
        this.achieveDate = new Date();
    }

    public Bill(Integer id, Integer personId, Integer questId, String statusString, String stringDate) throws ParseException {
        this.id = id;
        this.person = PersonDAO.getInstance().getPerson(personId);
        this.quest = QuestDAO.getInstance().getQuest(questId);
        parseDate(stringDate);
        parseStatus(statusString);

        if(lastId < id) {
            lastId = id;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getPerson() {
        return person;
    }

    public void setPerson(Account person) {
        this.person = person;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest() {
        this.quest = null;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus() {
        if (this.status.equals(false)) {
            this.status = true;
            updateWallet();
        }
    }

    public Date getAchieveDate() {
        return achieveDate;
    }

    public void setAchieveDate(Date achieveDate) {
        this.achieveDate = achieveDate;
    }


    public String parseDate() {
        return this.FORMATTER.format(this.achieveDate);
    }

    private void parseDate(String stringDate) throws ParseException {
        Date date = this.FORMATTER.parse(stringDate);
        this.achieveDate = date;
    }

    private void parseStatus(String statusString) {
        if (statusString.equals("unpaid")) {
            this.status = false;
        } else if (statusString.equals("paid")) {
            this.status = true;
        }
    }

    public String parseStatus() {
        String statusString = null;
        if (this.status.equals(false)) {
            statusString = "unpaid";
        } else if (this.status.equals(true)) {
            statusString = "paid";
        }
        return statusString;
    }

    private void updateWallet() {
        Wallet wallet = WalletDAO.getInstance().getWallet(this.person);
        wallet.increaseMoney(this.quest.getPrice());
    }
}
