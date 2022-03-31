package org.fdroid.model;

import org.fdroid.util.Formatter;

public class AppCommon extends PojoCommon {
    private String packageName;
    private String changelog;
    private String suggestedVersionName;
    private String suggestedVersionCode;
    private String issueTracker;
    private String license;
    private String sourceCode;
    private String webSite;
    private long added;
    private String icon;
    private long lastUpdated;

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getSuggestedVersionName() {
        return suggestedVersionName;
    }

    public void setSuggestedVersionName(String suggestedVersionName) {
        this.suggestedVersionName = suggestedVersionName;
    }

    public String getSuggestedVersionCode() {
        return suggestedVersionCode;
    }

    public void setSuggestedVersionCode(String suggestedVersionCode) {
        this.suggestedVersionCode = suggestedVersionCode;
    }

    public String getIssueTracker() {
        return issueTracker;
    }

    public void setIssueTracker(String issueTracker) {
        this.issueTracker = issueTracker;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    protected void toString(StringBuilder sb) {
        super.toString(sb);
        Formatter.add(sb, "packageName", this.packageName);
        Formatter.add(sb, "changelog", this.changelog);
        Formatter.add(sb, "suggestedVersionName", this.suggestedVersionName);
        Formatter.add(sb, "suggestedVersionCode", this.suggestedVersionCode);
        Formatter.add(sb, "issueTracker", this.issueTracker);
        Formatter.add(sb, "license", this.license);
        Formatter.add(sb, "sourceCode", this.sourceCode);
        Formatter.add(sb, "webSite", this.webSite);
        Formatter.addDate(sb, "added", this.added);
        Formatter.addDate(sb, "lastUpdated", this.lastUpdated);
        Formatter.add(sb, "icon", this.icon);
    }

}
