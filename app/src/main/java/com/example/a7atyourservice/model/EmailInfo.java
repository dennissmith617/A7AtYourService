package com.example.a7atyourservice.model;

public class EmailInfo {

    private String email_address;
    private String domain;
    private boolean valid_syntax;
    private boolean disposable;
    private boolean webmail;
    private boolean deliverable;
    private boolean catch_all;
    private boolean gibberish;
    private boolean spam;

    // Represents the email data from API
    public EmailInfo(String email_address, String domain, boolean valid_syntax,
                     boolean disposable, boolean webmail, boolean deliverable,
                     boolean catch_all, boolean gibberish, boolean spam) {
        this.email_address = email_address;
        this.domain = domain;
        this.valid_syntax = valid_syntax;
        this.disposable = disposable;
        this.webmail = webmail;
        this.deliverable = deliverable;
        this.catch_all = catch_all;
        this.gibberish = gibberish;
        this.spam = spam;
    }

    public String getEmail_address() {
        return email_address;
    }

    public String getDomain() {
        return domain;
    }

    public boolean getValid_syntax() {
        return valid_syntax;
    }

    public boolean getDisposable() {
        return disposable;
    }
    public boolean getWebmail() {
        return webmail;
    }
    public boolean getDeliverable() {
        return deliverable;
    }
    public boolean getCatch_all() {
        return catch_all;
    }
    public boolean getGibberish() {
        return gibberish;
    }
    public boolean getSpam() {
        return spam;
    }

}
