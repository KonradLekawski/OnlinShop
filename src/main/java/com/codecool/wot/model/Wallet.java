package com.codecool.wot.model;

import com.codecool.wot.dao.LevelDAO;
import com.codecool.wot.dao.PersonDAO;

public class Wallet {
    private Account person;
    private Double totalCoolcoinsEarn;
    private Double balance;
    private Level level;

    public Wallet(Integer personId) {
        this.person = PersonDAO.getInstance().getPerson(personId);
        this.totalCoolcoinsEarn = 0.0;
        this.balance = 0.0;
        this.level = LevelDAO.getInstance().getLevel(1);
    }

    public Wallet(Integer personId, Double totalCoolcoinsEarn, Double balance, Integer levelId) {
        this.person = PersonDAO.getInstance().getPerson(personId);
        this.totalCoolcoinsEarn = totalCoolcoinsEarn;
        this.balance = balance;
        this.level = LevelDAO.getInstance().getLevel(levelId);
    }

    public Account getPerson() {
        return person;
    }

    public void setPerson(Account person) {
        this.person = person;
    }

    public Double getTotalCoolcoinsEarn() {
        return totalCoolcoinsEarn;
    }

    public void setTotalCoolcoinsEarn(Double totalCoolcoinsEarn) {
        this.totalCoolcoinsEarn = totalCoolcoinsEarn;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void increaseMoney(Double ccEarn) {
        this.balance += ccEarn;
        this.totalCoolcoinsEarn += ccEarn;
    }

    public void decreseMoney(Double ccSpend) {
        this.balance -= ccSpend;
    }
}