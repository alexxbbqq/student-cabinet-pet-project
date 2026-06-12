package ru.university.studentapi.integration.onec;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.integration.onec")
public class OneCProperties {
    private String mode;
    private String baseUrl;
    private String username;
    private String password;
    private boolean syncEnabled = true;
    private long syncFixedDelayMs = 600000L;
    private List<String> demoStudentIds = new ArrayList<>();

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isSyncEnabled() { return syncEnabled; }
    public void setSyncEnabled(boolean syncEnabled) { this.syncEnabled = syncEnabled; }
    public long getSyncFixedDelayMs() { return syncFixedDelayMs; }
    public void setSyncFixedDelayMs(long syncFixedDelayMs) { this.syncFixedDelayMs = syncFixedDelayMs; }
    public List<String> getDemoStudentIds() { return demoStudentIds; }
    public void setDemoStudentIds(List<String> demoStudentIds) { this.demoStudentIds = demoStudentIds; }
}
