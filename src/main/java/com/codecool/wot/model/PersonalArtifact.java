package com.codecool.wot.model;

import com.codecool.wot.dao.ArtifactDAO;
import com.codecool.wot.dao.PersonDAO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalArtifact {
    private final DateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Account person;
    private Artifact artifact;
    private Boolean status;
    private Date purchaseDate;

    public PersonalArtifact(Integer personId, Integer artifactId, String stringDate) throws ParseException {
        this.person = PersonDAO.getInstance().getPerson(personId);
        this.artifact = ArtifactDAO.getInstance().getArtifact(artifactId);
        this.status = false;
        this.purchaseDate  = new Date();
        // call method that reduce ballance in user wallet
    }

    public PersonalArtifact(Integer personId, Integer artifactId, String statusString, String stringDate) throws ParseException {
        this.person = PersonDAO.getInstance().getPerson(personId);
        this.artifact = ArtifactDAO.getInstance().getArtifact(artifactId);
        parseDate(stringDate);
        parseStatus(statusString);
    }

    public Account getPerson() {
        return person;
    }

    public void setPerson(Account person) {
        this.person = person;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus() {
        if (this.status.equals(false)) {
            this.status = true;
        }
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String parseDate() {
        return this.FORMATTER.format(this.purchaseDate);
    }

    private void parseDate(String stringDate) throws ParseException {
        Date date = this.FORMATTER.parse(stringDate);
        this.purchaseDate = date;
    }

    private void parseStatus(String statusString) {
        if (statusString.equals("unused")) {
            this.status = false;
        } else if (statusString.equals("used")) {
            this.status = true;
        }
    }

    public String parseStatus() {
        String statusString = null;
        if (this.status.equals(false)) {
            statusString = "unused";
        } else if (this.status.equals(true)) {
            statusString = "used";
        }
        return statusString;
    }
}
