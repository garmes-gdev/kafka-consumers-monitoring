package com.gdev.db.zookeeper;

public class AclResource {

    // {"principal":"User:pmgarmes","permissionType":"Allow","operation":"Read","host":"*"}
    String principal;
    String permissionType;
    String operation;
    String host;

    public AclResource(String principal, String permissionType, String operation, String host) {
        this.principal = principal;
        this.permissionType = permissionType;
        this.operation = operation;
        this.host = host;
    }

    public AclResource() {
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
