package com.cmap.ehcache.model;

import java.io.Serializable;

//@Entity
//@Table(name = "sys_cache_message")
public class MessageBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1882173650948233428L;

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id")
    private String id;

    //@Column(name = "step")
    private String step;

    //@Column(name = "process")
    private String process;

    //@Column(name = "isNew")
    private String isNew;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
}
