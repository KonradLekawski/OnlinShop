package com.codecool.wot.model;

public class Level {
    private static Integer lastId = 1;
    private Integer id;
    private String name;
    private String description;
    private Double coolcoinValue;

    public Level(String name, String description, Double coolcoinValue) {
        this.id = ++lastId;
        this.name = name;
        this.description = description;
        this.coolcoinValue = coolcoinValue;
    }

    public Level(Integer id, String name, String description, Double coolcoinValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coolcoinValue = coolcoinValue;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCoolcoinValue() {
        return coolcoinValue;
    }

    public void setCoolcoinValue(Double coolcoinValue) {
        this.coolcoinValue = coolcoinValue;
    }
}
