package com.example.filterapp.classes;

public class EmailNotification {
    private boolean staffSignUp;
    private boolean changeBranch;
    private boolean changePosition;
    private boolean customerDeleted;
    private boolean staffStartNavigation;
    private boolean soldNewFilter;
    private boolean serviceDone;
    private boolean companyEmailDetailsChanged;
    private boolean adminVerificationPassChange;
    private boolean identityVerificationPassChange;
    private boolean staffDeleted;
    private String email;

    public EmailNotification() {
        this(true, true, true, true, true,
                true, true, true, true,
                true, true, "");
    }

    public EmailNotification(boolean staffSignUp, boolean changeBranch, boolean changePosition, boolean customerDeleted, boolean staffStartNavigation,
                             boolean soldNewFilter, boolean serviceDone, boolean companyEmailDetailsChanged, boolean adminVerificationPassChange,
                             boolean identityVerificationPassChange, boolean staffDeleted, String email) {
        this.staffSignUp = staffSignUp;
        this.changeBranch = changeBranch;
        this.changePosition = changePosition;
        this.customerDeleted = customerDeleted;
        this.staffStartNavigation = staffStartNavigation;
        this.soldNewFilter = soldNewFilter;
        this.serviceDone = serviceDone;
        this.companyEmailDetailsChanged = companyEmailDetailsChanged;
        this.adminVerificationPassChange = adminVerificationPassChange;
        this.identityVerificationPassChange = identityVerificationPassChange;
        this.staffDeleted = staffDeleted;
        this.email = email;
    }

    public boolean isStaffSignUp() {
        return staffSignUp;
    }

    public void setStaffSignUp(boolean staffSignUp) {
        this.staffSignUp = staffSignUp;
    }

    public boolean isChangeBranch() {
        return changeBranch;
    }

    public void setChangeBranch(boolean changeBranch) {
        this.changeBranch = changeBranch;
    }

    public boolean isChangePosition() {
        return changePosition;
    }

    public void setChangePosition(boolean changePosition) {
        this.changePosition = changePosition;
    }

    public boolean isCustomerDeleted() {
        return customerDeleted;
    }

    public void setCustomerDeleted(boolean customerDeleted) {
        this.customerDeleted = customerDeleted;
    }

    public boolean isStaffStartNavigation() {
        return staffStartNavigation;
    }

    public void setStaffStartNavigation(boolean staffStartNavigation) {
        this.staffStartNavigation = staffStartNavigation;
    }

    public boolean isSoldNewFilter() {
        return soldNewFilter;
    }

    public void setSoldNewFilter(boolean soldNewFilter) {
        this.soldNewFilter = soldNewFilter;
    }

    public boolean isServiceDone() {
        return serviceDone;
    }

    public void setServiceDone(boolean serviceDone) {
        this.serviceDone = serviceDone;
    }

    public boolean isCompanyEmailDetailsChanged() {
        return companyEmailDetailsChanged;
    }

    public void setCompanyEmailDetailsChanged(boolean companyEmailDetailsChanged) {
        this.companyEmailDetailsChanged = companyEmailDetailsChanged;
    }

    public boolean isAdminVerificationPassChange() {
        return adminVerificationPassChange;
    }

    public void setAdminVerificationPassChange(boolean adminVerificationPassChange) {
        this.adminVerificationPassChange = adminVerificationPassChange;
    }

    public boolean isIdentityVerificationPassChange() {
        return identityVerificationPassChange;
    }

    public void setIdentityVerificationPassChange(boolean identityVerificationPassChange) {
        this.identityVerificationPassChange = identityVerificationPassChange;
    }

    public boolean isStaffDeleted() {
        return staffDeleted;
    }

    public void setStaffDeleted(boolean staffDeleted) {
        this.staffDeleted = staffDeleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
