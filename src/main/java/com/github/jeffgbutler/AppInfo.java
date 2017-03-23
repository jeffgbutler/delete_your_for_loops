package com.github.jeffgbutler;

public enum AppInfo {
    SALES(1, 2237),
    HR(2, 4352),
    JIRA(3, 3657),
    ADMIN(4,5565);
    
    private int columnNumber;
    private int appId;
    
    private AppInfo(int columnNumber, int appId) {
        this.columnNumber = columnNumber;
        this.appId = appId;
    }

    public int columnNumber() {
        return columnNumber;
    }
    
    public String getInsertStatement(String userId) {
        return "insert into ApplicationPermission(user_id, application_id) values('"
                + userId
                + "', "
                + appId
                + ");";
    }
}
